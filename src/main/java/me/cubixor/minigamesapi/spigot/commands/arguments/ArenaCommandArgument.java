package me.cubixor.minigamesapi.spigot.commands.arguments;

import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.ArenasRegistry;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public abstract class ArenaCommandArgument extends CommandArgument {

    protected final ArenasManager arenasManager;
    protected final ArenasRegistry arenasRegistry;
    private final boolean requireInServer;
    private final Boolean shouldBeActive;

    protected ArenaCommandArgument(ArenasManager arenasManager,
                                   String name,
                                   String permission,
                                   int argLength,
                                   String messagesPath,
                                   boolean requireInServer,
                                   Boolean shouldBeActive) {
        super(name, permission, argLength, messagesPath);
        this.arenasManager = arenasManager;
        this.arenasRegistry = arenasManager.getRegistry();
        this.requireInServer = requireInServer;
        this.shouldBeActive = shouldBeActive;
    }

    @Override
    protected boolean validate(Player player, String[] args) {
        if (!super.validate(player, args)) {
            return false;
        }

        String arenaName = args[1];

        if (!arenasRegistry.isValidArena(arenaName)) {
            Messages.send(player, "general.arena-invalid");
            return false;
        }

        if (requireInServer && arenasRegistry.isRemoteArena(arenaName)) {
            Messages.send(player, "bungee.not-on-server");
            return false;
        }

        if (shouldBeActive != null) {
            Arena arena = arenasRegistry.getArena(arenaName);

            if (shouldBeActive && !arena.isActive()) {
                Messages.send(player, "game.arena-join-not-active", "%arena%", arenaName);
                return false;
            }

            if (!shouldBeActive && arena.isActive()) {
                Messages.send(player, "arena-setup.active-block", "%arena%", arenaName);
                return false;
            }
        }

        return true;
    }
}
