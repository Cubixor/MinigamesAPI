package me.cubixor.minigamesapi.spigot.events;

import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameResetEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final LocalArena localArena;
    private boolean resetFinished = true;

    public GameResetEvent(LocalArena localArena) {
        this.localArena = localArena;
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

    public boolean isResetFinished() {
        return resetFinished;
    }

    public void setResetFinished(boolean resetFinished) {
        this.resetFinished = resetFinished;
    }
}
