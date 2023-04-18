/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.ClientLib;

import java.util.ArrayList;

public final class UISettings {
    public static boolean chatTextShadow;
    public static ArrayList<String> settings;
    public static boolean chatBackground;
    public static boolean scoreboardBackground;

    static {
        settings = new ArrayList();
        chatTextShadow = true;
        chatBackground = true;
        scoreboardBackground = true;
    }

    public static void init() {
        settings.add("chatTextShadow");
        settings.add("chatBackground");
        settings.add("scoreboardBackground");
    }
}

