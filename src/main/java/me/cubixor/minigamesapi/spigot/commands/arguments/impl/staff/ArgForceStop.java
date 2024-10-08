package me.cubixor.minigamesapi.spigot.commands.arguments.impl.staff;

import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaCommandArgument;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgForceStop extends ArenaCommandArgument {


    public ArgForceStop(ArenasManager arenasManager) {
        super(arenasManager, "forcestop", "staff.stop", 2, "arena-moderate.force-stop", false, true);
    }

    @Override
    public void handle(Player player, String[] args) {
        String arenaName = args[1];
        Arena arena = arenasRegistry.getArena(arenaName);

        if (arena.getState().isWaitingStarting()) {
            Messages.send(player, "arena-moderate.force-stop-not-started", "%arena%", arenaName);
            return;
        }

        if (!arena.getPlayers().contains(player.getName())) {
            Messages.send(player, "arena-moderate.force-stop-success");
        }

        arenasManager.forceStop(arena, player);
    }
}
