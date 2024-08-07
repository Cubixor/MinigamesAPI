package me.cubixor.minigamesapi.spigot.commands.arguments.impl.staff;

import com.google.common.collect.ImmutableMap;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        List<String> result = new ArrayList<>();

        if (args.length == 2 &&
                args[0].equalsIgnoreCase(getName()) &&
                Permissions.has(sender, getPermission())) {

            for (String p : arenasManager.getRegistry().getPlayerNames()) {
                if (p.toLowerCase().startsWith(args[1])) {
                    result.add(p);
                }
            }
        }

        return result;
    }
}
