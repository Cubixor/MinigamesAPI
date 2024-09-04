package me.cubixor.minigamesapi.spigot.game;

import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;

public interface ArenaFactory {
    LocalArena createBlankArena(String name, String serverName, ArenasManager arenasManager, StatsManager statsManager);

    LocalArena loadArenaFromConfig(String name, String serverName, ArenasManager arenasManager, StatsManager statsManager);

    void initArena(LocalArena localArena, ArenasManager arenasManager, StatsManager statsManager);

}
