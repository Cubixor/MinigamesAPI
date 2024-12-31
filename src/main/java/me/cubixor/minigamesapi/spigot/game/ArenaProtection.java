package me.cubixor.minigamesapi.spigot.game;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import org.bukkit.event.player.*;

public class ArenaProtection implements Listener {

    protected final ArenasManager arenasManager;

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
        cancelNotGame(evt.getPlayer(), evt);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent evt) {
        cancelNotGame(evt.getPlayer(), evt);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent evt) {
        cancelNotGame(evt.getPlayer(), evt);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent evt) {
        cancelNotGame(evt.getPlayer(), evt);
    }

    @EventHandler
    public void onHurt(EntityDamageEvent evt) {
        if (!(evt.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) evt.getEntity();
        LocalArena localArena = arenasManager.getRegistry().getPlayerLocalArena(player);

        if (localArena == null) return;
        if (localArena.getState().equals(GameState.GAME)) return;

        evt.setCancelled(true);

        /*if (localArena.getState().isWaitingStarting() && evt.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            Location loc = arenasManager.getConfigManager().getLocation(localArena.getName(), BasicConfigField.WAITING_LOBBY);
            player.teleport(loc);
        }*/
    }

    @EventHandler
    public void onMove(PlayerMoveEvent evt) {
        Player player = evt.getPlayer();
        LocalArena localArena = arenasManager.getRegistry().getPlayerLocalArena(player);

        if (localArena == null) return;

        if (localArena.getState().isWaitingStarting() && evt.getTo().getY() < -64) {
            Location loc = arenasManager.getConfigManager().getLocation(localArena.getName(), BasicConfigField.WAITING_LOBBY);
            player.teleport(loc);
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent evt) {
        if (!evt.getEntity().getType().equals(EntityType.PLAYER)) {
            return;
        }
        cancelNotGame((Player) evt.getEntity(), evt);
    }

    @EventHandler
    public void onClick(InventoryClickEvent evt) {
        cancelNotGame((Player) evt.getWhoClicked(), evt);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent evt) {
        cancelNotGame(evt.getPlayer(), evt);
    }

    @EventHandler
    public void onHandChange(PlayerSwapHandItemsEvent evt) {
        cancelNotGame(evt.getPlayer(), evt);
    }

    private void cancelNotGame(Player player, Cancellable evt) {
        if (isInNotGameArena(player)) {
            evt.setCancelled(true);
        }
    }

    private boolean isInNotGameArena(Player player) {
        LocalArena localArena = arenasManager.getRegistry().getPlayerLocalArena(player);

        if (localArena == null) return false;

        return !localArena.getState().equals(GameState.GAME);
    }

    protected void cancelInArena(Player player, Cancellable evt) {
        if (arenasManager.getRegistry().isInArena(player)) {
            evt.setCancelled(true);
        }
    }
}
