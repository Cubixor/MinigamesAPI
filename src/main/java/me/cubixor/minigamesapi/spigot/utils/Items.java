package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.XItemStack;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {

    private static FileConfiguration config;
    private static GameItem leaveItem;
    private Items() {
    }

    public static void init(FileConfiguration config) {
        Items.config = config;
        Items.leaveItem = new GameItem("items.leave-item", "game.leave-item-name", "game.leave-item-lore");
    }

    public static GameItem getLeaveItem() {
        return leaveItem;
    }

    public static class GameItem {
        private final ItemStack item;
        private final int slot;

        public GameItem(String configPath, String namePath, String lorePath) {
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
    }
}
