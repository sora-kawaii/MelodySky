/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.render;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pw.knx.feather.tessellate.Tessellation;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Utils.math.Vec2f;
import xyz.Melody.Utils.math.Vec3f;
import xyz.Melody.Utils.render.gl.GLClientState;

public final class RenderUtil {
    public static final Tessellation tessellator;
    private static final List<Integer> csBuffer;
    private static final Consumer<Integer> ENABLE_CLIENT_STATE;
    private static final Consumer<Integer> DISABLE_CLIENT_STATE;
    private static Minecraft mc;
    public static BufferedReader reader;
    public static Socket s;
    public static PrintWriter w;
    public static InputStream i;

    public static int width() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78326_a();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78328_b();
    }

    public static int getHexRGB(int n) {
        return 0xFF000000 | n;
    }

    public static void drawImage(ResourceLocation resourceLocation, float f, float f2, float f3, float f4) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(resourceLocation);
        Gui.func_146110_a((int)((int)f), (int)((int)f2), (float)0.0f, (float)0.0f, (int)((int)f3), (int)((int)f4), (float)f3, (float)f4);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawMelodyLogo(float f, float f2, float f3, Color color) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(new ResourceLocation("Melody/Melody.png"));
        Gui.func_146110_a((int)((int)f), (int)((int)f2), (float)0.0f, (float)0.0f, (int)((int)f3), (int)((int)f3), (float)f3, (float)f3);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawBorderedRect(float f, float f2, float f3, float f4, float f5, int n, int n2) {
        RenderUtil.drawFastRoundedRect(f, f2, f3, f4, 1.0f, n2);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        RenderUtil.glColor(n);
        GL11.glLineWidth(f5);
        GL11.glBegin(1);
        GL11.glVertex2d(f, f2);
        GL11.glVertex2d(f, f4);
        GL11.glVertex2d(f3, f4);
        GL11.glVertex2d(f3, f2);
        GL11.glVertex2d(f, f2);
        GL11.glVertex2d(f3, f2);
        GL11.glVertex2d(f, f4);
        GL11.glVertex2d(f3, f4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GlStateManager.func_179117_G();
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

    public static void startDrawing() {
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
    }

    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
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

    public static void drawLine(Vec2f vec2f, Vec2f vec2f2, float f) {
        RenderUtil.drawLine(vec2f.getX(), vec2f.getY(), vec2f2.getX(), vec2f2.getY(), f);
    }

    public static void drawLine(Vec3f vec3f, Vec3f vec3f2, float f) {
        RenderUtil.drawLine((float)vec3f.getX(), (float)vec3f.getY(), (float)vec3f.getZ(), (float)vec3f2.getX(), (float)vec3f2.getY(), (float)vec3f2.getZ(), f);
    }

    public static void drawLine(float f, float f2, float f3, float f4, float f5) {
        RenderUtil.drawLine(f, f2, 0.0f, f3, f4, 0.0f, f5);
    }

    public static void drawLine(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        GL11.glPushMatrix();
        GL11.glLineWidth(f7);
        RenderUtil.setupRender(true);
        RenderUtil.setupClientState(GLClientState.VERTEX, true);
        tessellator.addVertex(f, f2, f3).addVertex(f4, f5, f6).draw(3);
        RenderUtil.setupClientState(GLClientState.VERTEX, false);
        RenderUtil.setupRender(false);
        GL11.glPopMatrix();
    }

    public static void setupClientState(GLClientState gLClientState, boolean bl) {
        csBuffer.clear();
        if (gLClientState.ordinal() > 0) {
            csBuffer.add(gLClientState.getCap());
        }
        csBuffer.add(32884);
        csBuffer.forEach(bl ? ENABLE_CLIENT_STATE : DISABLE_CLIENT_STATE);
    }

    public static void setupRender(boolean bl) {
        if (bl) {
            GlStateManager.func_179147_l();
            GL11.glEnable(2848);
            GlStateManager.func_179097_i();
            GlStateManager.func_179090_x();
            GlStateManager.func_179112_b((int)770, (int)771);
            GL11.glHint(3154, 4354);
        } else {
            GlStateManager.func_179084_k();
            GlStateManager.func_179098_w();
            GL11.glDisable(2848);
            GlStateManager.func_179126_j();
        }
        GlStateManager.func_179132_a((!bl ? 1 : 0) != 0);
    }

    public static void entityOutlineAXIS(Entity entity, int n, EventRender3D eventRender3D) {
        RenderUtil.drawOutlinedBoundingBox(entity.func_174813_aQ().func_72314_b(0.15, 0.15, 0.15), n, 2.0f, eventRender3D.getPartialTicks());
    }

    public static void setColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f == 0.0f ? 1.0f : f);
    }

    public static void enableGL3D(float f) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GlStateManager.func_179140_f();
        GlStateManager.func_179106_n();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(f);
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

    public static void draw3DLine(double d, double d2, double d3, double d4, double d5, double d6, float f, float f2, float f3, float f4, float f5) {
        d -= RenderUtil.mc.func_175598_ae().field_78730_l;
        d4 -= RenderUtil.mc.func_175598_ae().field_78730_l;
        d2 -= RenderUtil.mc.func_175598_ae().field_78731_m;
        d5 -= RenderUtil.mc.func_175598_ae().field_78731_m;
        d3 -= RenderUtil.mc.func_175598_ae().field_78728_n;
        d6 -= RenderUtil.mc.func_175598_ae().field_78728_n;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(f5);
        GL11.glColor4f(f, f2, f3, f4);
        GL11.glBegin(2);
        GL11.glVertex3d(d, d2, d3);
        GL11.glVertex3d(d4, d5, d6);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedBlockESP(double d, double d2, double d3, Color color, float f) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidBlockESP(BlockPos blockPos, int n, float f) {
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), blockPos.func_177958_n() + 1, blockPos.func_177956_o() + 1, blockPos.func_177952_p() + 1), n, 2.0f, f);
    }

    public static void drawSolidBlockESP(BlockPos blockPos, int n, float f, float f2) {
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), blockPos.func_177958_n() + 1, blockPos.func_177956_o() + 1, blockPos.func_177952_p() + 1), n, f, f2);
    }

    public static void drawFullBlockESP(BlockPos blockPos, Color color, float f) {
        Entity entity = Minecraft.func_71410_x().func_175606_aa();
        double d = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)f;
        double d2 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)f;
        double d3 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)f;
        double d4 = (double)blockPos.func_177958_n() - d;
        double d5 = (double)blockPos.func_177956_o() - d2;
        double d6 = (double)blockPos.func_177952_p() - d3;
        GlStateManager.func_179097_i();
        GlStateManager.func_179129_p();
        GlStateManager.func_179140_f();
        RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(d4, d5, d6, d4 + 1.0, d5 + 1.0, d6 + 1.0), color, 1.0f);
        GlStateManager.func_179145_e();
        GlStateManager.func_179126_j();
        GlStateManager.func_179089_o();
    }

    public static void drawFilledBoundingBox(AxisAlignedBB axisAlignedBB, Color color, float f) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179140_f();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179090_x();
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f * 0.8f), (float)((float)color.getGreen() / 255.0f * 0.8f), (float)((float)color.getBlue() / 255.0f * 0.8f), (float)((float)color.getAlpha() / 255.0f * f));
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f * 0.9f), (float)((float)color.getGreen() / 255.0f * 0.9f), (float)((float)color.getBlue() / 255.0f * 0.9f), (float)((float)color.getAlpha() / 255.0f * f));
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB axisAlignedBB, int n, float f, float f2) {
        Entity entity = Minecraft.func_71410_x().func_175606_aa();
        Color color = new Color(n);
        double d = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)f2;
        double d2 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)f2;
        double d3 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)f2;
        GlStateManager.func_179117_G();
        GlStateManager.func_179129_p();
        GlStateManager.func_179097_i();
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)(-d), (double)(-d2), (double)(-d3));
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GL11.glLineWidth(f);
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        RenderGlobal.func_181563_a((AxisAlignedBB)axisAlignedBB, (int)color.getRed(), (int)color.getGreen(), (int)color.getBlue(), (int)color.getAlpha());
        GlStateManager.func_179137_b((double)d, (double)d2, (double)d3);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179121_F();
        GlStateManager.func_179126_j();
        GlStateManager.func_179089_o();
        GlStateManager.func_179117_G();
    }

    private static void dbb(AxisAlignedBB axisAlignedBB, float f, float f2, float f3) {
        float f4 = 0.25f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181666_a(f, f2, f3, f4).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawFastRoundedRect(float f, float f2, float f3, float f4, float f5, int n) {
        float f6;
        if (f == f3 || f2 == f4) {
            return;
        }
        int n2 = 18;
        float f7 = 5.0f;
        float f8 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f9 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f10 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f11 = (float)(n & 0xFF) / 255.0f;
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f(f9, f10, f11, f8);
        GL11.glBegin(5);
        GL11.glVertex2f(f + f5, f2);
        GL11.glVertex2f(f + f5, f4);
        GL11.glVertex2f(f3 - f5, f2);
        GL11.glVertex2f(f3 - f5, f4);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(f, f2 + f5);
        GL11.glVertex2f(f + f5, f2 + f5);
        GL11.glVertex2f(f, f4 - f5);
        GL11.glVertex2f(f + f5, f4 - f5);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(f3, f2 + f5);
        GL11.glVertex2f(f3 - f5, f2 + f5);
        GL11.glVertex2f(f3, f4 - f5);
        GL11.glVertex2f(f3 - f5, f4 - f5);
        GL11.glEnd();
        GL11.glBegin(6);
        float f12 = f3 - f5;
        float f13 = f2 + f5;
        GL11.glVertex2f(f12, f13);
        int n3 = 0;
        for (n3 = 0; n3 <= 18; ++n3) {
            f6 = (float)n3 * 5.0f;
            GL11.glVertex2f((float)((double)f12 + (double)f5 * Math.cos(Math.toRadians(f6))), (float)((double)f13 - (double)f5 * Math.sin(Math.toRadians(f6))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f12 = f + f5;
        f13 = f2 + f5;
        GL11.glVertex2f(f12, f13);
        for (n3 = 0; n3 <= 18; ++n3) {
            f6 = (float)n3 * 5.0f;
            GL11.glVertex2f((float)((double)f12 - (double)f5 * Math.cos(Math.toRadians(f6))), (float)((double)f13 - (double)f5 * Math.sin(Math.toRadians(f6))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f12 = f + f5;
        f13 = f4 - f5;
        GL11.glVertex2f(f12, f13);
        for (n3 = 0; n3 <= 18; ++n3) {
            f6 = (float)n3 * 5.0f;
            GL11.glVertex2f((float)((double)f12 - (double)f5 * Math.cos(Math.toRadians(f6))), (float)((double)f13 + (double)f5 * Math.sin(Math.toRadians(f6))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f12 = f3 - f5;
        f13 = f4 - f5;
        GL11.glVertex2f(f12, f13);
        for (n3 = 0; n3 <= 18; ++n3) {
            f6 = (float)n3 * 5.0f;
            GL11.glVertex2f((float)((double)f12 + (double)f5 * Math.cos(Math.toRadians(f6))), (float)((double)f13 + (double)f5 * Math.sin(Math.toRadians(f6))));
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179117_G();
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

    public static void prepareScissorBox(float f, float f2, float f3, float f4) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        int n = scaledResolution.func_78325_e();
        GL11.glScissor((int)(f * (float)n), (int)(((float)scaledResolution.func_78328_b() - f4) * (float)n), (int)((f3 - f) * (float)n), (int)((f4 - f2) * (float)n));
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void glColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
    }

    public static void drawFilledESP(Entity entity, Color color, EventRender3D eventRender3D) {
        double d = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.func_175598_ae().field_78730_l;
        double d2 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.func_175598_ae().field_78731_m;
        double d3 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.func_175598_ae().field_78728_n;
        double d4 = entity.func_174813_aQ().field_72336_d - entity.func_174813_aQ().field_72340_a - (double)0.1f;
        double d5 = entity.func_174813_aQ().field_72337_e - entity.func_174813_aQ().field_72338_b + (double)0.2f;
        RenderUtil.drawEntityESP(d, d2, d3, d4, d5, 0.0f, 0.0f, 0.0f, 0.0f, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f, 1.6f, eventRender3D.getPartialTicks());
    }

    public static void drawFilledESP(Entity entity, Color color, EventRender3D eventRender3D, float f) {
        double d = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.func_175598_ae().field_78730_l;
        double d2 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.func_175598_ae().field_78731_m;
        double d3 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.func_175598_ae().field_78728_n;
        double d4 = entity.func_174813_aQ().field_72336_d - entity.func_174813_aQ().field_72340_a - (double)0.1f;
        double d5 = entity.func_174813_aQ().field_72337_e - entity.func_174813_aQ().field_72338_b + (double)0.2f;
        RenderUtil.drawEntityESP(d, d2, d3, d4, d5, 0.0f, 0.0f, 0.0f, 0.0f, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f, f, eventRender3D.getPartialTicks());
    }

    public static void drawEntityESP(double d, double d2, double d3, double d4, double d5, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(f, f2, f3, f4);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(d - d4, d2, d3 - d4, d + d4, d2 + d5, d3 + d4));
        GL11.glLineWidth(f9);
        GL11.glColor4f(f5, f6, f7, f8);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(d - d4, d2, d3 - d4, d + d4, d2 + d5, d3 + d4));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
        GlStateManager.func_179117_G();
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

    public static Color rainbow(long l2, float f, float f2) {
        float f3 = ((float)l2 + (-10.0f + f) * 2.0E8f) / 3.0E9f % -360.0f;
        long l3 = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(f3, 0.35f, 1.0f)), 16);
        Color color = new Color((int)l3);
        return new Color((float)color.getRed() / 255.0f * f2, (float)color.getGreen() / 255.0f * f2, (float)color.getBlue() / 255.0f * f2, (float)color.getAlpha() / 255.0f);
    }

    public static void drawFilledCircle(float f, float f2, float f3, Color color) {
        int n = 50;
        double d = Math.PI * 2 / (double)n;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < n; ++i) {
            float f4 = (float)((double)f3 * Math.sin((double)i * d));
            float f5 = (float)((double)f3 * Math.cos((double)i * d));
            GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
            GL11.glVertex2f(f + f4, f2 + f5);
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawPlayerIcon(EntityPlayer entityPlayer, int n, int n2, int n3) {
        if (entityPlayer != null) {
            mc.func_110434_K().func_110577_a(mc.func_147114_u().func_175102_a(entityPlayer.func_110124_au()).func_178837_g());
            Gui.func_152125_a((int)n2, (int)n3, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)n, (int)n, (float)64.0f, (float)64.0f);
            if (entityPlayer.func_175148_a(EnumPlayerModelParts.HAT)) {
                Gui.func_152125_a((int)n2, (int)n3, (float)40.0f, (float)8.0f, (int)8, (int)8, (int)n, (int)n, (float)64.0f, (float)64.0f);
            }
        }
    }

    public static void drawOnSlot(int n, int n2, int n3, int n4) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        int n5 = (scaledResolution.func_78326_a() - 176) / 2;
        int n6 = (scaledResolution.func_78328_b() - 222) / 2;
        int n7 = n5 + n2;
        int n8 = n6 + n3;
        if (n != 90) {
            n8 += (6 - (n - 36) / 9) * 9;
        }
        GL11.glTranslated(0.0, 0.0, 1.0);
        Gui.func_73734_a((int)n7, (int)n8, (int)(n7 + 16), (int)(n8 + 16), (int)n4);
        GL11.glTranslated(0.0, 0.0, -1.0);
    }

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

    public static void drawItemStack(ItemStack itemStack, int n, int n2) {
        if (itemStack == null) {
            return;
        }
        RenderUtil.drawItemStackWithText(itemStack, n, n2, null);
    }

    public static void drawItemStackWithText(ItemStack itemStack, int n, int n2, String string) {
        if (itemStack == null) {
            return;
        }
        RenderItem renderItem = Minecraft.func_71410_x().func_175599_af();
        RenderHelper.func_74520_c();
        renderItem.field_77023_b = -145.0f;
        renderItem.func_180450_b(itemStack, n, n2);
        renderItem.func_180453_a(Minecraft.func_71410_x().field_71466_p, itemStack, n, n2, string);
        renderItem.field_77023_b = 0.0f;
        RenderHelper.func_74518_a();
    }

    static {
        mc = Minecraft.func_71410_x();
        tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
        csBuffer = new ArrayList<Integer>();
        ENABLE_CLIENT_STATE = GL11::glEnableClientState;
        DISABLE_CLIENT_STATE = GL11::glEnableClientState;
    }
}

