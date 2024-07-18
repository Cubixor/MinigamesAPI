package me.cubixor.minigamesapi.spigot.arena;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;
import me.cubixor.minigamesapi.spigot.sockets.PacketSenderSpigot;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArenasManager {

    private final ArenaPlayersManager arenaPlayersManager;
    private final ArenasConfigManager configManager;
    private final SignManager signManager;
    private final PacketSenderSpigot packetSender;
    private final Map<String, LocalArena> localArenas = new HashMap<>();
    private final Map<String, Arena> remoteArenas = new HashMap<>();

    public ArenasManager(ArenasConfigManager configManager, ArenaPlayersManager arenaPlayersManager, PacketSenderSpigot packetSender) {
        this.arenaPlayersManager = arenaPlayersManager;
        this.configManager = configManager;
        this.packetSender = packetSender;

        //TODO Decouple that
        signManager = new SignManager(this);
        MinigamesAPI.getInstance().getServer().getPluginManager().registerEvents(signManager, MinigamesAPI.getInstance());

        loadArenas();
    }

    public void loadArenas() {
        for (String name : configManager.getArenas()) {
            LocalArena localArena = new LocalArena(
                    name,
                    MinigamesAPI.getInstance().getName(),
                    configManager.getBoolean(name, BasicConfigField.ACTIVE),
                    configManager.getBoolean(name, BasicConfigField.VIP),
                    configManager.getInt(name, BasicConfigField.MIN_PLAYERS),
                    configManager.getInt(name, BasicConfigField.MAX_PLAYERS)
            );
            localArenas.put(name, localArena);
        }
    }

    public void addArena(String arena) {
        LocalArena localArena = new LocalArena(arena);

        localArenas.put(arena, localArena);
        configManager.insertArena(arena);
        signManager.addArena(arena);

        updateArena(localArena);
    }

    public void removeArena(String arena) {
        localArenas.remove(arena);
        configManager.removeArena(arena);
        signManager.removeArena(arena);

        if (MinigamesAPI.isBungee()) {
            Map<String, Arena> arenaMap = Collections.singletonMap(arena, null);
            packetSender.sendUpdateArenasPacket(arenaMap);
        }
    }

    public void updateArenaMinPlayers(String arena, int count) {
        LocalArena localArena = localArenas.get(arena);
        localArena.setMinPlayers(count);
        configManager.updateField(arena, BasicConfigField.MIN_PLAYERS, count);

        updateArena(localArena);
    }

    public void updateArenaMaxPlayers(String arena, int count) {
        LocalArena localArena = localArenas.get(arena);
        localArena.setMaxPlayers(count);
        configManager.updateField(arena, BasicConfigField.MAX_PLAYERS, count);

        updateArena(localArena);
    }

    public void updateArenaVip(String arena, boolean vip) {
        LocalArena localArena = localArenas.get(arena);
        localArena.setVip(vip);
        configManager.updateField(arena, BasicConfigField.VIP, vip);

        updateArena(localArena);
    }

    public void updateArenaActive(String arena, boolean active) {
        if (active) {
            //TODO Force stop
        }

        LocalArena localArena = localArenas.get(arena);
        localArena.setState(GameState.INACTIVE);
        configManager.updateField(arena, BasicConfigField.ACTIVE, active);

        updateArena(localArena);
    }

    public void updateArena(LocalArena localArena) {
        signManager.updateSigns(localArena.getName());

        if (MinigamesAPI.isBungee()) {
            Arena arenaObj = localArena.toArena();

            Map<String, Arena> arenaMap = Collections.singletonMap(arenaObj.getName(), arenaObj);
            packetSender.sendUpdateArenasPacket(arenaMap);
        }
    }

    public void updateRemoteArenas(Map<String, Arena> updatedArenas) {
        for (Map.Entry<String, Arena> entry : updatedArenas.entrySet()) {
            if (entry.getValue() == null) {
                remoteArenas.remove(entry.getKey());
                continue;
            }

            remoteArenas.put(entry.getKey(), entry.getValue());
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
        Messages.sendAll(localArena.getBukkitPlayers(), "arena-moderate.force-start-success", "%player%", playerName);

        //TODO Update state
    }

    public void forceStop(Arena arena, Player player) {
        if (arena.isLocal()) {
            forceLocalStop((LocalArena) arena, player.getName());
        } else {
            packetSender.sendForceStopPacket(arena, player);
        }
    }

    public void forceLocalStop(LocalArena localArena, String playerName) {


        Messages.sendAll(localArena.getBukkitPlayers(), "arena-moderate.force-stop-success", "%player%", playerName);

        //TODO Update state
    }

    public boolean isInArena(Player player) {
        return localArenas.values().stream().anyMatch(arena -> arena.getPlayers().contains(player.getName()))
                || remoteArenas.values().stream().anyMatch(arena -> arena.getPlayers().contains(player.getName()));
    }

    public Arena getArena(String name) {
        if (localArenas.containsKey(name)) {
            return localArenas.get(name);
        } else if (remoteArenas.containsKey(name)) {
            return remoteArenas.get(name);
        }

        return null;
    }

    public Arena getPlayerArena(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player != null) {
            return getPlayerLocalArena(player);
        } else {
            Optional<Arena> arena = remoteArenas.values().stream().filter(a -> a.getPlayers().contains(playerName)).findFirst();
            return arena.orElse(null);
        }
    }

    public LocalArena getPlayerLocalArena(Player player) {
        Optional<LocalArena> arena = localArenas.values().stream().filter(a -> a.getBukkitPlayers().contains(player)).findFirst();
        return arena.orElse(null);
    }

    public boolean isValidArena(String arena) {
        return isLocalArena(arena) || isRemoteArena(arena);
    }

    public boolean isLocalArena(String arena) {
        return localArenas.containsKey(arena);
    }

    public boolean isRemoteArena(String arena) {
        return remoteArenas.containsKey(arena);
    }

    public ArenasConfigManager getConfigManager() {
        return configManager;
    }

    public Map<String, LocalArena> getLocalArenas() {
        return localArenas;
    }

    public Map<String, Arena> getRemoteArenas() {
        return remoteArenas;
    }

    public Set<String> getLocalArenaNames() {
        return localArenas.keySet();
    }

    public Set<String> getAllArenaNames() {
        return Stream.concat(localArenas.keySet().stream(), remoteArenas.keySet().stream()).collect(Collectors.toSet());
    }

    public PacketSenderSpigot getPacketSender() {
        return packetSender;
    }

    public ArenaPlayersManager getArenaPlayersManager() {
        return arenaPlayersManager;
    }
}
