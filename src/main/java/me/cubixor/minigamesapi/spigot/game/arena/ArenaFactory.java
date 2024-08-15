package me.cubixor.minigamesapi.spigot.game.arena;

import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;

public class ArenaFactory {

    private final ArenasManager arenasManager;
    private final StatsManager statsManager;
    private final String serverName;

    public ArenaFactory(ArenasManager arenasManager, StatsManager statsManager, String serverName) {
        this.arenasManager = arenasManager;
        this.statsManager = statsManager;
        this.serverName = serverName;
    }

    public LocalArena createBlankArena(String name) {
        return new LocalArena(arenasManager, statsManager, name, serverName);
    }

    public LocalArena loadArenaFromConfig(String name) {
        ArenasConfigManager configManager = arenasManager.getConfigManager();

        return new LocalArena(
                arenasManager,
                statsManager,
                name,
                serverName,
                configManager.getBoolean(name, BasicConfigField.ACTIVE),
                configManager.getBoolean(name, BasicConfigField.VIP),
                configManager.getInt(name, BasicConfigField.MIN_PLAYERS),
                configManager.getInt(name, BasicConfigField.MAX_PLAYERS)
        );
    }
}
