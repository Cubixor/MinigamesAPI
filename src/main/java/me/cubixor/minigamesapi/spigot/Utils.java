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

}
