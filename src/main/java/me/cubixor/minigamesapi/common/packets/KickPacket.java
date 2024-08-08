package me.cubixor.minigamesapi.common.packets;

import me.cubixor.socketsmc.common.packets.Packet;

public class KickPacket implements Packet {

    private final String arenaName;
    private final String player;
    private final String target;

    public KickPacket(String player, String target, String arenaName) {
        this.arenaName = arenaName;
        this.player = player;
        this.target = target;
    }

    public String getArenaName() {
        return arenaName;
    }

    public String getPlayer() {
        return player;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "KickPacket{" +
                "arenaName='" + arenaName + '\'' +
                ", player='" + player + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
