package me.cubixor.minigamesapi.spigot.game.items.impl;

import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.game.items.ClickableItem;
import org.bukkit.entity.Player;

public class LeaveItem extends ClickableItem {

    public LeaveItem() {
        super("items.leave-item", "game.leave-item-name", "game.leave-item-lore");
    }

    public void handleClick(ArenasManager arenasManager, LocalArena localArena, Player player) {
        arenasManager.getArenaPlayersManager().leaveArena(player, localArena);
    }
}
