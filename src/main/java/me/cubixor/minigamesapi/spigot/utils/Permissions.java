package me.cubixor.minigamesapi.spigot.utils;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import org.bukkit.command.CommandSender;

public class Permissions {

    private Permissions() {
    }

    public static boolean has(CommandSender player, String permission) {
        return player.hasPermission(MinigamesAPI.getPlugin().getName() + "." + permission);
    }
}
