package me.cubixor.minigamesapi.spigot.arena;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.MockMain;
import me.cubixor.minigamesapi.spigot.config.CustomConfig;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArenasManagerTest {

    private ServerMock mock;
    private JavaPlugin plugin;
    private ArenasManager arenasManager;

    @BeforeEach
    public void setUp() {
        mock = MockBukkit.mock();
        plugin = MockBukkit.load(MockMain.class);

        CustomConfig config = new CustomConfig("arenas.yml");
        ArenasConfigManager arenasConfigManager = new ArenasConfigManager(config);
        arenasManager = new ArenasManager(arenasConfigManager, null);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testCommandBlock() {
        PlayerMock player = mock.addPlayer();
    }
}
