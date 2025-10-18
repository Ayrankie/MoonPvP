package org.ayrankie.moonPvP.Punishments;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import okhttp3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class KickCommand implements CommandExecutor {

    public static HikariDataSource dataSource;

    public KickCommand(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Kullanım: /kick <oyuncu> [sebep]");
            return true;
        }

        String staff = sender.getName();
        String playerName = args[0].toLowerCase();
        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage("Oyuncu bulunamadı!");
            return true;
        }

        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);

        Integer cezaId = null;
        // Ceza veritabanına ekleniyor ve ID alınıyor
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO punishments (player, staff, type, timestamp, endTime, reason, revoked) VALUES (?, ?, 'kick', ?, ? , ?, 0)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, playerName.toLowerCase());
            ps.setString(2, staff);
            ps.setString(3, timestamp);
            ps.setString(4, timestamp);
            ps.setString(5, reason);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                cezaId = rs.getInt(1);
            }

        } catch (Exception e) {
            sender.sendMessage("Ceza veritabanına kaydedilemedi: " + e.getMessage());
            e.printStackTrace();
            return true;
        }

        sendPunishmentLog(staff, playerName, "kick", timestamp, "-" , reason);

        String idMsg = cezaId != null ? (" (CEZA ID: " + cezaId + ")") : "";
        sender.sendMessage( ChatColor.GOLD + playerName + " adlı oyuncunun bağlantısı Kesildi! Sebep: " + reason + idMsg);
        target.kickPlayer(ChatColor.GRAY + "Sunucudan " + ChatColor.DARK_RED + "Uzaklaştırıldın!" +
                ChatColor.GRAY + "\nSebep: " + ChatColor.RED + reason +
                ChatColor.GRAY + "\n\nCezana itiraz etmek veya daha fazla bilgi almak için Discord'a katıl: " + ChatColor.AQUA + "\nhttps://discord.gg/moonpvp"
        );
        return true;
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
                Bukkit.getLogger().info("Kick ceza logu gönderildi: " + response.code() + " " + response.message());
                response.close();
            }
        });
    }

    public static String getKickReasonDatabase(String playerName) {

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT reason FROM punishments WHERE player=? AND type='kick' ORDER BY id DESC LIMIT 1"
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

}
