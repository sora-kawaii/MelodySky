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
    private static Minecraft mc = Minecraft.func_71410_x();

    public static void outlineEntity(EventRenderEntityModel eventRenderEntityModel, Color color) {
        OutlineUtils.outlineEntity(eventRenderEntityModel.getModel(), eventRenderEntityModel.getEntity(), eventRenderEntityModel.getLimbSwing(), eventRenderEntityModel.getLimbSwingAmount(), eventRenderEntityModel.getAgeInTicks(), eventRenderEntityModel.getHeadYaw(), eventRenderEntityModel.getHeadPitch(), eventRenderEntityModel.getScaleFactor(), color);
    }

    public static void outlineEntity(ModelBase modelBase, Entity entity, float f, float f2, float f3, float f4, float f5, float f6, Color color) {
        boolean bl = OutlineUtils.mc.field_71474_y.field_74347_j;
        float f7 = OutlineUtils.mc.field_71474_y.field_74333_Y;
        OutlineUtils.mc.field_71474_y.field_74347_j = false;
        OutlineUtils.mc.field_71474_y.field_74333_Y = Float.MAX_VALUE;
        GlStateManager.func_179117_G();
        OutlineUtils.setColor(color);
        OutlineUtils.renderOne(4.0f);
        modelBase.func_78088_a(entity, f, f2, f3, f4, f5, f6);
        OutlineUtils.setColor(color);
        OutlineUtils.renderTwo();
        modelBase.func_78088_a(entity, f, f2, f3, f4, f5, f6);
        OutlineUtils.setColor(color);
        OutlineUtils.renderThree();
        modelBase.func_78088_a(entity, f, f2, f3, f4, f5, f6);
        OutlineUtils.setColor(color);
        OutlineUtils.renderFour(color);
        modelBase.func_78088_a(entity, f, f2, f3, f4, f5, f6);
        OutlineUtils.setColor(color);
        OutlineUtils.renderFive();
        OutlineUtils.setColor(Color.WHITE);
        OutlineUtils.mc.field_71474_y.field_74347_j = bl;
        OutlineUtils.mc.field_71474_y.field_74333_Y = f7;
    }

    private static void renderOne(float f) {
        OutlineUtils.checkSetupFBO();
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
        OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)240.0f, (float)240.0f);
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
        Framebuffer framebuffer = Minecraft.func_71410_x().func_147110_a();
        if (framebuffer != null && framebuffer.field_147624_h > -1) {
            OutlineUtils.setupFBO(framebuffer);
            framebuffer.field_147624_h = -1;
        }
    }

    private static void setupFBO(Framebuffer framebuffer) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.field_147624_h);
        int n = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, n);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, OutlineUtils.mc.field_71443_c, OutlineUtils.mc.field_71440_d);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, n);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, n);
    }
}

