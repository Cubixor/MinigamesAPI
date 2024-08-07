package me.cubixor.minigamesapi.spigot.commands.arguments.impl.setup;

import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaCommandArgument;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgSetMinPlayers extends ArenaCommandArgument {

    public ArgSetMinPlayers(ArenasManager arenasManager) {
        super(arenasManager, "setminplayers", "setup.setminplayers", 3, "arena-setup.set-min-players", true, false);
    }

    @Override
    public void handle(Player player, String[] args) {
        String arenaName = args[1];

        int min;
        try {
            min = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            Messages.send(player, "arena-setup.set-min-players-invalid-count");
            return;
        }

        if (min < 2) {
            Messages.send(player, "arena-setup.set-min-players-too-small");
            return;
        }

        arenasManager.updateArenaMinPlayers(arenaName, min);
        Messages.send(player, "arena-setup.set-min-players-success", "%arena%", arenaName);
    }
}
