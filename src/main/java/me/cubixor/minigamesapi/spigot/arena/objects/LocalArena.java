package me.cubixor.minigamesapi.spigot.arena.objects;


import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.arena.StateManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocalArena extends Arena {

    private final StateManager stateManager = new StateManager(this);
    private final Map<Player, PlayerData> playerData = new HashMap<>();
    private int timer = -1;

    public LocalArena(String name) {
        super(name, MinigamesAPI.getPlugin().getName(), false, false, 0, 0);
    }

    public LocalArena(String name, String server, boolean active, boolean vip, int minPlayers, int maxPlayers) {
        super(name, server, active, vip, minPlayers, maxPlayers);
    }

    public Arena toArena() {
        return new Arena(getName(), getServer(), isActive(), isVip(), getMinPlayers(), getMaxPlayers(), getState(), getPlayers());
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public Set<Player> getBukkitPlayers() {
        return playerData.keySet();
    }

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
    }
}
