package xyz.mlserver.mobescape.listeners;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.mlserver.mobescape.utils.game.MobEscapeGUI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class MEGUIClickListener implements Listener {

    /**
     * メインGUIのクリックイベント
     */
    @EventHandler
    public void on(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().getName().equalsIgnoreCase(MobEscapeGUI.getTitle())) return;
        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();

        if (e.getCurrentItem().isSimilar(MobEscapeGUI.getNextPage())) {
            MobEscapeGUI.open(player, MobEscapeGUI.getPage(e.getClickedInventory()) + 1);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        } else if (e.getCurrentItem().isSimilar(MobEscapeGUI.getPreviousPage())) {
            MobEscapeGUI.open(player, MobEscapeGUI.getPage(e.getClickedInventory()) - 1);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        } else if (e.getCurrentItem().isSimilar(MobEscapeGUI.getClose())) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        } else if (MobEscapeGUI.isMapItem(e.getCurrentItem())) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            MobEscapeGUI.getMap(e.getCurrentItem()).join(player);
        }
    }

    /**
     * 編集GUIのクリックイベント
     */
    @EventHandler
    public void onEdit(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().getName().startsWith(MobEscapeGUI.getEditTitle())) return;
        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();

        for (MobEscapeMap map : MobEscapeMap.getMapHashMap().values()) {
            if (e.getClickedInventory().getName().equalsIgnoreCase(MobEscapeGUI.getEditTitle() + " " + map.getName())) {
                if (MobEscapeGUI.isArenaItem(e.getCurrentItem())) {
                    TextComponent message = new TextComponent(ChatColor.GOLD + "クリックでアリーナを設定する。");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mobescape set arena"));
                    player.spigot().sendMessage(message);
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    return;
                } else if (MobEscapeGUI.isPosItem(e.getCurrentItem())) {
                    TextComponent message = new TextComponent(ChatColor.GOLD + "クリックでPos1を設定する。");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mobescape set pos1"));
                    player.spigot().sendMessage(message);
                    message = new TextComponent(ChatColor.GOLD + "クリックでPos2を設定する。");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mobescape set pos2"));
                    player.spigot().sendMessage(message);
                    message = new TextComponent(ChatColor.GOLD + "クリックでPosを設定する。(WE使用)");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mobescape set pos"));
                    player.spigot().sendMessage(message);
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    return;
                } else if (MobEscapeGUI.isGoalItem(e.getCurrentItem())) {
                    TextComponent message = new TextComponent(ChatColor.GOLD + "クリックでGoalPosを設定する。(WE使用)");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mobescape set goal"));
                    player.spigot().sendMessage(message);
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    return;
                } else if (MobEscapeGUI.isNameItem(e.getCurrentItem())) {
                    TextComponent message = new TextComponent(ChatColor.GOLD + "クリックで名前を変更するコマンドをサジェストする。");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mobescape set name "));
                    player.spigot().sendMessage(message);
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    return;
                } else if (MobEscapeGUI.isIconItem(e.getCurrentItem())) {
                    TextComponent message = new TextComponent(ChatColor.GOLD + "クリックでアイコンを変更するコマンドをサジェストする。(手にアイテムを持ちながら実行)");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mobescape set icon"));
                    player.spigot().sendMessage(message);
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    return;
                } else if (e.getCurrentItem().isSimilar(MobEscapeGUI.getLoadItem())) {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    MobEscapeMap.loadMap(map);
                    MobEscapeGUI.openEdit(map, player);
                    player.sendMessage(ChatColor.GREEN + "マップをロードしました。");
                    return;
                } else if (e.getCurrentItem().isSimilar(MobEscapeGUI.getSaveItem())) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    MobEscapeMap.saveMap(map);
                    player.sendMessage(ChatColor.GREEN + "マップを保存しました。");
                    return;
                } else if (e.getCurrentItem().isSimilar(MobEscapeGUI.getClose())) {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    MobEscapeMap.setEditingMap(player, null);
                    MobEscapeGUI.openEdit(null, player);
                    return;
                }
            } else {
                if (e.getCurrentItem().isSimilar(MobEscapeGUI.getEditMapItem(map))) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    MobEscapeMap.setEditingMap(player, map);
                    MobEscapeGUI.openEdit(map, player);
                    return;
                } else if (e.getCurrentItem().isSimilar(MobEscapeGUI.getClose())) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    return;
                }
            }
        }
    }

}
