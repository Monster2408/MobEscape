package xyz.mlserver.mobescape.utils.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class MEGameGoalEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final MobEscapeMap map;
    private final Double time;

    public MEGameGoalEvent(MobEscapeMap map, Player player, Double time) {
        this.map = map;
        this.player = player;
        this.time = time;
    }

    public Player getPlayer() {
        return player;
    }

    public MobEscapeMap getMap() {
        return map;
    }

    public Double getTime() {
        return time;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}