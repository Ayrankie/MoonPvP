package org.ayrankie.moonPvP.PvPListeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PvPListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKnockback(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            ItemStack damagedBy = ((Player) event.getDamager()).getInventory().getItemInMainHand();
            Vector velocity = damaged.getVelocity();

            double multiplierX = 0.4; // Default
            double multiplierZ = 0.4;
            double multiplierY = 0.5;

            if (damagedBy.getType().toString().toLowerCase().contains("sword")) {
                multiplierX = 0.2;
                multiplierZ = 0.22;
            } else if (damagedBy.getType().toString().toLowerCase().contains("axe")) {
                multiplierX = 0.4;
                multiplierZ = 0.4;
            } else {
                multiplierX = 0.5;
                multiplierZ = 0.5;
            }

            velocity.setX(velocity.getX() * multiplierX);
            velocity.setZ(velocity.getZ() * multiplierZ);
            velocity.setY(velocity.getY() * multiplierY);

            damaged.setVelocity(velocity);
        }
    }
}