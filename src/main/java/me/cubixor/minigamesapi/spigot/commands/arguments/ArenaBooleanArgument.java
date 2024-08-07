package me.cubixor.minigamesapi.spigot.commands.arguments;

import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class ArenaBooleanArgument extends ArenaCommandArgument {


    protected ArenaBooleanArgument(ArenasManager arenasManager, String name, String permission, int argLength, String messagesPath, boolean requireInServer, Boolean shouldBeActive) {
        super(arenasManager, name, permission, argLength, messagesPath, requireInServer, shouldBeActive);
    }

    @Override
    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        List<String> result = super.handleTabComplete(sender, args);
        if (args.length == 3 &&
                args[0].equalsIgnoreCase(getName()) &&
                Permissions.has(sender, getPermission())) {

            if ("true".startsWith(args[2])) {
                result.add("true");
            }
            if ("false".startsWith(args[2])) {
                result.add("false");
            }
        }

        return result;
    }
}
