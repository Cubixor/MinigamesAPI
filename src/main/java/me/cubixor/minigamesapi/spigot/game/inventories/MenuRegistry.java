package me.cubixor.minigamesapi.spigot.game.inventories;

import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;

import java.util.HashSet;
import java.util.Set;

public class MenuRegistry {

    private final LocalArena arena;
    private final Set<Menu> menus;

    public MenuRegistry(LocalArena arena) {
        this.arena = arena;
        this.menus = new HashSet<>();
    }

    public Set<Menu> getMenus() {
        return menus;
    }
}
