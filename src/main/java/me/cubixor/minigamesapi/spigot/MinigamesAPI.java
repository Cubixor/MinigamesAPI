package me.cubixor.minigamesapi.spigot;

import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.ArenasRegistry;
import me.cubixor.minigamesapi.spigot.integrations.PlaceholderExpansion;
import me.cubixor.minigamesapi.spigot.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MinigamesAPI {

    private static JavaPlugin plugin;
    private static boolean usesPAPI = false;

    private MinigamesAPI() {
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void INIT(JavaPlugin javaPlugin) {
        //Initialize legacy material support
        VersionUtils.initialize();

        plugin = javaPlugin;
    }

    public static void registerPAPI(ArenasRegistry arenasRegistry, StatsManager statsManager) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlaceholderExpansion placeholderExpansion = new PlaceholderExpansion(arenasRegistry, statsManager);
            placeholderExpansion.register();
            usesPAPI = true;
        }
    }

    public static boolean usesPAPI() {
        return usesPAPI;
    }

    public static void disable(ArenasManager arenasManager) {
        arenasManager.getRegistry().getLocalArenas().values().forEach(arena -> arena.getStateManager().reset());
    }
}
