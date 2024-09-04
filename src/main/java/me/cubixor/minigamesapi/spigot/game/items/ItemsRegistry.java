package me.cubixor.minigamesapi.spigot.game.items;

import com.google.common.collect.ImmutableSet;
import me.cubixor.minigamesapi.spigot.game.items.impl.LeaveItem;
import me.cubixor.minigamesapi.spigot.game.items.impl.WandItem;

import java.util.Set;

public class ItemsRegistry {

    private final ClickableItem leaveItem;
    private final ClickableItem setupWandItem;

    public ItemsRegistry() {
        this.leaveItem = new LeaveItem();
        this.setupWandItem = new WandItem();
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
}
