package me.cubixor.minigamesapi.spigot.commands.arguments.impl.setup;

import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgCreate extends CommandArgument {

    private final ArenasManager arenasManager;

    public ArgCreate(ArenasManager arenasManager) {
        super("create", "setup.create", 2, "arena-setup.create");
        this.arenasManager = arenasManager;
    }

    @Override
    public void handle(Player player, String[] args) {
        String arena = args[1];

        if (arenasManager.getRegistry().isValidArena(arena)) {
            Messages.send(player, "arena-setup.create-already-exists", "%arena%", arena);
            return;
        }

        arenasManager.addArena(arena);

        Messages.send(player, messagesPath + "-success", "%arena%", arena);
    }
}
