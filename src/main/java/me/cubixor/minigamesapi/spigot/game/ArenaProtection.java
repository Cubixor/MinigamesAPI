package me.cubixor.minigamesapi.spigot.game;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ArenaProtection implements Listener {

    private final ArenasManager arenasManager;

    public ArenaProtection(ArenasManager arenasManager) {
        this.arenasManager = arenasManager;

        Bukkit.getServer().getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent evt) {
        LocalArena localArena = arenasManager.getRegistry().getPlayerLocalArena(evt.getPlayer());
        if (localArena == null) return;

        arenasManager.getArenaPlayersManager().leaveArena(evt.getPlayer(), localArena);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent evt) {
        cancelWaitingStarting(evt.getPlayer(), evt);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent evt) {
        cancelWaitingStarting(evt.getPlayer(), evt);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent evt) {
        cancelWaitingStarting(evt.getPlayer(), evt);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent evt) {
        cancelWaitingStarting(evt.getPlayer(), evt);
    }

    @EventHandler
    public void onHurt(EntityDamageEvent evt) {
        if (!(evt.getEntity() instanceof Player)) {
            return;
        }

        cancelWaitingStarting((Player) evt.getEntity(), evt);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent evt) {
        if (!evt.getEntity().getType().equals(EntityType.PLAYER)) {
            return;
        }
        cancelWaitingStarting((Player) evt.getEntity(), evt);
    }

    @EventHandler
    public void onClick(InventoryClickEvent evt) {
        cancelWaitingStarting((Player) evt.getWhoClicked(), evt);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent evt) {
        cancelWaitingStarting(evt.getPlayer(), evt);
    }

    private void cancelWaitingStarting(Player player, Cancellable evt) {
        if (isInWaitingStartingArena(player)) {
            evt.setCancelled(true);
        }
    }

    private boolean isInWaitingStartingArena(Player player) {
        LocalArena localArena = arenasManager.getRegistry().getPlayerLocalArena(player);

        if (localArena == null) return false;

        return localArena.getState().isWaitingStarting();
    }
}
