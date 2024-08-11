package me.cubixor.minigamesapi.spigot.game;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.cubixor.minigamesapi.spigot.MockMain;
import me.cubixor.minigamesapi.spigot.config.CustomConfig;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.game.items.ItemsRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatBlockerTest {

    private ServerMock mock;
    private JavaPlugin plugin;
    private ArenasManager arenasManager;

    @BeforeEach
    public void setUp() {
        mock = MockBukkit.mock();
        plugin = MockBukkit.load(MockMain.class);

        CustomConfig config = new CustomConfig("arenas.yml");
        ArenasConfigManager arenasConfigManager = new ArenasConfigManager(config);
        ArenasRegistry arenasRegistry = new ArenasRegistry();
        ItemsRegistry itemsRegistry = new ItemsRegistry();
        arenasManager = new ArenasManager(arenasRegistry, arenasConfigManager, null,null,null, itemsRegistry);
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
