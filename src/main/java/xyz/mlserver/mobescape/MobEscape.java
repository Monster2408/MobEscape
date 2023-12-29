package xyz.mlserver.mobescape;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.mlserver.mc.util.CustomConfiguration;
import xyz.mlserver.mobescape.commands.MobEscapeCmd;
import xyz.mlserver.mobescape.listeners.BukkitPlayerJoinListener;
import xyz.mlserver.mobescape.listeners.BukkitPlayerQuitListener;
import xyz.mlserver.mobescape.listeners.MEGUIClickListener;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

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

        getServer().getPluginManager().registerEvents(new BukkitPlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new BukkitPlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new MEGUIClickListener(), this);

        getCommand("mobescape").setExecutor(new MobEscapeCmd());

        plugin = this;

        MobEscapeAPI.loadMap();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
