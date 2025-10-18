package org.ayrankie.moonPvP.Punishments;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

import static org.ayrankie.moonPvP.Punishments.TempCommandMute.*;

public class PunishmentListeners implements Listener {

    // Chat eventinde:
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

        if (isMutedDatabase(name)) {
            event.setCancelled(true);
            String remaining = getMuteRemainingDatabase(name);
            String reason = getMuteReasonDatabase(name);
            player.sendMessage("Susturulmuşsun! Kalan süre: " + remaining + " | Sebep: " + reason);
        }
    }
}