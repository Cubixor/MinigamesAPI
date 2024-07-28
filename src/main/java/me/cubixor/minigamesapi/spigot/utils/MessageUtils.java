package me.cubixor.minigamesapi.spigot.utils;


import me.cubixor.minigamesapi.spigot.game.arena.Arena;

public class MessageUtils {

    private MessageUtils() {
    }

    public static String getStringState(Arena arena) {
        if (arena == null) return Messages.get("general.state-offline");
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

    public static String convertPlaytime(int playtime) {
        int hours;
        int minutes;
        int seconds;
        String h = Messages.get("other.stats-playtime-hours");
        String min = Messages.get("other.stats-playtime-minutes");
        String sec = Messages.get("other.stats-playtime-seconds");
        String timeString;
        if (playtime < 60) {
            seconds = playtime;
            timeString = seconds + sec;
        } else if (playtime < 3600) {
            minutes = playtime / 60;
            seconds = playtime % 60;
            timeString = minutes + min + " " + seconds + sec;
        } else {
            hours = playtime / 3600;
            minutes = (playtime % 3600) / 60;
            seconds = playtime % 60;
            timeString = hours + h + " " + minutes + min + " " + seconds + sec;
        }

        return timeString;
    }
}
