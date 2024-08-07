package me.cubixor.minigamesapi.bungee.socket;

import me.cubixor.minigamesapi.common.packets.*;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.socketsmc.bungee.SocketServer;
import me.cubixor.socketsmc.bungee.SocketServerSender;
import me.cubixor.socketsmc.bungee.event.PacketReceivedEventBungee;
import me.cubixor.socketsmc.bungee.event.SocketConnectedEventBungee;
import me.cubixor.socketsmc.bungee.event.SocketDisconnectedEventBungee;
import me.cubixor.socketsmc.common.packets.Packet;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class ClientManager implements Listener {


    private final SocketServer socketServer;
    private final SocketServerSender socketSender;
    private final Map<String, Arena> arenas = new HashMap<>();
    private final Map<String, String> playerServers = new HashMap<>();

    public ClientManager(SocketServer socketServer) {
        this.socketServer = socketServer;
        this.socketSender = socketServer.getSender();
    }

    @EventHandler
    public void onClientConnect(SocketConnectedEventBungee evt) {
        sendAllArenas(evt.getServer());
    }

    @EventHandler
    public void onClientDisconnect(SocketDisconnectedEventBungee evt) {
        Map<String, Arena> toRemove = new HashMap<>();
        arenas.entrySet().removeIf(entry -> {
            if (entry.getValue().getServer().equals(evt.getServer())) {
                toRemove.put(entry.getKey(), null);
                return true;
            }
            return false;
        });

        sendRemoveArenas(evt.getServer(), toRemove);
    }

    @EventHandler
    public void onPacketReceived(PacketReceivedEventBungee evt) {
        Packet packet = evt.getPacket();
        String server = evt.getServer();

        if (packet instanceof ArenaUpdatePacket) {
            ArenaUpdatePacket arenaUpdatePacket = (ArenaUpdatePacket) packet;
            arenas.putAll(arenaUpdatePacket.getArenas());
            socketSender.sendPacketToAllExcept(arenaUpdatePacket, server);
        } else if (packet instanceof JoinPacket) {
            JoinPacket joinPacket = (JoinPacket) packet;
            if (joinArena(joinPacket)) {
                sendJoinPacket(joinPacket, server);
            }
        } else if (packet instanceof LeavePacket) {
            LeavePacket leavePacket = (LeavePacket) packet;
            sendBackToServer(leavePacket);
        } else if (packet instanceof KickPacket) {
            KickPacket kickPacket = (KickPacket) packet;
            socketSender.sendPacketToServer(packet, getArenaServer(kickPacket.getArenaName()));
        } else if (packet instanceof ForceStartStopPacket) {
            ForceStartStopPacket forceStartStopPacket = (ForceStartStopPacket) packet;
            socketSender.sendPacketToServer(packet, getArenaServer(forceStartStopPacket.getArenaName()));
        }
    }

    private boolean joinArena(JoinPacket joinPacket) {
        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(joinPacket.getPlayerName());
        if (proxiedPlayer == null) {
            return false;
        }

        playerServers.put(joinPacket.getPlayerName(), proxiedPlayer.getServer().getInfo().getName());

        if (!joinPacket.isLocalJoin()) {
            proxiedPlayer.connect(ProxyServer.getInstance().getServerInfo(getArenaServer(joinPacket.getArenaName())));

            return true;
        }

        return false;
    }

    private void sendBackToServer(LeavePacket leavePacket) {
        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(leavePacket.getPlayer());
        if (proxiedPlayer == null) {
            return;
        }

        String firstChoiceServer;
        String secondChoiceServer;

        if (leavePacket.shouldUsePrevServer()) {
            firstChoiceServer = playerServers.get(proxiedPlayer.getName());
            secondChoiceServer = leavePacket.getLobby();
        } else {
            firstChoiceServer = leavePacket.getLobby();
            secondChoiceServer = playerServers.get(proxiedPlayer.getName());
        }

        if (!connectToServer(proxiedPlayer, firstChoiceServer)) {
            connectToServer(proxiedPlayer, secondChoiceServer);
        }


        playerServers.remove(leavePacket.getPlayer());
    }

    private boolean connectToServer(ProxiedPlayer player, String serverString) {
        if (serverString == null) {
            return false;
        }

        ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(serverString);

        if (targetServer == null) {
            return false;
        }

        if (player.getServer().getInfo().equals(targetServer)) {
            return true;
        }

        if (socketServer.getSpigotSockets().containsKey(serverString)) {
            player.connect(targetServer);
            return true;
        }


        return false;
    }

    private void sendRemoveArenas(String server, Map<String, Arena> toRemove) {
        ArenaUpdatePacket packet = new ArenaUpdatePacket(toRemove);
        socketSender.sendPacketToAllExcept(packet, server);
    }

    private void sendAllArenas(String server) {
        ArenaUpdatePacket packet = new ArenaUpdatePacket(arenas);
        socketSender.sendPacketToAllExcept(packet, server);
    }

    private void sendJoinPacket(JoinPacket joinPacket, String server) {
        socketSender.sendPacketToServer(joinPacket, server);
    }

    private String getArenaServer(String arenaName) {
        return arenas.get(arenaName).getServer();
    }
}
