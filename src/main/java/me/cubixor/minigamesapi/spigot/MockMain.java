package me.cubixor.minigamesapi.spigot;

import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.arena.ChatBlocker;
import me.cubixor.minigamesapi.spigot.commands.MainCommand;
import me.cubixor.minigamesapi.spigot.commands.MainCommandCompleter;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.config.ConfigManager;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenaSetupChecker;
import me.cubixor.minigamesapi.spigot.config.stats.BasicStatsField;
import me.cubixor.minigamesapi.spigot.sockets.PacketManagerSpigot;
import me.cubixor.minigamesapi.spigot.sockets.PacketSenderSpigot;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MockMain extends JavaPlugin {

    public MockMain() {
        super();
    }

    protected MockMain(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        load();
    }

    private void load() {
        MinigamesAPI.INIT(this);
        ConfigManager configManager = new ConfigManager(Arrays.asList(BasicStatsField.values()));

        PacketSenderSpigot packetSender = new PacketSenderSpigot(configManager.getConnectionConfig());
        ArenasManager arenasManager = new ArenasManager(configManager.getArenasConfigManager(), packetSender);
        PacketManagerSpigot packetManager = new PacketManagerSpigot(arenasManager, packetSender);

        ChatBlocker chatBlocker = new ChatBlocker(arenasManager);
        ArenaSetupChecker arenaSetupChecker = new ArenaSetupChecker(configManager.getArenasConfigManager());

        List<CommandArgument> args = MainCommand.getCommonArguments(arenasManager, arenaSetupChecker, configManager.getStatsManager());
        MainCommand mainCommand = new MainCommand(args);
        MainCommandCompleter mainCommandCompleter = new MainCommandCompleter(args, arenasManager);

        getServer().getPluginCommand(getName()).setExecutor(mainCommand);
        getServer().getPluginCommand(getName()).setTabCompleter(mainCommandCompleter);
    }
}
