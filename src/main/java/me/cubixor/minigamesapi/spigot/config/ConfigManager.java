package me.cubixor.minigamesapi.spigot.config;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.config.stats.DBStatsManager;
import me.cubixor.minigamesapi.spigot.config.stats.FileStatsManager;
import me.cubixor.minigamesapi.spigot.config.stats.StatsField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.socketsmc.spigot.SocketClient;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private final MinigamesAPI plugin = MinigamesAPI.getInstance();
    private final CustomConfig messagesConfig;
    private final CustomConfig arenasConfig;
    private final CustomConfig connectionConfig;

    private final StatsManager statsManager;
    private final ArenasConfigManager arenasConfigManager;

    public ConfigManager(List<StatsField> statsFields) {
        plugin.saveDefaultConfig();
        messagesConfig = new CustomConfig("messages.yml");
        arenasConfig = new CustomConfig("arenas.yml");
        connectionConfig = new CustomConfig("connection.yml");

        arenasConfigManager = new ArenasConfigManager(arenasConfig);

        if (getConfig().getBoolean("database.enabled-stats")) {
            CustomConfig playersConfig = new CustomConfig("players.yml");
            statsManager = new FileStatsManager(statsFields, playersConfig);
        } else {
            DBManager dbManager = setupDB();
            dbManager.createStatsTable(statsFields);

            statsManager = new DBStatsManager(statsFields, dbManager);
        }
    }

    public DBManager setupDB() {
        return new DBManager(
                connectionConfig.get().getString("database.address"),
                connectionConfig.get().getString("database.username"),
                connectionConfig.get().getString("database.password"),
                connectionConfig.get().getString("database.table")
        );
    }

    public SocketClient setupSocket() {
        return new SocketClient(
                MinigamesAPI.getInstance(),
                connectionConfig.get().getString("bungee-socket.host"),
                connectionConfig.get().getInt("bungee-socket.port"),
                connectionConfig.get().getString("bungee-socket.server-name"),
                connectionConfig.get().getBoolean("bungee-socket.debug")
        );
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig.get();
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public ArenasConfigManager getArenasConfigManager() {
        return arenasConfigManager;
    }
}
