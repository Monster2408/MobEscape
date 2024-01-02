package xyz.mlserver.mobescape.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class BukkitPlayerDamageByEntityListener implements Listener {

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (e.getEntity() == null) return;
        if (e.getDamager() == null) return;
        if (!(e.getEntity() instanceof Player)) return;
        Entity attackerEntity = e.getDamager();
        Player victim = (Player) e.getEntity();
        for (MobEscapeMap map: MobEscapeAPI.getMapHashMap().values()) {
            if (!MobEscapeAPI.getMembers(map).contains(victim)) continue;
            if (MobEscapeAPI.getMob(map).getEntity() != attackerEntity) continue;
            map.death(victim, MobEscapeMap.DeathReason.DEATH);
        }
    }

}
