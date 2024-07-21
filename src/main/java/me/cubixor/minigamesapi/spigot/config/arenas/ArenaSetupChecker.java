package me.cubixor.minigamesapi.spigot.config.arenas;


import me.cubixor.minigamesapi.spigot.arena.objects.LocalArena;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArenaSetupChecker {

    private final ArenasConfigManager arenasConfigManager;

    public ArenaSetupChecker(ArenasConfigManager arenasConfigManager) {
        this.arenasConfigManager = arenasConfigManager;
    }

    public boolean checkReady(LocalArena arena) {
        return getReadyMap(arena).containsValue(false);
    }

    public Map<ConfigField, Boolean> getReadyMap(LocalArena arena) {
        LinkedHashMap<ConfigField, Boolean> ready = new LinkedHashMap<>();

        ready.put(BasicConfigField.MIN_PLAYERS, arena.getMinPlayers() > 0);
        ready.put(BasicConfigField.MAX_PLAYERS, arena.getMaxPlayers() > 0);
        ready.put(BasicConfigField.MAIN_LOBBY, arenasConfigManager.getLocation(arena.getName(), BasicConfigField.MAIN_LOBBY) != null);
        ready.put(BasicConfigField.WAITING_LOBBY, arenasConfigManager.getLocation(arena.getName(), BasicConfigField.WAITING_LOBBY) != null);

        return ready;
    }
}
