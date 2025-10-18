package org.ayrankie.moonPvP.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class AdminLoginListener implements Listener {

    private final JavaPlugin plugin;

    public AdminLoginListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        FileConfiguration config = plugin.getConfig();
        boolean useProtection = config.getBoolean("useProtection", true);
        if (!useProtection) return;

        String ip = event.getAddress().getHostAddress().trim();
        String playername = event.getName();

        if (config.isConfigurationSection("admins")) {
            if (config.getConfigurationSection("admins").contains(playername)) {
                String allowedIp = config.getString("admins." + playername);
                if (allowedIp != null && !allowedIp.equals(ip)) {
                    String msg = "§cÇalış senin de olur kardeşim.";
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, msg);
                }
            }
        }
    }
}