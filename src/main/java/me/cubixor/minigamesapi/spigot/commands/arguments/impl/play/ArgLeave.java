package me.cubixor.minigamesapi.spigot.commands.arguments.impl.play;

import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgLeave extends CommandArgument {

    private final ArenasManager arenasManager;

    public ArgLeave(ArenasManager arenasManager) {
        super("leave", "play.leave", 1, null);
        this.arenasManager = arenasManager;
    }

    @Override
    public void handle(Player player, String[] args) {
        LocalArena arena = arenasManager.getRegistry().getPlayerLocalArena(player);

        if (arena == null) {
            Messages.send(player, "game.arena-leave-not-in-game");
            return;
        }

        arenasManager.getArenaPlayersManager().leaveArena(player, arena);
    }
}
