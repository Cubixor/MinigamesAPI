package me.cubixor.minigamesapi.spigot.game;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;
import me.cubixor.minigamesapi.spigot.config.arenas.ConfigField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.events.GameCreateEvent;
import me.cubixor.minigamesapi.spigot.events.GameResetEvent;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.game.items.ItemsRegistry;
import me.cubixor.minigamesapi.spigot.sockets.PacketSenderSpigot;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

public class ArenasManager {

    private final ArenasRegistry registry;
    private final ArenaPlayersManager arenaPlayersManager;
    private final ArenasConfigManager configManager;
    private final SignManager signManager;
    private final PacketSenderSpigot packetSender;
    private final StatsManager statsManager;
    private final ItemsRegistry itemsRegistry;
    private final ArenaFactory arenaFactory;
    private final boolean bungee;

    public ArenasManager(ArenasRegistry registry,
                         ArenasConfigManager configManager,
                         SignManager signManager,
                         PacketSenderSpigot packetSender,
                         StatsManager statsManager,
                         ItemsRegistry itemsRegistry,
                         ArenaFactory arenaFactory) {
        this.registry = registry;
        this.configManager = configManager;
        this.signManager = signManager;
        this.packetSender = packetSender;
        this.statsManager = statsManager;
        this.itemsRegistry = itemsRegistry;
        this.arenaFactory = arenaFactory;

        arenaPlayersManager = new ArenaPlayersManager(this);

        loadArenas();
        bungee = MinigamesAPI.getPlugin().getConfig().getBoolean("bungee.bungee-mode");
    }

    private void loadArenas() {
        for (String name : configManager.getArenas()) {
            LocalArena localArena = arenaFactory.loadArenaFromConfig(name, getPacketSender().getServerName(), this, statsManager);
            registry.getLocalArenas().put(name, localArena);
        }

        signManager.updateAllSigns();
    }

    public void addArena(String arena) {
        LocalArena localArena = arenaFactory.createBlankArena(arena, getPacketSender().getServerName(), this, statsManager);

        registry.getLocalArenas().put(arena, localArena);
        configManager.insertArena(arena);
        signManager.updateSigns("quickjoin");

        updateArena(localArena);

        Bukkit.getPluginManager().callEvent(new GameCreateEvent(localArena));
    }

    public void removeArena(String arena) {
        registry.getLocalArenas().remove(arena);
        configManager.removeArena(arena);
        signManager.removeArena(arena);

        if (isBungee()) {
            Map<String, Arena> arenaMap = Collections.singletonMap(arena, null);
            packetSender.sendUpdateArenasPacket(arenaMap);
        }
    }

    public void updateArenaMinPlayers(String arena, int count) {
        LocalArena localArena = registry.getLocalArenas().get(arena);
        localArena.setMinPlayers(count);
        configManager.updateField(arena, BasicConfigField.MIN_PLAYERS, count);

        updateArena(localArena);
    }

    public void updateArenaMaxPlayers(String arena, int count) {
        LocalArena localArena = registry.getLocalArenas().get(arena);
        localArena.setMaxPlayers(count);
        configManager.updateField(arena, BasicConfigField.MAX_PLAYERS, count);

        updateArena(localArena);
    }

    public void updateArenaVip(String arena, boolean vip) {
        LocalArena localArena = registry.getLocalArenas().get(arena);
        localArena.setVip(vip);
        configManager.updateField(arena, BasicConfigField.VIP, vip);

        updateArena(localArena);
    }

    public void updateArenaActive(String arena, boolean active) {
        LocalArena localArena = registry.getLocalArenas().get(arena);
        if (!active) {
            forceLocalStop(localArena);
            localArena.setState(GameState.INACTIVE);
        } else {
            localArena.getStateManager().reset();
        }

        configManager.updateField(arena, BasicConfigField.ACTIVE, active);

        updateArena(localArena);
    }

    public void updateArenaField(String arena, ConfigField configField, Object value) {
        getConfigManager().updateField(arena, configField, value);
    }

    public void updateArena(LocalArena localArena) {
        signManager.updateSigns(localArena.getName());

        if (isBungee()) {
            Arena arenaObj = localArena.toArena();

            Map<String, Arena> arenaMap = Collections.singletonMap(arenaObj.getName(), arenaObj);
            packetSender.sendUpdateArenasPacket(arenaMap);
        }
    }

    public void updateRemoteArenas(Map<String, Arena> updatedArenas) {
        for (Map.Entry<String, Arena> entry : updatedArenas.entrySet()) {
            if (entry.getValue() == null) {
                registry.getRemoteArenas().remove(entry.getKey());
            } else {
                registry.getRemoteArenas().put(entry.getKey(), entry.getValue());
            }

            signManager.updateSigns(entry.getKey());
        }
        signManager.updateSigns("quickjoin");
    }

    public void forceStart(Arena arena, Player player) {
        if (arena.isLocal()) {
            forceLocalStart((LocalArena) arena);
        } else {
            packetSender.sendForceStartPacket(arena, player);
        }
    }

    public void forceLocalStart(LocalArena localArena) {
        if (!localArena.getState().isWaitingStarting()) return;

        int tpTime = MinigamesAPI.getPlugin().getConfig().getInt("full-waiting-time");
        if (localArena.getState().equals(GameState.STARTING)) {
            if (localArena.getTimer() > tpTime) {
                localArena.getStateManager().setFullStarting();
            }
        } else if (localArena.getState().equals(GameState.WAITING)) {
            localArena.getStateManager().setStarting();
            localArena.getStateManager().setFullStarting();
        }

        Messages.sendAll(localArena.getBukkitPlayers(), "arena-moderate.force-start-success");
    }

    public void forceStop(Arena arena, Player player) {
        if (arena.isLocal()) {
            forceLocalStop((LocalArena) arena);
        } else {
            packetSender.sendForceStopPacket(arena, player);
        }
    }

    public void forceLocalStop(LocalArena localArena) {
        Messages.sendAll(localArena.getBukkitPlayers(), "arena-moderate.force-stop-success");

        localArena.getStateManager().reset();
    }

    public ArenasConfigManager getConfigManager() {
        return configManager;
    }

    public PacketSenderSpigot getPacketSender() {
        return packetSender;
    }

    public ArenaPlayersManager getArenaPlayersManager() {
        return arenaPlayersManager;
    }

    public boolean isBungee() {
        return bungee;
    }

    public ArenasRegistry getRegistry() {
        return registry;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public ItemsRegistry getItemsRegistry() {
        return itemsRegistry;
    }
}
