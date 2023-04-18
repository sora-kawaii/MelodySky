/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Particles;

import org.lwjgl.opengl.GL11;

public final class RenderUtils {
    public static void connectPoints(float f, float f2, float f3, float f4) {
        GL11.glEnable(2881);
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.8f);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(1);
        GL11.glVertex2f(f, f2);
        GL11.glVertex2f(f3, f4);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glDisable(2881);
    }

    public static void drawCircle(float f, float f2, float f3, int n) {
        GL11.glEnable(2832);
        float f4 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(n & 0xFF) / 255.0f;
        GL11.glEnable(2881);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth(1.0f);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)f + Math.sin((double)i * Math.PI / 180.0) * (double)f3, (double)f2 + Math.cos((double)i * Math.PI / 180.0) * (double)f3);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glEnable(2832);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}

