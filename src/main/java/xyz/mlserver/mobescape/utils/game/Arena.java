package xyz.mlserver.mobescape.utils.game;

import org.bukkit.Location;
import xyz.mlserver.mobescape.utils.bukkit.LocationParser;

public class Arena {

    private String owner;
    private String name;
    private final int id;
    private int maxPlayer = -1;
    private int minPlayer = -1;
    private String spawn;

    public Arena(String owner, String name, int id, String spawn) {
        this.owner = owner;
        this.name = name;
        this.id = id;
        this.maxPlayer = -1;
        this.minPlayer = -1;
        this.spawn = spawn;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public Location getSpawn() {
        if (spawn == null) return null;
        return LocationParser.parseLocation(spawn);
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public void setMinPlayer(int minPlayer) {
        this.minPlayer = minPlayer;
    }

    public void setSpawn(Location spawn) {
        this.spawn = LocationParser.parseJson(spawn);
    }

}
