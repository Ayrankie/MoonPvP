package org.ayrankie.moonPvP.Punishments;

import org.ayrankie.moonPvP.Punishments.GUIs.BanGUI;
import org.ayrankie.moonPvP.Punishments.GUIs.MuteGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.zaxxer.hikari.HikariDataSource;

public class BanCommand implements CommandExecutor {

    private final HikariDataSource dataSource;

    public BanCommand(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Ban kategorileri ve taban süreleri (dakika cinsinden, perma için -1)
    private static final String[] banReasonsArray = {
            "",
            "Claim Taciz",                          // 1
            "Dolandırıcılık",                          // 2
            "T Spawn Trap, TPA Trap",                      // 3
            "Hile Bulundurmak",                              // 4
            "Hile Kontrolünden Kaçmak",                           // 5
            "Hile Kullanımı",                       // 6
            "Etkisiz Bug Kullanımı",            // 7
            "Dini / Milli Hakaret",           // 8
            "Bug Kullanımı",                               // 9
            "DOXX",                                    // 10 (özel)
    };
    private static final int[] banBaseMinutes = {
            0,
            1440,   // 1 gün
            1440,   // 1 gün
            4320,   // 3 gün
            4320,   // 3 gün
            10080,  // 7 gün
            10080,  // 7 gün
            1440,   // 1 gün
            43200,  // 30 gün
            10080,  // 7 gün
            -1      // Perma
    };

    private String formatDuration(long totalSeconds) {
        if (totalSeconds == -1) return "Süresiz (Perma)";
        long months = totalSeconds / (30L * 24 * 3600); totalSeconds %= (30L * 24 * 3600);
        long weeks = totalSeconds / (7L * 24 * 3600); totalSeconds %= (7L * 24 * 3600);
        long days = totalSeconds / (24 * 3600); totalSeconds %= (24 * 3600);
        long hours = totalSeconds / 3600; totalSeconds %= 3600;
        long minutes = totalSeconds / 60; totalSeconds %= 60;
        long seconds = totalSeconds;

        StringBuilder sb = new StringBuilder();
        if (months > 0) sb.append(months).append(" ay ");
        if (weeks > 0) sb.append(weeks).append(" hafta ");
        if (days > 0) sb.append(days).append(" gün ");
        if (hours > 0) sb.append(hours).append(" saat ");
        if (minutes > 0) sb.append(minutes).append(" dakika ");
        if (seconds > 0) sb.append(seconds).append(" saniye");
        return sb.toString().trim();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            String playerName = args[0];
            if (!(sender instanceof Player)) {
                sender.sendMessage("Bu komut sadece oyundan kullanılabilir!");
                return true;
            }
            Player staff = (Player) sender;
            // Online ise Player, offline ise OfflinePlayer
            Player target = Bukkit.getPlayerExact(playerName);
            if (target != null) {
                BanGUI.openBanGUI(staff, target);
            } else {
                org.bukkit.OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(playerName);
                BanGUI.openBanGUIOffline(staff, offlineTarget);
            }
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("Kullanım: /ban <oyuncu> <süre | kategoriNo | perma> [sebep]");
            return true;
        }
        String staff = sender.getName();
        String playerName = args[0].toLowerCase();

        // Perma ban (hem /ban oyuncu perma [sebep] hem kategorili perma)
        if (args[1].equalsIgnoreCase("perma")) {
            String reason = args.length >= 3 ? String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length)) : "Perma Ban";
            applyBan(sender, staff, playerName, -1, reason, true, -1, "Perma Ban");
            return true;
        }

        // KATEGORİLİ BAN: /ban oyuncu kategoriNo
        if (args[1].matches("\\d+")) {
            int banType = Integer.parseInt(args[1]);
            if (banType < 1 || banType >= banReasonsArray.length) {
                sender.sendMessage("Geçersiz ban kategorisi!");
                return true;
            }
            // Çarpan kontrolü: oyuncunun o sebep ile kaç ban aldığı
            int previousCount = 0;

            try (Connection conn = dataSource.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM punishments WHERE LOWER(player)=? AND type='ban' AND reason LIKE ?"
                );
                ps.setString(1, playerName.toLowerCase());
                ps.setString(2, banReasonsArray[banType] + "%");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) previousCount = rs.getInt(1);
            } catch (Exception e) {
                sender.sendMessage("Çarpan sorgulanırken hata: " + e.getMessage());
                e.printStackTrace();
                return true;
            }
            int multiplier = previousCount + 1;
            String reason = banReasonsArray[banType] + " [" + multiplier + "x]";
            int baseMinutes = banBaseMinutes[banType];
            boolean perma = baseMinutes == -1;
            long banDurationMillis = perma ? -1 : baseMinutes * (long) Math.pow(2, previousCount) * 60 * 1000L;
            applyBan(sender, staff, playerName, banDurationMillis, reason, perma, banType, banReasonsArray[banType]);
            return true;
        }

        // KLASİK BAN: /ban oyuncu süre sebep
        String durationArg = args[1];
        String reason = args.length >= 3 ? String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length)) : "";
        long banDurationMillis = parseDuration(durationArg);
        boolean perma = banDurationMillis == -1;
        if (banDurationMillis <= 0 && !perma) {
            sender.sendMessage("Süre formatı yanlış! (örn: 1g2s30d veya perma)");
            return true;
        }
        applyBan(sender, staff, playerName, banDurationMillis, reason, perma, -1, reason);
        return true;
    }

    // Ban uygula ve kaydet
    private void applyBan(CommandSender sender, String staff, String playerName, long banDurationMillis, String reason, boolean perma, int banType, String baseReason) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String endTime = perma ? "9999-12-31 23:59:59"
                : LocalDateTime.now().plusSeconds(banDurationMillis / 1000).format(formatter);

        Integer cezaId = null;
        // Ceza veritabanına ekleniyor ve ID alınıyor

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO punishments (player, staff, type, timestamp, endTime, reason, revoked) VALUES (?, ?, 'ban', ?, ?, ?, 0)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, playerName.toLowerCase());
            ps.setString(2, staff);
            ps.setString(3, timestamp);
            ps.setString(4, endTime);
            ps.setString(5, reason);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                cezaId = rs.getInt(1);
            }
        } catch (Exception e) {
            sender.sendMessage("Ceza veritabanına kaydedilemedi: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Player target = Bukkit.getPlayerExact(playerName);
        if (target != null) {
            String msg =
                    "§cSunucudan yasaklandın!\n" +
                            "§eCeza Türü: §4BAN\n" +
                            "§eSebep: §6" + reason + "\n" +
                            "§eSüre: " + (perma ? "Süresiz (Perma Ban)" : formatDuration(banDurationMillis / 1000)) + "\n" +
                            "§7Ceza ID ile /gerivites komutunu kullanabilirsin.";
            target.kickPlayer(msg);
        }

        String idMsg = cezaId != null ? (" (CEZA ID: " + cezaId + ")") : "";
        sender.sendMessage(ChatColor.GOLD + playerName + " adlı oyuncu " + (perma ? "süresiz olarak" : formatDuration(banDurationMillis / 1000) + " boyunca") + " banlandı! Sebep: " + reason + idMsg);
    }

    private long parseDuration(String arg) {
        if (arg.equalsIgnoreCase("perma")) return -1;
        long total = 0;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("(\\d+)([ahgsd])");
        java.util.regex.Matcher m = p.matcher(arg);
        while (m.find()) {
            int val = Integer.parseInt(m.group(1));
            switch (m.group(2)) {
                case "a": total += val * 30L * 24 * 60 * 60 * 1000; break; // 1 ay = 30 gün
                case "h": total += val * 7L * 24 * 60 * 60 * 1000; break; // 1 hafta = 7 gün
                case "g": total += val * 24L * 60 * 60 * 1000; break; // 1 gün
                case "s": total += val * 60L * 60 * 1000; break; // 1 saat
                case "d": total += val * 60L * 1000; break; // 1 dakika
            }
        }
        return total;
    }
}