/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import net.minecraft.util.ResourceLocation;
import xyz.Melody.Utils.render.RenderUtil;

public final class Logo {
    public static void M(float x, float y, float scale, float scale1) {
        RenderUtil.drawImage(Logo.getImage("M.png"), x, y, scale, scale1);
    }

    public static void e(float x, float y, float scale, float scale1) {
        RenderUtil.drawImage(Logo.getImage("e.png"), x, y, scale, scale1);
    }

    public static void l(float x, float y, float scale, float scale1) {
        RenderUtil.drawImage(Logo.getImage("l.png"), x, y, scale, scale1);
    }

    public static void o(float x, float y, float scale, float scale1) {
        RenderUtil.drawImage(Logo.getImage("o.png"), x, y, scale, scale1);
    }

    public static void d(float x, float y, float scale, float scale1) {
        RenderUtil.drawImage(Logo.getImage("d.png"), x, y, scale, scale1);
    }

    public static void y(float x, float y, float scale, float scale1) {
        RenderUtil.drawImage(Logo.getImage("y.png"), x, y, scale, scale1);
    }

    private static ResourceLocation getImage(String name) {
        return new ResourceLocation("Melody/Logo/" + name);
    }
}

