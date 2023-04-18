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
    private static Minecraft mc = Minecraft.func_71410_x();
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
        GlStateManager.func_179131_c((float)fArray[0], (float)fArray[1], (float)fArray[2], (float)fArray[3]);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.field_147621_c != GLUtils.mc.field_71443_c || framebuffer.field_147618_d != GLUtils.mc.field_71440_d) {
            if (framebuffer != null) {
                framebuffer.func_147608_a();
            }
            return new Framebuffer(GLUtils.mc.field_71443_c, GLUtils.mc.field_71440_d, true);
        }
        return framebuffer;
    }

    public static void bindTexture(int n) {
        GL11.glBindTexture(3553, n);
    }

    public static void rotateX(float f, double d, double d2, double d3) {
        GlStateManager.func_179137_b((double)d, (double)d2, (double)d3);
        GlStateManager.func_179114_b((float)f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179137_b((double)(-d), (double)(-d2), (double)(-d3));
    }

    public static void rotateY(float f, double d, double d2, double d3) {
        GlStateManager.func_179137_b((double)d, (double)d2, (double)d3);
        GlStateManager.func_179114_b((float)f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179137_b((double)(-d), (double)(-d2), (double)(-d3));
    }

    public static void rotateZ(float f, double d, double d2, double d3) {
        GlStateManager.func_179137_b((double)d, (double)d2, (double)d3);
        GlStateManager.func_179114_b((float)f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179137_b((double)(-d), (double)(-d2), (double)(-d3));
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
        return Mouse.getX() * GLUtils.getScreenWidth() / Minecraft.func_71410_x().field_71443_c;
    }

    public static int getMouseY() {
        return GLUtils.getScreenHeight() - Mouse.getY() * GLUtils.getScreenHeight() / Minecraft.func_71410_x().field_71443_c - 1;
    }

    public static int getScreenWidth() {
        return Minecraft.func_71410_x().field_71443_c / GLUtils.getScaleFactor();
    }

    public static int getScreenHeight() {
        return Minecraft.func_71410_x().field_71440_d / GLUtils.getScaleFactor();
    }

    public static int getScaleFactor() {
        int n = 1;
        boolean bl = Minecraft.func_71410_x().func_152349_b();
        int n2 = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
        if (n2 == 0) {
            n2 = 1000;
        }
        while (n < n2 && Minecraft.func_71410_x().field_71443_c / (n + 1) >= 320 && Minecraft.func_71410_x().field_71440_d / (n + 1) >= 240) {
            ++n;
        }
        if (bl && n % 2 != 0 && n != 1) {
            --n;
        }
        return n;
    }
}

