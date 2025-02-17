package me.cubixor.minigamesapi.spigot.game.arena;


import me.cubixor.minigamesapi.spigot.events.GameStateChangeEvent;
import me.cubixor.minigamesapi.spigot.events.TimerTickEvent;
import me.cubixor.minigamesapi.spigot.game.inventories.MenuRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LocalArena extends Arena {

    private final Map<Player, PlayerData> playerData = new HashMap<>();
    private final Set<BukkitTask> tasks = new HashSet<>();
    private final ScoreboardManager scoreboardManager;
    private StateManager stateManager;
    private MenuRegistry menuRegistry;
    private int timer = -1;

    public LocalArena(String name, String server) {
        this(name, server, false, false, 0, 0);
    }

    public LocalArena(String name, String server, boolean active, boolean vip, int minPlayers, int maxPlayers) {
        super(name, server, active, vip, minPlayers, maxPlayers);
        this.scoreboardManager = new ScoreboardManager(this);
    }

    public void initialize(StateManager stateManager, MenuRegistry menuRegistry) {
        this.stateManager = stateManager;
        this.menuRegistry = menuRegistry;
    }

    public Arena toArena() {
        return new Arena(getName(), getServer(), isActive(), isVip(), getMinPlayers(), getMaxPlayers(), getState(), getPlayers(), getData());
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
        if (prevState.equals(state)) return;
        this.state = state;
        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(this, prevState, getState()));
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
        Bukkit.getServer().getPluginManager().callEvent(new TimerTickEvent(this));
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

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public MenuRegistry getMenuRegistry() {
        return menuRegistry;
    }

    public void addTask(BukkitTask task) {
        tasks.add(task);
    }

    public void cancelTasks() {
        tasks.forEach(BukkitTask::cancel);
        tasks.clear();
    }
}
