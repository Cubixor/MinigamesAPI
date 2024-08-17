package me.cubixor.minigamesapi.spigot.events;

import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Map;

public class ArenaStatusParseEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Arena arena;
    private final Map<String, String> replacement;

    public ArenaStatusParseEvent(boolean async, Arena arena, Map<String, String> replacement) {
        super(async);
        this.arena = arena;
        this.replacement = replacement;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Arena getArena() {
        return arena;
    }

    public void addReplacement(String key, String value) {
        replacement.put(key, value);
    }
}
