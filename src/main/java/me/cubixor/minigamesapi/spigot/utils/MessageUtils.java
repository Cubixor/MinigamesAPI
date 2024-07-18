package me.cubixor.minigamesapi.spigot.utils;


import me.cubixor.minigamesapi.spigot.arena.Arena;

public class MessageUtils {

    private MessageUtils() {
    }

    public static String getStringState(Arena arena) {
        switch (arena.getState()) {
            case INACTIVE:
                return Messages.get("general.state-inactive");
            case WAITING:
                return Messages.get("general.state-waiting");
            case STARTING:
                return Messages.get("general.state-starting");
            case GAME:
                return Messages.get("general.state-game");
            case ENDING:
                return Messages.get("general.state-ending");
        }
        return "";
    }
}
