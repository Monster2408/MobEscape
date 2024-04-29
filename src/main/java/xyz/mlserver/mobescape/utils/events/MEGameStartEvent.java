package xyz.mlserver.mobescape.utils.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class MEGameStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final MobEscapeMap map;

    public MEGameStartEvent(MobEscapeMap map) {
        this.map = map;
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