package xyz.mlserver.mobescape.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.UUID;

public class MobEscapeDB {
    private static Table<String, Integer, Integer> playerWinCount;
    private static Table<String, Integer, Integer> playerPlayedCount;
    private static Table<String, Integer, Double> playerBestGoalTime;
    private static String dbFile = "score.db";

    public static Table<String, Integer, Integer> getPlayerWinCount() {
        if (playerWinCount == null) playerWinCount = HashBasedTable.create();
        return playerWinCount;
    }

    public static Table<String, Integer, Integer> getPlayerPlayedCount() {
        if (playerPlayedCount == null) playerPlayedCount = HashBasedTable.create();
        return playerPlayedCount;
    }

    public static Table<String, Integer, Double> getPlayerBestGoalTime() {
        if (playerBestGoalTime == null) playerBestGoalTime = HashBasedTable.create();
        return playerBestGoalTime;
    }

    public static void addWinCount(UUID uuid, int arenaId) {
        String uuidStr = uuid.toString();
        if (!getPlayerWinCount().contains(uuidStr, arenaId)) getPlayerWinCount().put(uuidStr, arenaId, 0);
        getPlayerWinCount().put(uuidStr, arenaId, getPlayerWinCount().get(uuidStr, arenaId) + 1);
    }

    public static void addPlayedCount(UUID uuid, int arenaId) {
        String uuidStr = uuid.toString();
        if (!getPlayerPlayedCount().contains(uuidStr, arenaId)) getPlayerPlayedCount().put(uuidStr, arenaId, 0);
        getPlayerPlayedCount().put(uuidStr, arenaId, getPlayerPlayedCount().get(uuidStr, arenaId) + 1);
    }

    public static void setBestGoalTime(UUID uuid, int arenaId, double time) {
        String uuidStr = uuid.toString();
        if (!getPlayerBestGoalTime().contains(uuidStr, arenaId)) getPlayerBestGoalTime().put(uuidStr, arenaId, time);
        else if (getPlayerBestGoalTime().get(uuidStr, arenaId) > time) getPlayerBestGoalTime().put(uuidStr, arenaId, time);
    }

}
