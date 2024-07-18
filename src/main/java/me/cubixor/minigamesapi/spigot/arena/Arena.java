package me.cubixor.minigamesapi.spigot.arena;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Arena implements Serializable {

    private final String name;
    private final String server;
    private boolean vip;
    private int minPlayers;
    private int maxPlayers;

    private GameState state;
    private List<String> players = new ArrayList<>();
    private final List<String> playersLeft = new ArrayList<>();


    public Arena(String name, String server, boolean active, boolean vip, int minPlayers, int maxPlayers) {
        this.name = name;
        this.server = server;
        this.vip = vip;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.state = active ? GameState.WAITING : GameState.INACTIVE;
    }

    public Arena(String name, String server, boolean active, boolean vip, int minPlayers, int maxPlayers, GameState state, List<String> players) {
        this(name, server, active, vip, minPlayers, maxPlayers);
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
        return state.equals(GameState.INACTIVE);
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
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
