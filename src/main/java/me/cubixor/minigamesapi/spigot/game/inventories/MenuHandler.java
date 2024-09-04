package me.cubixor.minigamesapi.spigot.game.inventories;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.ArenasRegistry;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collections;

public class MenuHandler implements Listener {

    private final ArenasRegistry arenasRegistry;

    public MenuHandler(ArenasRegistry arenasRegistry) {
        this.arenasRegistry = arenasRegistry;

        Bukkit.getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getClickedInventory() == null) return;
        if (evt.getCurrentItem() == null || evt.getCurrentItem().getType().equals(Material.AIR)) return;

        Player player = (Player) evt.getWhoClicked();
        LocalArena arena = arenasRegistry.getPlayerLocalArena(player);

        if (arena == null) return;

        Menu menu = getMenuByInventory(arena, evt.getClickedInventory());
        if (menu == null) return;

        evt.setCancelled(true);
        menu.handleClick(evt, player);

        Sounds.playSound("click", player.getLocation(), Collections.singleton(player));
        player.getOpenInventory().close();

        menu.update();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent evt) {
        Player player = (Player) evt.getPlayer();
        LocalArena arena = arenasRegistry.getPlayerLocalArena(player);
        if (arena == null) return;

        Menu menu = getMenuByInventory(arena, evt.getInventory());
        if (menu == null) return;

        menu.update();
    }

    private Menu getMenuByInventory(LocalArena arena, Inventory inventory) {
        for (Menu menu : arena.getMenuRegistry().getMenus()) {
            if (inventory.equals(menu.getInventory())) {
                return menu;
            }
        }
        return null;
    }
}
