package me.cubixor.minigamesapi.spigot.commands.arguments.impl.setup;

import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaCommandArgument;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgSetMaxPlayers extends ArenaCommandArgument {

    public ArgSetMaxPlayers(ArenasManager arenasManager) {
        super(arenasManager, "setmaxplayers", "setup.setmaxplayers", 3, "arena-setup.set-max-players", true, false);
    }

    @Override
    public void handle(Player player, String[] args) {
        String arenaName = args[1];

        int max;
        try {
            max = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            Messages.send(player, "arena-setup.set-max-players-invalid-count");
            return;
        }

        if (max < 2) {
            Messages.send(player, "arena-setup.set-max-players-too-small");
            return;
        }

        arenasManager.updateArenaMaxPlayers(arenaName, max);
        Messages.send(player, "arena-setup.set-max-players-success", "%arena%", arenaName);
    }
}
