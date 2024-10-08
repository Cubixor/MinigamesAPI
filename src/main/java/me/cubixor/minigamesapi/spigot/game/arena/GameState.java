package me.cubixor.minigamesapi.spigot.game.arena;

import java.io.Serializable;

public enum GameState implements Serializable {
    WAITING, STARTING, GAME, ENDING, INACTIVE, OFFLINE;


    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public boolean isWaitingStarting() {
        return this.equals(GameState.WAITING) || this.equals(GameState.STARTING);
    }
}
