package me.cubixor.minigamesapi.spigot.config.arenas;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.CustomConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

class ArenasConfigManagerTest {

    private ServerMock mock;
    private JavaPlugin plugin;
    private ArenasConfigManager arenasConfigManager;

    @BeforeEach
    public void setUp() {
        mock = MockBukkit.mock();
        plugin = MockBukkit.load(MinigamesAPI.class);

        CustomConfig config = new CustomConfig("arenas.yml");
        arenasConfigManager = new ArenasConfigManager(config);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testArenaInsert() {
        arenasConfigManager.insertArena("test");
        Set<String> arenas = arenasConfigManager.getArenas();
        Set<String> expected = Collections.singleton("test");

        Assertions.assertEquals(expected, arenas);
    }

    @Test
    void testArenaLocations() {
        final String arena = "test";
        arenasConfigManager.insertArena(arena);

        Location testLoc = new Location(mock.addSimpleWorld("world"), 1, 1, 1);

        arenasConfigManager.updateField(arena, BasicConfigField.MAIN_LOBBY, testLoc);
        Location configLoc = arenasConfigManager.getLocation(arena, BasicConfigField.MAIN_LOBBY);

        Assertions.assertEquals(testLoc, configLoc);
    }


    @Test
    void testArenaLocationAreas() {
        final String arena = "test";
        arenasConfigManager.insertArena(arena);

        World world = mock.addSimpleWorld("world");
        Location testLoc1 = new Location(world, 1, 1, 1);
        Location testLoc2 = new Location(world, 5, 5, 5);
        Location[] area = new Location[]{testLoc1, testLoc2};

        arenasConfigManager.updateField(arena, BasicConfigField.WAITING_LOBBY, area);
        Location[] configArea = arenasConfigManager.getArea(arena, BasicConfigField.WAITING_LOBBY);

        Assertions.assertArrayEquals(area, configArea);
    }


    @Test
    void testSigns() {
        final String arena = "test";
        World world = mock.addSimpleWorld("world");

        Location testLoc1 = new Location(world, 1, 1, 1);
        Location testLoc2 = new Location(world, 5, 5, 5);
        Location testLoc3 = new Location(world, 10, 1, 4);

        arenasConfigManager.addSign(arena, testLoc1);
        arenasConfigManager.addSign(arena, testLoc2);
        arenasConfigManager.addSign(arena, testLoc3);
        arenasConfigManager.removeSign(arena, testLoc2);

        List<Location> signsExpected = Arrays.asList(testLoc1, testLoc3);
        List<Location> signs = arenasConfigManager.getAllSigns().get(arena);

        Assertions.assertEquals(signsExpected, signs);
    }

}
