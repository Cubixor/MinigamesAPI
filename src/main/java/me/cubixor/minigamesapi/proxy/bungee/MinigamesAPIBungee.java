package me.cubixor.minigamesapi.proxy.bungee;

import me.cubixor.socketsmc.bungee.BungeeProxy;
import me.cubixor.socketsmc.proxy.SocketServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MinigamesAPIBungee extends Plugin {

    private Configuration config;
    private SocketServer socketServer;

    @Override
    public void onEnable() {
        loadConfigs();
        setupSocketServer();

        BungeeClientManager bungeeClientManager = new BungeeClientManager(socketServer);
        getProxy().getPluginManager().registerListener(this, bungeeClientManager);
    }

    @Override
    public void onDisable() {
        if (socketServer != null) {
            socketServer.closeConnections();
            getProxy().getScheduler().cancel(this);
        }
    }

    private void setupSocketServer() {
        socketServer = new SocketServer(
                new BungeeProxy(this),
                config.getInt("socket-port"),
                config.getBoolean("debug")
        );
    }

    private void loadConfigs() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        try {
            String fileName = "connection-proxy.yml";
            File file = new File(getDataFolder(), fileName);
            if (!file.exists()) {
                InputStream in = getResourceAsStream(fileName);
                Files.copy(in, file.toPath());
                in.close();
            }

            config = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new File(getDataFolder(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
