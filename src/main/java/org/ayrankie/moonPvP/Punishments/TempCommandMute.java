package org.ayrankie.moonPvP.Punishments;

import okhttp3.*;
import org.ayrankie.moonPvP.Database;
import org.ayrankie.moonPvP.Punishments.GUIs.MuteGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zaxxer.hikari.HikariDataSource;

public class TempCommandMute implements CommandExecutor {

    private static final HashMap<String, Long> mutedPlayers = new HashMap<>();
    private static final HashMap<String, String> muteReasons = new HashMap<>();

    private final HikariDataSource dataSource;

    public TempCommandMute(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Kategorize mute listesi
    private static final String[] muteReasonsArray = {
            "",
            "Sohbet Düzenini Bozmak",                               // 1
            "Kışkırtma/Aşağılama",                                  // 2
            "Hakaret/Argo",                                         // 3
            "Kısaltılmış Küfür",                                    // 4
            "Cinsel İçerikli İfade",                                // 5
            "Küfür",                                                // 6
            "Ailevi Küfür İma",                                     // 7
            "Ailevi Küfür",                                         // 8
            "Kişisel Reklam",                                       // 9
            "Yetkiliye Saygısızlık",                                // 10
            "Dini/Milli Değerlere Saygısızlık",                     // 11
            "Irkçılık",                                             // 12
            "Uzayan Din/Siyaset Tartışması",                        // 13
            "Sunucu İsim Paylaşımı / Reklam",                       // 14
            "Kampanya Başlatmak",                                   // 15
    };
    private static final int[] muteBaseMinutes = {
            0,
            15, // 1
            90, // 2
            60, // 3
            90, // 4
            120, // 5
            180, // 6
            360, // 7
            720, // 8
            360, // 9
            720, // 10
            43200, // 11
            720, // 12
            720, // 13
            720, // 14
            360, // 15
    };

    public static void checkAndReconnectPool() {
        Database.checkAndReconnectPool();
    }

    private String formatDuration(long totalSeconds) {
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
            String playerName = args[0].toLowerCase();

            if (sender instanceof Player) {
                Player target = Bukkit.getPlayerExact(playerName);
                // Oyuncu offline ise null gönder, GUI'de offline olduğuna dair uyarı verebilirsin.
                MuteGUI.openMuteGUI((Player)sender, target, playerName); // playerName parametresi ile offline mute imkanı
            } else {
                sender.sendMessage("Bu komut sadece oyundan kullanılabilir!");
            }
            return true;
        }

        // KATEGORİ MUTE: /mute oyuncu kategoriNo
        if (args.length == 2 && args[1].matches("\\d+")) {
            String staff = sender.getName();
            Player staffplayer = sender instanceof Player ? (Player) sender : null;
            String playerName = args[0].toLowerCase();
            int muteType = Integer.parseInt(args[1]);
            if (muteType < 1 || muteType >= muteReasonsArray.length) {
                sender.sendMessage("Geçersiz mute kategorisi!");
                return true;
            }
            // Aktif mute kontrolü
            try (Connection conn = dataSource.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT id FROM punishments WHERE LOWER(player)=? AND type='mute' AND revoked=0 AND endTime > ? ORDER BY id DESC LIMIT 1"
                );
                ps.setString(1, playerName.toLowerCase());
                ps.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    sender.sendMessage("Bu oyuncu zaten susturulmuş ve cezası aktif! Tekrar mute atamazsın.");
                    return true;
                }
            } catch (Exception e) {
                sender.sendMessage("Ceza kontrolü sırasında hata: " + e.getMessage());
                e.printStackTrace();
                return true;
            }
            // Çarpan kontrolü: oyuncunun o sebep ile kaç mute aldığı
            int previousCount = 0;
            try (Connection conn = dataSource.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM punishments WHERE LOWER(player)=? AND type='mute' AND reason LIKE ?"
                );
                ps.setString(1, playerName.toLowerCase());
                ps.setString(2, muteReasonsArray[muteType] + "%");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) previousCount = rs.getInt(1);
            } catch (Exception e) {
                sender.sendMessage("Ceza sorgulanırken hata: " + e.getMessage());
                e.printStackTrace();
                return true;
            }
            int multiplier = previousCount + 1;
            String reason = muteReasonsArray[muteType] + " [" + multiplier + "x]";
            int baseMinutes = muteBaseMinutes[muteType];
            long muteDurationMinutes = baseMinutes * (long) Math.pow(2, previousCount);
            long muteDurationMillis = muteDurationMinutes * 60 * 1000L;

            mutedPlayers.put(playerName, System.currentTimeMillis() + muteDurationMillis);
            muteReasons.put(playerName, reason);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(formatter);
            LocalDateTime endDateTime = LocalDateTime.now().plusSeconds(muteDurationMillis / 1000);
            String endTime = endDateTime.format(formatter);

            Integer cezaId = null;
            try (Connection conn = dataSource.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO punishments (player, staff, type, timestamp, endTime, reason, revoked) VALUES (?, ?, 'mute', ?, ?, ?, 0)",
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
            } catch (SQLIntegrityConstraintViolationException ex) {
                sender.sendMessage("Bu oyuncu zaten susturulmuş ve cezası aktif! Tekrar mute atamazsın.");
                return true;
            } catch (Exception e) {
                sender.sendMessage("Ceza veritabanına kaydedilemedi: " + e.getMessage());
                e.printStackTrace();
                return true;
            }
            sendPunishmentLog(staff, playerName, "mute", timestamp, endTime, reason);

            String idMsg = cezaId != null ? (" (CEZA ID: " + cezaId + ")") : "";

            String mutedMsg =
                    ChatColor.GOLD  + "------------------------------"
                    + "\n\n" + ChatColor.RED + "\nSunucu tarafından susturuldun! Süre: " + ChatColor.GOLD + ChatColor.BOLD + formatDuration(muteDurationMillis / 1000)
                    + "\n" + ChatColor.LIGHT_PURPLE + " Sebep: " + reason + "\n\n" +
                    ChatColor.GOLD  + "\n------------------------------";
            String mutedMsgStaff = ChatColor.GOLD  + "------------------------------"
                    + "\n\n" + ChatColor.RED + "\nOyuncu " + playerName + " sunucu tarafından susturuldu! Süre: " + ChatColor.GOLD + ChatColor.BOLD + formatDuration(muteDurationMillis / 1000)
                    + "\n" + ChatColor.LIGHT_PURPLE + " Sebep: " + reason + idMsg + "\n\n" +
                    ChatColor.GOLD  + "\n------------------------------";

            sender.sendMessage(mutedMsgStaff);
            sender.sendMessage(ChatColor.RED + "Yoksa yanlışlıkla hatalı işlem mi uyguladın? /gerivites <ceza_id> <sebep>");
            Player target = Bukkit.getPlayerExact(playerName);
            if (target != null) {
                target.sendMessage(mutedMsg);
                if (staffplayer != null) {
                    staffplayer.performCommand("chatlog " + target.getName());
                }
            }
            return true;
        }

        // KLASİK MUTE: /mute oyuncu süre sebep
        if (args.length >= 3) {
            String staff = sender.getName();
            Player staffplayer = sender instanceof Player ? (Player) sender : null;
            String playerName = args[0].toLowerCase();

            try (Connection conn = dataSource.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT id FROM punishments WHERE LOWER(player)=? AND type='mute' AND revoked=0 AND endTime > ? ORDER BY id DESC LIMIT 1"
                );
                ps.setString(1, playerName.toLowerCase());
                ps.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    sender.sendMessage("Bu oyuncu zaten susturulmuş ve cezası aktif! Tekrar mute atamazsın.");
                    return true;
                }
            } catch (Exception e) {
                sender.sendMessage("Ceza kontrolü sırasında hata: " + e.getMessage());
                e.printStackTrace();
                return true;
            }

            String durationArg = args[1];
            String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
            long muteDurationMillis = parseDuration(durationArg);
            if (muteDurationMillis <= 0) {
                sender.sendMessage("Süre formatı yanlış! (örn: 1g2s30d)");
                return true;
            }

            mutedPlayers.put(playerName, System.currentTimeMillis() + muteDurationMillis);
            muteReasons.put(playerName, reason);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(formatter);
            LocalDateTime endDateTime = LocalDateTime.now().plusSeconds(muteDurationMillis / 1000);
            String endTime = endDateTime.format(formatter);

            Integer cezaId = null;
            try (Connection conn = dataSource.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO punishments (player, staff, type, timestamp, endTime, reason, revoked) VALUES (?, ?, 'mute', ?, ?, ?, 0)",
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
            } catch (SQLIntegrityConstraintViolationException ex) {
                sender.sendMessage("Bu oyuncu zaten susturulmuş ve cezası aktif! Tekrar mute atamazsın.");
                return true;
            } catch (Exception e) {
                sender.sendMessage("Ceza veritabanına kaydedilemedi: " + e.getMessage());
                e.printStackTrace();
                return true;
            }

            sendPunishmentLog(staff, playerName, "mute", timestamp, endTime, reason);

            String idMsg = cezaId != null ? (" (CEZA ID: " + cezaId + ")") : "";
            sender.sendMessage(ChatColor.GOLD + playerName + " adlı oyuncu " + formatDuration(muteDurationMillis / 1000) + " boyunca susturuldu! Sebep: " + reason + idMsg);
            sender.sendMessage(ChatColor.RED + "Yoksa tamamen yanlışlıkla hatalı işlem mi uyguladın? /gerivites <ceza_id> <sebep>");
            Player target = Bukkit.getPlayerExact(playerName);
            if (target != null) {
                target.sendMessage(ChatColor.RED + "Sunucu tarafından susturuldun! Süre: " + ChatColor.GOLD + ChatColor.BOLD + formatDuration(muteDurationMillis / 1000) + ChatColor.LIGHT_PURPLE + " Sebep: " + reason);
                if (staffplayer != null) {
                    staffplayer.performCommand("chatlog " + target.getName());
                }
            }
            return true;
        }

        sender.sendMessage("Kullanım: /mute <oyuncu> <süre> <sebep>  veya  /mute <oyuncu> <kategoriNo>");
        return true;
    }

    public static boolean isMutedDatabase(String playerName) {

        try (Connection conn = Database.getDataSource().getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT endTime, revoked FROM punishments WHERE player=? AND type='mute' ORDER BY id DESC LIMIT 1"
            );
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return false;
            if (rs.getInt("revoked") == 1) return false;
            String endTimeString = rs.getString("endTime");
            LocalDateTime endTime = LocalDateTime.parse(endTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return LocalDateTime.now().isBefore(endTime);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getMuteReason(String playerName) {
        return muteReasons.getOrDefault(playerName, "Bilinmiyor");
    }

    public static String getMuteRemainingDatabase(String playerName) {

        try (Connection conn = Database.getDataSource().getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT endTime FROM punishments WHERE player=? AND type='mute' ORDER BY id DESC LIMIT 1"
            );
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return "0 saniye";
            String endTimeString = rs.getString("endTime");
            LocalDateTime endTime = LocalDateTime.parse(endTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(endTime)) return "0 saniye";
            long seconds = java.time.Duration.between(now, endTime).getSeconds();
            long days = seconds / (24 * 3600); seconds %= (24 * 3600);
            long hours = seconds / 3600; seconds %= 3600;
            long minutes = seconds / 60; seconds %= 60;
            StringBuilder sb = new StringBuilder();
            if (days > 0) sb.append(days).append("g ");
            if (hours > 0) sb.append(hours).append("s ");
            if (minutes > 0) sb.append(minutes).append("d ");
            if (seconds > 0) sb.append(seconds).append("sn");
            return sb.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "0 saniye";
        }
    }

    public static String getMuteReasonDatabase(String playerName) {

        try (Connection conn = Database.getDataSource().getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT reason FROM punishments WHERE player=? AND type='mute' ORDER BY id DESC LIMIT 1"
            );
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return "Bilinmiyor";
            return rs.getString("reason");
        } catch (Exception e) {
            e.printStackTrace();
            return "Bilinmiyor";
        }
    }

    private void sendPunishmentLog(String staff, String player, String type, String timestamp, String endTime, String reason) {
        OkHttpClient client = new OkHttpClient();

        String json = String.format(
                "{\"staff\":\"%s\",\"player\":\"%s\",\"type\":\"%s\",\"timestamp\":\"%s\",\"endTime\":\"%s\",\"reason\":\"%s\"}",
                staff, player, type, timestamp, endTime, reason
        );

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("http://72.60.129.142:3001/api/punishments")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Bukkit.getLogger().warning("API isteği başarısız: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) {
                Bukkit.getLogger().info("Mute ceza logu gönderildi: " + response.code() + " " + response.message());
                response.close();
            }
        });
    }

    // Süre parser: 1a2h3g4s5d -> ms
    private long parseDuration(String arg) {
        long total = 0;
        Pattern p = Pattern.compile("(\\d+)([ahgsd])");
        Matcher m = p.matcher(arg);
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
