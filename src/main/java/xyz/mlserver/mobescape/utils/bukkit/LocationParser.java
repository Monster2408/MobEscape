package xyz.mlserver.mobescape.utils.bukkit;

import com.google.gson.Gson;
import org.bukkit.Location;

public class LocationParser {

    public static Location parseLocation(String json){
        Gson gson = new Gson();
        LocationJson locJson = gson.fromJson(json, LocationJson.class);
        if(locJson != null) return locJson.getLocation();
        return null;
    }

    public static LocationJson parseLocationJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, LocationJson.class);
    }

    public static String parseJson(LocationJson locJson){
        Gson gson = new Gson();
        return gson.toJson(locJson);
    }

    public static String parseJson(Location loc){
        Gson gson = new Gson();
        LocationJson locJson = new LocationJson(loc);
        return gson.toJson(locJson);
    }

}
