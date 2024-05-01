package xyz.mlserver.mobescape.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.mlserver.mobescape.utils.MobEscapeDB;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;

public class BukkitPlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        MobEscapeDB.getPlayerNameMap().put(player.getUniqueId().toString(), player.getName());
        MobEscapeAPI.showPlayers(player);
    }

}
