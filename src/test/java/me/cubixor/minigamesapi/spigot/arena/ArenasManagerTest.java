package me.cubixor.minigamesapi.spigot.arena;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.MockMain;
import me.cubixor.minigamesapi.spigot.config.CustomConfig;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

        MinigamesAPI.INIT(plugin);
        CustomConfig config = new CustomConfig("arenas.yml");
        ArenasConfigManager arenasConfigManager = new ArenasConfigManager(config);
        ArenasRegistry arenasRegistry = new ArenasRegistry();
        SignManager signManager = new SignManager(arenasConfigManager, arenasRegistry);
        arenasManager = new ArenasManager(arenasRegistry, arenasConfigManager, signManager, null);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testArenaCreate() {
        final String name = "test";
        arenasManager.addArena(name);
        arenasManager.updateArenaMinPlayers(name, 2);
        arenasManager.updateArenaMaxPlayers(name, 10);
        arenasManager.updateArenaVip(name, true);
        arenasManager.updateArenaActive(name, true);

        Arena arena = arenasManager.getRegistry().getArena(name);

        Assertions.assertEquals(2, arena.getMinPlayers());
        Assertions.assertEquals(10, arena.getMaxPlayers());
        Assertions.assertTrue(arena.isVip());
        Assertions.assertTrue(arena.isActive());
    }
}
