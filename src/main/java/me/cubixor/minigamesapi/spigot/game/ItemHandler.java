package me.cubixor.minigamesapi.spigot.game;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Set;

public class ItemHandler implements Listener {

    private final ArenasManager arenasManager;

    public ItemHandler(ArenasManager arenasManager) {
        this.arenasManager = arenasManager;

        Bukkit.getServer().getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        if (evt.getItem() == null) return;
        if (!evt.getHand().equals(EquipmentSlot.HAND)) return;

        LocalArena localArena = arenasManager.getRegistry().getPlayerLocalArena(evt.getPlayer());
        if (localArena == null) return;

        if (evt.getItem().equals(Items.getLeaveItem().getItem())) {
            arenasManager.getArenaPlayersManager().leaveArena(evt.getPlayer(), localArena);
        }
    }
}
