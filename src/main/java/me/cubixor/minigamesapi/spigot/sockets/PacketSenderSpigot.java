package me.cubixor.minigamesapi.spigot.sockets;

import me.cubixor.minigamesapi.common.packets.ArenaUpdatePacket;
import me.cubixor.minigamesapi.common.packets.ForceStartStopPacket;
import me.cubixor.minigamesapi.common.packets.JoinPacket;
import me.cubixor.minigamesapi.common.packets.LeavePacket;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.arena.Arena;
import me.cubixor.socketsmc.spigot.SocketClientSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class PacketSenderSpigot {

    private final SocketClientSender socketSender;
    private final LeaveStrategy leaveStrategy;
    private final String lobbyServer;

    public PacketSenderSpigot(SocketClientSender socketSender) {
        this.socketSender = socketSender;
        this.leaveStrategy = LeaveStrategy.valueOf(MinigamesAPI.getInstance().getConfig().getString("bungee.on-leave"));
        this.lobbyServer = MinigamesAPI.getInstance().getConfig().getString("bungee.lobby-server");
    }

    public void sendUpdateArenasPacket(Map<String, Arena> arenas) {
        ArenaUpdatePacket packet = new ArenaUpdatePacket(arenas);
        socketSender.sendPacket(packet);
    }

    public void sendJoinPacket(Arena arena, Player player, boolean localJoin) {
        JoinPacket joinPacket = new JoinPacket(arena.getName(), player.getName(), localJoin);
        socketSender.sendPacket(joinPacket);
    }

    public void sendLeavePacket(String playerName) {
        leaveStrategy.invoke(this, playerName);
    }

    private void sendLeavePacket(String player, String lobby, boolean usePrevServer) {
        LeavePacket packet = new LeavePacket(player, lobby, usePrevServer);
        socketSender.sendPacket(packet);
    }

    private void sendStartStopPacket(Arena arena, Player player, boolean start) {
        ForceStartStopPacket packet = new ForceStartStopPacket(arena.getName(), player.getName(), start);
        socketSender.sendPacket(packet);
    }

    public void sendForceStartPacket(Arena arena, Player player) {
        sendStartStopPacket(arena, player, true);
    }

    public void sendForceStopPacket(Arena arena, Player player) {
        sendStartStopPacket(arena, player, false);
    }

    private enum LeaveStrategy {
        JOIN_SERVER {
            @Override
            public void invoke(PacketSenderSpigot sender, String player) {
                sender.sendLeavePacket(player, sender.lobbyServer, true);
            }
        }, LOBBY_SERVER {
            @Override
            public void invoke(PacketSenderSpigot sender, String player) {
                sender.sendLeavePacket(player, sender.lobbyServer, false);
            }
        }, LEAVE_COMMAND {
            @Override
            public void invoke(PacketSenderSpigot sender, String player) {
                for (String s : MinigamesAPI.getInstance().getConfig().getStringList("bungee.leave-commands")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", player));
                }
            }
        };

        public abstract void invoke(PacketSenderSpigot sender, String player);
    }
}
