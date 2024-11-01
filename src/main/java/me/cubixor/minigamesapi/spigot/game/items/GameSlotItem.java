package me.cubixor.minigamesapi.spigot.game.items;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GameSlotItem extends GameItem {

    protected final int slot;

    public GameSlotItem(String configPath, String namePath, String lorePath) {
        super(configPath, namePath, lorePath);
        slot = MinigamesAPI.getPlugin().getConfig().getConfigurationSection(configPath).getInt("slot");
    }

    public GameSlotItem(ItemStack item, String namePath, String lorePath) {
        super(item, namePath, lorePath);
        slot = 0;
    }

    public int getSlot() {
        return slot;
    }

    public void putInMenu(Inventory inv) {
        inv.setItem(slot, item);
    }
}
