package xyz.mlserver.mobescape.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.mlserver.mobescape.utils.game.MobEscapeGUI;

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

}
