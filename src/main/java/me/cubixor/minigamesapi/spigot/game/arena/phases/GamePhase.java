package me.cubixor.minigamesapi.spigot.game.arena.phases;

import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;

public abstract class GamePhase {

    protected final LocalArena localArena;

    protected GamePhase(LocalArena localArena) {
        this.localArena = localArena;
    }

    public abstract void run();

    public abstract void stop();

    public abstract GameState getGameState();
}
