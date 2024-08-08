package me.cubixor.minigamesapi.common.packets;

import me.cubixor.socketsmc.common.packets.Packet;

public class JoinPacket implements Packet {

    private final String arenaName;
    private final String playerName;

    public JoinPacket(String arenaName, String player) {
        this.arenaName = arenaName;
        this.playerName = player;
    }

    public String getArenaName() {
        return arenaName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "JoinPacket{" +
                "arenaName='" + arenaName + '\'' +
                ", playerName='" + playerName + '\'' +
                '}';
    }
}

