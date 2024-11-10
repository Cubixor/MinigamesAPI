package me.cubixor.minigamesapi.spigot.commands;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.commands.arguments.impl.help.ArgHelpAdmin;
import me.cubixor.minigamesapi.spigot.commands.arguments.impl.help.ArgHelpGeneral;
import me.cubixor.minigamesapi.spigot.commands.arguments.impl.help.ArgHelpStaff;
import me.cubixor.minigamesapi.spigot.commands.arguments.impl.play.*;
import me.cubixor.minigamesapi.spigot.commands.arguments.impl.setup.*;
import me.cubixor.minigamesapi.spigot.commands.arguments.impl.staff.ArgForceStart;
import me.cubixor.minigamesapi.spigot.commands.arguments.impl.staff.ArgForceStop;
import me.cubixor.minigamesapi.spigot.commands.arguments.impl.staff.ArgKick;
import me.cubixor.minigamesapi.spigot.commands.arguments.impl.staff.ArgSetActive;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenaSetupChecker;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.inventories.GlobalMenuRegistry;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainCommand implements CommandExecutor {

    private final Map<String, CommandArgument> arguments;
    private final CooldownManager cooldownManager;

    public MainCommand(List<CommandArgument> arguments) {
        this.arguments = arguments.stream()
                .collect(Collectors.toMap(CommandArgument::getName, arg -> arg));
        cooldownManager = new CooldownManager();

        MinigamesAPI.getPlugin().getServer().getPluginCommand(MinigamesAPI.getPlugin().getName()).setExecutor(this);
    }

    public static List<CommandArgument> getCommonArguments(ArenasManager arenasManager, ArenaSetupChecker arenaSetupChecker, StatsManager statsManager, GlobalMenuRegistry globalMenuRegistry) {
        //TODO Implement reload
        return Arrays.asList(
                new ArgHelpGeneral(),
                new ArgHelpStaff(),
                new ArgHelpAdmin(),
                new ArgCreate(arenasManager),
                new ArgDelete(arenasManager),
                new ArgCheck(arenasManager, arenaSetupChecker),
                new ArgSetActive(arenasManager, arenaSetupChecker),
                new ArgSetVip(arenasManager),
                new ArgSetMainLobby(arenasManager),
                new ArgSetWaitingLobby(arenasManager),
                new ArgSetMinPlayers(arenasManager),
                new ArgSetMaxPlayers(arenasManager),
                new ArgForceStart(arenasManager),
                new ArgForceStop(arenasManager),
                new ArgKick(arenasManager),
                new ArgJoin(arenasManager),
                new ArgLeave(arenasManager),
                new ArgQuickJoin(arenasManager),
                new ArgList(arenasManager.getRegistry()),
                new ArgStats(statsManager),
                new ArgArenasMenu(globalMenuRegistry)
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Messages.send(sender, "general.must-be-player");
            return true;
        }
        Player player = (Player) sender;

        if (cooldownManager.check(player)) {
            return true;
        }
        cooldownManager.add(player);

        String arg = args.length == 0 ? "help" : args[0].toLowerCase();

        if (!arguments.containsKey(arg)) {
            Messages.send(sender, "general.unknown-command");
            return true;
        }

        arguments.get(arg).validateAndHandle(player, args);

        return true;
    }
}
