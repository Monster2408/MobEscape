package xyz.mlserver.mobescape.utils.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class MEGameJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final MobEscapeMap map;

    public MEGameJoinEvent(MobEscapeMap map, Player player) {
        this.map = map;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public MobEscapeMap getMap() {
        return map;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}