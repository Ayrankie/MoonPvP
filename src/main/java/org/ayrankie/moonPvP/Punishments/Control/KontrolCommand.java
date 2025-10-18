package org.ayrankie.moonPvP.Punishments.Control;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class KontrolCommand implements CommandExecutor {
    private static final HashMap<String, Long> frozedPlayers = new HashMap<>();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length != 1){
            sender.sendMessage("Kullanım: /kontrol (oyuncu)");
            return true;
        }
        String playerName = args[0].toLowerCase();
        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage("Oyuncu bulunamadı!");
            return true;
        }
        if(frozedPlayers.containsKey(playerName)){
            frozedPlayers.remove(playerName);
            target.sendMessage("§cArtık kontrol altında değilsin.");
            target.sendTitle("§aKONTROLÜN TAMAMLANDI", "§7Artık hareket edebilirsin!", 10, 70, 20);
            sender.sendMessage("§a" + playerName + " artık kontrol altında değil.");
        } else {
            frozedPlayers.put(playerName, System.currentTimeMillis());
            target.sendMessage("§aArtık kontrol altındasın. Hareket etme!");
            target.sendTitle("§cKONTROL ALTINDASIN", "§7Hareket etme!", 10, 70, 20);
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1));
            sender.sendMessage("§c" + playerName + " artık kontrol altında.");
        }
        return true;
    }

    public static boolean isFrozen(Player player) {
       if(frozedPlayers.containsKey(player.getName().toLowerCase())){
           return true;
       }
         return false;
    }
}
