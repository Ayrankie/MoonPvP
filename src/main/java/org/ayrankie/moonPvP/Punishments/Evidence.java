package org.ayrankie.moonPvP.Punishments;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Evidence implements CommandExecutor {

        private final HikariDataSource dataSource;

    public Evidence(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Kullanım: /kanıt <ceza_numarası> <kanıt>");
            return true;
        }

        String cezaId = args[0];
        String evidence = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));


        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO punishment_evidence (punishment_id, evidence) VALUES (?, ?)"
            );
            ps.setString(1, cezaId);
            ps.setString(2, evidence);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                sender.sendMessage(ChatColor.GREEN + "Kanıt başarıyla kaydedildi!");
            } else {
                sender.sendMessage("Ceza numarası bulunamadı!");
            }
        } catch (Exception e) {
            sender.sendMessage("Veritabanı hatası: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }
}