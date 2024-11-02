package me.cubixor.minigamesapi.spigot.commands;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.*;

import java.util.Collections;
import java.util.List;

public class LeaveCommand implements CommandExecutor, TabCompleter {

    public LeaveCommand() {
        Server server = MinigamesAPI.getPlugin().getServer();
        PluginCommand pluginCommand = server.getPluginCommand("leave");

        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.dispatchCommand(sender, MinigamesAPI.getPlugin().getName().toLowerCase() + " leave");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
