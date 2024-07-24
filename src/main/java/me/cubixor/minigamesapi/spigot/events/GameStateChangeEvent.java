package me.cubixor.minigamesapi.spigot.events;

import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//TODO Unused
public class GameStateChangeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final LocalArena localArena;
    private final GameState from;
    private final GameState to;

    public GameStateChangeEvent(LocalArena localArena, GameState from, GameState to) {
        this.localArena = localArena;
        this.from = from;
        this.to = to;
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

    public GameState getFrom() {
        return from;
    }

    public GameState getTo() {
        return to;
    }
}
