package me.cubixor.minigamesapi.spigot;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.plugin.java.JavaPlugin;

public class MinigamesAPI {

    private static JavaPlugin plugin;

    private MinigamesAPI() {
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void INIT(JavaPlugin javaPlugin) {
        //Initialize legacy material support
        XMaterial.matchXMaterial("BLACK_STAINED_GLASS").get().parseItem().getData();

        plugin = javaPlugin;
    }
}
