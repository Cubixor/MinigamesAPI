package me.cubixor.minigamesapi.spigot.game;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.MockMain;
import me.cubixor.minigamesapi.spigot.config.CustomConfig;
import me.cubixor.minigamesapi.spigot.config.arenas.ArenasConfigManager;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.items.ItemsRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArenasManagerTest {

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
        ItemsRegistry itemsRegistry = new ItemsRegistry();
        SignManager signManager = new SignManager(arenasConfigManager, arenasRegistry);
        DefaultArenaFactory arenaFactory = new DefaultArenaFactory();
        arenasManager = new ArenasManager(arenasRegistry, arenasConfigManager, signManager, null, null, itemsRegistry, arenaFactory);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testArenaSetup() {
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
        Assertions.assertEquals(GameState.WAITING, arena.getState());
    }

    @Test
    void testArenaRemove() {
        final String name = "test";
        ArenasRegistry arenasRegistry = arenasManager.getRegistry();
        arenasManager.addArena(name);

        Assertions.assertTrue(arenasRegistry.isValidArena(name));
        Assertions.assertTrue(arenasRegistry.isLocalArena(name));

        arenasManager.removeArena(name);

        Assertions.assertFalse(arenasRegistry.isValidArena(name));
        Assertions.assertFalse(arenasRegistry.isLocalArena(name));
    }
}
