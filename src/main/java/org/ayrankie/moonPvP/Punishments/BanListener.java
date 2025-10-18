package org.ayrankie.moonPvP.Punishments;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BanListener implements Listener {

    private final com.zaxxer.hikari.HikariDataSource dataSource;

    public BanListener(com.zaxxer.hikari.HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String playerName = event.getName().toLowerCase();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT reason, endTime, revoked FROM punishments WHERE LOWER(player)=? AND type='ban' ORDER BY id DESC LIMIT 1"
            );
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("revoked") == 0) {
                String endTime = rs.getString("endTime");
                String reason = rs.getString("reason");
                // Eğer süresi dolmamışsa veya perma ban ise ban devam ediyor
                boolean perma = "9999-12-31 23:59:59".equals(endTime);
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                java.time.LocalDateTime banEnd = java.time.LocalDateTime.parse(endTime, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (perma || now.isBefore(banEnd)) {
                    String msg =
                            "§cSunucudan yasaklandın!\n" +
                                    "§eSebep: §6" + reason + "\n" +
                                    "§eCezanın Kalkacağı Tarih " + (perma ? "Süresiz (Perma Ban)" : endTime) + "\n" +
                                    "§7Cezana itiraz etmek veya daha fazla bilgi almak için Discord'a katıl:\n" +
                                    "§9https://discord.gg/moonpvp";
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, msg);
                }
            }
        } catch (Exception e) {
            org.bukkit.Bukkit.getLogger().warning("BanListener DB hata: " + e.getMessage());
        }
    }
}