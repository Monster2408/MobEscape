package xyz.mlserver.mobescape.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class BukkitPlayerFoodLevelChangeListener implements Listener {

    @EventHandler
    public void on(FoodLevelChangeEvent e) {
        if (e.getEntity() == null) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (!MobEscapeAPI.getMembers(map).contains(player)) continue;
            e.setCancelled(true);
        }
    }

}
