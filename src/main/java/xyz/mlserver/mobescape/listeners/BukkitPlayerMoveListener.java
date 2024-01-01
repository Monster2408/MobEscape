package xyz.mlserver.mobescape.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.GamePhase;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class BukkitPlayerMoveListener implements Listener {

    @EventHandler
    public void on(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        for (MobEscapeMap map: MobEscapeAPI.getMapHashMap().values()) {
            if (!MobEscapeAPI.getGamePhaseMap().containsKey(map)) continue;
            if (!MobEscapeAPI.getMembers(map).contains(player)) continue;
            if (MobEscapeAPI.getGamePhaseMap().get(map) == GamePhase.READY) {
                if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
                    e.setCancelled(true);
                    return;
                }
            } else if (MobEscapeAPI.getGamePhaseMap().get(map) == GamePhase.GAME) {
                if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
                if (map.getGoalPos1().getX() <= player.getLocation().getX() && player.getLocation().getX() <= map.getGoalPos2().getX()) {
                    if (map.getGoalPos1().getZ() <= player.getLocation().getZ() && player.getLocation().getZ() <= map.getGoalPos2().getZ()) {
                        map.goal(player);
                    }
                } else if (map.getUnderY() >= player.getLocation().getY()) {
                    map.death(player, MobEscapeMap.DeathReason.UNDER_Y);
                }
            }
        }
    }

}
