package me.cubixor.minigamesapi.spigot.arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArenasRegistry {

    private final Map<String, LocalArena> localArenas = new HashMap<>();
    private final Map<String, Arena> remoteArenas = new HashMap<>();

    public boolean isInArena(Player player) {
        return localArenas.values().stream().anyMatch(arena -> arena.getPlayers().contains(player.getName()))
                || remoteArenas.values().stream().anyMatch(arena -> arena.getPlayers().contains(player.getName()));
    }

    public Arena getArena(String name) {
        if (localArenas.containsKey(name)) {
            return localArenas.get(name);
        } else if (remoteArenas.containsKey(name)) {
            return remoteArenas.get(name);
        }

        return null;
    }

    public Arena getPlayerArena(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player != null) {
            return getPlayerLocalArena(player);
        } else {
            Optional<Arena> arena = remoteArenas.values().stream().filter(a -> a.getPlayers().contains(playerName)).findFirst();
            return arena.orElse(null);
        }
    }

    public LocalArena getPlayerLocalArena(Player player) {
        Optional<LocalArena> arena = localArenas.values().stream().filter(a -> a.getBukkitPlayers().contains(player)).findFirst();
        return arena.orElse(null);
    }

    public boolean isValidArena(String arena) {
        return isLocalArena(arena) || isRemoteArena(arena);
    }

    public boolean isLocalArena(String arena) {
        return localArenas.containsKey(arena);
    }

    public boolean isRemoteArena(String arena) {
        return remoteArenas.containsKey(arena);
    }

    public Map<String, LocalArena> getLocalArenas() {
        return localArenas;
    }

    public Map<String, Arena> getRemoteArenas() {
        return remoteArenas;
    }

    public Set<String> getLocalArenaNames() {
        return localArenas.keySet();
    }

    public Set<String> getAllArenaNames() {
        return Stream.concat(localArenas.keySet().stream(), remoteArenas.keySet().stream()).collect(Collectors.toSet());
    }
}
