package xyz.mlserver.mobescape.utils.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MobEscapeGUI {

    public static ItemStack getNextPage() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a次のページへ");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPreviousPage() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a前のページへ");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getClose() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§c閉じる");
        item.setItemMeta(meta);
        return item;
    }

    public static String getTitle() {
        return ChatColor.DARK_RED + "" + ChatColor.BOLD +  "Mob" + ChatColor.GOLD + "" + ChatColor.BOLD +  "Escape";
    }

    public static ItemStack getPageItem(int page) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(String.valueOf(page));
        item.setItemMeta(meta);
        return item;
    }

    public static int getPage(Inventory gui) {
        ItemStack item = gui.getItem(49);
        if (item == null) return -1;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return -1;
        String name = meta.getDisplayName();
        if (name == null) return -1;
        return Integer.parseInt(name);
    }

    public static void open(Player player, int page) {
        String title = getTitle();
        Inventory gui = Bukkit.createInventory(null, 9*6, title);
        int num = (page - 1) * 45;
        List<MobEscapeMap> maps = new ArrayList<>(MobEscapeMap.getMapHashMap().values());
        MobEscapeMap map;
        for (int i = 0; i < 45; i++) {
            if (maps.size() > num) {
                map = maps.get(num);
                gui.setItem(i, getMapItem(map));
            }
            num++;
        }
        if (page > 1) gui.setItem(48, getPreviousPage());
        gui.setItem(49, getPageItem(page));
        if (maps.size() > num + 45) gui.setItem(50, getNextPage());
        gui.setItem(53, getClose());
        player.openInventory(gui);
    }

    public static void open(Player player) {
        open(player, 1);
    }

    public static ItemStack getMapItem(MobEscapeMap map) {
        ItemStack item = new ItemStack(map.getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(map.getName());
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isMapItem(ItemStack item) {
        if (item == null) return false;
        return (getMap(item) != null);
    }

    public static MobEscapeMap getMap(ItemStack item) {
        if (item == null) return null;
        if (item.getItemMeta() == null) return null;
        for (MobEscapeMap map : MobEscapeMap.getMapHashMap().values()) {
            if (item.isSimilar(getMapItem(map))) return map;
        }
        return null;
    }

}
