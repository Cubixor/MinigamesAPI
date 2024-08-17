package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.XMaterial;

public class VersionUtils {

    private static boolean before13;

    public static void initialize() {
        try {
            Class.forName("com.cryptomorin.xseries.XMaterial");
            Class.forName("com.cryptomorin.xseries.XSound");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        before13 = !XMaterial.matchXMaterial("KELP").get().isSupported();
    }

    public static boolean isBefore13() {
        return before13;
    }
}
