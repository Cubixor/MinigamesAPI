package me.cubixor.minigamesapi.common.packets;

import me.cubixor.socketsmc.common.packets.Packet;

public class KickPacket implements Packet {

    private final String arenaName;
    private final String target;

    public KickPacket(String arenaName, String target) {
        this.arenaName = arenaName;
        this.target = target;
    }

    public String getArenaName() {
        return arenaName;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "KickPacket{" +
                "arenaName='" + arenaName + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
