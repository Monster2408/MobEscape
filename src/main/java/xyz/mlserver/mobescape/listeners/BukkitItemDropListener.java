package xyz.mlserver.mobescape.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class BukkitItemDropListener implements Listener {

    @EventHandler
    public void on(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (MobEscapeAPI.getMembers(map).contains(player)) {
                e.setCancelled(true);
                return;
            }
        }
    }

}
