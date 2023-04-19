/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.render;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public final class Scissor {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void start(float x1, float y1, float x2, float y2) {
        if (y1 > y2) {
            float temp = y2;
            y2 = y1;
            y1 = temp;
        }
        GL11.glScissor((int)x1, (int)((float)Display.getHeight() - y2), (int)(x2 - x1), (int)(y2 - y1));
        GL11.glEnable(3089);
    }

    public static void end() {
        GL11.glDisable(3089);
    }
}

