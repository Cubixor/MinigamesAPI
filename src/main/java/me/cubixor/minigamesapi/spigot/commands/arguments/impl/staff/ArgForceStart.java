package me.cubixor.minigamesapi.spigot.commands.arguments.impl.staff;

import me.cubixor.minigamesapi.spigot.arena.Arena;
import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaCommandArgument;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgForceStart extends ArenaCommandArgument {


    public ArgForceStart(ArenasManager arenasManager) {
        super(arenasManager, "forcestart", "staff.start", 2, "arena-moderate.force-start", false, true);
    }

    @Override
    public void handle(Player player, String[] args) {
        String arenaName = args[1];
        Arena arena = arenasRegistry.getArena(arenaName);

        if (!arena.getState().isWaitingStarting()) {
            Messages.send(player, "arena-moderate.force-start-already-started", "%arena%", arenaName);
            return;
        }

        if (arena.getPlayers().isEmpty()) {
            Messages.send(player, "arena-moderate.force-start-no-players", "%arena%", arenaName);
            return;
        }

        arenasManager.forceStart(arena, player);

        if (!arena.getPlayers().contains(player.getName())) {
            Messages.send(player, "arena-moderate.force-start-success", "%player%", player.getName());
        }
    }
}
