package org.ayrankie.moonPvP.Punishments;

import org.ayrankie.moonPvP.Database;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RevokeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Kullanım: /gerivites <ceza_id> <sebep>");
            return true;
        }
        String id = args[0];
        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        try (Connection conn = Database.getDataSource().getConnection()) {
            PreparedStatement checkPs = conn.prepareStatement(
                    "SELECT type, player, revoked, staff, timestamp FROM punishments WHERE id = ?"
            );
            checkPs.setString(1, id);
            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {
                sender.sendMessage("Ceza bulunamadı.");
                return true;
            }
            String type = rs.getString("type");
            String playerName = rs.getString("player");
            String staffName = rs.getString("staff");
            long timestamp = rs.getTimestamp("timestamp").getTime();
            if (rs.getInt("revoked") == 1) {
                sender.sendMessage("Bu ceza zaten affedilmiş! Tekrar affedilemez.");
                return true;
            }

            boolean hasBypass = sender.hasPermission("moonpvp.revoke.bypass");
            boolean isPlayer = sender instanceof Player;
            long now = System.currentTimeMillis();
            long diffMinutes = (now - timestamp) / (60 * 1000);

            if (!hasBypass) {
                // Sadece son 15 dakika ve kendi uyguladığı ceza kaldırılabilir
                if (!isPlayer || !sender.getName().equalsIgnoreCase(staffName)) {
                    sender.sendMessage("Sadece kendi verdiğiniz cezaları kaldırabilirsiniz.");
                    return true;
                }
                if (diffMinutes > 15) {
                    sender.sendMessage("Sadece son 15 dakika içinde verdiğiniz cezaları kaldırabilirsiniz.");
                    return true;
                }
            }

            // revoked_by alanı ile güncelle
            String revokedBy = sender.getName();

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE punishments SET revoked = 1, revoke_reason = ?, revoked_by = ? WHERE id = ?"
            );
            ps.setString(1, reason);
            ps.setString(2, revokedBy);
            ps.setString(3, id);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                sender.sendMessage("Ceza başarıyla kaldırıldı. (ID: " + id + ")");
                // Ban geri çekiliyorsa Bukkit ban listesinden kaldır
                if ("ban".equalsIgnoreCase(type)) {
                    Bukkit.getBanList(org.bukkit.BanList.Type.NAME).pardon(playerName);
                }
            } else {
                sender.sendMessage("Ceza bulunamadı veya kaldırma başarısız oldu.");
            }
        } catch (Exception e) {
            sender.sendMessage("Ceza kaldırma işlemi sırasında hata: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}