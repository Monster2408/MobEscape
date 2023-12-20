package xyz.mlserver.mobescape;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.mlserver.mc.util.CustomConfiguration;

public final class MobEscape extends JavaPlugin {

    public static CustomConfiguration config;
    public static CustomConfiguration messageYml;

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = new CustomConfiguration(this);
        config.saveDefaultConfig();
        messageYml = new CustomConfiguration(this, "message.yml");
        messageYml.saveDefaultConfig();

        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
