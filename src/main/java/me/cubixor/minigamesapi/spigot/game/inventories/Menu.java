package me.cubixor.minigamesapi.spigot.game.inventories;

import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;

public abstract class Menu extends GlobalMenu {

    private final LocalArena arena;

    protected Menu(LocalArena arena) {
        this.arena = arena;
    }

    public LocalArena getArena() {
        return arena;
    }
}
