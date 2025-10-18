package org.ayrankie.moonPvP;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class LuckPermsToAuthMeSync {

    // LuckPerms'den oyuncunun ana grubunu çek
    public static String getPrimaryGroup(Player player) {
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            return user.getPrimaryGroup(); // Örn: "admin", "oyuncu", "mod"
        }
        return "oyuncu"; // default
    }

    // AuthMe tablosuna permission_role olarak yaz
    public static void syncRoleToAuthMe(Player player) {
        String username = player.getName();
        String role = getPrimaryGroup(player);
        try (Connection conn = Database.getDataSource().getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE authme SET permission_role = ? WHERE username = ?"
            );
            ps.setString(1, role);
            ps.setString(2, username);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}