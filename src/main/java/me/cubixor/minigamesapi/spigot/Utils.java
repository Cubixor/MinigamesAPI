package me.cubixor.minigamesapi.spigot;

import java.util.*;

public class Utils {

    private Utils() {
    }

    public static <K, V extends Comparable<V>> void sortByValueInPlace(LinkedHashMap<K, V> map) {
        if (map.isEmpty()) {
            return;  // Nothing to sort in an empty map
        }

        // Create a copy of the map entries
        List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());

        // Sort the copy of entries by value (ascending order)
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Clear the original map
        map.clear();

        // Add the sorted entries back to the original map (LinkedHashMap)
        for (Map.Entry<K, V> entry : sortedEntries) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    public static <K, V> LinkedHashMap<K, V> shuffleLinkedHashMap(LinkedHashMap<K, V> map) {
        // Convert the entries of the map into a list
        List<Map.Entry<K, V>> entries = new ArrayList<>(map.entrySet());

        // Shuffle the list
        Collections.shuffle(entries);

        // Create a new LinkedHashMap to preserve the shuffled order
        LinkedHashMap<K, V> shuffledMap = new LinkedHashMap<>();

        // Add the shuffled entries back into the LinkedHashMap
        for (Map.Entry<K, V> entry : entries) {
            shuffledMap.put(entry.getKey(), entry.getValue());
        }

        return shuffledMap;
    }

}
