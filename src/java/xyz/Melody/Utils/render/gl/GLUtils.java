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

    public static float[] getColor(int n) {
        return new float[]{(float)(n >> 16 & 0xFF) / 255.0f, (float)(n >> 8 & 0xFF) / 255.0f, (float)(n & 0xFF) / 255.0f, (float)(n >> 24 & 0xFF) / 255.0f};
    }

    public static void glColor(int n) {
        float[] fArray = GLUtils.getColor(n);
        GlStateManager.color(fArray[0], fArray[1], fArray[2], fArray[3]);
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

    public static void bindTexture(int n) {
        GL11.glBindTexture(3553, n);
    }

    public static void rotateX(float f, double d, double d2, double d3) {
        GlStateManager.translate(d, d2, d3);
        GlStateManager.rotate(f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-d, -d2, -d3);
    }

    public static void rotateY(float f, double d, double d2, double d3) {
        GlStateManager.translate(d, d2, d3);
        GlStateManager.rotate(f, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(-d, -d2, -d3);
    }

    public static void rotateZ(float f, double d, double d2, double d3) {
        GlStateManager.translate(d, d2, d3);
        GlStateManager.rotate(f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-d, -d2, -d3);
    }

    public static Vec3f toScreen(Vec3f vec3f) {
        return GLUtils.toScreen(vec3f.getX(), vec3f.getY(), vec3f.getZ());
    }

    public static Vec3f toScreen(double d, double d2, double d3) {
        boolean bl = GLU.gluProject((float)d, (float)d2, (float)d3, MODELVIEW, PROJECTION, VIEWPORT, (FloatBuffer)TO_SCREEN_BUFFER.clear());
        if (bl) {
            return new Vec3f(TO_SCREEN_BUFFER.get(0), (float)Display.getHeight() - TO_SCREEN_BUFFER.get(1), TO_SCREEN_BUFFER.get(2));
        }
        return null;
    }

    public static Vec3f toWorld(Vec3f vec3f) {
        return GLUtils.toWorld(vec3f.getX(), vec3f.getY(), vec3f.getZ());
    }

    public static Vec3f toWorld(double d, double d2, double d3) {
        boolean bl = GLU.gluUnProject((float)d, (float)d2, (float)d3, MODELVIEW, PROJECTION, VIEWPORT, (FloatBuffer)TO_WORLD_BUFFER.clear());
        if (bl) {
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
        int n = 1;
        boolean bl = Minecraft.getMinecraft().isUnicode();
        int n2 = Minecraft.getMinecraft().gameSettings.guiScale;
        if (n2 == 0) {
            n2 = 1000;
        }
        while (n < n2 && Minecraft.getMinecraft().displayWidth / (n + 1) >= 320 && Minecraft.getMinecraft().displayHeight / (n + 1) >= 240) {
            ++n;
        }
        if (bl && n % 2 != 0 && n != 1) {
            --n;
        }
        return n;
    }
}

