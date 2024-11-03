package me.cubixor.minigamesapi.spigot.commands.arguments.impl.play;

import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.game.inventories.GlobalMenuRegistry;
import org.bukkit.entity.Player;

public class ArgArenasMenu extends CommandArgument {

    private final GlobalMenuRegistry globalMenuRegistry;

    public ArgArenasMenu(GlobalMenuRegistry globalMenuRegistry) {
        super("arenasmenu", "play.menu", 1, "arenas-menu.command");
        this.globalMenuRegistry = globalMenuRegistry;
    }

    @Override
    protected void handle(Player player, String[] args) {
        globalMenuRegistry.getArenasMenu().open(player);
    }
}
