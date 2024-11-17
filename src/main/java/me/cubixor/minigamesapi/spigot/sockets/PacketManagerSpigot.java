package me.cubixor.minigamesapi.spigot.sockets;

import me.cubixor.minigamesapi.common.packets.ArenaUpdatePacket;
import me.cubixor.minigamesapi.common.packets.ForceStartStopPacket;
import me.cubixor.minigamesapi.common.packets.JoinPacket;
import me.cubixor.minigamesapi.common.packets.KickPacket;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.ArenasRegistry;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.socketsmc.common.packets.Packet;
import me.cubixor.socketsmc.spigot.event.PacketReceivedEventSpigot;
import me.cubixor.socketsmc.spigot.event.SocketConnectedEventSpigot;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PacketManagerSpigot implements Listener {


    private final ArenasManager arenasManager;
    private final ArenasRegistry arenasRegistry;
    private final PacketSenderSpigot packetSender;

    private final Map<String, Arena> playersToJoin = new HashMap<>();

    public PacketManagerSpigot(ArenasManager arenasManager, PacketSenderSpigot packetSender) {
        this.arenasManager = arenasManager;
        this.arenasRegistry = arenasManager.getRegistry();
        this.packetSender = packetSender;

        Bukkit.getServer().getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
    }

    @EventHandler
    public void onServerConnect(SocketConnectedEventSpigot evt) {
        Map<String, Arena> arenas = arenasRegistry
                .getLocalArenas()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toArena()));
        packetSender.sendUpdateArenasPacket(arenas);
    }

    @EventHandler
    public void onPacketReceived(PacketReceivedEventSpigot evt) {
        Packet packet = evt.getPacket();

        if (packet instanceof ArenaUpdatePacket) {
            ArenaUpdatePacket arenaUpdatePacket = (ArenaUpdatePacket) packet;
            arenasManager.updateRemoteArenas(arenaUpdatePacket.getArenas());
        } else if (packet instanceof ForceStartStopPacket) {
            ForceStartStopPacket forceStartStopPacket = (ForceStartStopPacket) packet;
            LocalArena localArena = arenasRegistry.getLocalArenas().get(forceStartStopPacket.getArenaName());

            if (forceStartStopPacket.isActionStart()) {
                arenasManager.forceLocalStart(localArena);
            } else {
                arenasManager.forceLocalStop(localArena);
            }
        } else if (packet instanceof JoinPacket) {
            JoinPacket joinPacket = (JoinPacket) packet;
            LocalArena localArena = arenasRegistry.getLocalArenas().get(joinPacket.getArenaName());

            playersToJoin.put(joinPacket.getPlayerName(), localArena);
            new BukkitRunnable() {
                @Override
                public void run() {
                    playersToJoin.remove(joinPacket.getPlayerName());
                }
            }.runTaskLater(MinigamesAPI.getPlugin(), 100L);
        } else if (packet instanceof KickPacket) {
            KickPacket kickPacket = (KickPacket) packet;
            LocalArena localArena = arenasRegistry.getLocalArenas().get(((KickPacket) packet).getArenaName());
            if (!localArena.getPlayers().contains(kickPacket.getTarget())) return;

            arenasManager.getArenaPlayersManager().leaveArena(Bukkit.getPlayerExact(kickPacket.getTarget()), localArena);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent evt) {
        if (playersToJoin.containsKey(evt.getPlayer().getName())) {
            Arena arena = playersToJoin.remove(evt.getPlayer().getName());

            if (!arenasManager.getArenaPlayersManager().joinArena(evt.getPlayer(), arena.getName())) {
                //TODO Check if needed
                //packetSender.sendLeavePacket(evt.getPlayer().getName());
            }
        }
    }

}
