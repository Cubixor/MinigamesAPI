package me.cubixor.minigamesapi.proxy.velocity;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.cubixor.minigamesapi.proxy.AbstractClientManager;
import me.cubixor.socketsmc.proxy.SocketServer;
import me.cubixor.socketsmc.velocity.events.PacketReceivedEventVelocity;
import me.cubixor.socketsmc.velocity.events.SocketConnectedEventVelocity;
import me.cubixor.socketsmc.velocity.events.SocketDisconnectedEventVelocity;

import java.util.Optional;

public class VelocityClientManager extends AbstractClientManager {

    private final ProxyServer server;

    public VelocityClientManager(SocketServer socketServer, ProxyServer server) {
        super(socketServer);
        this.server = server;
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onClientConnect(SocketConnectedEventVelocity evt) {
        super.onClientConnect(evt);
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onClientDisconnect(SocketDisconnectedEventVelocity evt) {
        super.onClientDisconnect(evt);
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onPacketReceived(PacketReceivedEventVelocity evt) {
        super.onPacketReceived(evt);
    }

    @Override
    protected boolean connectToServer(String playerName, String serverString) {
        Optional<Player> playerOptional = server.getPlayer(playerName);
        if (!playerOptional.isPresent()) {
            return false;
        }
        Player player = playerOptional.get();

        if (serverString == null) {
            return false;
        }

        Optional<RegisteredServer> targetServerOptional = server.getServer(serverString);
        if (!targetServerOptional.isPresent()) {
            return false;
        }
        RegisteredServer targetServer = targetServerOptional.get();


        if (targetServer.getPlayersConnected().contains(player)) {
            return true;
        }

        if (socketServer.getSpigotSockets().containsKey(serverString)) {
            player.createConnectionRequest(targetServer).connect();
            return true;
        }


        return false;
    }

    @Override
    protected String getPlayerServerName(String playerName) {
        return server.getPlayer(playerName).get()
                .getCurrentServer().get()
                .getServerInfo().getName();
    }
}
