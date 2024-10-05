package me.cubixor.minigamesapi.spigot.game.inventories;

import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public abstract class Menu {

    private final LocalArena arena;
    private Inventory inventory;

    protected Menu(LocalArena arena) {
        this.arena = arena;
    }

    protected abstract Inventory create();

    public abstract void update();

    public abstract void handleClick(InventoryClickEvent evt, Player player);

    public void open(Player player) {
        player.openInventory(getInventory());
    }

    public void setup() {
        inventory = create();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public LocalArena getArena() {
        return arena;
    }
}
