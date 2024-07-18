package me.cubixor.minigamesapi.spigot.commands.arguments;

import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import org.bukkit.entity.Player;

public abstract class CommandArgument {

    protected final String messagesPath;
    private final String name;
    private final String permission;
    private final int argLength;

    protected CommandArgument(String name, String permission, int argLength, String messagesPath) {
        this.name = name;
        this.permission = permission;
        this.argLength = argLength;
        this.messagesPath = messagesPath;
    }

    public void validateAndHandle(Player player, String[] args) {
        if (!validate(player, args)) return;
        handle(player, args);
    }

    protected abstract void handle(Player player, String[] args);

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    protected boolean validate(Player player, String[] args) {
        if (!Permissions.has(player, permission)) {
            Messages.send(player, "general.no-permission");
            return false;
        }

        if (argLength > 1 && args.length < argLength) {
            Messages.send(player, messagesPath + "-usage");
            return false;
        }

        return true;
    }
}
