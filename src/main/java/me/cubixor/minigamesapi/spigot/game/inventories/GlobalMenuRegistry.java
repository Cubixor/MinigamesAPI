package me.cubixor.minigamesapi.spigot.game.inventories;

import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.inventories.impl.ArenasMenu;
import me.cubixor.minigamesapi.spigot.game.items.ItemsRegistry;

public class GlobalMenuRegistry extends MenuRegistry {

    private final GlobalMenu arenasMenu;

    public GlobalMenuRegistry(ArenasManager arenasManager, ItemsRegistry itemsRegistry) {
        arenasMenu = new ArenasMenu(arenasManager, itemsRegistry);
        arenasMenu.setup();
        getMenus().add(arenasMenu);
    }

    public GlobalMenu getArenasMenu() {
        return arenasMenu;
    }
}
