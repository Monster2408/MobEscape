package xyz.mlserver.mobescape.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class BukkitBlockClickListener implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        Player player = e.getPlayer();
        String uuidStr = player.getUniqueId().toString();
        if (!MobEscapeAPI.getSignTypeList().contains(e.getClickedBlock().getType())) return;
        Sign sign = (Sign) e.getClickedBlock().getState();
        String leaveLine0Text = MobEscapeAPI.getLeaveSignText(0);
        if (leaveLine0Text != null && leaveLine0Text.equalsIgnoreCase(sign.getLine(0))) {
            for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
                if (MobEscapeAPI.getMembers(map).contains(player)) {
                    map.leave(player);
                    return;
                }
            }
        }
        String joinLine0Text = MobEscapeAPI.getJoinSignText(0, null);
        if (joinLine0Text != null && joinLine0Text.equalsIgnoreCase(sign.getLine(0))) {
            String idText = sign.getLine(1).split("ID: " + ChatColor.RED)[1];
            if (idText == null) return;
            int id = Integer.parseInt(idText);
            for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
                if (map.getId() == id) {
                    map.join(player);
                    return;
                }
            }
        }

    }
}
