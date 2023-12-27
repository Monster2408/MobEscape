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
import java.util.UUID;

public class MobEscapeMap {

    private String name;
    private final int id;
    private int maxPlayer;
    private int minPlayer;
    private List<String> spawns;
    private String arenaLobby;
    private String material;
    private List<String> members;
    private HashMap<Integer, String> path;

    private String pos1;
    private String pos2;
    private String goalPos1;
    private String goalPos2;

    public MobEscapeMap(String name, int id) {
        this.name = name;
        this.id = id;
        this.maxPlayer = -1;
        this.minPlayer = -1;
        this.spawns = new ArrayList<>();
        this.arenaLobby = null;
        this.material = null;
        this.members = new ArrayList<>();
        this.pos1 = null;
        this.pos2 = null;
        this.path = new HashMap<>();
        this.goalPos1 = null;
        this.goalPos2 = null;
    }

    public Location getGoalPos1() {
        return LocationParser.parseLocation(goalPos1);
    }

    public void setGoalPos1(Location goalPos1) {
        this.goalPos1 = LocationParser.parseJson(goalPos1);
    }

    public Location getGoalPos2() {
        return LocationParser.parseLocation(goalPos2);
    }

    public void setGoalPos2(Location goalPos2) {
        this.goalPos2 = LocationParser.parseJson(goalPos2);
    }


    public HashMap<Integer, String> getPath() {
        if (path == null) path = new HashMap<>();
        return path;
    }

    public void setPath(HashMap<Integer, String> path) {
        this.path = path;
    }

    public void addPath(Location loc) {
        int num = -1;
        if (getPath().isEmpty()) num = 0;
        else {
            for (int i : getPath().keySet()) {
                if (i > num) num = i;
            }
        }
        getPath().put((num + 1), LocationParser.parseJson(loc));
    }

    public void removePath() {
        int num = -1;
        if (!getPath().isEmpty()) {
            for (int i : getPath().keySet()) {
                if (i > num) num = i;
            }
            getPath().remove(num);
        }
    }

    public void deletePath() {
        getPath().clear();
    }

    public Location getPos1() {
        return LocationParser.parseLocation(pos1);
    }

    public Location getPos2() {
        return LocationParser.parseLocation(pos2);
    }

    public void setPos1(Location pos1) {
        this.pos1 = LocationParser.parseJson(pos1);
    }

    public void setPos2(Location pos2) {
        this.pos2 = LocationParser.parseJson(pos2);
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
        if (spawns == null) spawns = new ArrayList<>();
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
        if (spawns == null) spawns = new ArrayList<>();
        spawns.add(LocationParser.parseJson(spawn.clone()));
    }

    public void deleteSpawn() {
        spawns = new ArrayList<>();
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
        if (material == null) setMaterial(Material.GRASS);
        return Material.getMaterial(material);
    }

    public void addMember(Player player) {
        String uuid = player.getUniqueId().toString();
        if (!getMembers().contains(uuid)) getMembers().add(uuid);
    }

    public void removeMember(Player player) {
        getMembers().remove(player.getUniqueId().toString());
    }

    public List<String> getMembers() {
        if (members == null) members = new ArrayList<>();
        return members;
    }

    public void join(Player player) {
        if (getArenaLobby() == null) return;
        addMember(player);
        player.teleport(getArenaLobby());
    }

    // ---------------------------- //

    private static Location lobby = null;

    public static Location getLobby() {
        return lobby;
    }

    public static void setLobby(Location lobby) {
        MobEscapeMap.lobby = lobby;
    }

    private static final File file = new File(MobEscape.getPlugin().getDataFolder() + "/map");

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

    public static void saveMap() {
        if (getMapHashMap().isEmpty()) return;
        checkFile();
        for (MobEscapeMap map : getMapHashMap().values()) {
            saveMap(map);
        }
    }

    public static void saveMap(MobEscapeMap map) {
        checkFile();
        File file = new File(MobEscape.getPlugin().getDataFolder() + "/map/" + map.getId() + ".yml");
        map.getMembers().clear();
        YamlConfiguration yaml = parseYaml(map);
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadMap() {
        checkFile();
        if (file == null) return;
        getMapHashMap().clear();
        for (File file1 : Objects.requireNonNull(file.listFiles())) {
            if (file1.getName().endsWith(".yml")) {
                String fileName = file1.getName().replace(".yml", "");
                int id = Integer.parseInt(fileName);
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file1);
                MobEscapeMap map = parseMap(yaml);
                getMapHashMap().put(id, map);
            }
        }
    }

    public static void loadMap(MobEscapeMap map) {
        checkFile();
        if (file == null) return;
        for (File file1 : Objects.requireNonNull(file.listFiles())) {
            if (file1.getName().equalsIgnoreCase(map.getId() + ".yml")) {
                String fileName = file1.getName().replace(".yml", "");
                int id = Integer.parseInt(fileName);
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file1);
                getMapHashMap().put(id, parseMap(yaml));
                return;
            }
        }
    }

    public static YamlConfiguration parseYaml(MobEscapeMap map){
        Gson gson = new Gson();
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("data", gson.toJson(map));
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
