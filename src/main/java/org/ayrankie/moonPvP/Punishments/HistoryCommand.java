package org.ayrankie.moonPvP.Punishments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HistoryCommand implements CommandExecutor {

    private final HikariDataSource dataSource;

    public HistoryCommand(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Kullanım: /geçmiş <oyuncu>");
            return true;
        }
        String playerName = args[0].toLowerCase();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, reason, timestamp, type FROM punishments WHERE LOWER(player)=? ORDER BY id DESC LIMIT 5"
            );
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            sender.sendMessage("§6" + playerName + " adlı oyuncunun son 5 cezası:");
            while (rs.next()) {
                found = true;
                int id = rs.getInt("id");
                String reason = rs.getString("reason");
                String date = rs.getString("timestamp");
                String type = rs.getString("type");
                sender.sendMessage("§e#" + id + " §7- §c" + reason + " §7- §b" + date + " §7- §a [" + type + "]");
                sender.sendMessage("-------------------------------");
            }
            if (!found) {
                sender.sendMessage("§cBu oyuncunun hiç ceza kaydı yok.");
            }
        } catch (Exception e) {
            sender.sendMessage("Veritabanı hatası: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }
}