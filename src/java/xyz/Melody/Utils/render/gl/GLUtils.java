/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.render.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import xyz.Melody.Utils.math.Vec3f;

public final class GLUtils {
    private static Minecraft mc = Minecraft.getMinecraft();
    public static final FloatBuffer MODELVIEW = BufferUtils.createFloatBuffer(16);
    public static final FloatBuffer PROJECTION = BufferUtils.createFloatBuffer(16);
    public static final IntBuffer VIEWPORT = BufferUtils.createIntBuffer(16);
    public static final FloatBuffer TO_SCREEN_BUFFER = BufferUtils.createFloatBuffer(3);
    public static final FloatBuffer TO_WORLD_BUFFER = BufferUtils.createFloatBuffer(3);

    private GLUtils() {
    }

    public static void init() {
    }

    public static float[] getColor(int hex) {
        return new float[]{(float)(hex >> 16 & 0xFF) / 255.0f, (float)(hex >> 8 & 0xFF) / 255.0f, (float)(hex & 0xFF) / 255.0f, (float)(hex >> 24 & 0xFF) / 255.0f};
    }

    public static void glColor(int hex) {
        float[] color = GLUtils.getColor(hex);
        GlStateManager.color(color[0], color[1], color[2], color[3]);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != GLUtils.mc.displayWidth || framebuffer.framebufferHeight != GLUtils.mc.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(GLUtils.mc.displayWidth, GLUtils.mc.displayHeight, true);
        }
        return framebuffer;
    }

    public static void bindTexture(int texture) {
        GL11.glBindTexture(3553, texture);
    }

    public static void rotateX(float angle, double x, double y, double z) {
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-x, -y, -z);
    }

    public static void rotateY(float angle, double x, double y, double z) {
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(-x, -y, -z);
    }

    public static void rotateZ(float angle, double x, double y, double z) {
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-x, -y, -z);
    }

    public static Vec3f toScreen(Vec3f pos) {
        return GLUtils.toScreen(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec3f toScreen(double x, double y, double z) {
        boolean result = GLU.gluProject((float)x, (float)y, (float)z, MODELVIEW, PROJECTION, VIEWPORT, (FloatBuffer)TO_SCREEN_BUFFER.clear());
        if (result) {
            return new Vec3f(TO_SCREEN_BUFFER.get(0), (float)Display.getHeight() - TO_SCREEN_BUFFER.get(1), TO_SCREEN_BUFFER.get(2));
        }
        return null;
    }

    public static Vec3f toWorld(Vec3f pos) {
        return GLUtils.toWorld(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec3f toWorld(double x, double y, double z) {
        boolean result = GLU.gluUnProject((float)x, (float)y, (float)z, MODELVIEW, PROJECTION, VIEWPORT, (FloatBuffer)TO_WORLD_BUFFER.clear());
        if (result) {
            return new Vec3f(TO_WORLD_BUFFER.get(0), TO_WORLD_BUFFER.get(1), TO_WORLD_BUFFER.get(2));
        }
        return null;
    }

    public static FloatBuffer getModelview() {
        return MODELVIEW;
    }

    public static FloatBuffer getProjection() {
        return PROJECTION;
    }

    public static IntBuffer getViewport() {
        return VIEWPORT;
    }

    public static int getMouseX() {
        return Mouse.getX() * GLUtils.getScreenWidth() / Minecraft.getMinecraft().displayWidth;
    }

    public static int getMouseY() {
        return GLUtils.getScreenHeight() - Mouse.getY() * GLUtils.getScreenHeight() / Minecraft.getMinecraft().displayWidth - 1;
    }

    public static int getScreenWidth() {
        return Minecraft.getMinecraft().displayWidth / GLUtils.getScaleFactor();
    }

    public static int getScreenHeight() {
        return Minecraft.getMinecraft().displayHeight / GLUtils.getScaleFactor();
    }

    public static int getScaleFactor() {
        int scaleFactor = 1;
        boolean isUnicode = Minecraft.getMinecraft().isUnicode();
        int guiScale = Minecraft.getMinecraft().gameSettings.guiScale;
        if (guiScale == 0) {
            guiScale = 1000;
        }
        while (scaleFactor < guiScale && Minecraft.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Minecraft.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1) {
            --scaleFactor;
        }
        return scaleFactor;
    }
}

