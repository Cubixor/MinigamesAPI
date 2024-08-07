package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.XMaterial;

public class VersionUtils {

    private static boolean before13;

    public static void initialize() {
        XMaterial.matchXMaterial("BLACK_STAINED_GLASS").get().parseItem().getData();
        before13 = !XMaterial.matchXMaterial("KELP").get().isSupported();
    }

    public static boolean isBefore13() {
        return before13;
    }
}
