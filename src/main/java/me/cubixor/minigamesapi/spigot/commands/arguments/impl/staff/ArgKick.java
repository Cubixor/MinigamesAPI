package me.cubixor.minigamesapi.spigot.commands.arguments.impl.staff;

import com.google.common.collect.ImmutableMap;
import me.cubixor.minigamesapi.spigot.arena.objects.Arena;
import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgKick extends CommandArgument {

    private final ArenasManager arenasManager;

    public ArgKick(ArenasManager arenasManager) {
        super("kick", "staff.kick", 2, "arena-moderate.kick");
        this.arenasManager = arenasManager;
    }

    @Override
    public void handle(Player player, String[] args) {
        String targetName = args[1];

        Arena arena = arenasManager.getRegistry().getPlayerArena(targetName);
        if (arena == null) {
            Messages.send(player, "arena-moderate.kick-player-not-playing", "%player%", targetName);
            return;
        }

        arenasManager.getArenaPlayersManager().kickFromArena(targetName, arena);

        Messages.send(player, "arena-moderate.kick-success", ImmutableMap.of(
                "%arena%", arena.getName(),
                "%player%", targetName)
        );
    }
}
