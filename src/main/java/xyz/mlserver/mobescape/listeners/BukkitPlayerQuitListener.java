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
        String uuidStr = player.getUniqueId().toString();
        for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
            if (map.getMembers().contains(uuidStr)) {
                map.leave(player);
            }
        }
    }

}
