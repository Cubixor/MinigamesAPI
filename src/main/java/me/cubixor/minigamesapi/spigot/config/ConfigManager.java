package me.cubixor.minigamesapi.spigot.config;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.config.stats.DBStatsManager;
import me.cubixor.minigamesapi.spigot.config.stats.FileStatsManager;
import me.cubixor.minigamesapi.spigot.config.stats.StatsField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.Particles;
import me.cubixor.minigamesapi.spigot.utils.Sounds;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfigManager {

    private final JavaPlugin plugin = MinigamesAPI.getPlugin();
    private final CustomConfig arenasConfig;
    private final CustomConfig connectionConfig;
    private CustomConfig messagesConfig;

    private final StatsManager statsManager;
    private final ArenasConfigManager arenasConfigManager;

    public ConfigManager(List<StatsField> statsFields, String[] languages) {
        updateConfig();

        arenasConfig = new CustomConfig("arenas.yml");
        connectionConfig = new CustomConfig("connection.yml");
        for (String lang : languages) {
            CustomConfig messagesLangConfig = new CustomConfig("messages-" + lang + ".yml");
            messagesLangConfig.copyDefaults();
            if (getConfig().getString("language").equalsIgnoreCase(lang)) {
                messagesConfig = messagesLangConfig;
            }
        }

        arenasConfigManager = new ArenasConfigManager(arenasConfig);

        if (getConfig().getBoolean("database.enabled-stats")) {
            DBManager dbManager = setupDB();
            dbManager.createStatsTable(statsFields);

            statsManager = new DBStatsManager(statsFields, dbManager);
        } else {
            CustomConfig playersConfig = new CustomConfig("players.yml");
            statsManager = new FileStatsManager(statsFields, playersConfig);
        }

        PluginCommand cmd = plugin.getCommand(plugin.getName());
        String alias = cmd.getAliases().isEmpty() ? cmd.getName() : cmd.getAliases().get(0);
        Messages.init(messagesConfig.get(), alias);
        Particles.init(getConfig());
        Sounds.init(getConfig());
    }

    private void updateConfig() {
        plugin.saveDefaultConfig();

        boolean versionMismatch = getConfig().getDouble("config-version") != getConfig().getDefaults().getDouble("config-version");

        plugin.getConfig().options().copyDefaults(true);

        if (versionMismatch) {
            getConfig().set("config-version", getConfig().getDefaults().getDouble("config-version"));
            plugin.saveConfig();
        }
    }

    public DBManager setupDB() {
        return new DBManager(
                getConfig().getString("database.address"),
                getConfig().getString("database.username"),
                getConfig().getString("database.password"),
                getConfig().getString("database.table")
        );
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig.get();
    }

    public CustomConfig getConnectionConfig() {
        return connectionConfig;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public ArenasConfigManager getArenasConfigManager() {
        return arenasConfigManager;
    }
}
