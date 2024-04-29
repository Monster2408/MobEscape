package xyz.mlserver.mobescape.utils.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

public class MEGameDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final MobEscapeMap map;
    private final MobEscapeMap.DeathReason reason;

    public MEGameDeathEvent(MobEscapeMap map, Player player, MobEscapeMap.DeathReason reason) {
        this.map = map;
        this.player = player;
        this.reason = reason;
    }

    public Player getPlayer() {
        return player;
    }

    public MobEscapeMap getMap() {
        return map;
    }

    public MobEscapeMap.DeathReason getReason() {
        return reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}