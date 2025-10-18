package org.ayrankie.moonPvP.Punishments.Control;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class KontrolListener implements Listener {

    @EventHandler
    public void onFrozenMovement(PlayerMoveEvent event) {
        if (KontrolCommand.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (KontrolCommand.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent event) {
        if (KontrolCommand.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cKontrol altındayken komut kullanamazsın!");
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (KontrolCommand.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cKontrol altındayken sohbet edemezsin!");
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (KontrolCommand.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cKontrol altındayken eşya bırakamazsın!");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (KontrolCommand.isFrozen(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (KontrolCommand.isFrozen(player)) {
                event.setCancelled(true);
                player.sendMessage("§cKontrol altındayken envanteri kullanamazsın!");
            }
        }
    }

    @EventHandler
    public void onServerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (KontrolCommand.isFrozen(player)) {
            for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                if (onlineplayer.hasPermission("op")) {
                    onlineplayer.sendMessage(player.getName() + " §cadlı oyuncu kontrolden çıkış yaptı! BANLAYIN!");
                }
            }
        }
    }
}
