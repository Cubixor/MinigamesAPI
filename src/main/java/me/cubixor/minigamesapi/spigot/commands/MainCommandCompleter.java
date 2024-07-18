package me.cubixor.minigamesapi.spigot.commands;

import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainCommandCompleter implements TabCompleter {

    private final Map<String, CommandArgument> arguments;
    private final ArenasManager arenasManager;

    public MainCommandCompleter(List<CommandArgument> arguments, ArenasManager arenasManager) {
        this.arguments = arguments.stream()
                .collect(Collectors.toMap(CommandArgument::getName, arg -> arg));
        this.arenasManager = arenasManager;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return result;
        }

        switch (args.length) {
            case 1:
                for (CommandArgument arg : arguments.values()) {
                    if (arg.getName().startsWith(args[0].toLowerCase()) && Permissions.has(sender, arg.getPermission())) {
                        result.add(arg.getName());
                    }
                }
                break;
            case 2:
                boolean found = false;
                for (CommandArgument arg : arguments.values()) {
                    if (args[0].equalsIgnoreCase(arg.getName()) && Permissions.has(sender, arg.getPermission())) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    for (String arena : arenasManager.getAllArenaNames()) {
                        if (arena.toLowerCase().startsWith(args[1].toLowerCase())) {
                            result.add(arena);
                        }
                    }
                }

                break;
        }

        return result;
    }
}
