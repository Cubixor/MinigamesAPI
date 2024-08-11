package me.cubixor.minigamesapi.spigot.game.items;

import com.cryptomorin.xseries.XItemStack;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GameItem {

    private final ItemStack item;
    private final int slot;

    protected GameItem(String configPath, String namePath, String lorePath) {
        FileConfiguration config = MinigamesAPI.getPlugin().getConfig();

        item = XItemStack.deserialize(config.getConfigurationSection(configPath));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(namePath));
        itemMeta.setLore(Messages.getList(lorePath));
        item.setItemMeta(itemMeta);
        slot = config.getConfigurationSection(configPath).getInt("slot");
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public void give(Player player) {
        player.getInventory().setItem(slot, item);
    }

    public abstract void handleClick(ArenasManager arenasManager, LocalArena localArena, Player player);
}
