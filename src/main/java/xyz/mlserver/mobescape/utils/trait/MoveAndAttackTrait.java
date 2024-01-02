package xyz.mlserver.mobescape.utils.trait;

import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class MoveAndAttackTrait extends Trait implements Listener {
    public MoveAndAttackTrait() {
        super("moveandattack");
    }

    @Override
    public void run() {
        // NPCが動作する際に呼び出される
        if (npc.isSpawned()) {
            // 移動中でかつ目標に近づいたら攻撃する
            attack();
        }
    }

    private void attack() {
        Location location = npc.getEntity().getLocation().clone();
        location.getWorld().getNearbyEntities(location, 5, 5, 5).forEach(entity -> {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                for (MobEscapeMap map : MobEscapeAPI.getMobMap().keySet()) {
                    if (MobEscapeAPI.getMobMap().get(map).equals(npc)) {
                        if (MobEscapeAPI.getMembers(map).contains(player)) {
                            map.death(player, MobEscapeMap.DeathReason.MOB_DAMAGE);
                        }
                    }
                }
            }
        });
    }
}