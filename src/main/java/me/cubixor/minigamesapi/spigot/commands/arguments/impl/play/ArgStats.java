package me.cubixor.minigamesapi.spigot.commands.arguments.impl.play;

import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.config.stats.StatsField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArgStats extends CommandArgument {

    private final StatsManager statsManager;

    public ArgStats(StatsManager statsManager) {
        super("stats", "play.stats", 1, "other.stats-command");
        this.statsManager = statsManager;
    }

    @Override
    protected void handle(Player player, String[] args) {
        if (args.length == 1) {
            sendStats(player, player.getName());
            return;
        }

        String target = args[1];

        if (!Permissions.has(player, "play.stats.others") && !target.equalsIgnoreCase(player.getName())) {
            Messages.send(player, "general.no-permission");
            return;
        }

        if (!statsManager.getAllPlayers().contains(target)) {
            Messages.send(player, "general.invalid-player");
            return;
        }

        sendStats(player, target);
    }

    private void sendStats(Player player, String target) {
        List<String> statsPage = new ArrayList<>(Messages.getList("other.stats"));

        //TODO Playtime
        for (StatsField field : statsManager.getFields()) {
            String toReplace = "%" + field.getCode() + "%";
            String statsValue =  String.valueOf(statsManager.getStats(target, field));
            statsPage.replaceAll(s -> s.replace(toReplace, statsValue));
        }

        for (String s : statsPage) {
            player.sendMessage(s);
        }

    }
}
