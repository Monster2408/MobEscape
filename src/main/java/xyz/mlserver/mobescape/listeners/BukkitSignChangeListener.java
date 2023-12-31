package xyz.mlserver.mobescape.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class BukkitSignChangeListener implements Listener {

    @EventHandler
    public void on(SignChangeEvent e) {
        Player player = e.getPlayer();
        if (e.getLine(0).equalsIgnoreCase("[MobEscape]")) {
            if (!player.hasPermission("mobescape.sign.create")) return;
            if (e.getLine(1).isEmpty()) {
                player.sendMessage("§c§l[§4§lMobEscape§c§l] §c第2行目にはマップIDもしくは「Leave」を入力してください。");
                return;
            }
            if (e.getLine(1).equalsIgnoreCase("Leave")) {
                String lineText;
                for (int line = 0; line < 4; line++) {
                    lineText = MobEscapeAPI.getLeaveSignText(line);
                    if (lineText != null) e.setLine(line, lineText);
                }
                player.sendMessage("§c§l[§4§lMobEscape§c§l] §a看板を作成しました。");
                return;
            }
            for (MobEscapeMap map : MobEscapeAPI.getMapHashMap().values()) {
                if (map.getId() == Integer.parseInt(e.getLine(1))) {
                    String lineText;
                    for (int line = 0; line < 4; line++) {
                        lineText = MobEscapeAPI.getJoinSignText(line, map);
                        if (lineText != null) e.setLine(line, lineText);
                    }
                    player.sendMessage("§c§l[§4§lMobEscape§c§l] §a看板を作成しました。");
                    MobEscapeAPI.addSign(map, e.getBlock().getLocation());
                    return;
                }
            }
            player.sendMessage("§c§l[§4§lMobEscape§c§l] §cマップIDが見つかりませんでした。");
        }
    }

}
