package org.ayrankie.moonPvP.Listeners;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public class ChatFormatterListener implements Listener {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();

        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(p.getUniqueId());
        String prefix = "";
        String groupName = "";
        if (user != null) {
            CachedMetaData meta = user.getCachedData().getMetaData();
            prefix = meta.getPrefix() != null ? meta.getPrefix() : "";
            groupName = user.getPrimaryGroup();
        }

        String formattedName;
        if ("developer".equalsIgnoreCase(groupName)) {
            formattedName = "<gradient:#9f4102:#833e10>" + p.getName() + "</gradient>";
        } else if ("ac".equalsIgnoreCase(groupName)){
            formattedName = "<gradient:#7b3201:#9f4102>" + p.getName() + "</gradient>";
        } else if ("acplus".equalsIgnoreCase(groupName)){
            formattedName = "<gradient:#9d7b3a:#5c4317>" + p.getName() + "</gradient>";
        } else if ("admin".equalsIgnoreCase(groupName)){
            formattedName = "<gradient:#9d7b3a:#5c4317>" + p.getName() + "</gradient>";
        } else if ("founder".equalsIgnoreCase(groupName)){
            formattedName = "<gradient:#ea1e63:#730d2f>" + p.getName() + "</gradient>";
        } else if ("staff".equalsIgnoreCase(groupName)){
            formattedName = "<gradient:#1bbc9b:#1c7160>" + p.getName() + "</gradient>";
        } else if ("trial_staff".equalsIgnoreCase(groupName)){
            formattedName = "<gradient:#2ecd71:#1d7944>" + p.getName() + "</gradient>";
        } else if ("senior_staff".equalsIgnoreCase(groupName)){
            formattedName = "<gradient:#12bccd:#0c575e>" + p.getName() + "</gradient>";
        } else if ("mod ".equalsIgnoreCase(groupName)){
            formattedName = "<gradient:#ff0101:#831010>" + p.getName() + "</gradient>";
        } else {
            formattedName = "<white>" + p.getName() + "</white>";
        }

        // Oyuncunun mesajı MiniMessage ile efektli
        String formatted = prefix + " " + formattedName + " <gray>: </gray>" + event.getMessage();
        Component parsed = miniMessage.deserialize(formatted);

        event.setCancelled(true); // Default chat iptal
        for (Player recipient : event.getRecipients()) {
            recipient.sendMessage(parsed); // PaperMC'de Component ile gönder
        }
    }
}