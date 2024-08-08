package me.cubixor.minigamesapi.common.packets;

import me.cubixor.socketsmc.common.packets.Packet;

public class JoinPacket implements Packet {

    private final String arenaName;
    private final String playerName;
    private final boolean localJoin;

    public JoinPacket(String arenaName, String player, boolean localJoin) {
        this.arenaName = arenaName;
        this.playerName = player;
        this.localJoin = localJoin;
    }

    public String getArenaName() {
        return arenaName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isLocalJoin() {
        return localJoin;
    }

    @Override
    public String toString() {
        return "JoinPacket{" +
                "arenaName='" + arenaName + '\'' +
                ", playerName='" + playerName + '\'' +
                ", localJoin=" + localJoin +
                '}';
    }
}

