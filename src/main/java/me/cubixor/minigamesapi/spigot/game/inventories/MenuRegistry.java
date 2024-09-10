package me.cubixor.minigamesapi.spigot.game.inventories;

import java.util.HashSet;
import java.util.Set;

public class MenuRegistry {

    private final Set<Menu> menus;

    public MenuRegistry() {
        this.menus = new HashSet<>();
    }

    public Set<Menu> getMenus() {
        return menus;
    }
}
