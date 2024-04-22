package xyz.mlserver.mobescape.utils.game;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.mlserver.java.Log;
import xyz.mlserver.mobescape.utils.WorldEditHook;
import xyz.mlserver.mobescape.utils.api.MainAPI;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
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
    private HashMap<Integer, String> path;

    private String pos1;
    private String pos2;
    private String goalPos1;
    private String goalPos2;
    private Integer defaultCountDownTime;
    private Integer defaultArenaCountDownTime;
    private List<String> signLocList;
    private Double underY;
    private Float speed;

    public MobEscapeMap(String name, int id) {
        this.name = name;
        this.id = id;
        this.maxPlayer = -1;
        this.minPlayer = -1;
        this.spawns = new ArrayList<>();
        this.arenaLobby = null;
        this.material = null;
        this.pos1 = null;
        this.pos2 = null;
        this.path = new HashMap<>();
        this.goalPos1 = null;
        this.goalPos2 = null;
        this.defaultArenaCountDownTime = 30;
        this.defaultCountDownTime = 3;
        this.signLocList = new ArrayList<>();
        this.underY = 0.0;
        this.speed = 1.0f;
    }

    public Float getSpeed() {
        // デフォルトは1.0
        if (speed == null) speed = 1.0f;
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Double getUnderY() {
        if (underY == null) underY = 0.0;
        return underY;
    }

    public void setUnderY(Double underY) {
        this.underY = underY;
    }

    public void setSignLocList(List<String> signLocList) {
        this.signLocList = signLocList;
    }

    public List<String> getSignLocList() {
        if (signLocList == null) signLocList = new ArrayList<>();
        return signLocList;
    }

    public Integer getDefaultArenaCountDownTime() {
        if (defaultArenaCountDownTime == null) defaultArenaCountDownTime = 30;
        return defaultArenaCountDownTime;
    }

    public Integer getDefaultCountDownTime() {
        if (defaultCountDownTime == null) defaultCountDownTime = 3;
        return defaultCountDownTime;
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

    public boolean isCompleted() {
        if (getPos1() == null) return false;
        if (getPos2() == null) return false;
        if (getGoalPos1() == null) return false;
        if (getGoalPos2() == null) return false;
        if (getArenaLobby() == null) return false;
        if (getSpawns() == null || getSpawns().isEmpty()) return false;
        if (getPath().isEmpty()) return false;
        if (MobEscapeAPI.getLobby() == null) return false;
        return WorldEditHook.getSchematicFile(this).exists();
    }

    public boolean isEnd() {
        if (MobEscapeAPI.getMembers(this).isEmpty()) {
            if (MainAPI.isDebug()) Log.debug("Arena " + getName() + " is end because no players are in the arena.");
            return true;
        }
        if (MobEscapeAPI.getMembers(this).size() <= MobEscapeAPI.getGoalPlayers(this).size()) {
            if (MainAPI.isDebug())
                Log.debug("Arena " + getName() + " is end because the number of players is less than or equal to the number of goal players.");
            return true;
        }
        int spectate = 0;
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (MobEscapeAPI.getMembers(this).contains(all)) {
                if (all.getGameMode() == GameMode.SPECTATOR) spectate++;
            }
        }
        if (spectate >= MobEscapeAPI.getMembers(this).size()) {
            if (MainAPI.isDebug())
                Log.debug("Arena " + getName() + " is end because the number of spectators is greater than or equal to the number of players.");
            return true;
        }
        return false;
    }

    public void join(Player player) {
        if (getArenaLobby() == null) return;
        if (MobEscapeAPI.getGamePhaseMap().get(this) == GamePhase.WAITING) {
            player.sendMessage("§cマップ準備中です。しばらくお待ちください。");
            return;
        }
        List<Player> list = MobEscapeAPI.getMembers(this);
        if (list.contains(player)) return;
        list.add(player);
        MobEscapeAPI.getMembersMap().put(this, list);
        player.teleport(getArenaLobby());
        player.setFoodLevel(20);
        if (MobEscapeAPI.getGamePhaseMap().get(this) == GamePhase.ARENA) {
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("§cゲーム中のため観戦モードに設定しました。");
        }
        MobEscapeAPI.startArenaCountDown(this);
    }

    public void leave(Player player) {
        if (MobEscapeAPI.getLobby() == null) return;
        List<Player> list = MobEscapeAPI.getMembers(this);
        if (!list.contains(player)) return;
        list.remove(player);
        MobEscapeAPI.getMembersMap().put(this, list);
        player.teleport(MobEscapeAPI.getLobby().clone());
        if (MobEscapeAPI.getCountdownTaskMap().containsKey(this) && isEnd()) {
            MobEscapeAPI.getGamePhaseMap().put(this, GamePhase.STOP);
        }
    }

    public void goal(Player player) {
        if (!MobEscapeAPI.getMembers(this).contains(player)) return;
        List<Player> list = MobEscapeAPI.getGoalPlayers(this);
        if (list.contains(player)) return;
        list.add(player);
        MobEscapeAPI.getGoalPlayersMap().put(this, list);
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage("§aおめでとうございます！あなたはゴールしました！");
        if (!MobEscapeAPI.getWinCommandList().isEmpty()) {
            for (String command : MobEscapeAPI.getWinCommandList()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
            }
        }
        if (MobEscapeAPI.getCountdownTaskMap().containsKey(this) && isEnd()) {
            MobEscapeAPI.getGamePhaseMap().put(this, GamePhase.END);
        }
    }

    public void death(Player player, DeathReason reason) {
        if (!MobEscapeAPI.getMembers(this).contains(player)) return;
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage("§cあなたは死亡しました。理由: " + reason.name());
        if (MobEscapeAPI.getCountdownTaskMap().containsKey(this) && isEnd()) {
            MobEscapeAPI.getGamePhaseMap().put(this, GamePhase.END);
        }
    }

    public enum DeathReason {
        UNDER_Y(0, "範囲外に落ちた"),
        FALL(1, "奈落に落ちた"),
        MOB_DAMAGE(2, "モンスターに倒された"),
        DEATH(3, "死亡した"),
        ;

        DeathReason(int id, String reason) {
            this.id = id;
            this.reason = reason;
        }

        private final int id;
        public int getId() {
            return id;
        }

        private final String reason;

        public String getReason() {
            return reason;
        }
    }

}
