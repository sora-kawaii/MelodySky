package xyz.Melody.Utils.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pw.knx.feather.tessellate.Tessellation;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Utils.math.Vec2f;
import xyz.Melody.Utils.math.Vec3f;
import xyz.Melody.Utils.render.gl.GLClientState;

public class RenderUtil {
   public static final Tessellation tessellator = Tessellation.createExpanding(4, 1.0F, 2.0F);
   private static final List csBuffer = new ArrayList();
   private static final Consumer ENABLE_CLIENT_STATE = GL11::glEnableClientState;
   private static final Consumer DISABLE_CLIENT_STATE = GL11::glEnableClientState;
   private static Minecraft mc = Minecraft.getMinecraft();

   public static int width() {
      return (new ScaledResolution(Minecraft.getMinecraft())).getScaledWidth();
   }

   public static int height() {
      return (new ScaledResolution(Minecraft.getMinecraft())).getScaledHeight();
   }

   public static int getHexRGB(int hex) {
      return -16777216 | hex;
   }

   public static void drawImage(ResourceLocation image, float f, float g, float h, float i) {
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture((int)f, (int)g, 0.0F, 0.0F, (int)h, (int)i, h, i);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void drawMelodyLogo(float x, float y, float scale, Color color) {
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Melody/Melody.png"));
      Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, 0.0F, 0.0F, (int)scale, (int)scale, scale, scale);
      GL11.glColor4f((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getAlpha());
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
   }

   public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int color1, int color2) {
      drawFastRoundedRect(x, y, x2, y2, 1.0F, color2);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      glColor(color1);
      GL11.glLineWidth(l1);
      GL11.glBegin(1);
      GL11.glVertex2d((double)x, (double)y);
      GL11.glVertex2d((double)x, (double)y2);
      GL11.glVertex2d((double)x2, (double)y2);
      GL11.glVertex2d((double)x2, (double)y);
      GL11.glVertex2d((double)x, (double)y);
      GL11.glVertex2d((double)x2, (double)y);
      GL11.glVertex2d((double)x, (double)y2);
      GL11.glVertex2d((double)x2, (double)y2);
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

   public static Color blend(Color color1, Color color2, double ratio) {
      float r = (float)ratio;
      float ir = 1.0F - r;
      float[] rgb1 = new float[3];
      float[] rgb2 = new float[3];
      color1.getColorComponents(rgb1);
      color2.getColorComponents(rgb2);
      Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
      return color3;
   }

   public static void drawLine(Vec2f start, Vec2f end, float width) {
      drawLine(start.getX(), start.getY(), end.getX(), end.getY(), width);
   }

   public static void drawLine(Vec3f start, Vec3f end, float width) {
      drawLine((float)start.getX(), (float)start.getY(), (float)start.getZ(), (float)end.getX(), (float)end.getY(), (float)end.getZ(), width);
   }

   public static void drawLine(float x, float y, float x1, float y1, float width) {
      drawLine(x, y, 0.0F, x1, y1, 0.0F, width);
   }

   public static void drawLine(float x, float y, float z, float x1, float y1, float z1, float width) {
      GL11.glLineWidth(width);
      setupRender(true);
      setupClientState(GLClientState.VERTEX, true);
      tessellator.addVertex(x, y, z).addVertex(x1, y1, z1).draw(3);
      setupClientState(GLClientState.VERTEX, false);
      setupRender(false);
   }

   public static void setupClientState(GLClientState state, boolean enabled) {
      csBuffer.clear();
      if (state.ordinal() > 0) {
         csBuffer.add(state.getCap());
      }

      csBuffer.add(32884);
      csBuffer.forEach(enabled ? ENABLE_CLIENT_STATE : DISABLE_CLIENT_STATE);
   }

   public static void setupRender(boolean start) {
      if (start) {
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

      GlStateManager.depthMask(!start);
   }

   public static void entityOutlineAXIS(Entity e, int color, EventRender3D event) {
      drawOutlinedBoundingBox(e.getEntityBoundingBox().expand(0.15, 0.15, 0.15), color, event.getPartialTicks());
   }

   public static void setColor(int colorHex) {
      float alpha = (float)(colorHex >> 24 & 255) / 255.0F;
      float red = (float)(colorHex >> 16 & 255) / 255.0F;
      float green = (float)(colorHex >> 8 & 255) / 255.0F;
      float blue = (float)(colorHex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
   }

   public static void enableGL3D(float lineWidth) {
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
      GL11.glLineWidth(lineWidth);
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

   public static void draw3DLine(double x, double y, double z, double x1, double y1, double z1, float red, float green, float blue, float alpha, float lineWdith) {
      x -= mc.getRenderManager().viewerPosX;
      x1 -= mc.getRenderManager().viewerPosX;
      y -= mc.getRenderManager().viewerPosY;
      y1 -= mc.getRenderManager().viewerPosY;
      z -= mc.getRenderManager().viewerPosZ;
      z1 -= mc.getRenderManager().viewerPosZ;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glEnable(2848);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glLineWidth(lineWdith);
      GL11.glColor4f(red, green, blue, alpha);
      GL11.glBegin(2);
      GL11.glVertex3d(x, y, z);
      GL11.glVertex3d(x1, y1, z1);
      GL11.glEnd();
      GL11.glDisable(3042);
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawOutlinedBlockESP(double x, double y, double z, Color color, float lineWidth) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getAlpha());
      drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void drawSolidBlockESP(BlockPos bp, int color, float tick) {
      drawOutlinedBoundingBox(new AxisAlignedBB((double)bp.getX(), (double)bp.getY(), (double)bp.getZ(), (double)(bp.getX() + 1), (double)(bp.getY() + 1), (double)(bp.getZ() + 1)), color, tick);
   }

   public static void drawFullBlockESP(BlockPos pos, Color color, float partialTicks) {
      Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
      double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * (double)partialTicks;
      double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * (double)partialTicks;
      double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * (double)partialTicks;
      double x = (double)pos.getX() - viewerX;
      double y = (double)pos.getY() - viewerY;
      double z = (double)pos.getZ() - viewerZ;
      GlStateManager.disableDepth();
      GlStateManager.disableCull();
      GlStateManager.disableLighting();
      drawFilledBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), color, 1.0F);
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      GlStateManager.enableCull();
   }

   public static void drawFilledBoundingBox(AxisAlignedBB aabb, Color c, float alphaMultiplier) {
      GlStateManager.enableBlend();
      GlStateManager.disableLighting();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.disableTexture2D();
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.color((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, (float)c.getAlpha() / 255.0F);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
      worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
      tessellator.draw();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
      worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
      tessellator.draw();
      GlStateManager.color((float)c.getRed() / 255.0F * 0.8F, (float)c.getGreen() / 255.0F * 0.8F, (float)c.getBlue() / 255.0F * 0.8F, (float)c.getAlpha() / 255.0F * alphaMultiplier);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
      worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
      worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
      worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
      tessellator.draw();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
      tessellator.draw();
      GlStateManager.color((float)c.getRed() / 255.0F * 0.9F, (float)c.getGreen() / 255.0F * 0.9F, (float)c.getBlue() / 255.0F * 0.9F, (float)c.getAlpha() / 255.0F * alphaMultiplier);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
      worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
      tessellator.draw();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
      worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
      worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB aabb, int colourInt, float partialTicks) {
      Entity render = Minecraft.getMinecraft().getRenderViewEntity();
      Color colour = new Color(colourInt);
      double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * (double)partialTicks;
      double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * (double)partialTicks;
      double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * (double)partialTicks;
      GlStateManager.resetColor();
      GlStateManager.disableCull();
      GlStateManager.disableDepth();
      GlStateManager.pushMatrix();
      GlStateManager.translate(-realX, -realY, -realZ);
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glLineWidth(2.0F);
      GL11.glColor4f((float)colour.getRed() / 255.0F, (float)colour.getGreen() / 255.0F, (float)colour.getBlue() / 255.0F, (float)colour.getAlpha() / 255.0F);
      RenderGlobal.drawOutlinedBoundingBox(aabb, colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha());
      GlStateManager.translate(realX, realY, realZ);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
      GlStateManager.enableDepth();
      GlStateManager.enableCull();
      GlStateManager.resetColor();
   }

   private static void dbb(AxisAlignedBB abb, float r, float g, float b) {
      float a = 0.25F;
      Tessellator ts = Tessellator.getInstance();
      WorldRenderer vb = ts.getWorldRenderer();
      vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      ts.draw();
      vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      ts.draw();
      vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      ts.draw();
      vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      ts.draw();
      vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      ts.draw();
      vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
      vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
      ts.draw();
   }

   public static void drawFastRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
      if (x0 != x1 && y0 != y1) {
         int Semicircle = true;
         float f = 5.0F;
         float f2 = (float)(color >> 24 & 255) / 255.0F;
         float f3 = (float)(color >> 16 & 255) / 255.0F;
         float f4 = (float)(color >> 8 & 255) / 255.0F;
         float f5 = (float)(color & 255) / 255.0F;
         GL11.glDisable(2884);
         GL11.glDisable(3553);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         GL11.glColor4f(f3, f4, f5, f2);
         GL11.glBegin(5);
         GL11.glVertex2f(x0 + radius, y0);
         GL11.glVertex2f(x0 + radius, y1);
         GL11.glVertex2f(x1 - radius, y0);
         GL11.glVertex2f(x1 - radius, y1);
         GL11.glEnd();
         GL11.glBegin(5);
         GL11.glVertex2f(x0, y0 + radius);
         GL11.glVertex2f(x0 + radius, y0 + radius);
         GL11.glVertex2f(x0, y1 - radius);
         GL11.glVertex2f(x0 + radius, y1 - radius);
         GL11.glEnd();
         GL11.glBegin(5);
         GL11.glVertex2f(x1, y0 + radius);
         GL11.glVertex2f(x1 - radius, y0 + radius);
         GL11.glVertex2f(x1, y1 - radius);
         GL11.glVertex2f(x1 - radius, y1 - radius);
         GL11.glEnd();
         GL11.glBegin(6);
         float f6 = x1 - radius;
         float f7 = y0 + radius;
         GL11.glVertex2f(f6, f7);
         int j = false;

         float f11;
         int j;
         for(j = 0; j <= 18; ++j) {
            f11 = (float)j * 5.0F;
            GL11.glVertex2f((float)((double)f6 + (double)radius * Math.cos(Math.toRadians((double)f11))), (float)((double)f7 - (double)radius * Math.sin(Math.toRadians((double)f11))));
         }

         GL11.glEnd();
         GL11.glBegin(6);
         f6 = x0 + radius;
         f7 = y0 + radius;
         GL11.glVertex2f(f6, f7);

         for(j = 0; j <= 18; ++j) {
            f11 = (float)j * 5.0F;
            GL11.glVertex2f((float)((double)f6 - (double)radius * Math.cos(Math.toRadians((double)f11))), (float)((double)f7 - (double)radius * Math.sin(Math.toRadians((double)f11))));
         }

         GL11.glEnd();
         GL11.glBegin(6);
         f6 = x0 + radius;
         f7 = y1 - radius;
         GL11.glVertex2f(f6, f7);

         for(j = 0; j <= 18; ++j) {
            f11 = (float)j * 5.0F;
            GL11.glVertex2f((float)((double)f6 - (double)radius * Math.cos(Math.toRadians((double)f11))), (float)((double)f7 + (double)radius * Math.sin(Math.toRadians((double)f11))));
         }

         GL11.glEnd();
         GL11.glBegin(6);
         f6 = x1 - radius;
         f7 = y1 - radius;
         GL11.glVertex2f(f6, f7);

         for(j = 0; j <= 18; ++j) {
            f11 = (float)j * 5.0F;
            GL11.glVertex2f((float)((double)f6 + (double)radius * Math.cos(Math.toRadians((double)f11))), (float)((double)f7 + (double)radius * Math.sin(Math.toRadians((double)f11))));
         }

         GL11.glEnd();
         GL11.glEnable(3553);
         GL11.glEnable(2884);
         GL11.glDisable(3042);
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
         GlStateManager.resetColor();
      }
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.begin(3, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(3, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(1, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      tessellator.draw();
   }

   public static void prepareScissorBox(float x, float y, float x2, float y2) {
      ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
      int factor = scale.getScaleFactor();
      GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
   }

   public static void glColor(Color color) {
      GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
   }

   public static void glColor(int hex) {
      float alpha = (float)(hex >> 24 & 255) / 255.0F;
      float red = (float)(hex >> 16 & 255) / 255.0F;
      float green = (float)(hex >> 8 & 255) / 255.0F;
      float blue = (float)(hex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
   }

   public static void drawFilledESP(Entity entity, Color color, EventRender3D event) {
      double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)event.getPartialTicks() - mc.getRenderManager().viewerPosX;
      double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)event.getPartialTicks() - mc.getRenderManager().viewerPosY;
      double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)event.getPartialTicks() - mc.getRenderManager().viewerPosZ;
      double width = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX - 0.10000000149011612;
      double height = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + 0.20000000298023224;
      GlStateManager.resetColor();
      GL11.glEnable(3042);
      GL11.glPushMatrix();
      drawEntityESP(x, y, z, width, height, 0.0F, 0.0F, 0.0F, 0.0F, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F, 1.6F);
      GL11.glPopMatrix();
      GL11.glDisable(3042);
      GlStateManager.resetColor();
   }

   public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
      GlStateManager.resetColor();
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4f(red, green, blue, alpha);
      drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glLineWidth(lineWdith);
      GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
      drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GlStateManager.resetColor();
   }

   public static void drawBoundingBox(AxisAlignedBB aa) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      tessellator.draw();
      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
      worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
      worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
      tessellator.draw();
   }

   public static Color rainbow(long time, float count, float fade) {
      float hue = ((float)time + (-10.0F + count) * 2.0E8F) / 3.0E9F % -360.0F;
      long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 0.35F, 1.0F)), 16);
      Color c = new Color((int)color);
      return new Color((float)c.getRed() / 255.0F * fade, (float)c.getGreen() / 255.0F * fade, (float)c.getBlue() / 255.0F * fade, (float)c.getAlpha() / 255.0F);
   }

   public static void drawFilledCircle(int xx, int yy, float radius, Color col) {
      int sections = 50;
      double dAngle = 6.283185307179586 / (double)sections;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glBegin(6);

      for(int i = 0; i < sections; ++i) {
         float x = (float)((double)radius * Math.sin((double)i * dAngle));
         float y = (float)((double)radius * Math.cos((double)i * dAngle));
         GL11.glColor4f((float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, (float)col.getAlpha() / 255.0F);
         GL11.glVertex2f((float)xx + x, (float)yy + y);
      }

      GlStateManager.color(0.0F, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawPlayerIcon(EntityPlayer player, int size, int x, int y) {
      if (player != null) {
         mc.getTextureManager().bindTexture(mc.getNetHandler().getPlayerInfo(player.getUniqueID()).getLocationSkin());
         Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);
         if (player.isWearing(EnumPlayerModelParts.HAT)) {
            Gui.drawScaledCustomSizeModalRect(x, y, 40.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);
         }
      }

   }

   public static void drawOnSlot(int size, int xSlotPos, int ySlotPos, int color) {
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      int guiLeft = (sr.getScaledWidth() - 176) / 2;
      int guiTop = (sr.getScaledHeight() - 222) / 2;
      int x = guiLeft + xSlotPos;
      int y = guiTop + ySlotPos;
      if (size != 90) {
         y += (6 - (size - 36) / 9) * 9;
      }

      GL11.glTranslated(0.0, 0.0, 1.0);
      Gui.drawRect(x, y, x + 16, y + 16, color);
      GL11.glTranslated(0.0, 0.0, -1.0);
   }
}
