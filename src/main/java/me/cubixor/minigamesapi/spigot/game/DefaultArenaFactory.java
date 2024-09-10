package me.cubixor.minigamesapi.spigot.game;

import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.game.arena.StateManager;
import me.cubixor.minigamesapi.spigot.game.inventories.MenuRegistry;

public class DefaultArenaFactory implements ArenaFactory {

    public LocalArena createBlankArena(String name, String serverName, ArenasManager arenasManager, StatsManager statsManager) {
        LocalArena localArena = new LocalArena(name, serverName);
        initArena(localArena, arenasManager, statsManager);
        return localArena;
    }

    public LocalArena loadArenaFromConfig(String name, String serverName, ArenasManager arenasManager, StatsManager statsManager) {
        ArenasConfigManager configManager = arenasManager.getConfigManager();

        LocalArena localArena = new LocalArena(
                name,
                serverName,
                configManager.getBoolean(name, BasicConfigField.ACTIVE),
                configManager.getBoolean(name, BasicConfigField.VIP),
                configManager.getInt(name, BasicConfigField.MIN_PLAYERS),
                configManager.getInt(name, BasicConfigField.MAX_PLAYERS)
        );
        initArena(localArena, arenasManager, statsManager);
        return localArena;
    }

    public void initArena(LocalArena localArena, ArenasManager arenasManager, StatsManager statsManager) {
        StateManager stateManager = new StateManager(localArena, arenasManager, statsManager);
        MenuRegistry menuRegistry = new MenuRegistry();
        localArena.initialize(stateManager, menuRegistry);
    }
}
