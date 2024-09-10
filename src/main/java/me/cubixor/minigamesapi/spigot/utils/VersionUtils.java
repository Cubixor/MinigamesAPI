package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.reflection.XReflection;

public class VersionUtils {

    private static boolean before13;

    public static void initialize() {
        XMaterial.matchXMaterial("BLACK_STAINED_GLASS").get().parseItem().getData();
        XSound.matchXSound("CLICK").get().parseSound();

        before13 = !XReflection.supports(13);
    }

    public static boolean isBefore13() {
        return before13;
    }
}
