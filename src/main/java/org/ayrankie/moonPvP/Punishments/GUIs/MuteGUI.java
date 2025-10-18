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

public class MuteGUI implements Listener {

    public static void openMuteGUI(Player staff, Player target, String targetName) {
        Inventory gui = Bukkit.createInventory(null, 27, "Mute Panel: " + targetName);


        ItemStack kategori1 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta1 = kategori1.getItemMeta();
        meta1.setDisplayName(ChatColor.GOLD + "Sohbet Düzenini Bozmak");
        meta1.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori1.setItemMeta(meta1);
        gui.setItem(10, kategori1);

        ItemStack kategori2 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta2 = kategori2.getItemMeta();
        meta2.setDisplayName(ChatColor.GOLD + "Kışkırtma/Aşağılama");
        meta2.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori2.setItemMeta(meta2);
        gui.setItem(11, kategori2);

        ItemStack kategori3 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta3 = kategori3.getItemMeta();
        meta3.setDisplayName(ChatColor.GOLD + "Hakaret/Argo");
        meta3.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori3.setItemMeta(meta3);
        gui.setItem(12, kategori3);

        ItemStack kategori4 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta4 = kategori4.getItemMeta();
        meta4.setDisplayName(ChatColor.GOLD + "Kısaltılmış Küfür");
        meta4.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori4.setItemMeta(meta4);
        gui.setItem(13, kategori4);

        ItemStack kategori5 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta5 = kategori5.getItemMeta();
        meta5.setDisplayName(ChatColor.GOLD + "Cinsel İçerikli İfade");
        meta5.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori5.setItemMeta(meta5);
        gui.setItem(14, kategori5);

        ItemStack kategori6 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta6 = kategori6.getItemMeta();
        meta6.setDisplayName(ChatColor.GOLD + "Küfür");
        meta6.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori6.setItemMeta(meta6);
        gui.setItem(15, kategori6);

        ItemStack kategori7 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta7 = kategori7.getItemMeta();
        meta7.setDisplayName(ChatColor.GOLD + "Ailevi Küfür İması");
        meta7.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori7.setItemMeta(meta7);
        gui.setItem(16, kategori7);

        ItemStack kategori8 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta8 = kategori8.getItemMeta();
        meta8.setDisplayName(ChatColor.GOLD + "Ailevi Küfür");
        meta8.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori8.setItemMeta(meta8);
        gui.setItem(17, kategori8);

        ItemStack kategori9 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta9 = kategori9.getItemMeta();
        meta9.setDisplayName(ChatColor.GOLD + "Kişisel Reklam");
        meta9.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori9.setItemMeta(meta9);
        gui.setItem(19, kategori9);

        ItemStack kategori10 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta10 = kategori10.getItemMeta();
        meta10.setDisplayName(ChatColor.GOLD + "Yetkiliye Saygısızlık");
        meta10.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori10.setItemMeta(meta10);
        gui.setItem(20, kategori10);

        ItemStack kategori11 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta11 = kategori11.getItemMeta();
        meta11.setDisplayName(ChatColor.GOLD + "Dini/Milli Değerlere Saygısızlık");
        meta11.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori11.setItemMeta(meta11);
        gui.setItem(21, kategori11);

        ItemStack kategori12 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta12 = kategori2.getItemMeta();
        meta12.setDisplayName(ChatColor.GOLD + "Irkçılık");
        meta12.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori2.setItemMeta(meta12);
        gui.setItem(22, kategori2);

        ItemStack kategori13 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta13 = kategori13.getItemMeta();
        meta13.setDisplayName(ChatColor.GOLD + "Uzayan Din/Siyaset Tartışması");
        meta13.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori13.setItemMeta(meta13);
        gui.setItem(23, kategori13);

        ItemStack kategori14 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta14 = kategori14.getItemMeta();
        meta14.setDisplayName(ChatColor.GOLD + "Sunucu İsim Paylaşımı / Reklam");
        meta14.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori14.setItemMeta(meta14);
        gui.setItem(24, kategori14);

        ItemStack kategori15 = new ItemStack(Material.RED_CONCRETE);
        ItemMeta meta15 = kategori15.getItemMeta();
        meta15.setDisplayName(ChatColor.GOLD + "Kampanya Başlatmak");
        meta15.setLore(Arrays.asList(" ", "Oyuncuyu Susturmak için tıkla."));
        kategori15.setItemMeta(meta15);
        gui.setItem(25, kategori15);

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (target != null) {
            skullMeta.setOwningPlayer(target);
        }
        skullMeta.setDisplayName("Oyuncu: " + targetName);
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
        staff.setMetadata("muting", new FixedMetadataValue(MoonPvP.getPlugin(MoonPvP.class), "muting"));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("Mute Panel: ") && event.getWhoClicked().hasMetadata("muting")) {
            event.setCancelled(true); // GUI'de işlem yapılmasın
            Player staff = (Player) event.getWhoClicked();
            String panelTitle = event.getView().getTitle();
            String targetName = panelTitle.replace("Mute Panel: ", "");

            switch (event.getSlot()) {
                case 10:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 1");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 11:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 2");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 13:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 3");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 14:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 4");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 15:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 5");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 16:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 6");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 17:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 7");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 19:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 8");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 20:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 9");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 21:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 10");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 22:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 11");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 23:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 12");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 24:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 13");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 25:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 14");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                case 26:
                    staff.closeInventory();
                    staff.performCommand("mute " + targetName + " 15");
                    staff.removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().startsWith("Mute Panel: ") && event.getPlayer().hasMetadata("muting")) {
            event.getPlayer().removeMetadata("muting", MoonPvP.getPlugin(MoonPvP.class));
        }
    }
}