package xyz.mlserver.mobescape.utils.game;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.mlserver.mobescape.MobEscape;
import xyz.mlserver.mobescape.utils.bukkit.LocationParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Arena {

    private String owner;
    private String name;
    private final int id;
    private int maxPlayer;
    private int minPlayer;
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

    // ---------------------------- //

    private static final File file = new File(MobEscape.getPlugin().getDataFolder() + "/arena");

    public static boolean checkFile() {
        if (!file.exists()) return file.mkdirs();
        return true;
    }

    private static List<Arena> arenas;

    public static List<Arena> getArenas(){
        if (arenas == null) arenas = new ArrayList<>();
        return arenas;
    }

    private static HashMap<Integer, Arena> arenaMap;

    public static HashMap<Integer, Arena> getArenaMap(){
        if (arenaMap == null) arenaMap = new HashMap<>();
        return arenaMap;
    }

    private static HashMap<Player, Arena> editingArena;

    public static HashMap<Player, Arena> getEditingArena(){
        if (editingArena == null) editingArena = new HashMap<>();
        return editingArena;
    }

    public static void createArena(Player player, String name){
        int id = getNewId();
        Arena arena = new Arena(player.getUniqueId().toString(), name, id, null);
        getArenaMap().put(id, arena);
        getEditingArena().put(player, arena);
    }

    public static void saveArena(){
        if (getArenas().isEmpty()) return;
        checkFile();
        for (Arena arena : getArenas()) {
            File file = new File(MobEscape.getPlugin().getDataFolder() + "/arena/" + arena.getId() + ".yml");
            YamlConfiguration yaml = parseYaml(arena);
            try {
                yaml.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadArena(){
        checkFile();
        if (file == null) return;
        getArenaMap().clear();
        for (File file1 : Objects.requireNonNull(file.listFiles())) {
            if (file1.getName().endsWith(".yml")) {
                String fileName = file1.getName().replace(".yml", "");
                int id = Integer.parseInt(fileName);
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file1);
                Arena arena = parseArena(yaml);
                getArenaMap().put(id, arena);
            }
        }
    }

    public static YamlConfiguration parseYaml(Arena arena){
        Gson gson = new Gson();
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("data", gson.toJson(arena));
        return yaml;
    }

    public static Arena parseArena(YamlConfiguration yaml){
        Gson gson = new Gson();
        return gson.fromJson(yaml.getString("data"), Arena.class);
    }

    public static int getNewId() {
        int id = 0;
        checkFile();
        while (true) {
            if (!getArenaMap().containsKey(id)) return id;
            id++;
        }
    }

}
