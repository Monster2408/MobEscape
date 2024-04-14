package xyz.mlserver.mobescape.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class BukkitPlayerDeathEvent implements Listener {

    @EventHandler
    public void on(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (e.getDamage() < player.getHealth()) return;
        for (MobEscapeMap map: MobEscapeAPI.getMapHashMap().values()) {
            if (!MobEscapeAPI.getMembers(map).contains(player)) continue;
            e.setCancelled(true);
            map.death(player, MobEscapeMap.DeathReason.DEATH);
        }
    }

}
