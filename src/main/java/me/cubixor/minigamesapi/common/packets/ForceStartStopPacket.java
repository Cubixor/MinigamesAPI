package me.cubixor.minigamesapi.common.packets;

import me.cubixor.socketsmc.common.packets.Packet;

public class ForceStartStopPacket implements Packet {

    private final String arenaName;
    private final String player;
    private final boolean start;

    public ForceStartStopPacket(String arenaName, String player, boolean start) {
        this.arenaName = arenaName;
        this.player = player;
        this.start = start;
    }

    public String getArenaName() {
        return arenaName;
    }

    public String getPlayer() {
        return player;
    }

    public boolean isActionStart() {
        return start;
    }

    @Override
    public String toString() {
        return "ForceStartStopPacket{" +
                "arenaName='" + arenaName + '\'' +
                ", player='" + player + '\'' +
                ", start=" + start +
                '}';
    }
}
