package me.cubixor.minigamesapi.spigot.commands.arguments.impl.setup;

import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaCommandArgument;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenaSetupChecker;
import me.cubixor.minigamesapi.spigot.config.arenas.ConfigField;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArgCheck extends ArenaCommandArgument {

    private final ArenaSetupChecker arenaSetupChecker;

    public ArgCheck(ArenasManager arenasManager, ArenaSetupChecker arenaSetupChecker) {
        super(arenasManager, "check", "setup.check", 2, "arena-setup.check", true, null);
        this.arenaSetupChecker = arenaSetupChecker;
    }

    @Override
    public void handle(Player player, String[] args) {
        String arena = args[1];
        LocalArena localArena = arenasRegistry.getLocalArenas().get(arena);

        String setStr = Messages.get("arena-setup.check-set");
        String notSetStr = Messages.get("arena-setup.check-notset");
        List<String> checkPage = new ArrayList<>(Messages.getList("arena-setup.check-page"));

        Map<ConfigField, Boolean> readyMap = arenaSetupChecker.getReadyMap(localArena);

        for (Map.Entry<ConfigField, Boolean> entry : readyMap.entrySet()) {
            boolean isSet = entry.getValue();

            String toReplace = "%" + entry.getKey().toString().replace(".", "") + "%";
            checkPage.replaceAll(s -> s.replace(toReplace, isSet ? setStr : notSetStr));
        }

        boolean ready = !readyMap.containsValue(false);
        boolean active = localArena.isActive();
        String isActiveStr = active ? Messages.get("arena-setup.check-active") : Messages.get("arena-setup.check-not-active");
        String isReadyStr = ready ? Messages.get("arena-setup.check-ready") : Messages.get("arena-setup.check-not-ready");

        checkPage.replaceAll(s -> s
                .replace("%active%", isActiveStr)
                .replace("%ready%", isReadyStr)
                .replace("%arena%", arena)
        );

        player.sendMessage(checkPage.toArray(new String[0]));
    }
}
