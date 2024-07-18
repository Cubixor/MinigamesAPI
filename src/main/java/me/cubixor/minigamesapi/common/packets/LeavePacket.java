package me.cubixor.minigamesapi.common.packets;

import me.cubixor.socketsmc.common.packets.Packet;

public class LeavePacket implements Packet {

    private final String player;
    private final String lobby;
    private final boolean usePrevServer;

    public LeavePacket(String player, String lobby, boolean usePrevServer) {
        this.player = player;
        this.lobby = lobby;
        this.usePrevServer = usePrevServer;
    }

    public String getPlayer() {
        return player;
    }

    public String getLobby() {
        return lobby;
    }

}
