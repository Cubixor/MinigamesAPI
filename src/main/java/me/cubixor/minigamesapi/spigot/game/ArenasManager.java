package me.cubixor.minigamesapi.spigot.game;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;
import me.cubixor.minigamesapi.spigot.config.arenas.ConfigField;
import me.cubixor.minigamesapi.spigot.events.GameStateChangeEvent;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.sockets.PacketSenderSpigot;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.Map;

public class ArenasManager implements Listener {

    private final ArenasRegistry registry;
    private final ArenaPlayersManager arenaPlayersManager;
    private final ArenasConfigManager configManager;
    private final SignManager signManager;
    private final PacketSenderSpigot packetSender;
    private final boolean bungee;

    public ArenasManager(ArenasRegistry registry, ArenasConfigManager configManager, SignManager signManager, PacketSenderSpigot packetSender) {
        this.registry = registry;
        this.configManager = configManager;
        this.signManager = signManager;
        this.packetSender = packetSender;

        //TODO Decouple that
        arenaPlayersManager = new ArenaPlayersManager(this);

        loadArenas();
        bungee = MinigamesAPI.getPlugin().getConfig().getBoolean("bungee.bungee-mode");
    }

    @EventHandler
    public void onStateChange(GameStateChangeEvent evt) {
        updateArena(evt.getLocalArena());
    }

    private void loadArenas() {
        for (String name : configManager.getArenas()) {
            //TODO Proper server name
            LocalArena localArena = new LocalArena(
                    getArenaPlayersManager(),
                    name,
                    MinigamesAPI.getPlugin().getName(),
                    configManager.getBoolean(name, BasicConfigField.ACTIVE),
                    configManager.getBoolean(name, BasicConfigField.VIP),
                    configManager.getInt(name, BasicConfigField.MIN_PLAYERS),
                    configManager.getInt(name, BasicConfigField.MAX_PLAYERS)
            );
            registry.getLocalArenas().put(name, localArena);
        }
    }

    public void addArena(String arena) {
        LocalArena localArena = new LocalArena(getArenaPlayersManager(), arena);

        registry.getLocalArenas().put(arena, localArena);
        configManager.insertArena(arena);
        signManager.addArena(arena);

        updateArena(localArena);
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
        if (active) {
            //TODO Force stop
        }

        LocalArena localArena = registry.getLocalArenas().get(arena);
        localArena.setState(active ? GameState.WAITING : GameState.INACTIVE);
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
                continue;
            }

            registry.getRemoteArenas().put(entry.getKey(), entry.getValue());
            signManager.updateSigns(entry.getKey());
        }
    }

    public void forceStart(Arena arena, Player player) {
        if (arena.isLocal()) {
            forceLocalStart((LocalArena) arena, player.getName());
        } else {
            packetSender.sendForceStartPacket(arena, player);
        }
    }

    public void forceLocalStart(LocalArena localArena, String playerName) {
        localArena.getStateManager().setGame();

        Messages.sendAll(localArena.getBukkitPlayers(), "arena-moderate.force-start-success", "%player%", playerName);
    }

    public void forceStop(Arena arena, Player player) {
        if (arena.isLocal()) {
            forceLocalStop((LocalArena) arena, player.getName());
        } else {
            packetSender.sendForceStopPacket(arena, player);
        }
    }

    public void forceLocalStop(LocalArena localArena, String playerName) {
        localArena.getStateManager().reset();

        Messages.sendAll(localArena.getBukkitPlayers(), "arena-moderate.force-stop-success", "%player%", playerName);
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
}
