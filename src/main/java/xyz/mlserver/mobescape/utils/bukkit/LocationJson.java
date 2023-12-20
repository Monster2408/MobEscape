package xyz.mlserver.mobescape.utils.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationJson {
    private final Double x;
    private final Double y;
    private final Double z;
    private final Float pitch;
    private final Float yaw;
    private final String world;

    /**
     *
     * @param location
     */
    public LocationJson(Location location) {
        x = location.getX();
        y = location.getY();
        z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        world = location.getWorld().getName();
    }

    public Location getLocation(){
        World world = null;
        for (World target : Bukkit.getWorlds()) {
            if (target.getName().equalsIgnoreCase(this.world)) world = target;
        }
        if(world != null) return new Location(world, x, y, z, yaw, pitch);
        return null;
    }
}
