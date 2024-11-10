package me.cubixor.minigamesapi.spigot.commands;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainCommandCompleter implements TabCompleter {

    private final Map<String, CommandArgument> arguments;

    public MainCommandCompleter(List<CommandArgument> arguments) {
        this.arguments = arguments.stream()
                .collect(Collectors.toMap(CommandArgument::getName, arg -> arg));

        MinigamesAPI.getPlugin().getServer().getPluginCommand(MinigamesAPI.getPlugin().getName()).setTabCompleter(this);
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return result;
        }

        final String[] parsedArgs = Arrays.stream(args).map(String::toLowerCase).toArray(String[]::new);

        if (args.length == 1) {
            for (CommandArgument arg : arguments.values()) {
                if (arg.getName().startsWith(parsedArgs[0]) && Permissions.has(sender, arg.getPermission())) {
                    result.add(arg.getName());
                }
            }
        } else {
            for (CommandArgument arg : arguments.values()) {
                result.addAll(arg.handleTabComplete(sender, parsedArgs));
            }
        }

        return result;
    }
}
