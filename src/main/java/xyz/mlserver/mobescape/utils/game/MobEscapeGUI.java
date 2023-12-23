package xyz.mlserver.mobescape.utils.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MobEscapeGUI {

    public static void open(Player player, int page) {
        String title = "MobEscape";
        Inventory gui = Bukkit.createInventory(null, 9*6, title);
        int num = (page - 1) * 45;
        ItemStack item = null;
        ItemMeta meta = null;
        List<MobEscapeMap> maps = new ArrayList<>(MobEscapeMap.getMapHashMap().values());
        MobEscapeMap map;
        for (int i = 0; i < 45; i++) {
            if (maps.size() > num) {
                map = maps.get(num);
                item = new ItemStack(map.getMaterial());
                meta = item.getItemMeta();
                meta.setDisplayName(map.getName());
                item.setItemMeta(meta);
                gui.setItem(i, item);
            }
        }
        player.openInventory(gui);
    }

    public static void open(Player player) {
        open(player, 1);
    }

}
