package me.cubixor.minigamesapi.spigot.config.arenas;

import me.cubixor.minigamesapi.spigot.config.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;


public class ArenasConfigManager {

    private final CustomConfig arenasConfig;
    private final ConfigurationSection arenasSection;

    public ArenasConfigManager(CustomConfig arenasConfig) {
        this.arenasConfig = arenasConfig;
        this.arenasSection = arenasConfig.get().getConfigurationSection("arenas");
    }

    public String getString(String arena, ConfigField field) {
        return arenasSection.getConfigurationSection(arena).getString(field.toString());
    }

    public int getInt(String arena, ConfigField field) {
        return arenasSection.getConfigurationSection(arena).getInt(field.toString());
    }

    public boolean getBoolean(String arena, ConfigField field) {
        return arenasSection.getConfigurationSection(arena).getBoolean(field.toString());
    }

    public List<String> getStringList(String arena, ConfigField field) {
        return arenasSection.getConfigurationSection(arena).getStringList(field.toString());
    }

    private Location getLocation(String arena, String path) {
        String locString = arenasSection
                .getConfigurationSection(arena)
                .getString(path);

        if (locString == null) {
            return null;
        }

        return stringToLocation(locString);
    }

    public Location getLocation(String arena, ConfigField field, String arg) {
        return getLocation(arena, String.join(".", field.toString(), arg));
    }

    public Location getLocation(String arena, ConfigField field) {
        return getLocation(arena, field.toString());
    }

    private List<Location> getLocationList(String arena, String path) {
        List<String> locStrs = arenasSection.getConfigurationSection(arena).getStringList(path);
        return locStrs.stream()
                .map(this::stringToLocation)
                .collect(Collectors.toList());
    }

    public List<Location> getLocationList(String arena, ConfigField field) {
        return getLocationList(arena, field.toString());
    }

    public List<Location> getLocationList(String arena, ConfigField field, String arg) {
        return getLocationList(arena, String.join(".", field.toString(), arg));
    }

    public void addLocation(String arena, ConfigField field, Location loc) {
        List<Location> locations = getLocationList(arena, field);
        locations.add(loc);
        updateField(arena, field, locations);
    }

    public Location[] getArea(String arena, ConfigField field) {
        return getArea(arena, field.toString());
    }

    public Location[] getArea(String arena, ConfigField field, String arg) {
        return getArea(arena, String.join(".", field.toString(), arg));
    }

    public Location[] getArea(String arena, String path) {
        String locString = arenasSection.getConfigurationSection(arena).getString(path);
        if (locString == null) return null;

        String[] splitLoc = splitLocations(locString);

        Location[] locations = new Location[2];
        locations[0] = stringToLocation(splitLoc[0]);
        locations[1] = stringToLocation(splitLoc[1]);

        return locations;
    }

    public Set<String> getArenas() {
        return arenasSection.getKeys(false);
    }

    private List<String> getSignsStr(String arena) {
        List<String> signs = arenasConfig.get().getConfigurationSection("signs").getStringList(arena);
        return signs == null ? Collections.emptyList() : signs;
    }

    private void saveSigns(String arena, List<String> signs) {
        arenasConfig.get().getConfigurationSection("signs").set(arena, signs);
        arenasConfig.save();
    }

    public Map<String, List<Location>> getAllSigns() {
        Map<String, List<Location>> signs = new HashMap<>();

        for (String arena : arenasConfig.get().getConfigurationSection("signs").getKeys(false)) {
            signs.put(arena, getSigns(arena));
        }
        return signs;
    }

    private List<Location> getSigns(String arena) {
        return getSignsStr(arena).stream().map(this::stringToLocation).collect(Collectors.toList());
    }

    public void addSign(String arena, Location sign) {
        List<String> signs = getSignsStr(arena);
        signs.add(locationToString(sign));
        saveSigns(arena, signs);
    }

    public void removeSign(String arena, Location sign) {
        List<String> signs = getSignsStr(arena);
        signs.remove(locationToString(sign));
        saveSigns(arena, signs);
    }

    public void removeSigns(String arena) {
        saveSigns(arena, null);
    }

    public void insertArena(String name) {
        ConfigurationSection arenaSection = arenasSection.createSection(name);
        arenaSection.set(BasicConfigField.ACTIVE.toString(), false);
        arenaSection.set(BasicConfigField.VIP.toString(), false);
        arenaSection.set(BasicConfigField.MIN_PLAYERS.toString(), 0);
        arenaSection.set(BasicConfigField.MAX_PLAYERS.toString(), 0);

        arenasConfig.save();
    }

    public void removeArena(String name) {
        arenasSection.set(name, null);
        arenasConfig.save();
    }

    private void updateField(String arena, String path, Object value) {
        //Parse Locations to String
        if (value instanceof Location) {
            value = locationToString((Location) value);
        } else if (value instanceof Location[]) {
            Location[] list = (Location[]) value;
            value = joinLocations(list[0], list[1]);
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            if (!list.isEmpty() && list.get(0) instanceof Location) {
                List<Location> locList = (List<Location>) list;
                value = locList.stream().map(this::locationToString).collect(Collectors.toList());
            }
        }

        arenasSection.getConfigurationSection(arena).set(path, value);
        arenasConfig.save();
    }

    public void updateField(String arena, ConfigField path, String arg, Object value) {
        updateField(arena, String.join(".", path.toString(), arg), value);
    }

    public void updateField(String arena, ConfigField field, Object value) {
        updateField(arena, field.toString(), value);
    }

    private Location stringToLocation(String locStr) {
        String[] split = locStr.split(";");

        String worldStr = split[0];
        String xStr = split[1];
        String yStr = split[2];
        String zStr = split[3];
        String yawStr = split[4];
        String pitchStr = split[5];

        World world = Bukkit.getWorld(worldStr);
        float x = Float.parseFloat(xStr);
        float y = Float.parseFloat(yStr);
        float z = Float.parseFloat(zStr);
        float yaw = Float.parseFloat(yawStr);
        float pitch = Float.parseFloat(pitchStr);

        return new Location(world, x, y, z, yaw, pitch);
    }

    private String locationToString(Location loc) {
        String world = loc.getWorld().getName();
        String x = String.valueOf(loc.getX());
        String y = String.valueOf(loc.getY());
        String z = String.valueOf(loc.getZ());
        String yaw = String.valueOf(loc.getYaw());
        String pitch = String.valueOf(loc.getPitch());

        String[] locArray = new String[6];
        locArray[0] = world;
        locArray[1] = x;
        locArray[2] = y;
        locArray[3] = z;
        locArray[4] = yaw;
        locArray[5] = pitch;

        return String.join(";", locArray);
    }

    private String joinLocations(Location loc1, Location loc2) {
        String loc1Str = locationToString(loc1);
        String loc2Str = locationToString(loc2);

        return String.join(":", loc1Str, loc2Str);
    }

    private String[] splitLocations(String locString) {
        return locString.split(":");
    }
}
