package xyz.mlserver.mobescape.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class BukkitPlayerQuitListener implements Listener {

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (MobEscapeAPI.getMembers(map).contains(player)) {
                map.leave(player);
            }
        }
    }

}
