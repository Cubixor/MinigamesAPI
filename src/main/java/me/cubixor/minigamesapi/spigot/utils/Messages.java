package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.reflection.XReflection;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Messages {

    private static FileConfiguration messagesConfig;
    private static String prefix;
    private static String cmd;

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    private Messages() {
    }

    public static void init(FileConfiguration messagesConfig, String cmd) {
        Messages.messagesConfig = messagesConfig;
        Messages.prefix = parseColors(messagesConfig.getString("prefix"));
        Messages.cmd = cmd;
    }

    private static String parseColors(String message) {
        if (XReflection.supports(1, 16)) {
            Matcher match = HEX_PATTERN.matcher(message);
            while (match.find()) {
                String color = message.substring(match.start(), match.end());
                message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                match = HEX_PATTERN.matcher(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static Player getTargetPlayer(CommandSender sender) {
        return sender instanceof Player ? (Player) sender : null;
    }

    private static String parsePAPI(Player player, String message) {
        if (player != null && MinigamesAPI.usesPAPI()) {
            return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }

    public static String get(String path) {
        return get(null, path);
    }

    public static String get(Player player, String path) {
        return get(player, path, Collections.emptyMap());
    }

    public static String get(String path, String toReplace, String replacement) {
        return get(null, path, toReplace, replacement);
    }

    public static String get(Player player, String path, String toReplace, String replacement) {
        return get(player, path, Collections.singletonMap(toReplace, replacement));
    }

    public static String get(String path, Map<String, String> replacement) {
        return get(null, path, replacement);
    }

    public static String get(Player player, String path, Map<String, String> replacement) {
        String message = messagesConfig.getString(path);
        if (message == null) {
            return "?";
        }

        message = message
                .replace("%prefix%", prefix)
                .replace("%cmd%", cmd);

        for (Map.Entry<String, String> entry : replacement.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return parseColors(parsePAPI(player, message));
    }

    public static List<String> getList(String path) {
        return getList(null, path);
    }

    public static List<String> getList(Player player, String path) {
        return getList(player, path, Collections.emptyMap());
    }

    public static List<String> getList(String path, Map<String, String> replacement) {
        return getList(null, path, replacement);
    }

    public static List<String> getList(Player player, String path, Map<String, String> replacement) {
        List<String> message = messagesConfig.getStringList(path);
        return message.stream()
                .map(msg -> msg.replace("%prefix%", prefix))
                .map(msg -> msg.replace("%cmd%", cmd))
                .map(msg -> {
                    for (Map.Entry<String, String> entry : replacement.entrySet()) {
                        msg = msg.replace(entry.getKey(), entry.getValue());
                    }
                    return msg;
                })
                .map(msg -> parsePAPI(player, msg))
                .map(Messages::parseColors)
                .collect(Collectors.toList());
    }

    public static void send(CommandSender sender, String path) {
        sender.sendMessage(get(getTargetPlayer(sender), path));
    }

    public static void sendList(CommandSender sender, String path) {
        sender.sendMessage(getList(getTargetPlayer(sender), path).toArray(new String[]{}));
    }

    public static void sendList(CommandSender sender, String path, Map<String, String> replacement) {
        sender.sendMessage(getList(getTargetPlayer(sender), path, replacement).toArray(new String[]{}));
    }

    public static void sendListAll(Set<? extends CommandSender> players, String path, Map<String, String> replacement) {
        players.forEach(p -> p.sendMessage(getList(getTargetPlayer(p), path, replacement).toArray(new String[]{})));
    }

    public static void send(CommandSender sender, String path, String toReplace, String replacement) {
        sender.sendMessage(get(getTargetPlayer(sender), path, toReplace, replacement));
    }

    public static void send(CommandSender sender, String path, Map<String, String> replacement) {
        sender.sendMessage(get(getTargetPlayer(sender), path, replacement));
    }

    public static void sendAll(Set<? extends CommandSender> players, String path) {
        players.forEach(p -> p.sendMessage(get(getTargetPlayer(p), path)));
    }

    public static void sendAll(Set<? extends CommandSender> players, String path, String toReplace, String replacement) {
        sendAll(players, path, Collections.singletonMap(toReplace, replacement));
    }

    public static void sendAll(Set<? extends CommandSender> players, String path, Map<String, String> replacement) {
        players.forEach(p -> p.sendMessage(get(getTargetPlayer(p), path, replacement)));
    }
}
