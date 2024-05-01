package xyz.mlserver.mobescape.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import xyz.mlserver.mc.util.CustomConfiguration;
import xyz.mlserver.mobescape.MobEscape;
import xyz.mlserver.mobescape.utils.api.MainAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MobEscapeDB {

    /**
     * プレイヤーのプレイ回数
     * String: UUID
     * Integer: Arena ID
     * Integer: プレイ回数
     */
    private static Table<String, Integer, Integer> playerPlayedCount;

    /**
     * プレイヤーの勝利回数
     * String: UUID
     * Integer: Arena ID
     * Integer: 勝利回数
     */
    private static Table<String, Integer, Integer> playerWinCount;

    /**
     * プレイヤーの最速ゴールタイム
     * String: UUID
     * Integer: Arena ID
     * Double: ゴールタイム
     */
    private static Table<String, Integer, Integer> playerBestGoalTime;

    /**
     * ゲームのプレイ回数(DB保存用)
     * Integer: Arena ID
     * List<String>: UUID List
     */
    private static HashMap<Integer, List<String>> gamePlayed;

    /**
     * ゲームの勝利者(DB保存用)
     * Integer: Arena ID
     * List<String>: UUID List
     */
    private static HashMap<Integer, List<String>> gameWins;

    /**
     * ゲームの最速ゴールタイム(DB保存用)
     * Integer: Arena ID
     * String: UUID
     * Double: ゴールタイム
     */
    private static Table<Integer, String, Integer> gameBestGoalTime;
    private static CustomConfiguration dbFile;

    public static Table<String, Integer, Integer> getPlayerPlayedCount() {
        if (playerPlayedCount == null) playerPlayedCount = HashBasedTable.create();
        return playerPlayedCount;
    }

    public static Table<String, Integer, Integer> getPlayerWinCount() {
        if (playerWinCount == null) playerWinCount = HashBasedTable.create();
        return playerWinCount;
    }

    public static Table<String, Integer, Integer> getPlayerBestGoalTime() {
        if (playerBestGoalTime == null) playerBestGoalTime = HashBasedTable.create();
        return playerBestGoalTime;
    }

    public static HashMap<Integer, List<String>> getGamePlayed() {
        if (gamePlayed == null) gamePlayed = new HashMap<>();
        return gamePlayed;
    }

    public static HashMap<Integer, List<String>> getGameWins() {
        if (gameWins == null) gameWins = new HashMap<>();
        return gameWins;
    }

    public static Table<Integer, String, Integer> getGameBestGoalTime() {
        if (gameBestGoalTime == null) gameBestGoalTime = HashBasedTable.create();
        return gameBestGoalTime;
    }

    private static HashMap<String, String> playerNameMap;

    public static HashMap<String, String> getPlayerNameMap() {
        if (playerNameMap == null) playerNameMap = new HashMap<>();
        return playerNameMap;
    }

    public static Table<Integer, String, Integer> getSortByBestGoalTime(int arenaId) {
        HashMap<String, Integer> sortMap = new HashMap<>();
        for (String uuid : getPlayerBestGoalTime().rowKeySet()) {
            if (getPlayerBestGoalTime().contains(uuid, arenaId)) {
                int winCount = getPlayerBestGoalTime().get(uuid, arenaId);
                sortMap.put(uuid, winCount);
            }
        }

        // 2.Map.Entryのリストを作成する
        List<Map.Entry<String, Integer>> list_entries = new ArrayList<>(sortMap.entrySet());
        list_entries.sort(Map.Entry.comparingByValue());

        Table<Integer, String, Integer> table = HashBasedTable.create();
        int num = 1;
        for(Map.Entry<String, Integer> entry : list_entries) {
            table.put(num, entry.getKey(), entry.getValue());
            num++;
        }
        return table;
    }

    public static Table<Integer, String, Integer> getSortByWinCount(int arenaId) {
        HashMap<String, Integer> sortMap = new HashMap<>();
        for (String uuid : getPlayerWinCount().rowKeySet()) {
            if (getPlayerWinCount().contains(uuid, arenaId)) {
                int winCount = getPlayerWinCount().get(uuid, arenaId);
                sortMap.put(uuid, winCount);
            }
        }

        // 2.Map.Entryのリストを作成する
        List<Map.Entry<String, Integer>> list_entries = new ArrayList<>(sortMap.entrySet());
        list_entries.sort(Map.Entry.comparingByValue());

        Table<Integer, String, Integer> table = HashBasedTable.create();
        int num = 1;
        for(Map.Entry<String, Integer> entry : list_entries) {
            table.put(num, entry.getKey(), entry.getValue());
            num++;
        }
        return table;
    }

    public static void addPlayedCount(UUID uuid, int arenaId) {
        String uuidStr = uuid.toString();
        if (!getPlayerPlayedCount().contains(uuidStr, arenaId)) getPlayerPlayedCount().put(uuidStr, arenaId, 0);
        getPlayerPlayedCount().put(uuidStr, arenaId, getPlayerPlayedCount().get(uuidStr, arenaId) + 1);
        if (!getGamePlayed().containsKey(arenaId)) getGamePlayed().put(arenaId, new ArrayList<>());
        if (!getGamePlayed().get(arenaId).contains(uuidStr)) getGamePlayed().get(arenaId).add(uuidStr);
    }

    public static void addWinCount(UUID uuid, int arenaId) {
        String uuidStr = uuid.toString();
        if (!getPlayerWinCount().contains(uuidStr, arenaId)) getPlayerWinCount().put(uuidStr, arenaId, 0);
        getPlayerWinCount().put(uuidStr, arenaId, getPlayerWinCount().get(uuidStr, arenaId) + 1);
        if (!getGameWins().containsKey(arenaId)) getGameWins().put(arenaId, new ArrayList<>());
        if (!getGameWins().get(arenaId).contains(uuidStr)) getGameWins().get(arenaId).add(uuidStr);
    }

    public static boolean setBestGoalTime(UUID uuid, int arenaId, Integer time) {
        String uuidStr = uuid.toString();
        if (!getPlayerBestGoalTime().contains(uuidStr, arenaId) || getPlayerBestGoalTime().get(uuidStr, arenaId) > time) {
            getPlayerBestGoalTime().put(uuidStr, arenaId, time);
            getGameBestGoalTime().put(arenaId, uuidStr, time);
            return true;
        }
        return false;
    }

    public static void setup() {
        if (dbFile == null) {
            dbFile = new CustomConfiguration(MobEscape.getPlugin(), "stats.yml");
            dbFile.saveDefaultConfig();
        }
    }

    public static void load() {
        setup();
        // Tableの存在チェック
        for (String uuid : dbFile.getConfig().getConfigurationSection("stats").getKeys(false)) {
            for (String arenaId : dbFile.getConfig().getConfigurationSection("stats." + uuid).getKeys(false)) {
                int arenaIdInt = Integer.parseInt(arenaId);
                int playedCount = dbFile.getConfig().getInt("stats." + uuid + "." + arenaId + ".played");
                int winCount = dbFile.getConfig().getInt("stats." + uuid + "." + arenaId + ".wins");
                int bestGoalTime = dbFile.getConfig().getInt("stats." + uuid + "." + arenaId + ".best_time");
                getPlayerPlayedCount().put(uuid, arenaIdInt, playedCount);
                getPlayerWinCount().put(uuid, arenaIdInt, winCount);
                getPlayerBestGoalTime().put(uuid, arenaIdInt, bestGoalTime);
                if (playedCount > 0) {
                    if (!getPlayerPlayedCount().contains(uuid, arenaIdInt)) getPlayerPlayedCount().put(uuid, arenaIdInt, 0);
                }
                if (winCount > 0) {
                    if (!getPlayerWinCount().contains(uuid, arenaIdInt)) getPlayerWinCount().put(uuid, arenaIdInt, 0);
                }
                if (bestGoalTime > 0) {
                    if (!getPlayerBestGoalTime().contains(uuid, arenaIdInt)) getPlayerBestGoalTime().put(uuid, arenaIdInt, -1);
                }
            }
        }
    }

    public static void save(int arenaId) {
        setup();
        for (String uuid : getPlayerPlayedCount().rowKeySet()) {
            int playedCount = getPlayerPlayedCount().contains(uuid, arenaId) ? getPlayerPlayedCount().get(uuid, arenaId) : 0;
            int winCount = getPlayerWinCount().contains(uuid, arenaId) ? getPlayerWinCount().get(uuid, arenaId) : 0;
            int bestGoalTime = getPlayerBestGoalTime().contains(uuid, arenaId) ? getPlayerBestGoalTime().get(uuid, arenaId) : -1;
            dbFile.getConfig().set("stats." + uuid + "." + arenaId + ".played", playedCount);
            dbFile.getConfig().set("stats." + uuid + "." + arenaId + ".wins", winCount);
            dbFile.getConfig().set("stats." + uuid + "." + arenaId + ".best_time", bestGoalTime);
        }
        dbFile.saveConfig();
    }

}
