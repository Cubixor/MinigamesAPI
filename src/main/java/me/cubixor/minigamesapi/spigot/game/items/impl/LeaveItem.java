package me.cubixor.minigamesapi.spigot.game.items.impl;

import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.game.items.ClickableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LeaveItem extends ClickableItem {

    public LeaveItem() {
        super("items.leave-item", "game.leave-item-name", "game.leave-item-lore");
    }

    public void handleClick(ArenasManager arenasManager, LocalArena localArena, Player player, PlayerInteractEvent evt) {
        arenasManager.getArenaPlayersManager().leaveArena(player, localArena);
    }
}
