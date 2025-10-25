package org.ayrankie.moonPvP.TAB;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TabListFormatter {

    private final JavaPlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    // Grup sırası
    private static final List<String> GROUP_ORDER = Arrays.asList(
            "founder", "developer", "admin", "acplus", "ac", "senior_staff", "staff",
            "trial_staff", "mod", "builder", "default"
    );

    public TabListFormatter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // Grup önceliği
    private int getGroupPriority(String groupName) {
        int idx = GROUP_ORDER.indexOf(groupName.toLowerCase());
        return idx == -1 ? GROUP_ORDER.size() : idx;
    }

    // Tüm oyuncuların tab'ını güncelle (grup sırasına göre)
    public void updateTabForAll() {
        LuckPerms luckPerms = LuckPermsProvider.get();
        // Oyuncuları sıralı olarak al
        List<Player> sortedPlayers = Bukkit.getOnlinePlayers().stream()
                .map(p -> (Player) p)
                .sorted(Comparator.comparingInt(p -> {
                    User user = luckPerms.getUserManager().getUser(p.getUniqueId());
                    String groupName = user != null ? user.getPrimaryGroup() : "default";
                    return getGroupPriority(groupName);
                }))
                .toList();

        for (Player p : sortedPlayers) {
            User user = luckPerms.getUserManager().getUser(p.getUniqueId());
            String prefix = "";
            String groupName = "";
            if (user != null) {
                CachedMetaData meta = user.getCachedData().getMetaData();
                prefix = meta.getPrefix() != null ? meta.getPrefix() : "";
                groupName = user.getPrimaryGroup();
            }

            // Gradientli isim
            String formattedName = getGradientName(groupName, p.getName());

            // SIRALAMA için herhangi bir renk kodu ekleme!
            String tabDisplay = prefix + " " + formattedName;
            Component tabComponent = miniMessage.deserialize(tabDisplay);
            p.playerListName(tabComponent);

            setTabHeaderFooter(p);
        }
    }

    // Gradientli isim fonksiyonu (daha okunaklı)
    private String getGradientName(String groupName, String playerName) {
        if ("developer".equalsIgnoreCase(groupName)) {
            return "<gradient:#9f4102:#833e10>" + playerName + "</gradient>";
        } else if ("ac".equalsIgnoreCase(groupName)){
            return "<gradient:#7b3201:#9f4102>" + playerName + "</gradient>";
        } else if ("acplus".equalsIgnoreCase(groupName)){
            return "<gradient:#9d7b3a:#5c4317>" + playerName + "</gradient>";
        } else if ("admin".equalsIgnoreCase(groupName)){
            return "<gradient:#9d7b3a:#5c4317>" + playerName + "</gradient>";
        } else if ("founder".equalsIgnoreCase(groupName)){
            return "<gradient:#ea1e63:#730d2f>" + playerName + "</gradient>";
        } else if ("staff".equalsIgnoreCase(groupName)){
            return "<gradient:#1bbc9b:#1c7160>" + playerName + "</gradient>";
        } else if ("trial_staff".equalsIgnoreCase(groupName)){
            return "<gradient:#2ecd71:#1d7944>" + playerName + "</gradient>";
        } else if ("senior_staff".equalsIgnoreCase(groupName)){
            return "<gradient:#12bccd:#0c575e>" + playerName + "</gradient>";
        } else if ("mod".equalsIgnoreCase(groupName)){
            return "<gradient:#ff0101:#831010>" + playerName + "</gradient>";
        } else {
            return "<white>" + playerName + "</white>";
        }
    }

    // TAB başlık ve alt satırını ayarla
    public void setTabHeaderFooter(Player player) {
        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();
        String header = """
                <bold><gradient:#ea1e63:#9f4102>✦ MoonPvP ✦</gradient></bold>
                <gray>Sunucumuza hoşgeldin, <yellow>%player%</yellow>!</gray>
                <blue>Şu an <green>%online%</green> / <gray>%max%</gray> oyuncu var.</blue>
                <gray>──────────────────────────────</gray>
                """;
        String footer = """
                <gray>──────────────────────────────</gray>
                <gradient:#1bbc9b:#ea1e63>Discord: discord.gg/moonpvp</gradient>
                <gradient:#9d7b3a:#5c4317>Destek & Topluluk için sunucumuzu ziyaret edin!</gradient>
                <gradient:#9d7b3a:#5c4317>Sitemiz: <blue>moonpvp.com</blue></gradient>
                <gray>──────────────────────────────</gray>
                """;

        header = header.replace("%player%", player.getName())
                .replace("%online%", String.valueOf(online))
                .replace("%max%", String.valueOf(max));

        Component headerComponent = miniMessage.deserialize(header);
        Component footerComponent = miniMessage.deserialize(footer);

        player.sendPlayerListHeaderAndFooter(headerComponent, footerComponent);
    }

    // Otomatik güncelleme (her 6 saniye)
    public void startAutoUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateTabForAll();
            }
        }.runTaskTimer(plugin, 20, 120);
    }
}