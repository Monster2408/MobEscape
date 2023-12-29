package xyz.mlserver.mobescape.utils.api;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.mlserver.mobescape.MobEscape;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class MobEscapeAPI {

    private static Location lobby = null;

    public static Location getLobby() {
        return lobby;
    }

    public static void setLobby(Location lobby) {
        MobEscapeAPI.lobby = lobby;
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


    private static HashMap<MobEscapeMap, BukkitTask> countdownTaskMap;

    private static HashMap<MobEscapeMap, BukkitTask> getCountdownTaskMap() {
        if (countdownTaskMap == null) countdownTaskMap = new HashMap<>();
        return countdownTaskMap;
    }

    public static void setCountdownTaskMap(MobEscapeMap map, BukkitTask task) {
        getCountdownTaskMap().put(map, task);
    }

    /**
     * Arenaにプレイヤーが参加したときに呼び出されます。
     */
    public static void startArenaCountDown(MobEscapeMap map) {
        if (map.getMinPlayer() > map.getMembers().size()) return;
        if (map.getCountDownTimer() > -1) return;
        map.setArenaCountDownTimer(map.getArenaCountDownTime());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (map.getArenaCountDownTimer() <= 0) {
                    startGame(map);
                    cancel();
                    return;
                }
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (map.getMembers().contains(all.getUniqueId().toString())) {
                        all.setLevel(map.getArenaCountDownTimer());
                    }
                }
                map.setArenaCountDownTimer(map.getArenaCountDownTimer() - 1);
            }
        }.runTaskTimer(MobEscape.getPlugin(), 0, 2);
        setCountdownTaskMap(map, task);
    }

    public static void startGame(MobEscapeMap map) {
        if (map.getMembers().size() < map.getMinPlayer()) return;
        if (map.getGameTime() > -1) return;
        map.setGameTime(map.getGameTime());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (map.getGameTime() <= 0) {
                    // endGame(map);
                    cancel();
                    return;
                }
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (map.getMembers().contains(all.getUniqueId().toString())) {
                        all.setLevel(map.getGameTime());
                    }
                }
                map.setGameTime(map.getGameTime() - 1);
            }
        }.runTaskTimer(MobEscape.getPlugin(), 0, 2);
        setCountdownTaskMap(map, task);
    }


}
