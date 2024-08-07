package me.cubixor.minigamesapi.spigot.events;

import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimerTickEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final LocalArena localArena;
    private final GameState gameState;
    private final int timer;

    public TimerTickEvent(LocalArena localArena) {
        this.localArena = localArena;
        this.gameState = localArena.getState();
        this.timer = localArena.getTimer();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public LocalArena getLocalArena() {
        return localArena;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getTimer() {
        return timer;
    }
}
