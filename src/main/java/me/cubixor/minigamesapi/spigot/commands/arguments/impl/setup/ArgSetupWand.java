package me.cubixor.minigamesapi.spigot.commands.arguments.impl.setup;

import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.game.items.ItemsRegistry;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

public class ArgSetupWand extends CommandArgument {

    private final ItemsRegistry itemsRegistry;

    public ArgSetupWand(ItemsRegistry itemsRegistry) {
        super("wand", "setup.wand", 1, null);
        this.itemsRegistry = itemsRegistry;
    }

    @Override
    protected void handle(Player player, String[] args) {
        itemsRegistry.getSetupWandItem().give(player);
        Messages.send(player, "arena-setup.wand-item-receive");
    }
}
