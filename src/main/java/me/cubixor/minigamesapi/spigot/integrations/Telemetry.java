package me.cubixor.minigamesapi.spigot.integrations;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.ArenasRegistry;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.configuration.file.FileConfiguration;

public class Telemetry {

    public void runMetrics(ArenasRegistry arenasRegistry, int pluginID) {
        FileConfiguration config = MinigamesAPI.getPlugin().getConfig();

        Metrics metrics = new Metrics(MinigamesAPI.getPlugin(), pluginID);
        metrics.addCustomChart(new SimplePie("used_language", () -> config.getString("language", "en")));
        metrics.addCustomChart(new SingleLineChart("games", () -> arenasRegistry.getLocalArenas().size()));

        String bungeeMode;
        if (config.getBoolean("bungee.bungee-mode")) {
            bungeeMode = "bungee-mode";
        } else if (config.getBoolean("bungee.simple-bungee-mode")) {
            bungeeMode = "simple-bungee-mode";
        } else {
            bungeeMode = "false";
        }

        metrics.addCustomChart(new SimplePie("bungee", () -> bungeeMode));
    }
}
