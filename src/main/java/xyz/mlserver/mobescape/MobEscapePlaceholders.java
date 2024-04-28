package xyz.mlserver.mobescape;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

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
        return "tntrun";
    }

    @Override
    public String onRequest(OfflinePlayer p, String identifier) {

        if (p == null) {
            return "";
        }
        if (identifier.equals("version")) {
            return String.valueOf(plugin.getDescription().getVersion());

        }
        // arena_count
        // played
        // wined
        // %arena%_best_goal_time
        return null;
    }

}