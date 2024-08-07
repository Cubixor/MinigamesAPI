package me.cubixor.minigamesapi.spigot.commands.arguments.impl.staff;

import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaBooleanArgument;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaCommandArgument;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenaSetupChecker;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgSetActive extends ArenaBooleanArgument {

    private final ArenaSetupChecker arenaSetupChecker;

    public ArgSetActive(ArenasManager arenasManager, ArenaSetupChecker arenaSetupChecker) {
        super(arenasManager, "setactive", "staff.active", 3, "arena-moderate.active", true, null);
        this.arenaSetupChecker = arenaSetupChecker;
    }

    @Override
    public void handle(Player player, String[] args) {
        String arena = args[1];

        if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
            Messages.send(player, "arena-moderate.active-usage");
            return;
        }

        LocalArena localArena = arenasRegistry.getLocalArenas().get(arena);

        if (!arenaSetupChecker.checkReady(localArena)) {
            Messages.send(player, "arena-moderate.active-not-ready", "%arena%", arena);
            return;
        }


        boolean cmdActive = Boolean.parseBoolean(args[2]);
        boolean currActive = localArena.isActive();

        if (cmdActive && currActive) {
            Messages.send(player, "arena-moderate.active-arena-active", "%arena%", arena);
            return;
        }

        if (!cmdActive && !currActive) {
            Messages.send(player, "arena-moderate.active-arena-not-active", "%arena%", arena);
            return;
        }

        arenasManager.updateArenaActive(arena, cmdActive);

        if (cmdActive) {
            Messages.send(player, "arena-moderate.active-activate", "%arena%", arena);
        } else {
            Messages.send(player, "arena-moderate.active-deactivate", "%arena%", arena);
        }

    }
}
