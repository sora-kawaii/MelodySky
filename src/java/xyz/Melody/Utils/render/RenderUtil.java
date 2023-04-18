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
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }

    public static int getHexRGB(int n) {
        return 0xFF000000 | n;
    }

    public static void drawImage(ResourceLocation resourceLocation, float f, float f2, float f3, float f4) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture((int)f, (int)f2, 0.0f, 0.0f, (int)f3, (int)f4, f3, f4);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawMelodyLogo(float f, float f2, float f3, Color color) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Melody/Melody.png"));
        Gui.drawModalRectWithCustomSizedTexture((int)f, (int)f2, 0.0f, 0.0f, (int)f3, (int)f3, f3, f3);
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
        GlStateManager.resetColor();
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
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(!bl);
    }

    public static void entityOutlineAXIS(Entity entity, int n, EventRender3D eventRender3D) {
        RenderUtil.drawOutlinedBoundingBox(entity.getEntityBoundingBox().expand(0.15, 0.15, 0.15), n, 2.0f, eventRender3D.getPartialTicks());
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
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
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
        d -= RenderUtil.mc.getRenderManager().viewerPosX;
        d4 -= RenderUtil.mc.getRenderManager().viewerPosX;
        d2 -= RenderUtil.mc.getRenderManager().viewerPosY;
        d5 -= RenderUtil.mc.getRenderManager().viewerPosY;
        d3 -= RenderUtil.mc.getRenderManager().viewerPosZ;
        d6 -= RenderUtil.mc.getRenderManager().viewerPosZ;
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
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1), n, 2.0f, f);
    }

    public static void drawSolidBlockESP(BlockPos blockPos, int n, float f, float f2) {
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1), n, f, f2);
    }

    public static void drawFullBlockESP(BlockPos blockPos, Color color, float f) {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;
        double d4 = (double)blockPos.getX() - d;
        double d5 = (double)blockPos.getY() - d2;
        double d6 = (double)blockPos.getZ() - d3;
        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        RenderUtil.drawFilledBoundingBox(new AxisAlignedBB(d4, d5, d6, d4 + 1.0, d5 + 1.0, d6 + 1.0), color, 1.0f);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
    }

    public static void drawFilledBoundingBox(AxisAlignedBB axisAlignedBB, Color color, float f) {
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        GlStateManager.color((float)color.getRed() / 255.0f * 0.8f, (float)color.getGreen() / 255.0f * 0.8f, (float)color.getBlue() / 255.0f * 0.8f, (float)color.getAlpha() / 255.0f * f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        GlStateManager.color((float)color.getRed() / 255.0f * 0.9f, (float)color.getGreen() / 255.0f * 0.9f, (float)color.getBlue() / 255.0f * 0.9f, (float)color.getAlpha() / 255.0f * f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB axisAlignedBB, int n, float f, float f2) {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        Color color = new Color(n);
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f2;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f2;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f2;
        GlStateManager.resetColor();
        GlStateManager.disableCull();
        GlStateManager.disableDepth();
        GlStateManager.pushMatrix();
        GlStateManager.translate(-d, -d2, -d3);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(f);
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        RenderGlobal.drawOutlinedBoundingBox(axisAlignedBB, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GlStateManager.translate(d, d2, d3);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.resetColor();
    }

    private static void dbb(AxisAlignedBB axisAlignedBB, float f, float f2, float f3) {
        float f4 = 0.25f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
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
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
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
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }

    public static void prepareScissorBox(float f, float f2, float f3, float f4) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int n = scaledResolution.getScaleFactor();
        GL11.glScissor((int)(f * (float)n), (int)(((float)scaledResolution.getScaledHeight() - f4) * (float)n), (int)((f3 - f) * (float)n), (int)((f4 - f2) * (float)n));
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
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.getRenderManager().viewerPosX;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.getRenderManager().viewerPosY;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.getRenderManager().viewerPosZ;
        double d4 = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX - (double)0.1f;
        double d5 = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + (double)0.2f;
        RenderUtil.drawEntityESP(d, d2, d3, d4, d5, 0.0f, 0.0f, 0.0f, 0.0f, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f, 1.6f, eventRender3D.getPartialTicks());
    }

    public static void drawFilledESP(Entity entity, Color color, EventRender3D eventRender3D, float f) {
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.getRenderManager().viewerPosX;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.getRenderManager().viewerPosY;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)eventRender3D.getPartialTicks() - RenderUtil.mc.getRenderManager().viewerPosZ;
        double d4 = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX - (double)0.1f;
        double d5 = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + (double)0.2f;
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
        GlStateManager.resetColor();
    }

    public static void drawBoundingBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
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
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawPlayerIcon(EntityPlayer entityPlayer, int n, int n2, int n3) {
        if (entityPlayer != null) {
            mc.getTextureManager().bindTexture(mc.getNetHandler().getPlayerInfo(entityPlayer.getUniqueID()).getLocationSkin());
            Gui.drawScaledCustomSizeModalRect(n2, n3, 8.0f, 8.0f, 8, 8, n, n, 64.0f, 64.0f);
            if (entityPlayer.isWearing(EnumPlayerModelParts.HAT)) {
                Gui.drawScaledCustomSizeModalRect(n2, n3, 40.0f, 8.0f, 8, 8, n, n, 64.0f, 64.0f);
            }
        }
    }

    public static void drawOnSlot(int n, int n2, int n3, int n4) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int n5 = (scaledResolution.getScaledWidth() - 176) / 2;
        int n6 = (scaledResolution.getScaledHeight() - 222) / 2;
        int n7 = n5 + n2;
        int n8 = n6 + n3;
        if (n != 90) {
            n8 += (6 - (n - 36) / 9) * 9;
        }
        GL11.glTranslated(0.0, 0.0, 1.0);
        Gui.drawRect(n7, n8, n7 + 16, n8 + 16, n4);
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
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        RenderHelper.enableGUIStandardItemLighting();
        renderItem.zLevel = -145.0f;
        renderItem.renderItemAndEffectIntoGUI(itemStack, n, n2);
        renderItem.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, itemStack, n, n2, string);
        renderItem.zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
    }

    static {
        mc = Minecraft.getMinecraft();
        tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
        csBuffer = new ArrayList<Integer>();
        ENABLE_CLIENT_STATE = GL11::glEnableClientState;
        DISABLE_CLIENT_STATE = GL11::glEnableClientState;
    }
}

