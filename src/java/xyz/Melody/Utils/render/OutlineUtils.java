/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.render;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Event.events.rendering.EventRenderEntityModel;

public final class OutlineUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void outlineEntity(EventRenderEntityModel event, Color color) {
        OutlineUtils.outlineEntity(event.getModel(), event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getHeadYaw(), event.getHeadPitch(), event.getScaleFactor(), color);
    }

    public static void outlineEntity(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scaleFactor, Color color) {
        boolean fancyGraphics = OutlineUtils.mc.gameSettings.fancyGraphics;
        float gamma = OutlineUtils.mc.gameSettings.gammaSetting;
        OutlineUtils.mc.gameSettings.fancyGraphics = false;
        OutlineUtils.mc.gameSettings.gammaSetting = Float.MAX_VALUE;
        GlStateManager.resetColor();
        OutlineUtils.setColor(color);
        OutlineUtils.renderOne(4.0f);
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scaleFactor);
        OutlineUtils.setColor(color);
        OutlineUtils.renderTwo();
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scaleFactor);
        OutlineUtils.setColor(color);
        OutlineUtils.renderThree();
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scaleFactor);
        OutlineUtils.setColor(color);
        OutlineUtils.renderFour(color);
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scaleFactor);
        OutlineUtils.setColor(color);
        OutlineUtils.renderFive();
        OutlineUtils.setColor(Color.WHITE);
        OutlineUtils.mc.gameSettings.fancyGraphics = fancyGraphics;
        OutlineUtils.mc.gameSettings.gammaSetting = gamma;
    }

    private static void renderOne(float lineWidth) {
        OutlineUtils.checkSetupFBO();
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glEnable(2848);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }

    private static void renderTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }

    private static void renderThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }

    private static void renderFour(Color color) {
        OutlineUtils.setColor(color);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

    private static void renderFive() {
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

    private static void setColor(Color color) {
        GL11.glColor4d((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    private static void checkSetupFBO() {
        Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            OutlineUtils.setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }

    private static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        int stencilDepthBufferId = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencilDepthBufferId);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, OutlineUtils.mc.displayWidth, OutlineUtils.mc.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencilDepthBufferId);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencilDepthBufferId);
    }
}

