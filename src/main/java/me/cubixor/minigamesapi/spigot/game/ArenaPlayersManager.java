package me.cubixor.minigamesapi.spigot.game;

import com.cryptomorin.xseries.messages.Titles;
import com.google.common.collect.ImmutableMap;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.Utils;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;
import me.cubixor.minigamesapi.spigot.config.stats.BasicStatsField;
import me.cubixor.minigamesapi.spigot.events.GameJoinEvent;
import me.cubixor.minigamesapi.spigot.events.GameLeaveEvent;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.game.arena.PlayerData;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.Particles;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import me.cubixor.minigamesapi.spigot.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

public class ArenaPlayersManager {

    private final ArenasManager arenasManager;
    private final ArenasRegistry arenasRegistry;

    public ArenaPlayersManager(ArenasManager arenasManager) {
        this.arenasManager = arenasManager;
        this.arenasRegistry = arenasManager.getRegistry();
    }

    public boolean joinArena(Player player, String arenaString) {
        Arena arena = arenasRegistry.getArena(arenaString);
        if (!checkArenaJoin(player, arena)) {
            return false;
        }

        putInArena(player, arena);

        return true;
    }

    private boolean checkArenaJoin(Player player, Arena arena) {
        String arenaString = arena.getName();

        if (arenasRegistry.isInArena(player)) {
            Messages.send(player, "game.arena-join-already-in-game");
            return false;
        }

        if (!arena.isActive()) {
            Messages.send(player, "game.arena-join-not-active", "%arena%", arenaString);
            return false;
        }

        if (!checkVIP(arena, player)) {
            Messages.send(player, "game.arena-join-vip");
            return false;
        }

        if (!arena.getState().isWaitingStarting()) {
            Messages.send(player, "game.arena-join-arena-in-game", "%arena%", arenaString);
            return false;
        }

        if (arena.isFull()) {
            Messages.send(player, "game.arena-join-arena-full", "%arena%", arenaString);
            return false;
        }

        return true;
    }

    public void joinRandomArena(Player player) {
        if (arenasRegistry.isInArena(player)) {
            Messages.send(player, "game.arena-join-already-in-game");
            return;
        }

        LinkedHashMap<Arena, Integer> playersCount = getAvailableArenas(player);

        Utils.sortByValueInPlace(playersCount);
        Optional<Arena> toJoin = playersCount.keySet().stream().findFirst();

        if (!toJoin.isPresent()) {
            Messages.send(player, "game.quick-join-no-games-found");
            return;
        }

        putInArena(player, toJoin.get());
    }

    private LinkedHashMap<Arena, Integer> getAvailableArenas(Player player) {
        Set<String> arenas = arenasRegistry.getAllArenaNames();
        LinkedHashMap<Arena, Integer> playersCount = new LinkedHashMap<>();

        for (String arenaString : arenas) {
            Arena arena = arenasRegistry.getArena(arenaString);

            if (arena.getState().isWaitingStarting()
                    && !arena.isFull()
                    && checkVIP(arena, player)) {

                playersCount.put(arena, arena.getPlayers().size());
            }
        }
        return playersCount;
    }

    public void putInArena(Player player, Arena arena) {
        if (arena.isLocal()) {
            LocalArena localArena = (LocalArena) arena;
            putInLocalArena(player, localArena);
        } else {
            arenasManager.getPacketSender().sendJoinPacket(arena, player);
        }
    }

    public void putInLocalArena(Player player, LocalArena localArena) {
        String arenaString = localArena.getName();

        localArena.getPlayers().add(player.getName());

        PlayerData playerData = new PlayerData(player);
        localArena.getPlayerData().put(player, playerData);
        playerData.clearPlayerData();

        Location waitingLobby = arenasManager.getConfigManager().getLocation(arenaString, BasicConfigField.WAITING_LOBBY);
        player.teleport(waitingLobby);

        arenasManager.getItemsRegistry().getLeaveItem().give(player);
        player.getInventory().setHeldItemSlot(4);
        player.setGameMode(GameMode.ADVENTURE);

        //TODO Action bar
        //sendWaitingActionBar(player, localArena, min);

        Sounds.playSound("join", player.getLocation(), localArena.getBukkitPlayers());
        Particles.spawnParticle(waitingLobby.add(0, 1.5, 0), "join");

        int count = localArena.getPlayers().size();
        String maxString = String.valueOf(localArena.getMaxPlayers());
        String countString = String.valueOf(count);

        Messages.sendAll(localArena.getBukkitPlayers(), "game.arena-join-success", ImmutableMap.of(
                "%player%", player.getName(),
                "%count%", countString,
                "%max%", maxString)
        );


        localArena.getStateManager().updateOnJoin();

        arenasManager.updateArena(localArena);
        Bukkit.getPluginManager().callEvent(new GameJoinEvent(localArena, player));
    }

    public void leaveArena(Player player, LocalArena localArena) {
        Set<Player> players = new HashSet<>(localArena.getBukkitPlayers());
        String count = String.valueOf(players.size() - 1);
        String max = String.valueOf(localArena.getMaxPlayers());

        kickFromLocalArena(player, localArena, false);

        Messages.sendAll(players, "game.arena-leave-success", ImmutableMap.of(
                "%player%", player.getName(),
                "%count%", count,
                "%max%", max
        ));
    }

    public void kickFromArena(String playerName, Arena arena) {
        if (arena.isLocal()) {
            LocalArena localArena = (LocalArena) arena;
            kickFromLocalArena(Bukkit.getPlayerExact(playerName), localArena, false);
        } else {
            arenasManager.getPacketSender().sendKickPacket(arena, playerName);
        }
    }

    public void kickFromLocalArena(Player player, LocalArena localArena, boolean reset) {
        Location playerLocation = player.getLocation();
        PlayerData playerData = localArena.getPlayerData().remove(player);
        playerData.restorePlayerData();
        player.eject();

        if (localArena.getState().equals(GameState.GAME)) {
            arenasManager.getStatsManager().addStats(player.getName(), BasicStatsField.PLAYTIME, localArena.getTimer());
        }

        if (MinigamesAPI.getPlugin().getConfig().getBoolean("use-main-lobby")) {
            player.teleport(arenasManager.getConfigManager().getLocation(localArena.getName(), BasicConfigField.MAIN_LOBBY));
        } else {
            player.teleport(playerData.getLocation());
        }


        Sounds.playSound("leave", playerLocation, localArena.getBukkitPlayers());
        Particles.spawnParticle(playerLocation.add(0, 1.5, 0), "leave");
        Titles.clearTitle(player);

        localArena.getPlayers().remove(player.getName());
        localArena.getScoreboardManager().removePlayer(player);

        if (arenasManager.isBungee()) {
            arenasManager.getPacketSender().sendLeavePacket(player.getName());
        }

        if (!reset) {
            localArena.getStateManager().updateOnLeave();
            arenasManager.updateArena(localArena);
        }

        Bukkit.getPluginManager().callEvent(new GameLeaveEvent(localArena, player));
    }

    private boolean checkVIP(Arena arena, Player player) {
        return !arena.isVip() || Permissions.has(player, "vip");
    }
}

