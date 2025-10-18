package org.ayrankie.moonPvP.Listeners;

import org.ayrankie.moonPvP.LuckPermsToAuthMeSync;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        LuckPermsToAuthMeSync.syncRoleToAuthMe(event.getPlayer());
    }
}
