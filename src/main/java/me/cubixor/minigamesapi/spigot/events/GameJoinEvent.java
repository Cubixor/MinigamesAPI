package me.cubixor.minigamesapi.spigot.events;

import me.cubixor.minigamesapi.spigot.arena.objects.Arena;
import me.cubixor.minigamesapi.spigot.arena.objects.LocalArena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//TODO Unused
public class GameJoinEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final LocalArena localArena;
    private final Player player;

    public GameJoinEvent(LocalArena localArena, Player player) {
        this.localArena = localArena;
        this.player = player;
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

    public Player getPlayer() {
        return player;
    }
}
