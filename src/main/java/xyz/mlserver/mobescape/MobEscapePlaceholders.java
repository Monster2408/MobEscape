package xyz.mlserver.mobescape;

import com.google.common.collect.Table;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import xyz.mlserver.mobescape.utils.MobEscapeDB;
import xyz.mlserver.mobescape.utils.api.MainAPI;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

import java.util.HashMap;

public class MobEscapePlaceholders extends PlaceholderExpansion {
    private final MobEscape plugin;

    public MobEscapePlaceholders(MobEscape plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String getIdentifier() {
        return "mobescape";
    }

    @Override
    public String onRequest(OfflinePlayer p, String identifier) {

        if (p == null) {
            return "";
        }
        if (identifier.equals("version")) {
            return String.valueOf(plugin.getDescription().getVersion());
        }
        String[] args = identifier.split("_");
        if (identifier.startsWith("leaderboard_time_player_") || identifier.startsWith("leaderboard_time_score_")) {
            if (args.length == 5) {
                int id;
                try {
                    id = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    return ChatColor.RED + "Invalid arena ID!";
                }
                int rank_num;
                try {
                    rank_num = Integer.parseInt(args[4]);
                    if (rank_num < 1 || rank_num > 20) {
                        return ChatColor.RED + "Invalid rank number!";
                    }
                } catch (NumberFormatException e) {
                    return ChatColor.RED + "Invalid rank number!";
                }
                if (args[2].equals("player")) {
                    Table<Integer, String, Integer> map = MobEscapeDB.getSortByBestGoalTime(id);
                    if (!map.containsRow(rank_num)) return "";
                    String[] names = map.row(rank_num).keySet().toArray(new String[0]);
                    return MobEscapeDB.getPlayerNameMap().get(names[0]);
                } else if (args[2].equals("score")) {
                    Table<Integer, String, Integer> map = MobEscapeDB.getSortByBestGoalTime(id);
                    if (!map.containsRow(rank_num)) return "";
                    Integer[] scores = map.row(rank_num).values().toArray(new Integer[0]);
                    return MainAPI.getMinuteTime(scores[0]);
                }
            }
        }
        if (identifier.startsWith("leaderboard_wins_player_") || identifier.startsWith("leaderboard_wins_score_")) {
            if (args.length == 5) {
                int id;
                try {
                    id = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    return ChatColor.RED + "Invalid arena ID!";
                }
                int rank_num;
                try {
                    rank_num = Integer.parseInt(args[4]);
                    if (rank_num < 1 || rank_num > 20) {
                        return ChatColor.RED + "Invalid rank number!";
                    }
                } catch (NumberFormatException e) {
                    return ChatColor.RED + "Invalid rank number!";
                }
                if (args[2].equals("player")) {
                    Table<Integer, String, Integer> map = MobEscapeDB.getSortByWinCount(id);
                    if (!map.containsRow(rank_num)) return "";
                    String[] names = map.row(rank_num).keySet().toArray(new String[0]);
                    return MobEscapeDB.getPlayerNameMap().get(names[0]);
                } else if (args[2].equals("score")) {
                    Table<Integer, String, Integer> map = MobEscapeDB.getSortByWinCount(id);
                    if (!map.containsRow(rank_num)) return "";
                    Integer[] scores = map.row(rank_num).values().toArray(new Integer[0]);
                    return String.valueOf(scores[0]);
                }
            }
        }
        if (identifier.startsWith("arena_name_")) {
            if (args.length == 3) {
                int id;
                try {
                    id = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    return ChatColor.RED + "Invalid arena ID!";
                }
                if (MobEscapeAPI.getMapHashMap().containsKey(id)) return MobEscapeAPI.getMapHashMap().get(id).getName();
                return ChatColor.RED + "Invalid arena ID!";
            }
        }

        return null;
    }

}