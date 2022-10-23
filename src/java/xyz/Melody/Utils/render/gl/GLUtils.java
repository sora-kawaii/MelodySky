package xyz.Melody.Utils.render.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import xyz.Melody.Utils.math.Vec3f;

public final class GLUtils {
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
      return new float[]{(float)(hex >> 16 & 255) / 255.0F, (float)(hex >> 8 & 255) / 255.0F, (float)(hex & 255) / 255.0F, (float)(hex >> 24 & 255) / 255.0F};
   }

   public static void glColor(int hex) {
      float[] color = getColor(hex);
      GlStateManager.color(color[0], color[1], color[2], color[3]);
   }

   public static void rotateX(float angle, double x, double y, double z) {
      GlStateManager.translate(x, y, z);
      GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
      GlStateManager.translate(-x, -y, -z);
   }

   public static void rotateY(float angle, double x, double y, double z) {
      GlStateManager.translate(x, y, z);
      GlStateManager.rotate(angle, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(-x, -y, -z);
   }

   public static void rotateZ(float angle, double x, double y, double z) {
      GlStateManager.translate(x, y, z);
      GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate(-x, -y, -z);
   }

   public static Vec3f toScreen(Vec3f pos) {
      return toScreen(pos.getX(), pos.getY(), pos.getZ());
   }

   public static Vec3f toScreen(double x, double y, double z) {
      boolean result = GLU.gluProject((float)x, (float)y, (float)z, MODELVIEW, PROJECTION, VIEWPORT, (FloatBuffer)TO_SCREEN_BUFFER.clear());
      return result ? new Vec3f((double)TO_SCREEN_BUFFER.get(0), (double)((float)Display.getHeight() - TO_SCREEN_BUFFER.get(1)), (double)TO_SCREEN_BUFFER.get(2)) : null;
   }

   public static Vec3f toWorld(Vec3f pos) {
      return toWorld(pos.getX(), pos.getY(), pos.getZ());
   }

   public static Vec3f toWorld(double x, double y, double z) {
      boolean result = GLU.gluUnProject((float)x, (float)y, (float)z, MODELVIEW, PROJECTION, VIEWPORT, (FloatBuffer)TO_WORLD_BUFFER.clear());
      return result ? new Vec3f((double)TO_WORLD_BUFFER.get(0), (double)TO_WORLD_BUFFER.get(1), (double)TO_WORLD_BUFFER.get(2)) : null;
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
}
