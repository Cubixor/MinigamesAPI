package me.cubixor.minigamesapi.spigot.game.items;

import com.cryptomorin.xseries.XItemStack;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GameItem {

    protected final ItemStack item;

    public GameItem(String configPath, String namePath, String lorePath) {
        this(configPath, namePath, lorePath, null, null);
    }

    public GameItem(String configPath, String namePath, String lorePath, String toReplace, String replacement) {
        FileConfiguration config = MinigamesAPI.getPlugin().getConfig();

        item = XItemStack.deserialize(config.getConfigurationSection(configPath));
        ItemMeta itemMeta = item.getItemMeta();

        String displayName = Messages.get(namePath);
        List<String> lore = Messages.getList(lorePath);

        if (toReplace != null && replacement != null) {
            displayName = displayName.replace(toReplace, replacement);
            lore.replaceAll(s -> s.replace(toReplace, replacement));
        }

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }

    public ItemStack getItem() {
        return item;
    }
}
