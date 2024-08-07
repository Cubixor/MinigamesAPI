package me.cubixor.minigamesapi.spigot.game.arena;

import me.cubixor.minigamesapi.spigot.events.ScoreboardUpdateEvent;
import me.cubixor.minigamesapi.spigot.utils.MessageUtils;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardManager {

    private static final String[] lineNames = new String[]{
            ChatColor.BLACK.toString(),
            ChatColor.DARK_BLUE.toString(),
            ChatColor.DARK_GREEN.toString(),
            ChatColor.DARK_AQUA.toString(),
            ChatColor.DARK_RED.toString(),
            ChatColor.DARK_PURPLE.toString(),
            ChatColor.GOLD.toString(),
            ChatColor.GRAY.toString(),
            ChatColor.DARK_GRAY.toString(),
            ChatColor.BLUE.toString(),
            ChatColor.GREEN.toString(),
            ChatColor.AQUA.toString(),
            ChatColor.RED.toString(),
            ChatColor.LIGHT_PURPLE.toString(),
            ChatColor.YELLOW.toString(),
            ChatColor.WHITE.toString()
    };

    private final LocalArena localArena;
    private Scoreboard scoreboard;

    public ScoreboardManager(LocalArena localArena) {
        this.localArena = localArena;
    }

    public void updateScoreboard() {
        String arenaString = localArena.getName();
        String countString = String.valueOf(localArena.getPlayers().size());
        String maxString = String.valueOf(localArena.getMaxPlayers());
        String timeShortString = String.valueOf(localArena.getTimer());
        String timeLongString = MessageUtils.formatTime(localArena.getTimer(), "time-format");
        String date = MessageUtils.formatDate(LocalDateTime.now(), "date-format");

        Map<String, String> replacement = new HashMap<>();
        replacement.put("%arena%", arenaString);
        replacement.put("%players%", countString);
        replacement.put("%max%", maxString);
        replacement.put("%time-short%", timeShortString);
        replacement.put("%time-long%", timeLongString);
        replacement.put("%date%", date);

        Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(localArena, replacement));

        updateScoreboard(localArena.getState(), replacement);

        for (Player p : localArena.getBukkitPlayers()) {
            if (!p.getScoreboard().equals(scoreboard)) {
                p.setScoreboard(scoreboard);
            }
        }
    }

    private void updateScoreboard(GameState gameState, Map<String, String> replacement) {
        String stateString = gameState.toString();
        List<String> message = Messages.getList("game.scoreboard-" + stateString);
        message.replaceAll(s -> {
            for (Map.Entry<String, String> entry : replacement.entrySet()) {
                s = s.replace(entry.getKey(), entry.getValue());
            }
            return s;
        });

        int rowCount = message.size();

        if (scoreboard == null || scoreboard.getObjective(stateString) == null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective(stateString, "");
            objective.setDisplayName(Messages.get("game.scoreboard-title"));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);


            for (int i = rowCount; i > 0; i--) {
                Team team = scoreboard.registerNewTeam(String.valueOf(i));
                team.addEntry(lineNames[i]);
                Score score = objective.getScore(lineNames[i]);
                score.setScore(i);
            }
        }

        for (int i = rowCount; i > 0; i--) {
            int row = rowCount - i;
            Team team = scoreboard.getTeam(String.valueOf(i));
            setRow(team, message.get(row));
        }
    }

    private void setRow(Team team, String text) {
        if (!VersionUtils.isBefore13() || text.length() <= 16) {
            team.setPrefix(text);
            return;
        }

        boolean atColor = text.charAt(15) == '§';
        String str1 = text.substring(0, atColor ? 15 : 16);
        String str2 = text.substring(atColor ? 15 : 16, Math.min(text.length(), 30));

        team.setPrefix(str1);
        team.setSuffix((atColor ? "" : ChatColor.getLastColors(str1)) + str2);
    }
}