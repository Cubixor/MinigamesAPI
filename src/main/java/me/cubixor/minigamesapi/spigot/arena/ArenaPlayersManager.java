package me.cubixor.minigamesapi.spigot.arena;

import com.google.common.collect.ImmutableMap;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.Utils;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;
import me.cubixor.minigamesapi.spigot.events.GameJoinEvent;
import me.cubixor.minigamesapi.spigot.events.GameLeaveEvent;
import me.cubixor.minigamesapi.spigot.game.PlayerData;
import me.cubixor.minigamesapi.spigot.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

public class ArenaPlayersManager {

    private final ArenasManager arenasManager;

    public ArenaPlayersManager(ArenasManager arenasManager) {
        this.arenasManager = arenasManager;
    }

    public boolean joinArena(Player player, String arenaString) {
        Arena arena = arenasManager.getArena(arenaString);
        if (!checkArenaJoin(player, arena)) {
            return false;
        }

        putInArena(player, arena);

        return true;
    }

    private boolean checkArenaJoin(Player player, Arena arena) {
        String arenaString = arena.getName();

        if (arenasManager.isInArena(player)) {
            Messages.send(player, "game.arena-join-already-in-game");
            return false;
        }

        if (!arena.isActive()) {
            Messages.send(player, "game.arena-join-not-active", "%arena%", arenaString);
            return false;
        }

        if (checkVIP(arena, player)) {
            Messages.send(player, "game.arena-join-vip");
            return false;
        }

        //TODO Rejoin
        /*if (arena.getState().equals(GameState.GAME) && arena.getPlayersLeft().contains(player.getName())) {
            return true;
        }*/

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
        if (arenasManager.isInArena(player)) {
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
        Set<String> arenas = arenasManager.getAllArenaNames();
        LinkedHashMap<Arena, Integer> playersCount = new LinkedHashMap<>();

        for (String arenaString : arenas) {
            Arena arena = arenasManager.getArena(arenaString);

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
            if (MinigamesAPI.isBungee()) {
                arenasManager.getPacketSender().sendJoinPacket(localArena.toArena(), player, true);
            }
        } else {
            arenasManager.getPacketSender().sendJoinPacket(arena, player, false);
        }
    }

    public void putInLocalArena(Player player, LocalArena localArena) {
        //TODO Rejoin
        /*if (localArena.getPlayersLeft().contains(player.getName())) {
            rejoinLocalArena(player, localArena);
            return;
        }*/

        String arenaString = localArena.getName();

        localArena.getPlayers().add(player.getName());

        PlayerData playerData = new PlayerData(player);
        localArena.getPlayerData().put(player, playerData);
        playerData.clearPlayerData();

        Location waitingLobby = arenasManager.getConfigManager().getLocation(arenaString, BasicConfigField.WAITING_LOBBY);
        player.teleport(waitingLobby);

        Items.getLeaveItem().give(player);
        player.getInventory().setHeldItemSlot(4);

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

        //TODO Update state, scoreboard

        arenasManager.updateArena(localArena);
        MinigamesAPI.getInstance().getServer().getPluginManager().callEvent(new GameJoinEvent(localArena, player));
    }

    public void leaveArena(Player player, LocalArena localArena) {
        kickFromLocalArena(player, localArena);

        Set<Player> players = localArena.getBukkitPlayers();
        String count = String.valueOf(players.size());
        String max = String.valueOf(localArena.getMaxPlayers());
        Messages.sendAll(players, "game.arena-leave-success", ImmutableMap.of(
                "%player%", player.getName(),
                "%count%", count,
                "%max%", max
        ));
    }

    public void kickFromArena(String playerName, Arena arena) {

        if (arena.isLocal()) {
            LocalArena localArena = (LocalArena) arena;
            kickFromLocalArena(Bukkit.getPlayerExact(playerName), localArena);
        } else {
            arenasManager.getPacketSender().sendLeavePacket(playerName);
        }
    }

    public void kickFromLocalArena(Player player, LocalArena localArena) {
        localArena.getBukkitPlayers().remove(player);

        Location playerLocation = player.getLocation();
        PlayerData playerData = localArena.getPlayerData().remove(player);
        playerData.restorePlayerData();

        //TODO Add playtime stats

        if (MinigamesAPI.getInstance().getConfig().getBoolean("use-main-lobby")) {
            player.teleport(arenasManager.getConfigManager().getLocation(localArena.getName(), BasicConfigField.MAIN_LOBBY));
        } else {
            player.teleport(playerData.getLocation());
        }


        Sounds.playSound("leave", playerLocation, localArena.getBukkitPlayers());
        Particles.spawnParticle(playerLocation.add(0, 1.5, 0), "leave");

        //TODO Update state, scoreboard

        arenasManager.updateArena(localArena);
        MinigamesAPI.getInstance().getServer().getPluginManager().callEvent(new GameLeaveEvent(localArena, player));
    }

    private boolean checkVIP(Arena arena, Player player) {
        return !arena.isVip() || Permissions.has(player, "vip");
    }
}

