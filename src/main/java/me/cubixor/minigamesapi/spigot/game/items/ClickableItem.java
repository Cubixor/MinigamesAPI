package me.cubixor.minigamesapi.spigot.game.items;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.entity.Player;

public abstract class ClickableItem extends GameItem {

    private final int slot;

    protected ClickableItem(String configPath, String namePath, String lorePath) {
        super(configPath, namePath, lorePath);
        slot = MinigamesAPI.getPlugin().getConfig().getConfigurationSection(configPath).getInt("slot");
    }

    public void give(Player player) {
        player.getInventory().setItem(slot, item);
    }

    public abstract void handleClick(ArenasManager arenasManager, LocalArena localArena, Player player);
}
