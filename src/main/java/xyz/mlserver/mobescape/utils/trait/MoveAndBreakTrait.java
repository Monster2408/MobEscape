package xyz.mlserver.mobescape.utils.trait;

import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;

public class MoveAndBreakTrait extends Trait implements Listener {
    public MoveAndBreakTrait() {
        super("moveandbreak");
    }

    @Override
    public void run() {
        // NPCが動作する際に呼び出される
        if (npc.isSpawned()) {
            // 移動中でかつ目標に近づいたら攻撃する
            breakBlock();
        }
    }

    private void breakBlock() {
        Location location = npc.getEntity().getLocation().clone();
        // locationの半径5ブロック以内のブロックをAIRにする
        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -5; z <= 5; z++) {
                    Block block = location.clone().add(x, y, z).getBlock();
                    if (block.getType() != Material.AIR) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}
