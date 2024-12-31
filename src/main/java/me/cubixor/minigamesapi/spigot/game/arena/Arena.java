package me.cubixor.minigamesapi.spigot.game.arena;

import java.io.Serializable;
import java.util.*;

public class Arena implements Serializable {

    private final String name;
    private final String server;
    protected boolean vip;
    protected int minPlayers;
    protected int maxPlayers;
    protected GameState state;
    private List<String> players = new ArrayList<>();
    private final Map<String, String> data;

    public Arena(String name, String server, boolean active, boolean vip, int minPlayers, int maxPlayers) {
        this(name, server, active, vip, minPlayers, maxPlayers, new HashMap<>());
    }

    public Arena(String name, String server, boolean active, boolean vip, int minPlayers, int maxPlayers, Map<String, String> data) {
        this.name = name;
        this.server = server;
        this.vip = vip;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.state = active ? GameState.WAITING : GameState.INACTIVE;
        this.data = data;
    }

    public Arena(String name, String server, boolean active, boolean vip, int minPlayers, int maxPlayers, GameState state, List<String> players, Map<String, String> data) {
        this(name, server, active, vip, minPlayers, maxPlayers,data);
        this.state = state;
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public boolean isActive() {
        return !state.equals(GameState.INACTIVE);
    }

    public boolean isVip() {
        return vip;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public GameState getState() {
        return state;
    }

    public List<String> getPlayers() {
        return players;
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public boolean isLocal() {
        return this instanceof LocalArena;
    }

    public Map<String, String> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Arena{" +
                "name='" + name + '\'' +
                ", server='" + server + '\'' +
                ", state=" + state +
                ", players=" + players +
                '}';
    }
}
