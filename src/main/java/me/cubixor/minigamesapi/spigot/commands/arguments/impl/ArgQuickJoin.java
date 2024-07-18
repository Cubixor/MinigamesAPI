package me.cubixor.minigamesapi.spigot.commands.arguments.impl;

import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import org.bukkit.entity.Player;

public class ArgQuickJoin extends CommandArgument {

    private final ArenasManager arenasManager;

    public ArgQuickJoin(ArenasManager arenasManager) {
        super("quickjoin", "play.quickjoin", 1, null);
        this.arenasManager = arenasManager;
    }

    @Override
    public void handle(Player player, String[] args) {
        arenasManager.getArenaPlayersManager().joinRandomArena(player);
    }
}
