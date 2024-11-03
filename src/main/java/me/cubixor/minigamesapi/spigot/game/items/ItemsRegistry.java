package me.cubixor.minigamesapi.spigot.game.items;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableSet;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.items.impl.LeaveItem;
import me.cubixor.minigamesapi.spigot.game.items.impl.WandItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class ItemsRegistry {

    private final Map<GameState, ItemStack> colorItems;

    private final ClickableItem leaveItem;
    private final ClickableItem setupWandItem;

    public ItemsRegistry() {
        this.colorItems = loadStateColors();
        this.leaveItem = new LeaveItem();
        this.setupWandItem = new WandItem();
    }

    public Map<GameState, ItemStack> getColorItems() {
        return colorItems;
    }

    public ClickableItem getLeaveItem() {
        return leaveItem;
    }

    public ClickableItem getSetupWandItem() {
        return setupWandItem;
    }

    public Set<ClickableItem> getClickableItems() {
        return ImmutableSet.of(getLeaveItem());
    }

    private Map<GameState, ItemStack> loadStateColors() {
        Map<GameState, ItemStack> stateColors = new EnumMap<>(GameState.class);

        ConfigurationSection signColorsSection = MinigamesAPI.getPlugin().getConfig().getConfigurationSection("sign-colors");
        for (GameState state : GameState.values()) {
            ItemStack stateItem = XMaterial.matchXMaterial(signColorsSection.getString(state.toString())).get().parseItem();
            stateColors.put(state, stateItem);
        }

        return stateColors;
    }
}
