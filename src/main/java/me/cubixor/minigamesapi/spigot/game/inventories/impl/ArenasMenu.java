package me.cubixor.minigamesapi.spigot.game.inventories.impl;

import com.google.common.collect.ImmutableMap;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.game.inventories.GlobalMenu;
import me.cubixor.minigamesapi.spigot.game.items.GameItem;
import me.cubixor.minigamesapi.spigot.game.items.ItemsRegistry;
import me.cubixor.minigamesapi.spigot.utils.MessageUtils;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ArenasMenu extends GlobalMenu {

    private final ArenasManager arenasManager;
    private final ItemsRegistry itemsRegistry;

    private final HashMap<Integer, Arena> arenaSlots = new HashMap<>();

    public ArenasMenu(ArenasManager arenasManager, ItemsRegistry itemsRegistry) {
        this.arenasManager = arenasManager;
        this.itemsRegistry = itemsRegistry;
    }

    @Override
    protected Inventory create() {
        Inventory arenasInventory = Bukkit.createInventory(null, 54, Messages.get("arenas-menu.name"));

        GameItem quickJoinItem = new GameItem("items.menu-arenas-quickjoin", "arenas-menu.quickjoin-item-name", "arenas-menu.quickjoin-item-lore");
        GameItem closeItem = new GameItem("items.menu-arenas-close", "arenas-menu.close-item-name", "arenas-menu.close-item-lore");
        //GameItem statsItem = new GameItem("items.menu-arenas-stats", "arenas-menu.stats-item-name", "arenas-menu.stats-item-lore");

        arenasInventory.setItem(48, quickJoinItem.getItem());
        arenasInventory.setItem(49, closeItem.getItem());
        //arenasInventory.setItem(50, statsItem.getItem());

        return arenasInventory;
    }

    @Override
    public void update() {
        List<Arena> arenas = arenasManager.getRegistry().getAllArenas().collect(Collectors.toList());
        arenaSlots.clear();

        int slot = 0;
        for (Arena arena : arenas) {
            ItemStack arenaItem = createArenaItem(arena);
            getInventory().setItem(slot, arenaItem);
            arenaSlots.put(slot, arena);
            slot++;
        }
    }

    @Override
    public void handleClick(InventoryClickEvent evt, Player player) {
        switch (evt.getSlot()) {
            case 48:
                arenasManager.getArenaPlayersManager().joinRandomArena(player);
                break;
            case 49:
                break;
            default:
                Arena arena = arenaSlots.get(evt.getSlot());
                arenasManager.getArenaPlayersManager().joinArena(player, arena.getName());
                break;
        }
    }


    private ItemStack createArenaItem(Arena arena) {
        ItemStack arenaItem = itemsRegistry.getColorItems().get(arena.getState()).clone();
        ItemMeta arenaItemItemMeta = arenaItem.getItemMeta();

        String vip = arena.isVip() ? Messages.get("general.vip-prefix") : "";
        String count = String.valueOf(arena.getPlayers().size());
        String max = String.valueOf(arena.getMaxPlayers());
        String state = MessageUtils.getStringState(arena);
        Map<String, String> replacement = ImmutableMap.of(
                "%arena%", arena.getName(),
                "%?vip?%", vip,
                "%count%", count,
                "%max%", max,
                "%state%", state);

        String itemName = Messages.get("arenas-menu.arena-item-name", replacement);
        List<String> itemLore = Messages.getList("arenas-menu.arena-item-lore", replacement);

        arenaItemItemMeta.setDisplayName(itemName);
        arenaItemItemMeta.setLore(itemLore);
        arenaItem.setItemMeta(arenaItemItemMeta);
        return arenaItem;
    }
}
