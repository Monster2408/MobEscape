package xyz.mlserver.mobescape.utils.game;

import com.google.gson.Gson;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.mlserver.mobescape.MobEscape;
import xyz.mlserver.mobescape.utils.bukkit.LocationParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MobEscapeMap {

    private String name;
    private final int id;
    private int maxPlayer;
    private int minPlayer;
    private List<String> spawns;
    private String arenaLobby;
    private String material;

    public MobEscapeMap(String name, int id) {
        this.name = name;
        this.id = id;
        this.maxPlayer = -1;
        this.minPlayer = -1;
        this.spawns = new ArrayList<>();
        this.arenaLobby = null;
        this.material = Material.GRASS.name();
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

    public List<Location> getSpawns() {
        if (spawns == null) return null;
        if (spawns.isEmpty()) return null;
        List<Location> list = new ArrayList<>();
        Location loc;
        for (String spawn : spawns) {
            loc = LocationParser.parseLocation(spawn);
            if (loc == null) continue;
            list.add(loc.clone());
        }
        return list;
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

    public void addSpawn(Location spawn) {
        getSpawns().add(spawn.clone());
    }

    public void deleteSpawn() {
        getSpawns().clear();
    }

    public void setArenaLobby(Location arenaLobby) {
        this.arenaLobby = LocationParser.parseJson(arenaLobby);
    }

    public Location getArenaLobby() {
        return LocationParser.parseLocation(arenaLobby);
    }

    public void setMaterial(Material material) {
        this.material = material.name();
    }

    public Material getMaterial() {
        return Material.getMaterial(material);
    }

    // ---------------------------- //

    private static Location lobby = null;

    public static Location getLobby() {
        return lobby;
    }

    public static void setLobby(Location lobby) {
        MobEscapeMap.lobby = lobby;
    }

    private static final File file = new File(MobEscape.getPlugin().getDataFolder() + "/arena");

    public static boolean checkFile() {
        if (!file.exists()) return file.mkdirs();
        return true;
    }

    private static HashMap<Integer, MobEscapeMap> mapHashMap;

    public static HashMap<Integer, MobEscapeMap> getMapHashMap(){
        if (mapHashMap == null) mapHashMap = new HashMap<>();
        return mapHashMap;
    }

    private static HashMap<Player, MobEscapeMap> editingArena;

    public static HashMap<Player, MobEscapeMap> getEditingArena(){
        if (editingArena == null) editingArena = new HashMap<>();
        return editingArena;
    }

    public static void createMap(Player player, String name){
        int id = getNewId();
        MobEscapeMap map = new MobEscapeMap(name, id);
        getMapHashMap().put(id, map);
        getEditingArena().put(player, map);
    }

    public static void saveMap(){
        if (getMapHashMap().isEmpty()) return;
        checkFile();
        for (MobEscapeMap map : getMapHashMap().values()) {
            File file = new File(MobEscape.getPlugin().getDataFolder() + "/arena/" + map.getId() + ".yml");
            YamlConfiguration yaml = parseYaml(map);
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
        getMapHashMap().clear();
        for (File file1 : Objects.requireNonNull(file.listFiles())) {
            if (file1.getName().endsWith(".yml")) {
                String fileName = file1.getName().replace(".yml", "");
                int id = Integer.parseInt(fileName);
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file1);
                MobEscapeMap arena = parseMap(yaml);
                getMapHashMap().put(id, arena);
            }
        }
    }

    public static YamlConfiguration parseYaml(MobEscapeMap arena){
        Gson gson = new Gson();
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("data", gson.toJson(arena));
        return yaml;
    }

    public static MobEscapeMap parseMap(YamlConfiguration yaml){
        Gson gson = new Gson();
        return gson.fromJson(yaml.getString("data"), MobEscapeMap.class);
    }

    public static int getNewId() {
        int id = 0;
        checkFile();
        while (true) {
            if (!getMapHashMap().containsKey(id)) return id;
            id++;
        }
    }

    public static MobEscapeMap getEditMap(Player player) {
        if (!player.isOp()) return null;
        if (!getEditingArena().containsKey(player)) return null;
        return getEditingArena().get(player);
    }

    public static void setEditingMap(Player player, MobEscapeMap map) {
        getEditingArena().put(player, map);
    }

}
