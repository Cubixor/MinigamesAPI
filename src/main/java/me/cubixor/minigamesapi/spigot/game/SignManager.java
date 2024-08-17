package me.cubixor.minigamesapi.spigot.game;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
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
import org.bukkit.event.block.Action;
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
        if (!signs.containsKey(arena)) {
            signs.put(arena, new ArrayList<>());
        }

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

        if (!arenasRegistry.isValidArena(arena) && !evt.getLine(1).equalsIgnoreCase("quickjoin")) {
            return;
        }
        evt.setCancelled(true);

        addSign(arena, sign.getLocation());
        checkUpdateSign(sign.getLocation(), arena);
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

        if (!(evt.getPlayer().isSneaking() && Permissions.has(evt.getPlayer(), "setup.signs"))) {
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
        if (!evt.getClickedBlock().getType().toString().contains("SIGN")) {
            return;
        }

        Sign sign = (Sign) evt.getClickedBlock().getState();
        String arena = getSignArena(sign);

        if (arena == null) {
            return;
        }

        if (evt.getPlayer().isSneaking() && evt.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        evt.setCancelled(true);

        if (!Permissions.has(evt.getPlayer(), "play.signs")) {
            Messages.send(evt.getPlayer(), "general.no-permission");
            return;
        }

        if (arena.equals("quickjoin")) {
            evt.getPlayer().performCommand(plugin.getName().toLowerCase() + " quickjoin");
        } else {
            evt.getPlayer().performCommand(plugin.getName().toLowerCase() + " join " + arena);
        }
    }

    public void updateAllSigns() {
        signs.keySet().forEach(this::updateSigns);
    }

    public void updateSigns(String arena) {
        List<Location> signList = signs.getOrDefault(arena, Collections.emptyList());
        for (Location location : new ArrayList<>(signList)) {
            checkUpdateSign(location, arena);
        }
    }

    private void checkUpdateSign(Location location, String arenaString) {
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

        if (arenaString.equals("quickjoin")) {
            updateQuickJoinSign(sign);
        } else {
            Arena arena = arenasRegistry.getArena(arenaString);
            if (arena == null) {
                updateArenaSignOffline(sign, arenaString);
            } else {
                updateArenaSignOnline(sign, arena);
            }
            updateSignColors(sign, arena.getState());
        }

        Bukkit.getScheduler().runTask(plugin, () -> sign.update(true));
    }

    private void updateSignColors(Sign sign, GameState gameState) {
        if (plugin.getConfig().getBoolean("color-signs")) {
            ItemStack blockType = colorItems.get(gameState);
            Block attachedBlock = getAttachedBlock(sign.getBlock());

            attachedBlock.setType(blockType.getType());

            BlockState state = attachedBlock.getState();
            state.setData(blockType.getData());
            state.update();
        }
    }

    private void updateArenaSignOnline(Sign sign, Arena arena) {
        Map<String, String> replacement = MessageUtils.getArenaStatusReplacement(arena);

        setArenaSignLines(sign, replacement);
    }

    private void updateArenaSignOffline(Sign sign, String arenaName) {
        String count = "?";
        String max = "?";
        String gameState = MessageUtils.getStringState(null);
        String vip = "";

        Map<String, String> replacement = ImmutableMap.of(
                "%arena%", arenaName,
                "%count%", count,
                "%max%", max,
                "%state%", gameState,
                "%?vip?%", vip);

        setArenaSignLines(sign, replacement);
    }

    private void setArenaSignLines(Sign sign, Map<String, String> replacement) {
        sign.setLine(0, Messages.get("other.sign-first-line", replacement));
        sign.setLine(1, Messages.get("other.sign-second-line", replacement));
        sign.setLine(2, Messages.get("other.sign-third-line", replacement));
        sign.setLine(3, Messages.get("other.sign-fourth-line", replacement));
    }

    private void updateQuickJoinSign(Sign sign) {
        Map<String, String> replacement = Collections.singletonMap("%count%", String.valueOf(signs.size() - 1));
        sign.setLine(0, Messages.get("other.sign-quickjoin-first-line", replacement));
        sign.setLine(1, Messages.get("other.sign-quickjoin-second-line", replacement));
        sign.setLine(2, Messages.get("other.sign-quickjoin-third-line", replacement));
        sign.setLine(3, Messages.get("other.sign-quickjoin-fourth-line", replacement));
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
