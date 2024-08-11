package me.cubixor.minigamesapi.spigot.game.items;

import com.google.common.collect.ImmutableSet;
import me.cubixor.minigamesapi.spigot.game.items.impl.LeaveItem;

import java.util.Set;

public class ItemsRegistry {

    private final GameItem leaveItem;

    public ItemsRegistry() {
        this.leaveItem = new LeaveItem();
    }

    public GameItem getLeaveItem() {
        return leaveItem;
    }

    public Set<GameItem> getAllItems() {
        return ImmutableSet.of(getLeaveItem());
    }
}
