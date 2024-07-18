package me.cubixor.minigamesapi.spigot;

import me.cubixor.minigamesapi.spigot.config.ConfigManager;
import me.cubixor.minigamesapi.spigot.config.stats.BasicStatsField;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MinigamesAPI extends JavaPlugin {

    private static MinigamesAPI instance;

    private static boolean bungee;

    public MinigamesAPI() {
        super();
    }

    protected MinigamesAPI(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    public static MinigamesAPI getInstance() {
        return instance;
    }

    public static boolean isBungee() {
        return bungee;
    }

    /*public void load() {
        ConfigManager configManager = new ConfigManager();

        bungee = getConfig().getBoolean("bungee.bungee-mode");


        SocketClient socketClient = bungee ? configManager.setupSocket() : null;
        PacketSenderSpigot packetSender = bungee ? new PacketSenderSpigot(socketClient.getSender()) : null;

        ArenasManager arenasManager = new ArenasManager(
                configManager.getArenasConfigManager(), , packetSender);

        PacketManagerSpigot packetManager = bungee ? new PacketManagerSpigot(arenasManager, packetSender) : null;


        SignManager signManager = new SignManager();
        ChatBlocker chatBlocker = new ChatBlocker(arenasManager);


        getServer().getPluginManager().registerEvents(chatBlocker, this);
        getServer().getPluginManager().registerEvents(signManager, this);
        if (bungee) getServer().getPluginManager().registerEvents(packetManager, this);

        MainCommand mainCommand = new MainCommand()

    }*/

    @Override
    public void onEnable() {
        instance = this;

        ConfigManager configManager = new ConfigManager(Arrays.stream(BasicStatsField.values()).collect(Collectors.toList()));

        bungee = getConfig().getBoolean("bungee.bungee-mode");
    }
}
