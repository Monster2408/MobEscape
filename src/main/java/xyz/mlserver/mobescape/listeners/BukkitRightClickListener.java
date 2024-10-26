package xyz.mlserver.mobescape.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

import java.util.ArrayList;

public class BukkitRightClickListener implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        Player player = e.getPlayer();
        if (player.getInventory().getItemInMainHand() == null) return;
        ItemStack item = player.getInventory().getItemInMainHand().clone();
        if (item.isSimilar(MobEscapeAPI.getLeaveItem())) {
            e.setCancelled(true);
            for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
                if (MobEscapeAPI.getMembers(map).contains(player)) {
                    map.leave(player);
                    return;
                }
            }
        } else if (item.isSimilar(MobEscapeAPI.getStartVoteItem())) {
            e.setCancelled(true);
            for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
                if (MobEscapeAPI.getMembers(map).contains(player)) {
                    MobEscapeAPI.getMapStartVote().putIfAbsent(map, new ArrayList<>());
                    if (!MobEscapeAPI.getMapStartVote().get(map).contains(player.getUniqueId())) {
                        MobEscapeAPI.getMapStartVote().get(map).add(player.getUniqueId());
                        player.sendMessage("§c§l[§4§lMobEscape§c§l] §a投票しました。");
                    } else {
                        player.sendMessage("§c§l[§4§lMobEscape§c§l] §c既に投票しています。");
                    }
                    return;
                }
            }
        } else if (item.isSimilar(MobEscapeAPI.getAnotherPlayerShowTool())) {
            MobEscapeAPI.showPlayers(player);
            player.getInventory().setItem(1, MobEscapeAPI.getAnotherPlayerHideTool());
        } else if (item.isSimilar(MobEscapeAPI.getAnotherPlayerHideTool())) {
            MobEscapeAPI.hidePlayers(player);
            player.getInventory().setItem(1, MobEscapeAPI.getAnotherPlayerShowTool());
        }
    }

}
