package me.cubixor.minigamesapi.spigot.game.items;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ItemHandler implements Listener {

    private final ArenasManager arenasManager;
    private final ItemsRegistry itemsRegistry;

    public ItemHandler(ArenasManager arenasManager, ItemsRegistry itemsRegistry) {
        this.arenasManager = arenasManager;
        this.itemsRegistry = itemsRegistry;

        Bukkit.getServer().getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        if (evt.getItem() == null) return;
        if (!evt.getHand().equals(EquipmentSlot.HAND)) return;

        if(evt.getItem().equals(itemsRegistry.getSetupWandItem().getItem())){
            itemsRegistry.getSetupWandItem().handleClick(arenasManager, null, evt.getPlayer(), evt);
            evt.setCancelled(true);
            return;
        }

        LocalArena localArena = arenasManager.getRegistry().getPlayerLocalArena(evt.getPlayer());
        if (localArena == null) return;

        for (ClickableItem item : itemsRegistry.getClickableItems()) {
            if (item.getItem().equals(evt.getItem())) {
                (item).handleClick(arenasManager, localArena, evt.getPlayer(), evt);
                break;
            }
        }
    }
}
