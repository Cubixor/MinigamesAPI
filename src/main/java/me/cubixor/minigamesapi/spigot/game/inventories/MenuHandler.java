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
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.Collections;

public class MenuHandler implements Listener {

    private final ArenasRegistry arenasRegistry;
    private final MenuRegistry globalMenuRegistry;

    public MenuHandler(ArenasRegistry arenasRegistry, MenuRegistry globalMenuRegistry) {
        this.arenasRegistry = arenasRegistry;
        this.globalMenuRegistry = globalMenuRegistry;

        Bukkit.getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getClickedInventory() == null) return;

        Player player = (Player) evt.getWhoClicked();

        GlobalMenu globalMenu = getMenuByInventory(globalMenuRegistry.getMenus(), evt.getClickedInventory());
        if (globalMenu != null) {
            if (evt.getCurrentItem() == null || evt.getCurrentItem().getType().equals(Material.AIR)) {
                evt.setCancelled(true);
                return;
            }
            globalMenu.handleClick(evt, player);
            menuClick(player, globalMenu, evt);
            return;
        }

        LocalArena arena = arenasRegistry.getPlayerLocalArena(player);
        if (arena == null) return;

        Menu arenaMenu = (Menu) getMenuByInventory(arena.getMenuRegistry().getMenus(), evt.getClickedInventory());
        if (arenaMenu == null) return;

        if (evt.getCurrentItem() == null || evt.getCurrentItem().getType().equals(Material.AIR)) {
            evt.setCancelled(true);
            return;
        }
        arenaMenu.handleClick(evt, player);
        menuClick(player, arenaMenu, evt);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent evt) {
        if (evt.getInventory() == null) return;

        Player player = (Player) evt.getWhoClicked();

        GlobalMenu globalMenu = getMenuByInventory(globalMenuRegistry.getMenus(), evt.getInventory());
        if (globalMenu != null) {
            evt.setCancelled(true);
            return;
        }

        LocalArena arena = arenasRegistry.getPlayerLocalArena(player);
        if (arena == null) return;

        Menu arenaMenu = (Menu) getMenuByInventory(arena.getMenuRegistry().getMenus(), evt.getInventory());
        if (arenaMenu != null) {
            evt.setCancelled(true);
        }
    }

    private void menuClick(Player player, GlobalMenu menu, InventoryClickEvent evt) {
        evt.setCancelled(true);

        Sounds.playSound("click", player.getLocation(), Collections.singleton(player));
        if (player.getOpenInventory().getTopInventory().equals(evt.getInventory())) {
            player.getOpenInventory().close();
        }

        menu.update();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent evt) {
        Player player = (Player) evt.getPlayer();
        GlobalMenu globalMenu = getMenuByInventory(globalMenuRegistry.getMenus(), evt.getInventory());
        if (globalMenu != null) {
            globalMenu.update();
            return;
        }

        LocalArena arena = arenasRegistry.getPlayerLocalArena(player);
        if (arena == null) return;

        GlobalMenu menu = getMenuByInventory(arena.getMenuRegistry().getMenus(), evt.getInventory());
        if (menu == null) return;

        menu.update();
    }

    private GlobalMenu getMenuByInventory(Collection<GlobalMenu> menus, Inventory inventory) {
        for (GlobalMenu menu : menus) {
            if (inventory.equals(menu.getInventory())) {
                return menu;
            }
        }
        return null;
    }
}
