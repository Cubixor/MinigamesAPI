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
    private final int gameTime;

    public GameEndEvent(LocalArena localArena, List<Player> winners, int gameTime) {
        this.localArena = localArena;
        this.winners = winners;
        this.gameTime = gameTime;
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

    public int getGameTime() {
        return gameTime;
    }
}
