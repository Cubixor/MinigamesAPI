package me.cubixor.minigamesapi.spigot.commands.arguments;

import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.config.arenas.ConfigField;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public abstract class LocationArgument extends ArenaCommandArgument {

    private final ConfigField configField;

    protected LocationArgument(ArenasManager arenasManager,
                               String name,
                               String permission,
                               String messagesPath,
                               ConfigField configField) {
        super(arenasManager, name, permission, 2, messagesPath, true, false);
        this.configField = configField;
    }

    @Override
    protected void handle(Player player, String[] args) {
        String arena = args[1];
        arenasManager.getConfigManager().updateField(arena, configField, player.getLocation());
        Messages.send(player, messagesPath + "-success", "%arena%", arena);
    }
}
