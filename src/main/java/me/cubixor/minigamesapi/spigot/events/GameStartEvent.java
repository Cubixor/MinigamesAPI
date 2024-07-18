package me.cubixor.minigamesapi.spigot.events;

import me.cubixor.minigamesapi.spigot.arena.Arena;
import me.cubixor.minigamesapi.spigot.arena.LocalArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//TODO Unused
public class GameStartEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final LocalArena localArena;

    public GameStartEvent(LocalArena localArena) {
        this.localArena = localArena;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Arena getLocalArena() {
        return localArena;
    }
}
