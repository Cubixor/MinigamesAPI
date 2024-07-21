package me.cubixor.minigamesapi.spigot.arena;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.utils.MessageUtils;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SignManager implements Listener {

    private final JavaPlugin plugin;
    private final Map<String, List<Location>> signs;
    private final ArenasConfigManager arenasConfigManager;
    private final ArenasRegistry arenasRegistry;
    private final boolean colorSigns;
    private final Map<GameState, ItemStack> colorItems;

    public SignManager(ArenasConfigManager arenasConfigManager, ArenasRegistry arenasRegistry) {
        this.plugin = MinigamesAPI.getPlugin();
        this.arenasConfigManager = arenasConfigManager;
        this.arenasRegistry = arenasRegistry;

        this.colorSigns = plugin.getConfig().getBoolean("color-signs");
        this.colorItems = loadStateColors();

        signs = arenasConfigManager.getAllSigns();
        Bukkit.getServer().getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
    }

    private void addSign(String arena, Location loc) {
        signs.get(arena).add(loc);
        arenasConfigManager.addSign(arena, loc);
    }

    private void removeSign(String arena, Location location) {
        signs.get(arena).remove(location);
        arenasConfigManager.removeSign(arena, location);
    }

    private void removeSignBlock(Block sign) {
        try {
            Block block = getAttachedBlock(sign);
            if (sign.getType().toString().contains("SIGN")) {
                sign.setType(Material.AIR);

                if (colorSigns) {
                    block.setType(Material.AIR);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private Block getAttachedBlock(Block block) {
        Block attachedBlock;
        if (block.getType().toString().contains("WALL")) {
            attachedBlock = block.getRelative(XBlock.getDirection(block).getOppositeFace());
        } else {
            attachedBlock = block.getRelative(BlockFace.DOWN);
        }
        return attachedBlock;
    }

    public void addArena(String arena) {
        signs.put(arena, new ArrayList<>());

        updateSigns("quickjoin");
    }

    public void removeArena(String arena) {
        for (Location location : signs.get(arena)) {
            removeSignBlock(location.getBlock());
        }

        arenasConfigManager.removeSigns(arena);

        updateSigns("quickjoin");
    }

    @EventHandler
    public void signCreate(SignChangeEvent evt) {
        Sign sign = (Sign) evt.getBlock().getState();
        if (!(evt.getLine(0) != null
                && evt.getLine(1) != null
                && evt.getLine(0).equalsIgnoreCase("[" + plugin.getName() + "]")
                && Permissions.has(evt.getPlayer(), "setup.signs"))) {
            return;
        }

        String arena = evt.getLine(1);

        if (!signs.containsKey(arena) && !evt.getLine(1).equalsIgnoreCase("quickjoin")) {
            return;
        }

        addSign(arena, sign.getLocation());
        updateSign(sign.getLocation(), arena);

        evt.setCancelled(true);
    }

    @EventHandler
    public void signBreak(BlockBreakEvent evt) {
        if (!(evt.getBlock().getType().toString().contains("SIGN"))) {
            return;
        }

        Sign sign = (Sign) evt.getBlock().getState();
        String arena = getSignArena(sign);

        if (arena == null) {
            return;
        }
        if (!evt.getPlayer().isSneaking()) {
            evt.setCancelled(true);
            return;
        }
        if (!Permissions.has(evt.getPlayer(), "setup.signs")) {
            evt.getPlayer().sendMessage(Messages.get("general.no-permission"));
            evt.setCancelled(true);
            return;
        }

        removeSign(arena, sign.getLocation());
    }

    @EventHandler
    public void signClick(PlayerInteractEvent evt) {
        if (evt.getHand() == null || !evt.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }

        if (evt.getClickedBlock() == null) {
            return;
        }
        if (evt.getPlayer().isSneaking()) {
            return;
        }
        if (!evt.getClickedBlock().getType().toString().contains("SIGN")) {
            return;
        }

        Sign sign = (Sign) evt.getClickedBlock().getState();
        String arena = getSignArena(sign);

        if (arena == null) {
            return;
        }

        if (!Permissions.has(evt.getPlayer(), "play.signs")) {
            Messages.send(evt.getPlayer(), "general.no-permission");
            return;
        }
        evt.setCancelled(true);

        //TODO Join arena
        if (arena.equals("quickjoin")) {
            evt.getPlayer().performCommand(plugin.getName() + " quickjoin");
            //new PlayCommands().quickJoin(evt.getPlayer());
        } else {
            evt.getPlayer().performCommand(plugin.getName() + " join " + arena);
            //new PlayCommands().join(evt.getPlayer(), new String[]{"join", arena});
        }
    }

    public void updateSigns(String arena) {
        List<Location> signList = signs.getOrDefault(arena, Collections.emptyList());
        for (Location location : signList) {
            updateSign(location, arena);
        }
    }

    private void updateSign(Location location, String arenaString) {
        if (arenaString.equals("quickjoin")) {
            Block block;
            Sign sign;

            try {
                block = location.getBlock();
                sign = (Sign) block.getState();
                if (!block.getType().toString().contains("SIGN")) {
                    removeSign(arenaString, location);
                    return;
                }
            } catch (Exception e) {
                removeSign(arenaString, location);
                return;
            }

            Map<String, String> replacement = Collections.singletonMap("%count%", String.valueOf(signs.size()));
            sign.setLine(0, Messages.get("other.sign-quickjoin-first-line", replacement));
            sign.setLine(1, Messages.get("other.sign-quickjoin-second-line", replacement));
            sign.setLine(2, Messages.get("other.sign-quickjoin-third-line", replacement));
            sign.setLine(3, Messages.get("other.sign-quickjoin-fourth-line", replacement));
            sign.update(true);
            return;
        }


        Arena arena = arenasRegistry.getArena(arenaString);
        String count = String.valueOf(arena.getPlayers().size());
        String max = String.valueOf(arena.getMaxPlayers());
        String gameState = MessageUtils.getStringState(arena);
        String vip = arena.isVip() ? Messages.get("general.vip-prefix") : "";

        Sign sign;
        try {
            Block block = location.getBlock();
            sign = (Sign) block.getState();
            if (!block.getType().toString().contains("SIGN")) {
                removeSign(arenaString, location);
                return;
            }
        } catch (Exception e) {
            removeSign(arenaString, location);
            return;
        }

        if (plugin.getConfig().getBoolean("color-signs")) {
            ItemStack blockType = colorItems.get(arena.getState());
            Block attachedBlock = getAttachedBlock(sign.getBlock());

            attachedBlock.setType(blockType.getType());

            BlockState state = attachedBlock.getState();
            state.setData(blockType.getData());
            state.update();
        }

        Map<String, String> replacement = ImmutableMap.of("%arena%", arenaString, "%count%", count, "%max%", max, "%state%", gameState, "%?vip?%", vip);
        sign.setLine(0, Messages.get("other.sign-first-line", replacement));
        sign.setLine(1, Messages.get("other.sign-second-line", replacement));
        sign.setLine(2, Messages.get("other.sign-third-line", replacement));
        sign.setLine(3, Messages.get("other.sign-fourth-line", replacement));

        sign.update(true);
    }

    private String getSignArena(Sign sign) {
        for (Map.Entry<String, List<Location>> entry : signs.entrySet()) {
            if (entry.getValue().contains(sign.getLocation())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private Map<GameState, ItemStack> loadStateColors() {
        Map<GameState, ItemStack> stateColors = new EnumMap<>(GameState.class);

        ConfigurationSection signColorsSection = plugin.getConfig().getConfigurationSection("sign-colors");
        for (GameState state : GameState.values()) {
            ItemStack stateItem = XMaterial.matchXMaterial(signColorsSection.getString(state.toString())).get().parseItem();
            stateColors.put(state, stateItem);
        }

        return stateColors;
    }
}
