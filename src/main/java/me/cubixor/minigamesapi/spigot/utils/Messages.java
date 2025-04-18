package me.cubixor.minigamesapi.spigot.utils;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Messages {

    private static FileConfiguration messagesConfig;
    private static String prefix;
    private static String cmd;

    private Messages() {
    }

    public static void init(FileConfiguration messagesConfig, String cmd) {
        Messages.messagesConfig = messagesConfig;
        Messages.prefix = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("prefix"));
        Messages.cmd = cmd;
    }

    public static String get(String path) {
        String message = messagesConfig.getString(path);
        if (message == null) {
            return "�";
        }

        message = message
                .replace("%prefix%", prefix)
                .replace("%cmd%", cmd);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> getList(String path) {
        return getList(path, Collections.emptyMap());
    }

    public static List<String> getList(String path, Map<String, String> replacement) {
        List<String> message = messagesConfig.getStringList(path);
        return message.stream()
                .map(msg -> ChatColor.translateAlternateColorCodes('&', msg))
                .map(msg -> msg.replace("%cmd%", cmd))
                .map(msg -> {
                    for (Map.Entry<String, String> entry : replacement.entrySet()) {
                        msg = msg.replace(entry.getKey(), entry.getValue());
                    }
                    return msg;
                })
                .collect(Collectors.toList());
    }

    public static String get(String path, String toReplace, String replacement) {
        return get(path, Collections.singletonMap(toReplace, replacement));
    }

    public static String get(String path, Map<String, String> replacement) {
        String message = get(path);
        for (Map.Entry<String, String> entry : replacement.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }

    public static String getPAPI(Player player, String path, Map<String, String> replacement) {
        String message = get(path, replacement);
        if (MinigamesAPI.usesPAPI()) {
            return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }

    public static void send(CommandSender player, String path) {
        player.sendMessage(get(path));
    }

    public static void sendList(CommandSender player, String path) {
        player.sendMessage(getList(path).toArray(new String[]{}));
    }

    public static void sendList(CommandSender player, String path, Map<String, String> replacement) {
        player.sendMessage(getList(path, replacement).toArray(new String[]{}));
    }

    public static void sendListAll(Set<? extends CommandSender> players, String path, Map<String, String> replacement) {
        String[] message = getList(path, replacement).toArray(new String[]{});
        players.forEach(p -> p.sendMessage(message));
    }

    public static void send(CommandSender player, String path, String toReplace, String replacement) {
        player.sendMessage(get(path, toReplace, replacement));
    }

    public static void send(CommandSender player, String path, Map<String, String> replacement) {
        player.sendMessage(get(path, replacement));
    }

    public static void sendAll(Set<? extends CommandSender> players, String path) {
        players.forEach(p -> p.sendMessage(get(path)));
    }

    public static void sendAll(Set<? extends CommandSender> players, String path, String toReplace, String replacement) {
        sendAll(players, path, Collections.singletonMap(toReplace, replacement));
    }

    public static void sendAll(Set<? extends CommandSender> players, String path, Map<String, String> replacement) {
        String message = get(path, replacement);
        players.forEach(p -> p.sendMessage(message));
    }
}
