package me.cubixor.minigamesapi.spigot.game.items;

import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ClickableItem extends GameSlotItem {

    protected ClickableItem(String configPath, String namePath, String lorePath) {
        super(configPath, namePath, lorePath);
    }

    protected ClickableItem(ItemStack item, String namePath, String lorePath) {
        super(item, namePath, lorePath);
    }

    public void give(Player player) {
        player.getInventory().setItem(slot, item);
    }

    public abstract void handleClick(ArenasManager arenasManager, LocalArena localArena, Player player, PlayerInteractEvent evt);
}
