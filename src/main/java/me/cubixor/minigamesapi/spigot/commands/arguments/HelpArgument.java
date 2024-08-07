package me.cubixor.minigamesapi.spigot.commands.arguments;

import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public abstract class HelpArgument extends CommandArgument {

    protected HelpArgument(String name, String permission, String messagesPath) {
        super(name, permission, 1, messagesPath);
    }

    @Override
    protected void handle(Player player, String[] args) {
        Messages.sendList(player, messagesPath);
    }
}
