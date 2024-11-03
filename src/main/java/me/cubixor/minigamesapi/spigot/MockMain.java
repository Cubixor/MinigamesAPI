package me.cubixor.minigamesapi.spigot;

import me.cubixor.minigamesapi.spigot.commands.LeaveCommand;
import me.cubixor.minigamesapi.spigot.commands.MainCommand;
import me.cubixor.minigamesapi.spigot.commands.MainCommandCompleter;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.config.ConfigManager;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenaSetupChecker;
import me.cubixor.minigamesapi.spigot.config.stats.BasicStatsField;
import me.cubixor.minigamesapi.spigot.events.TimerTickEvent;
import me.cubixor.minigamesapi.spigot.game.*;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.inventories.GlobalMenuRegistry;
import me.cubixor.minigamesapi.spigot.game.inventories.MenuHandler;
import me.cubixor.minigamesapi.spigot.game.items.ItemHandler;
import me.cubixor.minigamesapi.spigot.game.items.ItemsRegistry;
import me.cubixor.minigamesapi.spigot.sockets.PacketManagerSpigot;
import me.cubixor.minigamesapi.spigot.sockets.PacketSenderSpigot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockMain extends JavaPlugin implements Listener {

    public MockMain() {
        super();
    }

    protected MockMain(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        load();
        getServer().getPluginManager().registerEvents(this, this);
    }

    private void load() {
        MinigamesAPI.INIT(this);
        ConfigManager configManager = new ConfigManager(Arrays.asList(BasicStatsField.values()), new String[]{"en"});

        ArenasRegistry arenasRegistry = new ArenasRegistry();
        ItemsRegistry itemsRegistry = new ItemsRegistry();
        PacketSenderSpigot packetSender = new PacketSenderSpigot(configManager.getConnectionConfig());
        SignManager signManager = new SignManager(configManager.getArenasConfigManager(), arenasRegistry, itemsRegistry);
        DefaultArenaFactory arenaFactory = new DefaultArenaFactory();
        ArenasManager arenasManager = new ArenasManager(arenasRegistry, configManager.getArenasConfigManager(), signManager, packetSender, configManager.getStatsManager(), itemsRegistry, arenaFactory);
        PacketManagerSpigot packetManager = new PacketManagerSpigot(arenasManager, packetSender);
        ArenaProtection arenaProtection = new ArenaProtection(arenasManager);
        ItemHandler itemHandler = new ItemHandler(arenasManager, itemsRegistry);
        GlobalMenuRegistry globalMenuRegistry = new GlobalMenuRegistry(arenasManager, itemsRegistry);
        MenuHandler menuHandler = new MenuHandler(arenasRegistry, globalMenuRegistry);
        ChatBlocker chatBlocker = new ChatBlocker(arenasRegistry);
        SimpleBungeeMode simpleBungeeMode = new SimpleBungeeMode(arenasManager);
        ArenaSetupChecker arenaSetupChecker = new ArenaSetupChecker(configManager.getArenasConfigManager());

        List<CommandArgument> args = MainCommand.getCommonArguments(arenasManager, arenaSetupChecker, configManager.getStatsManager(), globalMenuRegistry);
        MainCommand mainCommand = new MainCommand(args);
        MainCommandCompleter mainCommandCompleter = new MainCommandCompleter(args);
        LeaveCommand leaveCommand = new LeaveCommand();

        getServer().getPluginCommand(getName()).setExecutor(mainCommand);
        getServer().getPluginCommand(getName()).setTabCompleter(mainCommandCompleter);

        MinigamesAPI.registerPAPI(arenasRegistry, configManager.getStatsManager());
    }

    @EventHandler
    public void onGameTime(TimerTickEvent evt) {
        if (evt.getGameState().equals(GameState.GAME) && evt.getTimer() == 20) {
            evt.getLocalArena().getStateManager().setEnd(Collections.emptyList());
        }
    }
}
