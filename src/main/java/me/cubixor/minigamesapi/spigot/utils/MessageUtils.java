package me.cubixor.minigamesapi.spigot.utils;


import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.events.ArenaStatusParseEvent;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public static String formatTime(int time, String formatPath) {
        FileConfiguration config = MinigamesAPI.getPlugin().getConfig();
        String pattern = config.getString(formatPath);

        Instant instant = Instant.ofEpochSecond(time);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("UTC"));

        return dateTimeFormatter.format(instant);
    }

    public static String formatDate(LocalDateTime localDateTime, String formatPath) {
        FileConfiguration config = MinigamesAPI.getPlugin().getConfig();
        String pattern = config.getString(formatPath);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static Map<String, String> getArenaStatusReplacement(Arena arena) {
        String count = String.valueOf(arena.getPlayers().size());
        String max = String.valueOf(arena.getMaxPlayers());
        String gameState = MessageUtils.getStringState(arena);
        String vip = arena.isVip() ? Messages.get("general.vip-prefix") : "";

        Map<String, String> replacement = new LinkedHashMap<>();
        replacement.put("%arena%", arena.getName());
        replacement.put("%count%", count);
        replacement.put("%max%", max);
        replacement.put("%state%", gameState);
        replacement.put("%?vip?%", vip);


        Bukkit.getServer().getPluginManager().callEvent(new ArenaStatusParseEvent(!Bukkit.isPrimaryThread(), arena, replacement));

        return replacement;
    }

    public static String centerText(String text, int lineLength) {
        char[] chars = text.toCharArray(); // Get a list of all characters in text
        boolean isBold = false;
        double length = 0;
        ChatColor pholder = null;
        for (int i = 0; i < chars.length; i++) { // Loop through all characters
            // Check if the character is a ColorCode..
            if (chars[i] == '§' && chars.length != (i + 1) && (pholder = ChatColor.getByChar(chars[i + 1])) != null) {
                if (pholder != ChatColor.UNDERLINE && pholder != ChatColor.ITALIC
                        && pholder != ChatColor.STRIKETHROUGH && pholder != ChatColor.MAGIC) {
                    isBold = (chars[i + 1] == 'l'); // Setting bold  to true or false, depending on if the ChatColor is Bold.
                    length--; // Removing one from the length, of the string, because we don't wanna count color codes.
                    i += isBold ? 1 : 0;
                }
            } else {
                // If the character is not a color code:
                length++; // Adding a space
                length += (isBold ? (chars[i] != ' ' ? 0.1555555555555556 : 0) : 0); // Adding 0.156 spaces if the character is bold.
            }
        }

        double spaces = (lineLength - length) / 2; // Getting the spaces to add by (max line length - length) / 2

        // Adding the spaces
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            builder.append(' ');
        }
        String copy = builder.toString();
        builder.append(text).append(copy);

        return builder.toString();
    }
}
