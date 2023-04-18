/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.other;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import net.minecraft.client.Minecraft;

public final class StringUtil {
    public static Minecraft mc = Minecraft.func_71410_x();
    public static Socket socket;
    public static PrintWriter pw;
    public static InputStream in;

    public static String removeFormatting(String string) {
        return string.replaceAll("\u00a7[0-9a-fk-or]", "");
    }
}

