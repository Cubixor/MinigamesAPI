package me.cubixor.minigamesapi.spigot.game.items.impl;

import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.game.items.GameItem;
import org.bukkit.entity.Player;

public class LeaveItem extends GameItem {

    public LeaveItem() {
        super("items.leave-item", "game.leave-item-name", "game.leave-item-lore");
    }


    @Override
    public void handleClick(ArenasManager arenasManager, LocalArena localArena, Player player) {
        arenasManager.getArenaPlayersManager().leaveArena(player, localArena);
    }
}
