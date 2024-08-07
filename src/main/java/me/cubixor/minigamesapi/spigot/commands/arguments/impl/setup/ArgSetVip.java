package me.cubixor.minigamesapi.spigot.commands.arguments.impl.setup;

import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaBooleanArgument;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaCommandArgument;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgSetVip extends ArenaBooleanArgument {

    public ArgSetVip(ArenasManager arenasManager) {
        super(arenasManager, "setvip", "setup.setvip", 3, "arena-setup.set-vip", true, false);
    }

    @Override
    public void handle(Player player, String[] args) {
        String arena = args[1];

        if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
            Messages.send(player, "arena-setup.set-vip-usage");
            return;
        }

        LocalArena localArena = arenasRegistry.getLocalArenas().get(arena);

        boolean cmdVip = Boolean.parseBoolean(args[2]);
        boolean currVip = localArena.isVip();

        if (cmdVip && currVip) {
            Messages.send(player, "arena-setup.set-vip-arena-vip", "%arena%", arena);
            return;
        }

        if (!cmdVip && !currVip) {
            Messages.send(player, "arena-setup.set-vip-arena-not-vip", "%arena%", arena);
            return;
        }

        arenasManager.updateArenaVip(arena, cmdVip);


        if (cmdVip) {
            Messages.send(player, "arena-setup.set-vip-success-vip", "%arena%", arena);
        } else {
            Messages.send(player, "arena-setup.set-vip-success-not-vip", "%arena%", arena);
        }

    }
}
