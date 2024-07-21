package me.cubixor.minigamesapi.spigot.commands.arguments.impl.play;

import com.google.common.collect.ImmutableMap;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.arena.Arena;
import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.arena.ArenasRegistry;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.utils.MessageUtils;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Map;

public class ArgList extends CommandArgument {

    private final ArenasRegistry arenasRegistry;

    public ArgList(ArenasRegistry arenasRegistry) {
        //TODO No usage in messages
        super("list", "play.list", 1, "other.list");
        this.arenasRegistry = arenasRegistry;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void handle(Player player, String[] args) {
        if (arenasRegistry.getAllArenaNames().isEmpty()) {
            Messages.send(player, "other.list-empty");
            return;
        }

        LinkedList<TextComponent> msg = new LinkedList<>();

        for (String arenaString : arenasRegistry.getAllArenaNames()) {
            Arena arena = arenasRegistry.getArena(arenaString);

            String count = Integer.toString(arena.getPlayers().size());
            String gameState = MessageUtils.getStringState(arena);
            String max = Integer.toString(arena.getMaxPlayers());
            String vip = arena.isVip() ? Messages.get("general.vip-prefix") : "";

            Map<String, String> replacement = ImmutableMap.of("%arena%", arenaString, "%count%", count, "%max%", max, "%state%", gameState, "%?vip?%", vip);

            TextComponent message = new TextComponent(Messages.get("other.list-arena", replacement));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Messages.get("other.list-hover")).create()));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinigamesAPI.getPlugin().getName() + " join " + arenaString));
            msg.add(message);
        }

        Messages.sendList(player, "other.list-header");
        for (TextComponent tc : msg) {
            player.spigot().sendMessage(tc);
        }
        Messages.sendList(player, "other.list-footer");
    }
}
