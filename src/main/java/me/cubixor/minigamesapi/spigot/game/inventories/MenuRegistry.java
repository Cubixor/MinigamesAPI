package me.cubixor.minigamesapi.spigot.game.inventories;

import java.util.HashSet;
import java.util.Set;

public class MenuRegistry {

    private final Set<GlobalMenu> menus = new HashSet<>();

    public Set<GlobalMenu> getMenus() {
        return menus;
    }
}
