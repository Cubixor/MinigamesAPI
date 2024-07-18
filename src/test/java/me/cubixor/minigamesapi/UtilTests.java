package me.cubixor.minigamesapi;

import me.cubixor.minigamesapi.spigot.Utils;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

class UtilTests {

    @Test
    void sortTest() {
        LinkedHashMap<String, Integer> rankings = new LinkedHashMap<>();

        rankings.put("Bob", 850);
        rankings.put("Alice", 1000);
        rankings.put("Charlie", 1200);

        Utils.sortByValueInPlace(rankings);

        rankings.entrySet().forEach((e) -> {
            System.out.println(e.toString());
        });
    }
}
