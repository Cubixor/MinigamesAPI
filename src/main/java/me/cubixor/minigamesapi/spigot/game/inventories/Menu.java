package me.cubixor.minigamesapi.spigot.game.inventories;

import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class Menu extends GlobalMenu {

    private final LocalArena arena;

    protected Menu(LocalArena arena) {
        this.arena = arena;
    }

    @Override
    public void handleClick(InventoryClickEvent evt, Player player, LocalArena arena) {
        handleClick(evt, player);
    }

    public abstract void handleClick(InventoryClickEvent evt, Player player);

    public LocalArena getArena() {
        return arena;
    }
}
