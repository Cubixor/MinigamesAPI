package me.cubixor.minigamesapi.spigot;

import me.cubixor.minigamesapi.spigot.commands.MainCommand;
import me.cubixor.minigamesapi.spigot.commands.MainCommandCompleter;
import me.cubixor.minigamesapi.spigot.commands.arguments.CommandArgument;
import me.cubixor.minigamesapi.spigot.config.ConfigManager;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenaSetupChecker;
import me.cubixor.minigamesapi.spigot.config.stats.BasicStatsField;
import me.cubixor.minigamesapi.spigot.game.*;
import me.cubixor.minigamesapi.spigot.integrations.PlaceholderExpansion;
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

        ArenasRegistry arenasRegistry = new ArenasRegistry();
        PacketSenderSpigot packetSender = new PacketSenderSpigot(configManager.getConnectionConfig());
        SignManager signManager = new SignManager(configManager.getArenasConfigManager(), arenasRegistry);
        ArenasManager arenasManager = new ArenasManager(arenasRegistry, configManager.getArenasConfigManager(), signManager, packetSender, configManager.getStatsManager());
        PacketManagerSpigot packetManager = new PacketManagerSpigot(arenasManager, packetSender);
        ArenaProtection arenaProtection = new ArenaProtection(arenasManager);
        ItemHandler itemHandler = new ItemHandler(arenasManager);
        ChatBlocker chatBlocker = new ChatBlocker(arenasRegistry);
        ArenaSetupChecker arenaSetupChecker = new ArenaSetupChecker(configManager.getArenasConfigManager());

        List<CommandArgument> args = MainCommand.getCommonArguments(arenasManager, arenaSetupChecker, configManager.getStatsManager());
        MainCommand mainCommand = new MainCommand(args);
        MainCommandCompleter mainCommandCompleter = new MainCommandCompleter(args);

        getServer().getPluginCommand(getName()).setExecutor(mainCommand);
        getServer().getPluginCommand(getName()).setTabCompleter(mainCommandCompleter);

        new PlaceholderExpansion(arenasRegistry, configManager.getStatsManager());
    }
}
