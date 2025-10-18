package org.ayrankie.moonPvP.TAB;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionCommand implements CommandExecutor, TabCompleter {

    private static final List<String> ALLOWED_ROLES = Arrays.asList("developer", "founder", "mod", "staff", "trial_staff" , "senior_staff", "ac", "acplus");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("moonpvp.yetkiver")) {
            sender.sendMessage(ChatColor.RED + "Bu komutu kullanmak için yetkin yok.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Kullanım: /yetkiver <oyuncu> <yetki>");
            return true;
        }

        String playerName = args[0];
        String role = args[1].toLowerCase();

        if (!ALLOWED_ROLES.contains(role)) {
            sender.sendMessage(ChatColor.RED + "Bu yetkiyi veremezsin! Sadece: " + String.join(", ", ALLOWED_ROLES));
            return true;
        }

        // Oyuncu çevrimdışı olsa bile LuckPerms çalışır, ama istersen çevrimiçi kontrolü ekleyebilirsin.
        String commandStr = "lp user " + playerName + " parent set " + role;
        boolean result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandStr);

        if (result) {
            sender.sendMessage(ChatColor.GREEN + playerName + " kullanıcısına '" + role + "' yetkisi başarıyla verildi.");
        } else {
            sender.sendMessage(ChatColor.RED + "Bir hata oluştu, komut gönderilemedi.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            String prefix = args[1].toLowerCase();
            return ALLOWED_ROLES.stream()
                    .filter(role -> role.startsWith(prefix))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}