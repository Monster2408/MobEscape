package xyz.mlserver.mobescape.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitPlayerQuitListener implements Listener {

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player player = e.getPlayer();
    }

}
