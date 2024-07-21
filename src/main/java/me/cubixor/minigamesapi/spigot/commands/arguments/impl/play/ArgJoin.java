package me.cubixor.minigamesapi.spigot.commands.arguments.impl.play;

import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaCommandArgument;
import org.bukkit.entity.Player;

public class ArgJoin extends ArenaCommandArgument {


    public ArgJoin(ArenasManager arenasManager) {
        super(arenasManager, "join", "play.join", 2, "game.arena-join", false, true);
    }

    @Override
    public void handle(Player player, String[] args) {
        String arenaName = args[1];
        arenasManager.getArenaPlayersManager().joinArena(player, arenaName);
    }
}
