/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import net.minecraft.util.ResourceLocation;
import xyz.Melody.Utils.render.RenderUtil;

public final class Logo {
    public static void M(float f, float f2, float f3, float f4) {
        RenderUtil.drawImage(Logo.getImage("M.png"), f, f2, f3, f4);
    }

    public static void e(float f, float f2, float f3, float f4) {
        RenderUtil.drawImage(Logo.getImage("e.png"), f, f2, f3, f4);
    }

    public static void l(float f, float f2, float f3, float f4) {
        RenderUtil.drawImage(Logo.getImage("l.png"), f, f2, f3, f4);
    }

    public static void o(float f, float f2, float f3, float f4) {
        RenderUtil.drawImage(Logo.getImage("o.png"), f, f2, f3, f4);
    }

    public static void d(float f, float f2, float f3, float f4) {
        RenderUtil.drawImage(Logo.getImage("d.png"), f, f2, f3, f4);
    }

    public static void y(float f, float f2, float f3, float f4) {
        RenderUtil.drawImage(Logo.getImage("y.png"), f, f2, f3, f4);
    }

    private static ResourceLocation getImage(String string) {
        return new ResourceLocation("Melody/Logo/" + string);
    }
}

