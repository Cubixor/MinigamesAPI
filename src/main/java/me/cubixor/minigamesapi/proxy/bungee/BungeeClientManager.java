package me.cubixor.minigamesapi.proxy.bungee;

import me.cubixor.minigamesapi.common.packets.JoinPacket;
import me.cubixor.minigamesapi.proxy.AbstractClientManager;
import me.cubixor.socketsmc.bungee.events.PacketReceivedEventBungee;
import me.cubixor.socketsmc.bungee.events.SocketConnectedEventBungee;
import me.cubixor.socketsmc.bungee.events.SocketDisconnectedEventBungee;
import me.cubixor.socketsmc.proxy.SocketServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeClientManager extends AbstractClientManager implements Listener {


    public BungeeClientManager(SocketServer socketServer) {
        super(socketServer);
    }

    @EventHandler
    public void onClientConnect(SocketConnectedEventBungee evt) {
        super.onClientConnect(evt);
    }

    @EventHandler
    public void onClientDisconnect(SocketDisconnectedEventBungee evt) {
        super.onClientDisconnect(evt);
    }

    @EventHandler
    public void onPacketReceived(PacketReceivedEventBungee evt) {
        super.onPacketReceived(evt);
    }

    @Override
    protected boolean connectToServer(String playerName, String serverString) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player == null) {
            return true;
        }

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

    @Override
    protected String getPlayerServerName(String playerName) {
        return ProxyServer.getInstance()
                .getPlayer(playerName)
                .getServer()
                .getInfo()
                .getName();
    }
}
