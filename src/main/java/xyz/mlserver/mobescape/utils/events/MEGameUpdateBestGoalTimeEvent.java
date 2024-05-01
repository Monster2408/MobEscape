package xyz.mlserver.mobescape.utils.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class MEGameUpdateBestGoalTimeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final MobEscapeMap map;
    private final Integer beforeTime;
    private final Integer bestTime;

    public MEGameUpdateBestGoalTimeEvent(MobEscapeMap map, Player player, Integer beforeTime, Integer bestTime) {
        this.map = map;
        this.player = player;
        this.beforeTime = beforeTime;
        this.bestTime = bestTime;
    }

    public Player getPlayer() {
        return player;
    }

    public MobEscapeMap getMap() {
        return map;
    }

    public Integer getBeforeTime() {
        return beforeTime;
    }

    public Integer getBestTime() {
        return bestTime;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}