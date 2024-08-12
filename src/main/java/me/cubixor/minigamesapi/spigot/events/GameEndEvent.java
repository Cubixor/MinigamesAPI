package me.cubixor.minigamesapi.spigot.events;

import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class GameEndEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final LocalArena localArena;
    private final List<Player> winners;

    public GameEndEvent(LocalArena localArena, List<Player> winners) {
        this.localArena = localArena;
        this.winners = winners;
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

    public List<Player> getWinners() {
        return winners;
    }
}
