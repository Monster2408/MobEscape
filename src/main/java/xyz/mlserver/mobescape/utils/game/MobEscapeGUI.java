package xyz.mlserver.mobescape.utils.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static String getEditTitle() {
        return ChatColor.DARK_RED + "" + ChatColor.BOLD +  "Mob" + ChatColor.GOLD + "" + ChatColor.BOLD +  "Escape" + ChatColor.GRAY + ChatColor.RED + "" + ChatColor.BOLD +  "Edit";
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
        List<MobEscapeMap> maps = new ArrayList<>(MobEscapeAPI.getMapHashMap().values());
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

    public static void openEdit(MobEscapeMap map, Player player, int page) {
        String title = getEditTitle();
        int guiSize = 9*6;
        if (map != null) {
            title += " " + map.getName();
            guiSize = 9;
        }
        Inventory gui = Bukkit.createInventory(null, guiSize, title);
        if (map == null) {
            int num = (page - 1) * 45;
            List<MobEscapeMap> maps = new ArrayList<>(MobEscapeAPI.getMapHashMap().values());
            for (int i = 0; i < (guiSize - 9); i++) {
                if (maps.size() > num) {
                    map = maps.get(num);
                    gui.setItem(i, getEditMapItem(map));
                }
                num++;
            }
            if (page > 1) gui.setItem(48, getPreviousPage());
            gui.setItem(49, getPageItem(page));
            if (maps.size() > num + 45) gui.setItem(50, getNextPage());
        } else {
            gui.setItem(0, getPosItem(map));
            gui.setItem(1, getArenaItem(map));
            gui.setItem(2, getGoalItem(map));
            gui.setItem(3, getNameItem(map));
            gui.setItem(4, getIconItem(map));

            gui.setItem((guiSize - 3), getLoadItem()); // 6
            gui.setItem((guiSize - 2), getSaveItem()); // 7
        }
        gui.setItem((guiSize - 1), getClose()); // 8
        player.openInventory(gui);
    }

    public static void openEdit(MobEscapeMap map, Player player) {
        openEdit(map, player, 1);
    }

    public static boolean isPosItem(ItemStack item) {
        if (item == null) return false;
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (item.isSimilar(getPosItem(map))) return true;
        }
        return false;
    }

    public static ItemStack getPosItem(MobEscapeMap map) {
        ItemStack item = new ItemStack(Material.WOOD_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aマップ座標");
        List<String> lore = new ArrayList<>();
        lore.add("§aPos1:");
        if (map.getPos1() != null) {
            lore.add(" §7World: " + map.getPos1().getWorld().getName());
            lore.add(" §7X: " + map.getPos1().getBlockX());
            lore.add(" §7Y: " + map.getPos1().getBlockY());
            lore.add(" §7Z: " + map.getPos1().getBlockZ());
        } else {
            lore.add(" §c未設定");
        }
        lore.add("§aPos2:");
        if (map.getPos2() != null) {
            lore.add(" §7World: " + map.getPos2().getWorld().getName());
            lore.add(" §7X: " + map.getPos2().getBlockX());
            lore.add(" §7Y: " + map.getPos2().getBlockY());
            lore.add(" §7Z: " + map.getPos2().getBlockZ());
        } else {
            lore.add(" §c未設定");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isArenaItem(ItemStack item) {
        if (item == null) return false;
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (item.isSimilar(getArenaItem(map))) return true;
        }
        return false;
    }

    public static ItemStack getArenaItem(MobEscapeMap map) {
        ItemStack item = new ItemStack(Material.WOOD_DOOR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aアリーナ");
        List<String> lore = new ArrayList<>();
        lore.add("§aArena:");
        if (map.getArenaLobby() != null) {
            lore.add(" §7World: " + map.getArenaLobby().getWorld().getName());
            lore.add(" §7X: " + map.getArenaLobby().getBlockX());
            lore.add(" §7Y: " + map.getArenaLobby().getBlockY());
            lore.add(" §7Z: " + map.getArenaLobby().getBlockZ());
        } else {
            lore.add(" §c未設定");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getLoadItem() {
        ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aマップ読み込み");
        List<String> lore = new ArrayList<>();
        lore.add("§aマップを読み込みます");
        lore.add("§aマップを読み込むと、");
        lore.add("§aマップの座標がリセットされます");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getSaveItem() {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aマップ保存");
        List<String> lore = new ArrayList<>();
        lore.add("§aマップを保存します");
        lore.add("§aマップを保存すると、");
        lore.add("§aマップの座標がリセットされます");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
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
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (item.isSimilar(getMapItem(map))) return map;
        }
        return null;
    }

    public static ItemStack getEditMapItem(MobEscapeMap map) {
        ItemStack item = new ItemStack(map.getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(map.getName());
        List<String> lore = new ArrayList<>();
        if (map.getPos1() != null) lore.add("§aPos1: [" + map.getPos1().getBlockX() + ", " + map.getPos1().getBlockY() + ", " + map.getPos1().getBlockZ() + "]");
        else lore.add("§cPos1: 未設定");
        if (map.getPos2() != null) lore.add("§aPos2: [" + map.getPos2().getBlockX() + ", " + map.getPos2().getBlockY() + ", " + map.getPos2().getBlockZ() + "]");
        else lore.add("§cPos2: 未設定");
        if (map.getGoalPos1() != null) lore.add("§aGoalPos1: [" + map.getGoalPos1().getBlockX() + ", " + map.getGoalPos1().getBlockY() + ", " + map.getGoalPos1().getBlockZ() + "]");
        else lore.add("§cGoalPos1: 未設定");
        if (map.getGoalPos2() != null) lore.add("§aGoalPos2: [" + map.getGoalPos2().getBlockX() + ", " + map.getGoalPos2().getBlockY() + ", " + map.getGoalPos2().getBlockZ() + "]");
        else lore.add("§cGoalPos2: 未設定");
        if (map.getArenaLobby() != null) lore.add("§aArena: [" + map.getArenaLobby().getBlockX() + ", " + map.getArenaLobby().getBlockY() + ", " + map.getArenaLobby().getBlockZ() + "]");
        else lore.add("§cArena: 未設定");
        if (map.getMaterial() != null) lore.add("§aIcon: " + map.getMaterial().name());
        else lore.add("§cIcon: 未設定");
        lore.add("§aMaxPlayer: " + map.getMaxPlayer());
        lore.add("§aMinPlayer: " + map.getMinPlayer());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isEditMapItem(ItemStack item) {
        if (item == null) return false;
        return (getEditMap(item) != null);
    }

    public static MobEscapeMap getEditMap(ItemStack item) {
        if (item == null) return null;
        if (item.getItemMeta() == null) return null;
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (item.isSimilar(getEditMapItem(map))) return map;
        }
        return null;
    }

    public static boolean isNameItem(ItemStack item) {
        if (item == null) return false;
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (item.isSimilar(getNameItem(map))) return true;
        }
        return false;
    }

    public static ItemStack getNameItem(MobEscapeMap map) {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aマップ名変更");
        List<String> lore = new ArrayList<>();
        lore.add("§aマップの名前を変更します");
        lore.add("§a現在の名前: " + map.getName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isIconItem(ItemStack item) {
        if (item == null) return false;
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (item.isSimilar(getIconItem(map))) return true;
        }
        return false;
    }

    public static ItemStack getIconItem(MobEscapeMap map) {
        ItemStack item = new ItemStack(Material.PAINTING);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aアイコン変更");
        List<String> lore = new ArrayList<>();
        lore.add("§aマップのアイコンを変更します");
        lore.add("§a現在のアイコン: " + map.getMaterial().name());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isGoalItem(ItemStack item) {
        if (item == null) return false;
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (item.isSimilar(getGoalItem(map))) return true;
        }
        return false;
    }

    public static ItemStack getGoalItem(MobEscapeMap map) {
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aゴール変更");
        List<String> lore = new ArrayList<>();
        lore.add("§aマップのゴールを変更します");
        if (map.getGoalPos1() != null) lore.add("§aGoalPos1: [" + map.getGoalPos1().getBlockX() + ", " + map.getGoalPos1().getBlockY() + ", " + map.getGoalPos1().getBlockZ() + "]");
        else lore.add("§cGoalPos1: 未設定");
        if (map.getGoalPos2() != null) lore.add("§aGoalPos2: [" + map.getGoalPos2().getBlockX() + ", " + map.getGoalPos2().getBlockY() + ", " + map.getGoalPos2().getBlockZ() + "]");
        else lore.add("§cGoalPos2: 未設定");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
