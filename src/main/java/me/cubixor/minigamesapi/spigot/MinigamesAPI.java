package me.cubixor.minigamesapi.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public class MinigamesAPI {

    private static JavaPlugin plugin;

    private MinigamesAPI() {
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void INIT(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
    }
}
