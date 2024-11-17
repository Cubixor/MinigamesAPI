package me.cubixor.minigamesapi.proxy;

import me.cubixor.minigamesapi.common.packets.*;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.socketsmc.common.packets.Packet;
import me.cubixor.socketsmc.proxy.SocketServer;
import me.cubixor.socketsmc.proxy.SocketServerSender;
import me.cubixor.socketsmc.proxy.events.ProxyPacketReceivedEvent;
import me.cubixor.socketsmc.proxy.events.ProxySocketConnectedEvent;
import me.cubixor.socketsmc.proxy.events.ProxySocketDisconnectedEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractClientManager {

    protected final SocketServer socketServer;
    protected final Map<String, String> playerServers = new HashMap<>();
    private final SocketServerSender socketSender;
    private final Map<String, Arena> arenas = new HashMap<>();

    protected AbstractClientManager(SocketServer socketServer) {
        this.socketServer = socketServer;
        this.socketSender = socketServer.getSender();
    }

    public void onClientConnect(ProxySocketConnectedEvent evt) {
        sendAllArenas(evt.getServer());
    }

    public void onClientDisconnect(ProxySocketDisconnectedEvent evt) {
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

    public void onPacketReceived(ProxyPacketReceivedEvent evt) {
        Packet packet = evt.getPacket();
        String server = evt.getServer();

        if (packet instanceof ArenaUpdatePacket) {
            ArenaUpdatePacket arenaUpdatePacket = (ArenaUpdatePacket) packet;
            arenas.putAll(arenaUpdatePacket.getArenas());
            socketSender.sendPacketToAllExcept(arenaUpdatePacket, server);
        } else if (packet instanceof JoinPacket) {
            JoinPacket joinPacket = (JoinPacket) packet;
            if (joinArena(joinPacket)) {
                sendJoinPacket(joinPacket, getArenaServer(joinPacket.getArenaName()));
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

    private void sendRemoveArenas(String server, Map<String, Arena> toRemove) {
        ArenaUpdatePacket packet = new ArenaUpdatePacket(toRemove);
        socketSender.sendPacketToAllExcept(packet, server);
    }

    private void sendAllArenas(String server) {
        ArenaUpdatePacket packet = new ArenaUpdatePacket(arenas);
        socketSender.sendPacketToServer(packet, server);
    }

    private void sendJoinPacket(JoinPacket joinPacket, String server) {
        socketSender.sendPacketToServer(joinPacket, server);
    }

    protected String getArenaServer(String arenaName) {
        return arenas.get(arenaName).getServer();
    }

    private void sendBackToServer(LeavePacket leavePacket) {
        String playerName = leavePacket.getPlayer();

        String prevServer;
        if (playerServers.containsKey(playerName)) {
            prevServer = playerServers.remove(leavePacket.getPlayer());
        } else {
            prevServer = getPlayerServerName(playerName);
        }

        String firstChoiceServer;
        String secondChoiceServer;

        if (leavePacket.shouldUsePrevServer()) {
            firstChoiceServer = prevServer;
            secondChoiceServer = leavePacket.getLobby();
        } else {
            firstChoiceServer = leavePacket.getLobby();
            secondChoiceServer = prevServer;
        }

        if (!connectToServer(playerName, firstChoiceServer)) {
            connectToServer(playerName, secondChoiceServer);
        }
    }

    protected boolean joinArena(JoinPacket joinPacket) {
        if (connectToServer(joinPacket.getPlayerName(), getArenaServer(joinPacket.getArenaName()))) {
            playerServers.put(joinPacket.getPlayerName(), getPlayerServerName(joinPacket.getPlayerName()));
            return true;
        }

        return false;
    }

    protected abstract boolean connectToServer(String playerName, String serverString);

    protected abstract String getPlayerServerName(String playerName);
}