package org.ayrankie.moonPvP.Punishments.GUIs;

import org.ayrankie.moonPvP.MoonPvP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

public class BanGUI implements Listener {

    public static void openBanGUI(Player staff, Player target) {
        Inventory gui = Bukkit.createInventory(null, 27, "Ban Panel: " + target.getName());

        ItemStack kategori1 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta1 = kategori1.getItemMeta();
        meta1.setDisplayName(ChatColor.GOLD + "Claim Tacizi");
        meta1.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori1.setItemMeta(meta1);
        gui.setItem(10, kategori1);

        ItemStack kategori2 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta2 = kategori2.getItemMeta();
        meta2.setDisplayName(ChatColor.GOLD + "Dolandırıcılık ");
        meta2.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori2.setItemMeta(meta2);
        gui.setItem(11, kategori2);

        ItemStack kategori3 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta3 = kategori3.getItemMeta();
        meta3.setDisplayName(ChatColor.GOLD + "T Spawn Trap, TPA Trap");
        meta3.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori3.setItemMeta(meta3);
        gui.setItem(12, kategori3);

        ItemStack kategori4 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta4 = kategori4.getItemMeta();
        meta4.setDisplayName(ChatColor.GOLD + "Hile Bulundurmak");
        meta4.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori4.setItemMeta(meta4);
        gui.setItem(13, kategori4);

        ItemStack kategori5 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta5 = kategori5.getItemMeta();
        meta5.setDisplayName(ChatColor.GOLD + "Hile Kontrolünden Kaçmak");
        meta5.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori5.setItemMeta(meta5);
        gui.setItem(14, kategori5);

        ItemStack kategori6 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta6 = kategori6.getItemMeta();
        meta6.setDisplayName(ChatColor.GOLD + "Hile Kullanımı");
        meta6.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori6.setItemMeta(meta6);
        gui.setItem(15, kategori6);

        ItemStack kategori7 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta7 = kategori7.getItemMeta();
        meta7.setDisplayName(ChatColor.GOLD + "Etkisiz Bug Kullanımı");
        meta7.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori7.setItemMeta(meta7);
        gui.setItem(16, kategori7);

        ItemStack kategori8 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta8 = kategori8.getItemMeta();
        meta8.setDisplayName(ChatColor.GOLD + "Dini/Milli Hakaret");
        meta8.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori8.setItemMeta(meta8);
        gui.setItem(17, kategori8);

        ItemStack kategori9 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta9 = kategori9.getItemMeta();
        meta9.setDisplayName(ChatColor.GOLD + "Bug Kullanımı");
        meta9.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori9.setItemMeta(meta9);
        gui.setItem(19, kategori9);

        ItemStack kategori10 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta10 = kategori10.getItemMeta();
        meta10.setDisplayName(ChatColor.GOLD + "DOXX");
        meta10.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori10.setItemMeta(meta10);
        gui.setItem(20, kategori10);




        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(target);
        skullMeta.setDisplayName("Oyuncu: " + target.getName());
        skull.setItemMeta(skullMeta);
        gui.setItem(0, skull);

        ItemStack borderGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta borderMeta = borderGlass.getItemMeta();
        borderMeta.setDisplayName(" ");
        borderGlass.setItemMeta(borderMeta);
        gui.setItem(1, borderGlass);
        gui.setItem(2, borderGlass);
        gui.setItem(3, borderGlass);
        gui.setItem(4, borderGlass);
        gui.setItem(5, borderGlass);
        gui.setItem(6, borderGlass);
        gui.setItem(7, borderGlass);
        gui.setItem(8, borderGlass);
        gui.setItem(9, borderGlass);
        gui.setItem(18, borderGlass);


        staff.openInventory(gui);
        staff.setMetadata("banning", new FixedMetadataValue(MoonPvP.getPlugin(MoonPvP.class), "banning"));
    }

    public static void openBanGUIOffline(Player staff, org.bukkit.OfflinePlayer target) {
        Inventory gui = Bukkit.createInventory(null, 27, "Ban Panel: " + target.getName());

        // ... Kategori item'ları aynı şekilde ekle ...

        ItemStack kategori1 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta1 = kategori1.getItemMeta();
        meta1.setDisplayName(ChatColor.GOLD + "Claim Tacizi");
        meta1.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori1.setItemMeta(meta1);
        gui.setItem(10, kategori1);

        ItemStack kategori2 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta2 = kategori2.getItemMeta();
        meta2.setDisplayName(ChatColor.GOLD + "Dolandırıcılık ");
        meta2.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori2.setItemMeta(meta2);
        gui.setItem(11, kategori2);

        ItemStack kategori3 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta3 = kategori3.getItemMeta();
        meta3.setDisplayName(ChatColor.GOLD + "T Spawn Trap, TPA Trap");
        meta3.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori3.setItemMeta(meta3);
        gui.setItem(12, kategori3);

        ItemStack kategori4 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta4 = kategori4.getItemMeta();
        meta4.setDisplayName(ChatColor.GOLD + "Hile Bulundurmak");
        meta4.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori4.setItemMeta(meta4);
        gui.setItem(13, kategori4);

        ItemStack kategori5 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta5 = kategori5.getItemMeta();
        meta5.setDisplayName(ChatColor.GOLD + "Hile Kontrolünden Kaçmak");
        meta5.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori5.setItemMeta(meta5);
        gui.setItem(14, kategori5);

        ItemStack kategori6 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta6 = kategori6.getItemMeta();
        meta6.setDisplayName(ChatColor.GOLD + "Hile Kullanımı");
        meta6.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori6.setItemMeta(meta6);
        gui.setItem(15, kategori6);

        ItemStack kategori7 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta7 = kategori7.getItemMeta();
        meta7.setDisplayName(ChatColor.GOLD + "Etkisiz Bug Kullanımı");
        meta7.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori7.setItemMeta(meta7);
        gui.setItem(16, kategori7);

        ItemStack kategori8 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta8 = kategori8.getItemMeta();
        meta8.setDisplayName(ChatColor.GOLD + "Dini/Milli Hakaret");
        meta8.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori8.setItemMeta(meta8);
        gui.setItem(17, kategori8);

        ItemStack kategori9 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta9 = kategori9.getItemMeta();
        meta9.setDisplayName(ChatColor.GOLD + "Bug Kullanımı");
        meta9.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori9.setItemMeta(meta9);
        gui.setItem(19, kategori9);

        ItemStack kategori10 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta10 = kategori10.getItemMeta();
        meta10.setDisplayName(ChatColor.GOLD + "DOXX");
        meta10.setLore(Arrays.asList(" ", "Oyuncuyu Yasaklamak için tıkla."));
        kategori10.setItemMeta(meta10);
        gui.setItem(20, kategori10);


        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(target);
        skullMeta.setDisplayName("Oyuncu: " + target.getName());
        skull.setItemMeta(skullMeta);
        gui.setItem(0, skull);

        ItemStack borderGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta borderMeta = borderGlass.getItemMeta();
        borderMeta.setDisplayName(" ");
        borderGlass.setItemMeta(borderMeta);
        gui.setItem(1, borderGlass);
        gui.setItem(2, borderGlass);
        gui.setItem(3, borderGlass);
        gui.setItem(4, borderGlass);
        gui.setItem(5, borderGlass);
        gui.setItem(6, borderGlass);
        gui.setItem(7, borderGlass);
        gui.setItem(8, borderGlass);
        gui.setItem(9, borderGlass);
        gui.setItem(18, borderGlass);

        staff.openInventory(gui);
        staff.setMetadata("banning", new FixedMetadataValue(MoonPvP.getPlugin(MoonPvP.class), "banning"));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("Ban Panel: ") && event.getWhoClicked().hasMetadata("banning")) {
            event.setCancelled(true);
            Player staff = (Player) event.getWhoClicked();
            String panelTitle = event.getView().getTitle();
            String targetName = panelTitle.replace("Ban Panel: ", "");

            switch (event.getSlot()) {
                case 10:
                    staff.closeInventory();
                    staff.performCommand("ban " + targetName + " 1");
                    staff.removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 11:
                    staff.closeInventory();
                    staff.performCommand("ban " + targetName + " 2");
                    staff.removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 12:
                    staff.closeInventory();
                    staff.performCommand("ban " + targetName + " 3");
                    staff.removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 13:
                    staff.closeInventory();
                    staff.performCommand("ban " + targetName + " 4");
                    staff.removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 14:
                    staff.closeInventory();
                    staff.performCommand("ban " + targetName + " 5");
                    staff.removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 15:
                    staff.closeInventory();
                    staff.performCommand("ban " + targetName + " 6");
                    staff.removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 16:
                    staff.closeInventory();
                    staff.performCommand("ban " + targetName + " 7");
                    staff.removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 17:
                    staff.closeInventory();
                    staff.performCommand("ban " + targetName + " 8");
                    staff.removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 19:
                    staff.closeInventory();
                    staff.performCommand("ban " + targetName + " 9");
                    staff.removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 20:
                    staff.closeInventory();
                    staff.performCommand("ban " + targetName + " 10");
                    staff.removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().startsWith("Ban Panel: ") && event.getPlayer().hasMetadata("banning")) {
            event.getPlayer().removeMetadata("banning", MoonPvP.getPlugin(MoonPvP.class));
        }
    }
}