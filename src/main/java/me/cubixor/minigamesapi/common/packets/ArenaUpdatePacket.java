package me.cubixor.minigamesapi.common.packets;

import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.socketsmc.common.packets.Packet;

import java.util.Map;

public class ArenaUpdatePacket implements Packet {

    private final Map<String, Arena> arenas;

    public ArenaUpdatePacket(Map<String, Arena> arenas) {
        this.arenas = arenas;
    }

    public Map<String, Arena> getArenas() {
        return arenas;
    }

    @Override
    public String toString() {
        return "ArenaUpdatePacket{" +
                "arenas=" + arenas +
                '}';
    }
}
