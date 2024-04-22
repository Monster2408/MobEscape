package xyz.mlserver.mobescape.utils.api;

import com.google.gson.Gson;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.NavigatorParameters;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.mlserver.java.Log;
import xyz.mlserver.mobescape.MobEscape;
import xyz.mlserver.mobescape.utils.WorldEditHook;
import xyz.mlserver.mobescape.utils.bukkit.ActionBar;
import xyz.mlserver.mobescape.utils.bukkit.LocationParser;
import xyz.mlserver.mobescape.utils.game.GamePhase;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

import net.citizensnpcs.api.npc.NPC;
import xyz.mlserver.mobescape.utils.trait.MoveAndAttackTrait;
import xyz.mlserver.mobescape.utils.trait.MoveAndBreakTrait;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MobEscapeAPI {

    private static HashMap<MobEscapeMap, List<Player>> members;

    public static HashMap<MobEscapeMap, List<Player>> getMembersMap(){
        if (members == null) members = new HashMap<>();
        return members;
    }

    public static List<Player> getMembers(MobEscapeMap map){
        getMembersMap().putIfAbsent(map, new ArrayList<>());
        return getMembersMap().get(map);
    }

    private static HashMap<MobEscapeMap, Integer> arenaCountDownTime;

    public static HashMap<MobEscapeMap, Integer> getArenaCountDownTimeMap(){
        if (arenaCountDownTime == null) arenaCountDownTime = new HashMap<>();
        return arenaCountDownTime;
    }

    public static Integer getArenaCountDownTime(MobEscapeMap map){
        getArenaCountDownTimeMap().putIfAbsent(map, map.getDefaultArenaCountDownTime());
        return getArenaCountDownTimeMap().get(map);
    }

    private static HashMap<MobEscapeMap, Double> gameTime;

    public static HashMap<MobEscapeMap, Double> getGameTimeMap(){
        if (gameTime == null) gameTime = new HashMap<>();
        return gameTime;
    }

    public static Double getGameTime(MobEscapeMap map){
        getGameTimeMap().putIfAbsent(map, 0.0);
        return getGameTimeMap().get(map);
    }

    private static HashMap<MobEscapeMap, List<Player>> goalPlayers;

    public static HashMap<MobEscapeMap, List<Player>> getGoalPlayersMap(){
        if (goalPlayers == null) goalPlayers = new HashMap<>();
        return goalPlayers;
    }

    public static List<Player> getGoalPlayers(MobEscapeMap map){
        getGoalPlayersMap().putIfAbsent(map, new ArrayList<>());
        return getGoalPlayersMap().get(map);
    }

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

    public static void save() {
        if (getLobby() != null) MobEscape.dataYml.getConfig().set("lobby", LocationParser.parseJson(getLobby()));
        MobEscape.dataYml.saveConfig();
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
        MobEscapeAPI.getMembersMap().put(map, new ArrayList<>());
        YamlConfiguration yaml = parseYaml(map);
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WorldEditHook.saveSchematic(map.getPos1(), map.getPos2(), map.getId());
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
                List<String> tempList = new ArrayList<>();
                for (String signLoc : map.getSignLocList()) {
                    Location location = LocationParser.parseLocation(signLoc);
                    if (location == null) continue;
                    if (MobEscapeAPI.getSignTypeList().contains(location.getBlock().getType())) {
                        tempList.add(signLoc);
                    }
                }
                if (tempList.size() != map.getSignLocList().size()) {
                    map.setSignLocList(tempList);
                    MobEscapeAPI.saveMap(map);
                }
                getMapHashMap().put(id, map);
            }
        }
        if (MobEscape.dataYml.getConfig().get("lobby") != null) setLobby(LocationParser.parseLocation(MobEscape.dataYml.getConfig().getString("lobby")));
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

    public static HashMap<MobEscapeMap, BukkitTask> getCountdownTaskMap() {
        if (countdownTaskMap == null) countdownTaskMap = new HashMap<>();
        return countdownTaskMap;
    }

    public static List<Material> getSignTypeList() {
        return Arrays.asList(
                Material.SIGN,
                Material.SIGN_POST,
                Material.WALL_SIGN
        );
    }

    public static void addSign(MobEscapeMap map, Location blockLoc) {
        if (!getSignTypeList().contains(blockLoc.clone().getBlock().getType())) return;
        map.getSignLocList().add(LocationParser.parseJson(blockLoc.clone()));
        saveMap(map);
    }

    public static String getJoinSignText(int line, MobEscapeMap map) {
        String text = getJoinSignText(line);
        if (text == null) return null;
        if (map == null) return text;
        if (text.contains("%id%")) text = text.replace("%id%", String.valueOf(map.getId()));
        if (text.contains("%max%")) text = text.replace("%max%", String.valueOf(map.getMaxPlayer()));
        if (text.contains("%min%")) text = text.replace("%min%", String.valueOf(map.getMinPlayer()));
        if (text.contains("%count%")) text = text.replace("%count%", String.valueOf(MobEscapeAPI.getMembers(map).size()));
        return text;
    }

    public static String getJoinSignText(int line) {
        if (line == 0) return ChatColor.BLACK + "[" + ChatColor.GREEN + "Join " + ChatColor.RED + "MobEscape" + ChatColor.BLACK + "]";
        if (line == 1) return ChatColor.BLACK + "ID: " + ChatColor.RED + "%id%";
        if (line == 2) return ChatColor.BLACK + "上限: " + ChatColor.RED + "%max% " + ChatColor.BLACK + "下限: " + ChatColor.RED + "%min%";
        if (line == 3) return ChatColor.BLACK + "参加中: " + ChatColor.RED + "%count%";
        return null;
    }

    public static String getLeaveSignText(int line) {
        if (line == 0) return ChatColor.BLACK + "[" + ChatColor.GREEN + "Leave " + ChatColor.RED + "MobEscape" + ChatColor.BLACK + "]";
        if (line == 1 || line == 2 || line == 3) return "";
        return null;
    }

    public static void setCountdownTaskMap(MobEscapeMap map, BukkitTask task) {
        if (task != null) getCountdownTaskMap().put(map, task);
        else getCountdownTaskMap().remove(map);
    }

    private static HashMap<MobEscapeMap, NPC> mobMap;

    public static HashMap<MobEscapeMap, NPC> getMobMap() {
        if (mobMap == null) mobMap = new HashMap<>();
        return mobMap;
    }

    public static void setMobMap(MobEscapeMap map, NPC entity) {
        if (getMobMap().containsKey(map)) getMobMap().get(map).despawn();
        getMobMap().put(map, entity);
    }

    public static void removeMob(MobEscapeMap map) {
        if (getMobMap().containsKey(map)) getMobMap().get(map).despawn();
        getMobMap().remove(map);
    }

    public static NPC getMob(MobEscapeMap map) {
        if (!getMobMap().containsKey(map)) return null;
        return getMobMap().get(map);
    }

    public static void resetGame(MobEscapeMap map) {
        if (MobEscapeAPI.getMob(map) != null) {
            MobEscapeAPI.getMobMap().get(map).destroy();
        }
        MobEscapeAPI.getMobMap().remove(map);
        getGamePhaseMap().put(map, GamePhase.WAITING);
        WorldEditHook.loadSchematic(map.getId(), map.getPos1());
        getGamePhaseMap().put(map, GamePhase.ARENA);
        getGoalPlayersMap().put(map, new ArrayList<>());
        for (Player all : getMembersMap().get(map)) {
            all.teleport(MobEscapeAPI.getLobby());
            all.playSound(all.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
            all.setGameMode(GameMode.SURVIVAL);
        }
        getMembersMap().put(map, new ArrayList<>());
    }

    /**
     * Arenaにプレイヤーが参加したときに呼び出されます。
     */
    public static void startArenaCountDown(MobEscapeMap map) {
        if (!map.isCompleted()) {
            if (MainAPI.isDebug()) Log.debug("Map is not completed.");
        } else if (map.getMinPlayer() > MobEscapeAPI.getMembers(map).size()) {
            if (MainAPI.isDebug()) Log.debug("Map is not enough player.");
        } else if (getCountdownTaskMap().containsKey(map)) {
            if (MainAPI.isDebug()) Log.debug("Map is already started.");
        } else {
            MobEscapeAPI.getArenaCountDownTimeMap().put(map, map.getDefaultArenaCountDownTime());
            getGamePhaseMap().put(map, GamePhase.ARENA);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (MobEscapeAPI.getArenaCountDownTime(map) <= 0) {
                        cancel();
                        startGame(map);
                    } else if (getGamePhaseMap().get(map) == GamePhase.STOP) {
                        setCountdownTaskMap(map, null);
                        resetGame(map);
                        cancel();
                    } else {
                        int time = MobEscapeAPI.getArenaCountDownTime(map);
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (MobEscapeAPI.getMembers(map).contains(all)) {
                                String colorTemp = ChatColor.GREEN + "" + ChatColor.BOLD;
                                if (time % 2 == 0) colorTemp = ChatColor.RED + "" + ChatColor.BOLD;
                                ActionBar.send(all, colorTemp + "[待機中]ゲーム開始まで" + time + "秒");
                            }
                        }
                        MobEscapeAPI.getArenaCountDownTimeMap().put(map, time - 1);
                    }
                }
            }.runTaskTimer(MobEscape.getPlugin(), 0, 20);
            setCountdownTaskMap(map, task);
        }
    }

    /**
     * アリーナ待機終了後に呼び出されます。
     * @param map
     */
    public static void startGame(MobEscapeMap map) {
        if (MobEscapeAPI.getMembers(map).size() < map.getMinPlayer()) return;
        MobEscapeAPI.getGameTimeMap().put(map, -1.0*map.getDefaultCountDownTime());
        getGamePhaseMap().put(map, GamePhase.READY);
        int temp = 0;
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (MobEscapeAPI.getMembers(map).contains(all)) {
                if (map.getSpawns().size() <= temp) temp = 0;
                all.teleport(map.getSpawns().get(temp).clone());
                temp++;
            }
        }
        Location firstPath = LocationParser.parseLocation(map.getPath().get(1));
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.ENDER_DRAGON, "MobEscape" + map.getId());
        npc.spawn(firstPath);

        // 移動速度を設定
        npc.getNavigator().getLocalParameters().speedModifier(map.getSpeed());
        npc.addTrait(MoveAndAttackTrait.class);
        npc.addTrait(MoveAndBreakTrait.class);

        MobEscapeAPI.setMobMap(map, npc);
        BukkitTask task = new BukkitRunnable() {
            int time;
            int countdown = -1;
            int taskLocationNum = 2;
            @Override
            public void run() {
                if (getGamePhaseMap().get(map) == GamePhase.STOP) {
                    setCountdownTaskMap(map, null);
                    resetGame(map);
                    cancel();
                } else if (getGamePhaseMap().get(map) == GamePhase.END) {
                    setCountdownTaskMap(map, null);
                    resetGame(map);
                    cancel();
                } else {
                    time = (int) Math.floor(MobEscapeAPI.getGameTime(map));
                    if (MainAPI.isDebug()) Log.debug("GameTime: " + time);
                    if (time < 0) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (MobEscapeAPI.getMembers(map).contains(all)) {
                                ActionBar.send(all, "ゲーム開始まで" + time*-1 + "秒");
                                if (MainAPI.isDebug()) Log.debug("CountDownTemp: " + countdown);
                                if (time >= -3 && countdown != time) {
                                    countdown = time;
                                    all.playSound(all.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                                    if (time == -3) all.sendTitle(ChatColor.DARK_GREEN + "❸", ChatColor.GRAY + "開始まで・・・", 0, 30, 0);
                                    if (time == -2) all.sendTitle(ChatColor.GOLD + "❷", ChatColor.GRAY + "開始まで・・・", 0, 30, 0);
                                    if (time == -1) all.sendTitle(ChatColor.DARK_RED + "❶", ChatColor.GRAY + "開始まで・・・", 0, 30, 0);
                                }
                            }
                        }
                    } else if (getGamePhaseMap().get(map) == GamePhase.READY) {
                        getGamePhaseMap().put(map, GamePhase.GAME);
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (MobEscapeAPI.getMembers(map).contains(all)) {
                                ActionBar.send(all, MainAPI.getMinuteTime(MobEscapeAPI.getGameTime(map)));
                                all.sendTitle("開始", null, 0, 40, 4);
                            }
                        }
                    } else {
                        if (taskLocationNum >= map.getPath().size()) {
                            if (!MobEscapeAPI.getMob(map).getNavigator().isNavigating()) {
                                setCountdownTaskMap(map, null);
                                resetGame(map);
                                cancel();
                            }
                        } else {
                            if (!MobEscapeAPI.getMob(map).getNavigator().isNavigating()) {
                                Location target = LocationParser.parseLocation(map.getPath().get(taskLocationNum));
                                if (target.distance(MobEscapeAPI.getMob(map).getEntity().getLocation()) < 3) {
                                    taskLocationNum++;
                                    target = LocationParser.parseLocation(map.getPath().get(taskLocationNum));
                                }
                                MobEscapeAPI.getMob(map).getNavigator().setTarget(target.clone());
                            }
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (MobEscapeAPI.getMembers(map).contains(all)) {
                                    ActionBar.send(all, MainAPI.getMinuteTime(MobEscapeAPI.getGameTime(map)));
                                }
                            }
                        }
                    }
                    MobEscapeAPI.getGameTimeMap().put(map, MobEscapeAPI.getGameTime(map) + 0.1);
                }
            }
        }.runTaskTimer(MobEscape.getPlugin(), 0, 2);
        setCountdownTaskMap(map, task);
    }

    private static HashMap<MobEscapeMap, GamePhase> gamePhaseMap;

    public static HashMap<MobEscapeMap, GamePhase> getGamePhaseMap() {
        if (gamePhaseMap == null) gamePhaseMap = new HashMap<>();
        return gamePhaseMap;
    }

    private static List<String> winCommandList;

    public static void setWinCommandList() {
        List<String> winCommandList = MobEscape.config.getConfig().getStringList("win-command-list");
        if (winCommandList == null) winCommandList = new ArrayList<>();
        MobEscapeAPI.winCommandList = winCommandList;
    }

    public static List<String> getWinCommandList() {
        if (winCommandList == null) winCommandList = new ArrayList<>();
        return winCommandList;
    }
}
