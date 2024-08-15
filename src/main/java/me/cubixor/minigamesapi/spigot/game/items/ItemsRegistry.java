package me.cubixor.minigamesapi.spigot.game.items;

import com.google.common.collect.ImmutableSet;
import me.cubixor.minigamesapi.spigot.game.items.impl.LeaveItem;

import java.util.Set;

public class ItemsRegistry {

    private final ClickableItem leaveItem;

    public ItemsRegistry() {
        this.leaveItem = new LeaveItem();
    }

    public ClickableItem getLeaveItem() {
        return leaveItem;
    }

    public Set<ClickableItem> getClickableItems() {
        return ImmutableSet.of(getLeaveItem());
    }
}
