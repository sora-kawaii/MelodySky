/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.shader;

import java.awt.Color;
import java.nio.Buffer;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.ColorUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.injection.mixins.client.MCA;

public final class LayerBlur {
    private Minecraft mc = Minecraft.func_71410_x();
    private TimerUtil timer = new TimerUtil();
    private int colorTop;
    private int colorTopRight;
    private int colorBottom;
    private int colorBottomRight;
    private int colorNotification = 0;
    private int colorNotificationBottom = 0;
    private int tRed;
    private int tGreen;
    private int tBlue;
    private int bRed;
    private int bGreen;
    private int bBlue;
    public int lasttRed;
    public int lasttGreen;
    public int lasttBlue;
    public int lastbRed;
    public int lastbGreen;
    public int lastbBlue;

    public void blurArea(float f, float f2, float f3, float f4) {
        Color color;
        Color color2;
        int n;
        int n2;
        int n3;
        int n4;
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        this.lasttRed = this.tRed;
        this.lasttGreen = this.tGreen;
        this.lasttBlue = this.tBlue;
        this.lastbRed = this.bRed;
        this.lastbGreen = this.bGreen;
        this.lastbBlue = this.bBlue;
        if (this.timer.hasReached(50.0)) {
            n4 = 0;
            n3 = 0;
            Buffer buffer = null;
            int[] nArray = null;
            if (OpenGlHelper.func_148822_b()) {
                n4 = (int)f3;
                n3 = (int)f4;
            }
            n2 = n4 * n3;
            if (buffer == null || buffer.capacity() < n2) {
                buffer = BufferUtils.createIntBuffer(n2);
                nArray = new int[n2];
            }
            GL11.glPixelStorei(3333, 1);
            GL11.glPixelStorei(3317, 1);
            ((IntBuffer)buffer).clear();
            GL11.glReadPixels((int)f, (int)f2, n4, n3, 32993, 33639, (IntBuffer)buffer);
            ((IntBuffer)buffer).get(nArray);
            TextureUtil.func_147953_a((int[])nArray, (int)n4, (int)n3);
            n = (int)((double)((f + (f3 - f)) / (f3 - f)) * 1.5);
            this.colorTop = nArray[1 * scaledResolution.func_78325_e() * n4 + n3 * n + n3 / 2];
            this.colorBottom = nArray[(int)((1.0f + (f4 - f2)) * (float)scaledResolution.func_78325_e() * (float)n4 + (float)(n3 * n) + (float)(n3 / 2))];
            color2 = ColorUtils.blend(ColorUtils.colorFromInt(this.colorTop), ColorUtils.colorFromInt(this.colorTopRight));
            color = ColorUtils.blend(ColorUtils.colorFromInt(this.colorBottom), ColorUtils.colorFromInt(this.colorBottomRight));
            this.tRed = (int)((double)this.tRed + ((double)((color2.getRed() - this.tRed) / 5) + 0.1));
            this.tGreen = (int)((double)this.tGreen + ((double)((color2.getGreen() - this.tGreen) / 5) + 0.1));
            this.tBlue = (int)((double)this.tBlue + ((double)((color2.getBlue() - this.tBlue) / 5) + 0.1));
            this.bRed = (int)((double)this.bRed + ((double)((color.getRed() - this.bRed) / 5) + 0.1));
            this.bGreen = (int)((double)this.bGreen + ((double)((color.getGreen() - this.bGreen) / 5) + 0.1));
            this.bBlue = (int)((double)this.bBlue + ((double)((color.getBlue() - this.bBlue) / 5) + 0.1));
            this.tRed = Math.min(this.tRed, 255);
            this.tGreen = Math.min(this.tGreen, 255);
            this.tBlue = Math.min(this.tBlue, 255);
            this.tRed = Math.max(this.tRed, 0);
            this.tGreen = Math.max(this.tGreen, 0);
            this.tBlue = Math.max(this.tBlue, 0);
            this.bRed = Math.min(this.bRed, 255);
            this.bGreen = Math.min(this.bGreen, 255);
            this.bBlue = Math.min(this.bBlue, 255);
            this.bRed = Math.max(this.bRed, 0);
            this.bGreen = Math.max(this.bGreen, 0);
            this.bBlue = Math.max(this.bBlue, 0);
            this.timer.reset();
        }
        n4 = this.smoothAnimation(this.tRed, this.lasttRed);
        n3 = this.smoothAnimation(this.tGreen, this.lasttGreen);
        int n5 = this.smoothAnimation(this.tBlue, this.lasttBlue);
        int n6 = this.smoothAnimation(this.bRed, this.lastbRed);
        n2 = this.smoothAnimation(this.bGreen, this.lastbGreen);
        n = this.smoothAnimation(this.bBlue, this.lastbBlue);
        color2 = ColorUtils.lighter(new Color(n4, n3, n5, 50), 1.0);
        color = ColorUtils.lighter(new Color(n6, n2, n, 50), 1.0);
        LayerBlur.drawGradientRect(f, f2, f3, f4, color2.getRGB(), color.getRGB());
        GlStateManager.func_179121_F();
        GlStateManager.func_179117_G();
    }

    public static void drawGradientRect(float f, float f2, float f3, float f4, int n, int n2) {
        RenderUtil.enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        RenderUtil.glColor(n2);
        GL11.glVertex2f(f, f4);
        GL11.glVertex2f(f3, f4);
        RenderUtil.glColor(n);
        GL11.glVertex2f(f3, f2);
        GL11.glVertex2f(f, f2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        RenderUtil.disableGL2D();
    }

    private int smoothAnimation(double d, double d2) {
        return (int)(d * (double)((MCA)((Object)this.mc)).getTimer().field_74281_c + d2 * (double)(1.0f - ((MCA)((Object)this.mc)).getTimer().field_74281_c));
    }
}

