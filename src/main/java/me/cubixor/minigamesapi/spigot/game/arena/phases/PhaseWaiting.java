package me.cubixor.minigamesapi.spigot.game.arena.phases;

import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;

public class PhaseWaiting extends GamePhase {

    public PhaseWaiting(LocalArena localArena) {
        super(localArena);
    }

    @Override
    public void run() {
        localArena.setTimer(-1);
    }

    @Override
    public void stop() {

    }

    @Override
    public GameState getGameState() {
        return GameState.WAITING;
    }
}
