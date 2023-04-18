/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.ClickNew;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import xyz.Melody.GUI.ClickNew.Box;

public enum ClickGuiRenderUtil {
    INSTANCE;

    public static Minecraft mc;
    public static float delta;
    private static final ClickGuiRenderUtil[] $VALUES;

    public static boolean isHovering(int n, int n2, float f, float f2, float f3, float f4) {
        boolean bl = (float)n > f && (float)n < f3 && (float)n2 > f2 && (float)n2 < f4;
        return bl;
    }

    public static int width() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78326_a();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78328_b();
    }

    public static void enableGL3D(float f) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(f);
    }

    public static void drawArc(float f, float f2, double d, int n, int n2, double d2, int n3) {
        d *= 2.0;
        f *= 2.0f;
        f2 *= 2.0f;
        float f3 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(n & 0xFF) / 255.0f;
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glLineWidth(n3);
        GL11.glEnable(2848);
        GL11.glColor4f(f4, f5, f6, f3);
        GL11.glBegin(3);
        int n4 = n2;
        while ((double)n4 <= d2) {
            GL11.glVertex2d((double)f + Math.sin((double)n4 * Math.PI / 180.0) * d, (double)f2 + Math.cos((double)n4 * Math.PI / 180.0) * d);
            ++n4;
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static int rainbow(int n) {
        double d = Math.ceil((double)(System.currentTimeMillis() + (long)n) / 10.0);
        return Color.getHSBColor((float)((d %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static int rainbow(int n, float f) {
        double d = Math.ceil((double)(System.currentTimeMillis() + (long)n) / (double)f);
        return Color.getHSBColor((float)((d %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawBorderedRect(float f, float f2, float f3, float f4, float f5, int n) {
        R2DUtils.enableGL2D();
        ClickGuiRenderUtil.glColor(n);
        R2DUtils.drawRect(f + f5, f2, f3 - f5, f2 + f5);
        R2DUtils.drawRect(f, f2, f + f5, f4);
        R2DUtils.drawRect(f3 - f5, f2, f3, f4);
        R2DUtils.drawRect(f + f5, f4 - f5, f3 - f5, f4);
        R2DUtils.disableGL2D();
    }

    public static void drawCircle(double d, double d2, double d3, int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        boolean bl = GL11.glIsEnabled(3042);
        boolean bl2 = GL11.glIsEnabled(2848);
        boolean bl3 = GL11.glIsEnabled(3553);
        if (!bl) {
            GL11.glEnable(3042);
        }
        if (!bl2) {
            GL11.glEnable(2848);
        }
        if (bl3) {
            GL11.glDisable(3553);
        }
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(d + Math.sin((double)i * 3.141526 / 180.0) * d3, d2 + Math.cos((double)i * 3.141526 / 180.0) * d3);
        }
        GL11.glEnd();
        if (bl3) {
            GL11.glEnable(3553);
        }
        if (!bl2) {
            GL11.glDisable(2848);
        }
        if (!bl) {
            GL11.glDisable(3042);
        }
    }

    public static void drawCircle2(double d, double d2, double d3, int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.func_179092_a((int)516, (float)0.001f);
        GlStateManager.func_179131_c((float)f2, (float)f3, (float)f4, (float)f);
        GlStateManager.func_179141_d();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        Tessellator tessellator = Tessellator.func_178181_a();
        for (double d4 = 0.0; d4 < 360.0; d4 += 1.0) {
            double d5 = Math.sin(d4 * Math.PI / 180.0) * d3;
            double d6 = Math.cos(d4 * Math.PI / 180.0) * d3;
            GL11.glVertex2d((double)f3 + d, (double)f4 + d2);
        }
        GlStateManager.func_179084_k();
        GlStateManager.func_179118_c();
        GlStateManager.func_179098_w();
        GlStateManager.func_179092_a((int)516, (float)0.1f);
    }

    public static void drawFilledCircle(double d, double d2, double d3, int n, int n2) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(9);
        if (n2 == 1) {
            GL11.glVertex2d(d, d2);
            for (int i = 0; i <= 90; ++i) {
                double d4 = Math.sin((double)i * 3.141526 / 180.0) * d3;
                double d5 = Math.cos((double)i * 3.141526 / 180.0) * d3;
                GL11.glVertex2d(d - d4, d2 - d5);
            }
        } else if (n2 == 2) {
            GL11.glVertex2d(d, d2);
            for (int i = 90; i <= 180; ++i) {
                double d6 = Math.sin((double)i * 3.141526 / 180.0) * d3;
                double d7 = Math.cos((double)i * 3.141526 / 180.0) * d3;
                GL11.glVertex2d(d - d6, d2 - d7);
            }
        } else if (n2 == 3) {
            GL11.glVertex2d(d, d2);
            for (int i = 270; i <= 360; ++i) {
                double d8 = Math.sin((double)i * 3.141526 / 180.0) * d3;
                double d9 = Math.cos((double)i * 3.141526 / 180.0) * d3;
                GL11.glVertex2d(d - d8, d2 - d9);
            }
        } else if (n2 == 4) {
            GL11.glVertex2d(d, d2);
            for (int i = 180; i <= 270; ++i) {
                double d10 = Math.sin((double)i * 3.141526 / 180.0) * d3;
                double d11 = Math.cos((double)i * 3.141526 / 180.0) * d3;
                GL11.glVertex2d(d - d10, d2 - d11);
            }
        } else {
            for (int i = 0; i <= 360; ++i) {
                double d12 = Math.sin((double)i * 3.141526 / 180.0) * d3;
                double d13 = Math.cos((double)i * 3.141526 / 180.0) * d3;
                GL11.glVertex2f((float)(d - d12), (float)(d2 - d13));
            }
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void drawFullCircle(int n, int n2, double d, int n3, float f, int n4, int n5) {
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        d *= 2.0;
        n *= 2;
        n2 *= 2;
        float f2 = (float)(n5 >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n5 >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n5 >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n5 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glLineWidth(f);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(3);
        for (int i = n3 - n4; i <= n3; ++i) {
            double d2 = Math.sin((double)i * Math.PI / 180.0) * d;
            double d3 = Math.cos((double)i * Math.PI / 180.0) * d;
            GL11.glVertex2d((double)n + d2, (double)n2 + d3);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

    public static void drawBox(Box box) {
        if (box == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glEnd();
    }

    public static void glColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
    }

    public static Color rainbowEffect(int n) {
        float f = (float)(System.nanoTime() + (long)n) / 2.0E10f % 1.0f;
        Color color = new Color((int)Long.parseLong(Integer.toHexString(Color.HSBtoRGB(f, 1.0f, 1.0f)), 16));
        return new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void drawFullscreenImage(ResourceLocation resourceLocation) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3008);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(resourceLocation);
        Gui.func_146110_a((int)0, (int)0, (float)0.0f, (float)0.0f, (int)scaledResolution.func_78326_a(), (int)scaledResolution.func_78328_b(), (float)scaledResolution.func_78326_a(), (float)scaledResolution.func_78328_b());
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static double getAnimationState(double d, double d2, double d3) {
        float f = (float)(0.01 * d3);
        d = d < d2 ? (d + (double)f < d2 ? (d += (double)f) : d2) : (d - (double)f > d2 ? (d -= (double)f) : d2);
        return d;
    }

    public static String getShaderCode(InputStreamReader inputStreamReader) {
        String string = "";
        try {
            String string2;
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while ((string2 = bufferedReader.readLine()) != null) {
                string = ((StringBuilder)((Object)string)).toString() + string2 + "\n";
            }
            bufferedReader.close();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            System.exit(-1);
        }
        return string.toString();
    }

    public static void drawImage(ResourceLocation resourceLocation, int n, int n2, int n3, int n4) {
        ClickGuiRenderUtil.drawImage(resourceLocation, n, n2, n3, n4, 1.0f);
    }

    public static void drawImage(ResourceLocation resourceLocation, int n, int n2, int n3, int n4, float f) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(resourceLocation);
        Gui.func_146110_a((int)n, (int)n2, (float)0.0f, (float)0.0f, (int)n3, (int)n4, (float)n3, (float)n4);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawOutlinedRect(int n, int n2, int n3, int n4, int n5, Color color, Color color2) {
        ClickGuiRenderUtil.drawRect(n, n2, n3, n4, color2.getRGB());
        ClickGuiRenderUtil.drawRect(n, n2, n3, n2 + n5, color.getRGB());
        ClickGuiRenderUtil.drawRect(n, n4 - n5, n3, n4, color.getRGB());
        ClickGuiRenderUtil.drawRect(n, n2 + n5, n + n5, n4 - n5, color.getRGB());
        ClickGuiRenderUtil.drawRect(n3 - n5, n2 + n5, n3, n4 - n5, color.getRGB());
    }

    public static void drawImage(ResourceLocation resourceLocation, int n, int n2, int n3, int n4, Color color) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getRed() / 255.0f, 1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(resourceLocation);
        Gui.func_146110_a((int)n, (int)n2, (float)0.0f, (float)0.0f, (int)n3, (int)n4, (float)n3, (float)n4);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void doGlScissor(int n, int n2, int n3, int n4) {
        Minecraft minecraft = Minecraft.func_71410_x();
        int n5 = 1;
        int n6 = minecraft.field_71474_y.field_74335_Z;
        if (n6 == 0) {
            n6 = 1000;
        }
        while (n5 < n6 && minecraft.field_71443_c / (n5 + 1) >= 320 && minecraft.field_71440_d / (n5 + 1) >= 240) {
            ++n5;
        }
        GL11.glScissor(n * n5, minecraft.field_71440_d - (n2 + n4) * n5, n3 * n5, n4 * n5);
    }

    public static void drawblock(double d, double d2, double d3, int n, int n2, float f) {
        float f2 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n & 0xFF) / 255.0f;
        float f6 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f8 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f9 = (float)(n2 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(f3, f4, f5, f2);
        ClickGuiRenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 1.0));
        GL11.glLineWidth(f);
        GL11.glColor4f(f7, f8, f9, f6);
        ClickGuiRenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawRect(float f, float f2, float f3, float f4, int n) {
        float f5;
        if (f < f3) {
            f5 = f;
            f = f3;
            f3 = f5;
        }
        if (f2 < f4) {
            f5 = f2;
            f2 = f4;
            f4 = f5;
        }
        f5 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        GlStateManager.func_179117_G();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f6, (float)f7, (float)f8, (float)f5);
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(f, f4, 0.0).func_181675_d();
        worldRenderer.func_181662_b(f3, f4, 0.0).func_181675_d();
        worldRenderer.func_181662_b(f3, f2, 0.0).func_181675_d();
        worldRenderer.func_181662_b(f, f2, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179117_G();
    }

    public static void color(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
    }

    public static int createShader(String string, int n) throws Exception {
        int n2 = 0;
        try {
            n2 = ARBShaderObjects.glCreateShaderObjectARB(n);
            if (n2 == 0) {
                return 0;
            }
        }
        catch (Exception exception) {
            ARBShaderObjects.glDeleteObjectARB(n2);
            throw exception;
        }
        ARBShaderObjects.glShaderSourceARB(n2, string);
        ARBShaderObjects.glCompileShaderARB(n2);
        if (ARBShaderObjects.glGetObjectParameteriARB(n2, 35713) == 0) {
            throw new RuntimeException("Error creating shader:");
        }
        return n2;
    }

    public static void drawBorderRect(double d, double d2, double d3, double d4, int n, double d5) {
        ClickGuiRenderUtil.drawHLine(d, d2, d3, d2, (float)d5, n);
        ClickGuiRenderUtil.drawHLine(d3, d2, d3, d4, (float)d5, n);
        ClickGuiRenderUtil.drawHLine(d, d4, d3, d4, (float)d5, n);
        ClickGuiRenderUtil.drawHLine(d, d4, d, d2, (float)d5, n);
    }

    public static void drawHLine(double d, double d2, double d3, double d4, float f, int n) {
        float f2 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f3, (float)f4, (float)f5, (float)f2);
        GL11.glPushMatrix();
        GL11.glLineWidth(f);
        GL11.glBegin(3);
        GL11.glVertex2d(d, d2);
        GL11.glVertex2d(d3, d4);
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
        GL11.glPopMatrix();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawCircle(int n, int n2, float f, int n3) {
        float f2 = (float)(n3 >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n3 >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n3 >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n3 & 0xFF) / 255.0f;
        boolean bl = GL11.glIsEnabled(3042);
        boolean bl2 = GL11.glIsEnabled(2848);
        boolean bl3 = GL11.glIsEnabled(3553);
        if (!bl) {
            GL11.glEnable(3042);
        }
        if (!bl2) {
            GL11.glEnable(2848);
        }
        if (bl3) {
            GL11.glDisable(3553);
        }
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)n + Math.sin((double)i * 3.141526 / 180.0) * (double)f, (double)n2 + Math.cos((double)i * 3.141526 / 180.0) * (double)f);
        }
        GL11.glEnd();
        if (bl3) {
            GL11.glEnable(3553);
        }
        if (!bl2) {
            GL11.glDisable(2848);
        }
        if (!bl) {
            GL11.glDisable(3042);
        }
    }

    public static void renderOne(float f) {
        ClickGuiRenderUtil.checkSetupFBO();
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(f);
        GL11.glEnable(2848);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void renderTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }

    public static void renderThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void renderFour() {
        ClickGuiRenderUtil.setColor(new Color(255, 255, 255));
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)240.0f, (float)240.0f);
    }

    public static void renderFive() {
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glEnable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }

    public static void setColor(Color color) {
        GL11.glColor4d((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void checkSetupFBO() {
        Framebuffer framebuffer = Minecraft.func_71410_x().func_147110_a();
        if (framebuffer != null && framebuffer.field_147624_h > -1) {
            ClickGuiRenderUtil.setupFBO(framebuffer);
            framebuffer.field_147624_h = -1;
        }
    }

    public static void setupFBO(Framebuffer framebuffer) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.field_147624_h);
        int n = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, n);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.func_71410_x().field_71443_c, Minecraft.func_71410_x().field_71440_d);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, n);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, n);
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawBox(AxisAlignedBB axisAlignedBB) {
        if (axisAlignedBB == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glEnd();
    }

    public static void drawCompleteBox(AxisAlignedBB axisAlignedBB, float f, int n, int n2) {
        GL11.glLineWidth(f);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        ClickGuiRenderUtil.glColor(n);
        ClickGuiRenderUtil.drawBox(axisAlignedBB);
        ClickGuiRenderUtil.glColor(n2);
        ClickGuiRenderUtil.drawOutlinedBox(axisAlignedBB);
        ClickGuiRenderUtil.drawCrosses(axisAlignedBB);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
    }

    public static void drawCrosses(AxisAlignedBB axisAlignedBB, float f, int n) {
        GL11.glLineWidth(f);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        ClickGuiRenderUtil.glColor(n);
        ClickGuiRenderUtil.drawCrosses(axisAlignedBB);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
    }

    public static void drawOutlineBox(AxisAlignedBB axisAlignedBB, float f, int n) {
        GL11.glLineWidth(f);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        ClickGuiRenderUtil.glColor(n);
        ClickGuiRenderUtil.drawOutlinedBox(axisAlignedBB);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
    }

    public static void drawOutlinedBox(AxisAlignedBB axisAlignedBB) {
        if (axisAlignedBB == null) {
            return;
        }
        GL11.glBegin(3);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glEnd();
    }

    public static void drawCrosses(AxisAlignedBB axisAlignedBB) {
        if (axisAlignedBB == null) {
            return;
        }
        GL11.glBegin(1);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        GL11.glVertex3d(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        GL11.glEnd();
    }

    public static void drawBoundingBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawOutlinedBlockESP(double d, double d2, double d3, float f, float f2, float f3, float f4, float f5) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(f5);
        GL11.glColor4f(f, f2, f3, f4);
        ClickGuiRenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawBlockESP(double d, double d2, double d3, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(f, f2, f3, f4);
        ClickGuiRenderUtil.drawBoundingBox(new AxisAlignedBB(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 1.0));
        GL11.glLineWidth(f9);
        GL11.glColor4f(f5, f6, f7, f8);
        ClickGuiRenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidBlockESP(double d, double d2, double d3, float f, float f2, float f3, float f4) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(f, f2, f3, f4);
        ClickGuiRenderUtil.drawBoundingBox(new AxisAlignedBB(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 1.0));
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedEntityESP(double d, double d2, double d3, double d4, double d5, float f, float f2, float f3, float f4) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(f, f2, f3, f4);
        ClickGuiRenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(d - d4, d2, d3 - d4, d + d4, d2 + d5, d3 + d4));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidEntityESP(double d, double d2, double d3, double d4, double d5, float f, float f2, float f3, float f4) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(f, f2, f3, f4);
        ClickGuiRenderUtil.drawBoundingBox(new AxisAlignedBB(d - d4, d2, d3 - d4, d + d4, d2 + d5, d3 + d4));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(double d, double d2, double d3, double d4, double d5, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(f, f2, f3, f4);
        ClickGuiRenderUtil.drawBoundingBox(new AxisAlignedBB(d - d4, d2, d3 - d4, d + d4, d2 + d5, d3 + d4));
        GL11.glLineWidth(f9);
        GL11.glColor4f(f5, f6, f7, f8);
        ClickGuiRenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(d - d4, d2, d3 - d4, d + d4, d2 + d5, d3 + d4));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(double d, double d2, double d3, double d4, double d5, float f, float f2, float f3, float f4) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(f, f2, f3, f4);
        ClickGuiRenderUtil.drawBoundingBox(new AxisAlignedBB(d - d4, d2, d3 - d4, d + d4, d2 + d5, d3 + d4));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    private static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void drawFilledBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f);
        tessellator.func_78381_a();
    }

    public static void drawRoundedRect(float f, float f2, float f3, float f4, int n, int n2) {
        ClickGuiRenderUtil.drawRect(f + 0.5f, f2, f3 - 0.5f, f2 + 0.5f, n2);
        ClickGuiRenderUtil.drawRect(f + 0.5f, f4 - 0.5f, f3 - 0.5f, f4, n2);
        ClickGuiRenderUtil.drawRect(f, f2 + 0.5f, f3, f4 - 0.5f, n2);
    }

    public static void circle(float f, float f2, float f3, int n) {
        ClickGuiRenderUtil.arc(f, f2, 0.0f, 360.0f, f3, n);
    }

    public static void circle(float f, float f2, float f3, Color color) {
        ClickGuiRenderUtil.arc(f, f2, 0.0f, 360.0f, f3, color);
    }

    public static void arc(float f, float f2, float f3, float f4, float f5, int n) {
        ClickGuiRenderUtil.arcEllipse(f, f2, f3, f4, f5, f5, n);
    }

    public static void arc(float f, float f2, float f3, float f4, float f5, Color color) {
        ClickGuiRenderUtil.arcEllipse(f, f2, f3, f4, f5, f5, color);
    }

    public static void arcEllipse(float f, float f2, float f3, float f4, float f5, float f6, int n) {
        float f7;
        float f8;
        float f9;
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float f10 = 0.0f;
        if (f3 > f4) {
            f10 = f4;
            f4 = f3;
            f3 = f10;
        }
        float f11 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f12 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f13 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f14 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f12, (float)f13, (float)f14, (float)f11);
        if (f11 > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (f9 = f4; f9 >= f3; f9 -= 4.0f) {
                f8 = (float)Math.cos((double)f9 * Math.PI / 180.0) * f5 * 1.001f;
                f7 = (float)Math.sin((double)f9 * Math.PI / 180.0) * f6 * 1.001f;
                GL11.glVertex2f(f + f8, f2 + f7);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (f9 = f4; f9 >= f3; f9 -= 4.0f) {
            f8 = (float)Math.cos((double)f9 * Math.PI / 180.0) * f5;
            f7 = (float)Math.sin((double)f9 * Math.PI / 180.0) * f6;
            GL11.glVertex2f(f + f8, f2 + f7);
        }
        GL11.glEnd();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void arcEllipse(float f, float f2, float f3, float f4, float f5, float f6, Color color) {
        float f7;
        float f8;
        float f9;
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float f10 = 0.0f;
        if (f3 > f4) {
            f10 = f4;
            f4 = f3;
            f3 = f10;
        }
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        if ((float)color.getAlpha() > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (f9 = f4; f9 >= f3; f9 -= 4.0f) {
                f8 = (float)Math.cos((double)f9 * Math.PI / 180.0) * f5 * 1.001f;
                f7 = (float)Math.sin((double)f9 * Math.PI / 180.0) * f6 * 1.001f;
                GL11.glVertex2f(f + f8, f2 + f7);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (f9 = f4; f9 >= f3; f9 -= 4.0f) {
            f8 = (float)Math.cos((double)f9 * Math.PI / 180.0) * f5;
            f7 = (float)Math.sin((double)f9 * Math.PI / 180.0) * f6;
            GL11.glVertex2f(f + f8, f2 + f7);
        }
        GL11.glEnd();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawGradientSideways(double d, double d2, double d3, double d4, int n, int n2) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        float f5 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(d, d2);
        GL11.glVertex2d(d, d4);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(d3, d4);
        GL11.glVertex2d(d3, d2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void rectangleBordered(double d, double d2, double d3, double d4, double d5, int n, int n2) {
        ClickGuiRenderUtil.rectangle(d + d5, d2 + d5, d3 - d5, d4 - d5, n);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ClickGuiRenderUtil.rectangle(d + d5, d2, d3 - d5, d2 + d5, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ClickGuiRenderUtil.rectangle(d, d2, d + d5, d4, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ClickGuiRenderUtil.rectangle(d3 - d5, d2, d3, d4, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ClickGuiRenderUtil.rectangle(d + d5, d4 - d5, d3 - d5, d4, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void rectangle(double d, double d2, double d3, double d4, int n) {
        double d5;
        if (d < d3) {
            d5 = d;
            d = d3;
            d3 = d5;
        }
        if (d2 < d4) {
            d5 = d2;
            d2 = d4;
            d4 = d5;
        }
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f2, (float)f3, (float)f4, (float)f);
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(d, d4, 0.0).func_181675_d();
        worldRenderer.func_181662_b(d3, d4, 0.0).func_181675_d();
        worldRenderer.func_181662_b(d3, d2, 0.0).func_181675_d();
        worldRenderer.func_181662_b(d, d2, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawBorderedRect(double d, double d2, double d3, double d4, float f, int n, int n2) {
        ClickGuiRenderUtil.drawRect(d, d2, d3, d4, n2);
        float f2 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glLineWidth(f);
        GL11.glBegin(1);
        GL11.glVertex2d(d, d2);
        GL11.glVertex2d(d, d4);
        GL11.glVertex2d(d3, d4);
        GL11.glVertex2d(d3, d2);
        GL11.glVertex2d(d, d2);
        GL11.glVertex2d(d3, d2);
        GL11.glVertex2d(d, d4);
        GL11.glVertex2d(d3, d4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawRect(double d, double d2, double d3, double d4, int n) {
        int n2;
        if (d < d3) {
            n2 = (int)d;
            d = d3;
            d3 = n2;
        }
        if (d2 < d4) {
            n2 = (int)d2;
            d2 = d4;
            d4 = n2;
        }
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f2, (float)f3, (float)f4, (float)f);
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(d, d4, 0.0).func_181675_d();
        worldRenderer.func_181662_b(d3, d4, 0.0).func_181675_d();
        worldRenderer.func_181662_b(d3, d2, 0.0).func_181675_d();
        worldRenderer.func_181662_b(d, d2, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static Color blend(Color color, Color color2, double d) {
        float f = (float)d;
        float f2 = 1.0f - f;
        float[] fArray = new float[3];
        float[] fArray2 = new float[3];
        color.getColorComponents(fArray);
        color2.getColorComponents(fArray2);
        Color color3 = new Color(fArray[0] * f + fArray2[0] * f2, fArray[1] * f + fArray2[1] * f2, fArray[2] * f + fArray2[2] * f2);
        return color3;
    }

    public static void drawEntityOnScreen(int n, int n2, int n3, float f, float f2, EntityLivingBase entityLivingBase) {
        GlStateManager.func_179142_g();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)n, (float)n2, (float)40.0f);
        GlStateManager.func_179152_a((float)(-n3), (float)n3, (float)n3);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float f3 = entityLivingBase.field_70761_aq;
        float f4 = entityLivingBase.field_70177_z;
        float f5 = entityLivingBase.field_70125_A;
        float f6 = entityLivingBase.field_70758_at;
        float f7 = entityLivingBase.field_70759_as;
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(-((float)Math.atan(f2 / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        entityLivingBase.field_70761_aq = (float)Math.atan(f / 40.0f) * -14.0f;
        entityLivingBase.field_70177_z = (float)Math.atan(f / 40.0f) * -14.0f;
        entityLivingBase.field_70125_A = -((float)Math.atan(f2 / 40.0f)) * 15.0f;
        entityLivingBase.field_70759_as = entityLivingBase.field_70177_z;
        entityLivingBase.field_70758_at = entityLivingBase.field_70177_z;
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
        renderManager.func_178631_a(180.0f);
        renderManager.func_178633_a(false);
        renderManager.func_147940_a(entityLivingBase, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        renderManager.func_178633_a(true);
        entityLivingBase.field_70761_aq = f3;
        entityLivingBase.field_70177_z = f4;
        entityLivingBase.field_70125_A = f5;
        entityLivingBase.field_70758_at = f6;
        entityLivingBase.field_70759_as = f7;
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
    }

    public static void drawRoundRect(double d, double d2, double d3, double d4, int n) {
        ClickGuiRenderUtil.drawRect(d + 1.0, d2, d3 - 1.0, d4, n);
        ClickGuiRenderUtil.drawRect(d, d2 + 1.0, d + 1.0, d4 - 1.0, n);
        ClickGuiRenderUtil.drawRect(d + 1.0, d2 + 1.0, d + 0.5, d2 + 0.5, n);
        ClickGuiRenderUtil.drawRect(d + 1.0, d2 + 1.0, d + 0.5, d2 + 0.5, n);
        ClickGuiRenderUtil.drawRect(d3 - 1.0, d2 + 1.0, d3 - 0.5, d2 + 0.5, n);
        ClickGuiRenderUtil.drawRect(d3 - 1.0, d2 + 1.0, d3, d4 - 1.0, n);
        ClickGuiRenderUtil.drawRect(d + 1.0, d4 - 1.0, d + 0.5, d4 - 0.5, n);
        ClickGuiRenderUtil.drawRect(d3 - 1.0, d4 - 1.0, d3 - 0.5, d4 - 0.5, n);
    }

    public static void rectTexture(float f, float f2, float f3, float f4, Texture texture, int n) {
        if (texture == null) {
            return;
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        f = Math.round(f);
        f3 = Math.round(f3);
        f2 = Math.round(f2);
        f4 = Math.round(f4);
        float f5 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f6, (float)f7, (float)f8, (float)f5);
        GL11.glEnable(3042);
        GL11.glEnable(3553);
        texture.bind();
        float f9 = f3 / (float)texture.getTextureWidth() / (f3 / (float)texture.getImageWidth());
        float f10 = f4 / (float)texture.getTextureHeight() / (f4 / (float)texture.getImageHeight());
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(f, f2);
        GL11.glTexCoord2f(0.0f, f10);
        GL11.glVertex2f(f, f2 + f4);
        GL11.glTexCoord2f(f9, f10);
        GL11.glVertex2f(f + f3, f2 + f4);
        GL11.glTexCoord2f(f9, 0.0f);
        GL11.glVertex2f(f + f3, f2);
        GL11.glEnd();
        GL11.glDisable(3553);
        GL11.glDisable(3042);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public static void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }

    public static void drawRainbowRect(double d, double d2, double d3, double d4) {
        float f = (float)(System.currentTimeMillis() % 3000L) / 3000.0f;
        double d5 = d3 - d;
        int n = 0;
        while ((double)n <= d5) {
            Color color = new Color(Color.HSBtoRGB((float)((double)f / d5 / 2.0 + Math.sin((double)n / d5 * 0.6)) % 1.0f, 0.5f, 1.0f));
            ClickGuiRenderUtil.drawRect(d + (double)n, d2, d + (double)n + 1.0, d4, color.getRGB());
            ++n;
        }
    }

    static {
        $VALUES = new ClickGuiRenderUtil[]{INSTANCE};
        mc = Minecraft.func_71410_x();
    }

    public static class R2DUtils {
        public static void enableGL2D() {
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glDepthMask(true);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glHint(3155, 4354);
        }

        public static void disableGL2D() {
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glHint(3154, 4352);
            GL11.glHint(3155, 4352);
        }

        public static void drawRoundedRect(float f, float f2, float f3, float f4, int n, int n2) {
            R2DUtils.enableGL2D();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            R2DUtils.drawVLine(f *= 2.0f, (f2 *= 2.0f) + 1.0f, (f4 *= 2.0f) - 2.0f, n);
            R2DUtils.drawVLine((f3 *= 2.0f) - 1.0f, f2 + 1.0f, f4 - 2.0f, n);
            R2DUtils.drawHLine(f + 2.0f, f3 - 3.0f, f2, n);
            R2DUtils.drawHLine(f + 2.0f, f3 - 3.0f, f4 - 1.0f, n);
            R2DUtils.drawHLine(f + 1.0f, f + 1.0f, f2 + 1.0f, n);
            R2DUtils.drawHLine(f3 - 2.0f, f3 - 2.0f, f2 + 1.0f, n);
            R2DUtils.drawHLine(f3 - 2.0f, f3 - 2.0f, f4 - 2.0f, n);
            R2DUtils.drawHLine(f + 1.0f, f + 1.0f, f4 - 2.0f, n);
            R2DUtils.drawRect(f + 1.0f, f2 + 1.0f, f3 - 1.0f, f4 - 1.0f, n2);
            GL11.glScalef(2.0f, 2.0f, 2.0f);
            R2DUtils.disableGL2D();
            Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
        }

        public static void drawRect(double d, double d2, double d3, double d4, int n) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(n);
            R2DUtils.drawRect(d, d2, d3, d4);
            R2DUtils.disableGL2D();
        }

        private static void drawRect(double d, double d2, double d3, double d4) {
            GL11.glBegin(7);
            GL11.glVertex2d(d, d4);
            GL11.glVertex2d(d3, d4);
            GL11.glVertex2d(d3, d2);
            GL11.glVertex2d(d, d2);
            GL11.glEnd();
        }

        public static void glColor(int n) {
            float f = (float)(n >> 24 & 0xFF) / 255.0f;
            float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
            float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
            float f4 = (float)(n & 0xFF) / 255.0f;
            GL11.glColor4f(f2, f3, f4, f);
        }

        public static void drawRect(float f, float f2, float f3, float f4, int n) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(n);
            R2DUtils.drawRect(f, f2, f3, f4);
            R2DUtils.disableGL2D();
        }

        public static void drawBorderedRect(float f, float f2, float f3, float f4, float f5, int n) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(n);
            R2DUtils.drawRect(f + f5, f2, f3 - f5, f2 + f5);
            R2DUtils.drawRect(f, f2, f + f5, f4);
            R2DUtils.drawRect(f3 - f5, f2, f3, f4);
            R2DUtils.drawRect(f + f5, f4 - f5, f3 - f5, f4);
            R2DUtils.disableGL2D();
        }

        public static void drawBorderedRect(float f, float f2, float f3, float f4, int n, int n2) {
            R2DUtils.enableGL2D();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            R2DUtils.drawVLine(f *= 2.0f, f2 *= 2.0f, f4 *= 2.0f, n2);
            R2DUtils.drawVLine((f3 *= 2.0f) - 1.0f, f2, f4, n2);
            R2DUtils.drawHLine(f, f3 - 1.0f, f2, n2);
            R2DUtils.drawHLine(f, f3 - 2.0f, f4 - 1.0f, n2);
            R2DUtils.drawRect(f + 1.0f, f2 + 1.0f, f3 - 1.0f, f4 - 1.0f, n);
            GL11.glScalef(2.0f, 2.0f, 2.0f);
            R2DUtils.disableGL2D();
        }

        public static void drawGradientRect(float f, float f2, float f3, float f4, int n, int n2) {
            R2DUtils.enableGL2D();
            GL11.glShadeModel(7425);
            GL11.glBegin(7);
            R2DUtils.glColor(n);
            GL11.glVertex2f(f, f4);
            GL11.glVertex2f(f3, f4);
            R2DUtils.glColor(n2);
            GL11.glVertex2f(f3, f2);
            GL11.glVertex2f(f, f2);
            GL11.glEnd();
            GL11.glShadeModel(7424);
            R2DUtils.disableGL2D();
        }

        public static void drawHLine(float f, float f2, float f3, int n) {
            if (f2 < f) {
                float f4 = f;
                f = f2;
                f2 = f4;
            }
            R2DUtils.drawRect(f, f3, f2 + 1.0f, f3 + 1.0f, n);
        }

        public static void drawVLine(float f, float f2, float f3, int n) {
            if (f3 < f2) {
                float f4 = f2;
                f2 = f3;
                f3 = f4;
            }
            R2DUtils.drawRect(f, f2 + 1.0f, f + 1.0f, f3, n);
        }

        public static void drawHLine(float f, float f2, float f3, int n, int n2) {
            if (f2 < f) {
                float f4 = f;
                f = f2;
                f2 = f4;
            }
            R2DUtils.drawGradientRect(f, f3, f2 + 1.0f, f3 + 1.0f, n, n2);
        }

        public static void drawRect(float f, float f2, float f3, float f4) {
            GL11.glBegin(7);
            GL11.glVertex2f(f, f4);
            GL11.glVertex2f(f3, f4);
            GL11.glVertex2f(f3, f2);
            GL11.glVertex2f(f, f2);
            GL11.glEnd();
        }
    }
}

