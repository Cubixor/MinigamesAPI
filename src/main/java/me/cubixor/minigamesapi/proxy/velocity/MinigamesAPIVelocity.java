package me.cubixor.minigamesapi.proxy.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.cubixor.socketsmc.proxy.SocketServer;
import me.cubixor.socketsmc.velocity.VelocityProxy;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "mg_api", name = "MinigamesAPI", version = "1.0", authors = {"Cubixor"})
public class MinigamesAPIVelocity {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private CommentedConfigurationNode config;
    private SocketServer socketServer;

    @Inject
    public MinigamesAPIVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        loadConfigs();
        setupSocketServer();

        VelocityClientManager clientManager = new VelocityClientManager(socketServer, server);
        server.getEventManager().register(this, clientManager);
    }

    private void setupSocketServer() {
        socketServer = new SocketServer(
                new VelocityProxy(this, server, logger),
                config.node("socket-port").getInt(),
                config.node("debug").getBoolean()
        );
    }

    private void loadConfigs() {
        try {
            if (Files.notExists(dataDirectory)) {
                Files.createDirectory(dataDirectory);
            }

            final String fileName = "connection-proxy.yml";
            final Path configFile = dataDirectory.resolve(fileName);
            if (Files.notExists(configFile)) {
                try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
                    Files.copy(stream, configFile);
                }
            }

            final YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(configFile).build();
            config = loader.load();

        } catch (IOException e) {
            logger.error("Unable to load configuration file", e);
        }
    }
}
