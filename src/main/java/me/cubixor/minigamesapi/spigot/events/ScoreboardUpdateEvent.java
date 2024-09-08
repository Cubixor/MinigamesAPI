package me.cubixor.minigamesapi.spigot.events;

import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.Map;

public class ScoreboardUpdateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final LocalArena localArena;
    private final GameState gameState;
    private final Map<String, String> replacement;
    private final Map<String, List<String>> multiLineReplacement;

    public ScoreboardUpdateEvent(LocalArena localArena, Map<String, String> replacement, Map<String, List<String>> multiLineReplacement) {
        this.localArena = localArena;
        this.gameState = localArena.getState();
        this.replacement = replacement;
        this.multiLineReplacement = multiLineReplacement;
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

    public void addReplacement(String key, String value) {
        replacement.put(key, value);
    }

    public void addMultiLineReplacement(String key, List<String> value) {
        multiLineReplacement.put(key, value);
    }
}
