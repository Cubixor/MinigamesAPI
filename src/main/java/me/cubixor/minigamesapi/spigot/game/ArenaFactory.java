package me.cubixor.minigamesapi.spigot.game;

import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;

public class ArenaFactory {




    public LocalArena createBlankArena(String name, String serverName, ArenasManager arenasManager, StatsManager statsManager) {
        return new LocalArena(arenasManager, statsManager, name, serverName);
    }

    public LocalArena loadArenaFromConfig(String name, String serverName, ArenasManager arenasManager, StatsManager statsManager) {
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
