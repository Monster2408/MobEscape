package xyz.mlserver.mobescape.utils.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.mlserver.mobescape.utils.bukkit.LocationParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private Integer countDownTime;
    private Integer defaultCountDownTime;
    private Integer arenaCountDownTime;
    private Integer defaultArenaCountDownTime;
    private Integer gameTime;
    private Integer defaultGameTime;

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
        this.defaultArenaCountDownTime = 30;
        this.arenaCountDownTime = -1;
        this.defaultCountDownTime = 10;
        this.countDownTime = -1;
        this.defaultGameTime = 10;
        this.gameTime = -1;
    }

    public Integer getDefaultGameTime() {
        return defaultGameTime;
    }

    public void setDefaultGameTime(Integer defaultGameTime) {
        this.defaultGameTime = defaultGameTime;
    }

    public Integer getGameTime() {
        return gameTime;
    }

    public void setGameTime(Integer gameTime) {
        this.gameTime = gameTime;
    }

    public Integer getDefaultArenaCountDownTime() {
        return defaultArenaCountDownTime;
    }

    public void setDefaultArenaCountDownTime(Integer defaultArenaCountDownTime) {
        this.defaultArenaCountDownTime = defaultArenaCountDownTime;
    }

    public Integer getArenaCountDownTime() {
        return arenaCountDownTime;
    }

    public void setArenaCountDownTime(Integer arenaCountDownTime) {
        this.arenaCountDownTime = arenaCountDownTime;
    }

    public Integer getDefaultCountDownTime() {
        return defaultCountDownTime;
    }

    public void setDefaultCountDownTime(Integer defaultCountDownTime) {
        this.defaultCountDownTime = defaultCountDownTime;
    }

    public Integer getCountDownTime() {
        return countDownTime;
    }

    public void setCountDownTime(Integer countDownTime) {
        this.countDownTime = countDownTime;
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

}
