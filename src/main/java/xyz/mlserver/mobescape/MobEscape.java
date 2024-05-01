package xyz.mlserver.mobescape;

import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.mlserver.java.Log;
import xyz.mlserver.mc.util.CustomConfiguration;
import xyz.mlserver.mobescape.commands.MobEscapeCmd;
import xyz.mlserver.mobescape.commands.MobEscapeTab;
import xyz.mlserver.mobescape.listeners.BukkitBlockClickListener;
import xyz.mlserver.mobescape.listeners.BukkitItemDropListener;
import xyz.mlserver.mobescape.listeners.BukkitPlayerDamageByEntityListener;
import xyz.mlserver.mobescape.listeners.BukkitPlayerDamageEvent;
import xyz.mlserver.mobescape.listeners.BukkitPlayerDeathEvent;
import xyz.mlserver.mobescape.listeners.BukkitPlayerFoodLevelChangeListener;
import xyz.mlserver.mobescape.listeners.BukkitPlayerJoinListener;
import xyz.mlserver.mobescape.listeners.BukkitPlayerMoveListener;
import xyz.mlserver.mobescape.listeners.BukkitPlayerQuitListener;
import xyz.mlserver.mobescape.listeners.BukkitRightClickListener;
import xyz.mlserver.mobescape.listeners.BukkitSignChangeListener;
import xyz.mlserver.mobescape.listeners.MEGUIClickListener;
import xyz.mlserver.mobescape.utils.MobEscapeDB;
import xyz.mlserver.mobescape.utils.api.MainAPI;
import xyz.mlserver.mobescape.utils.api.MobEscapeAPI;
import xyz.mlserver.mobescape.utils.trait.MoveAndAttackTrait;
import xyz.mlserver.mobescape.utils.trait.MoveAndBreakTrait;

import java.util.ArrayList;
import java.util.List;

public final class MobEscape extends JavaPlugin {

    public static CustomConfiguration config;
    public static CustomConfiguration messageYml;
    public static CustomConfiguration dataYml;

    private static JavaPlugin plugin;
    private boolean placeholderapi = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = new CustomConfiguration(this);
        config.saveDefaultConfig();
        messageYml = new CustomConfiguration(this, "message.yml");
        messageYml.saveDefaultConfig();
        dataYml = new CustomConfiguration(this, "data.yml");
        dataYml.saveDefaultConfig();

        Plugin PlaceholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if (PlaceholderAPI != null && PlaceholderAPI.isEnabled()) {
            placeholderapi = true;
            Log.info("Successfully linked with PlaceholderAPI, version " + PlaceholderAPI.getDescription().getVersion());
            new MobEscapePlaceholders(this).register();
        }

        CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(MoveAndAttackTrait.class).withName("moveandattack"));
        CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(MoveAndBreakTrait.class).withName("moveandbreak"));

        getServer().getPluginManager().registerEvents(new BukkitBlockClickListener(), this);
        getServer().getPluginManager().registerEvents(new BukkitItemDropListener(), this);
        getServer().getPluginManager().registerEvents(new BukkitPlayerDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new BukkitPlayerDamageEvent(), this);
        getServer().getPluginManager().registerEvents(new BukkitPlayerDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new BukkitPlayerFoodLevelChangeListener(), this);
        getServer().getPluginManager().registerEvents(new BukkitPlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new BukkitPlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new BukkitPlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new BukkitRightClickListener(), this);
        getServer().getPluginManager().registerEvents(new BukkitSignChangeListener(), this);

        getServer().getPluginManager().registerEvents(new MEGUIClickListener(), this);

        getCommand("mobescape").setExecutor(new MobEscapeCmd());
        getCommand("mobescape").setTabCompleter(new MobEscapeTab());

        plugin = this;

        MobEscapeAPI.loadMap();
        MainAPI.setDebug(config.getConfig().getBoolean("debug", false));
        MobEscapeAPI.setWinCommandList();
        MobEscapeAPI.setUpdateBestTimeCommandList();

        MobEscapeDB.load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MobEscapeAPI.save();
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public boolean isPlaceholderAPI() {
        return placeholderapi;
    }
}
