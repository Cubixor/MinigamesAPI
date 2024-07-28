package me.cubixor.minigamesapi.spigot.game.arena;


import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.events.GameStateChangeEvent;
import me.cubixor.minigamesapi.spigot.game.ArenaPlayersManager;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocalArena extends Arena {

    private final StateManager stateManager;
    private final Map<Player, PlayerData> playerData = new HashMap<>();
    private int timer = -1;

    public LocalArena(ArenasManager arenasManager, String name) {
        this(arenasManager, name, MinigamesAPI.getPlugin().getName(), false, false, 0, 0);
    }

    public LocalArena(ArenasManager arenasManager, String name, String server, boolean active, boolean vip, int minPlayers, int maxPlayers) {
        super(name, server, active, vip, minPlayers, maxPlayers);
        this.stateManager = new StateManager(this, arenasManager);
    }

    public Arena toArena() {
        return new Arena(getName(), getServer(), isActive(), isVip(), getMinPlayers(), getMaxPlayers(), getState(), getPlayers());
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public void setState(GameState state) {
        GameState prevState = getState();
        this.state = state;
        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(this, prevState, getState()));
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

    public StateManager getStateManager() {
        return stateManager;
    }

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
    }
}