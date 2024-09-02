package me.cubixor.minigamesapi.spigot.events;

import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class GameChatEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final LocalArena localArena;
    private final Player player;
    private final Set<Player> receivers;
    private boolean cancel;
    private String message;

    public GameChatEvent(LocalArena localArena, Player player, Set<Player> receivers, String message) {
        this.localArena = localArena;
        this.player = player;
        this.receivers = receivers;
        this.message = message;
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

    public Player getPlayer() {
        return player;
    }

    public Set<Player> getReceivers() {
        return receivers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
