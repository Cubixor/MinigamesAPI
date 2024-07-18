package me.cubixor.minigamesapi.spigot.commands.arguments.impl;

import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaCommandArgument;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgSetMaxPlayers extends ArenaCommandArgument {

    public ArgSetMaxPlayers(ArenasManager arenasManager) {
        super(arenasManager, "setmaxplayers", "setup.setmaxplayers", 3, "arena-setup.set-max-players", true, false);
    }

    @Override
    public void handle(Player player, String[] args) {
        String arenaName = args[0];

        int max;
        try {
            max = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            Messages.send(player, "arena-setup.set-max-players-invalid-count");
            return;
        }

        arenasManager.updateArenaMaxPlayers(arenaName, max);
        Messages.send(player, "arena-setup.set-max-players-success", "%arena%", arenaName);
    }
}
