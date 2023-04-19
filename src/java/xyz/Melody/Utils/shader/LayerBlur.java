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
    private Minecraft mc = Minecraft.getMinecraft();
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

    public void blurArea(float x, float y, float x1, float y1) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.lasttRed = this.tRed;
        this.lasttGreen = this.tGreen;
        this.lasttBlue = this.tBlue;
        this.lastbRed = this.bRed;
        this.lastbGreen = this.bGreen;
        this.lastbBlue = this.bBlue;
        if (this.timer.hasReached(50.0)) {
            int width = 0;
            int height = 0;
            Buffer pixelBuffer = null;
            int[] pixelValues = null;
            if (OpenGlHelper.isFramebufferEnabled()) {
                width = (int)x1;
                height = (int)y1;
            }
            int n = width * height;
            if (pixelBuffer == null || pixelBuffer.capacity() < n) {
                pixelBuffer = BufferUtils.createIntBuffer(n);
                pixelValues = new int[n];
            }
            GL11.glPixelStorei(3333, 1);
            GL11.glPixelStorei(3317, 1);
            ((IntBuffer)pixelBuffer).clear();
            GL11.glReadPixels((int)x, (int)y, width, height, 32993, 33639, (IntBuffer)pixelBuffer);
            ((IntBuffer)pixelBuffer).get(pixelValues);
            TextureUtil.processPixelValues(pixelValues, width, height);
            int wiv = (int)((double)((x + (x1 - x)) / (x1 - x)) * 1.5);
            this.colorTop = pixelValues[1 * sr.getScaleFactor() * width + height * wiv + height / 2];
            this.colorBottom = pixelValues[(int)((1.0f + (y1 - y)) * (float)sr.getScaleFactor() * (float)width + (float)(height * wiv) + (float)(height / 2))];
            Color top = ColorUtils.blend(ColorUtils.colorFromInt(this.colorTop), ColorUtils.colorFromInt(this.colorTopRight));
            Color bottom = ColorUtils.blend(ColorUtils.colorFromInt(this.colorBottom), ColorUtils.colorFromInt(this.colorBottomRight));
            this.tRed = (int)((double)this.tRed + ((double)((top.getRed() - this.tRed) / 5) + 0.1));
            this.tGreen = (int)((double)this.tGreen + ((double)((top.getGreen() - this.tGreen) / 5) + 0.1));
            this.tBlue = (int)((double)this.tBlue + ((double)((top.getBlue() - this.tBlue) / 5) + 0.1));
            this.bRed = (int)((double)this.bRed + ((double)((bottom.getRed() - this.bRed) / 5) + 0.1));
            this.bGreen = (int)((double)this.bGreen + ((double)((bottom.getGreen() - this.bGreen) / 5) + 0.1));
            this.bBlue = (int)((double)this.bBlue + ((double)((bottom.getBlue() - this.bBlue) / 5) + 0.1));
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
        int tR = this.smoothAnimation(this.tRed, this.lasttRed);
        int tG = this.smoothAnimation(this.tGreen, this.lasttGreen);
        int tB = this.smoothAnimation(this.tBlue, this.lasttBlue);
        int bR = this.smoothAnimation(this.bRed, this.lastbRed);
        int bG = this.smoothAnimation(this.bGreen, this.lastbGreen);
        int bB = this.smoothAnimation(this.bBlue, this.lastbBlue);
        Color tC = ColorUtils.lighter(new Color(tR, tG, tB, 50), 1.0);
        Color bC = ColorUtils.lighter(new Color(bR, bG, bB, 50), 1.0);
        LayerBlur.drawGradientRect(x, y, x1, y1, tC.getRGB(), bC.getRGB());
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }

    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        RenderUtil.enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        RenderUtil.glColor(bottomColor);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        RenderUtil.glColor(topColor);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        RenderUtil.disableGL2D();
    }

    private int smoothAnimation(double current, double last) {
        return (int)(current * (double)((MCA)((Object)this.mc)).getTimer().renderPartialTicks + last * (double)(1.0f - ((MCA)((Object)this.mc)).getTimer().renderPartialTicks));
    }
}

