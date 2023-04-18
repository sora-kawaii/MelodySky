/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.ShaderBG;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import xyz.Melody.GUI.ShaderBG.Shader;

public abstract class FramebufferShader
extends Shader {
    private static Framebuffer framebuffer;
    protected float red;
    protected float green;
    protected float blue;
    protected float alpha = 1.0f;
    protected float radius = 2.0f;
    protected float quality = 1.0f;
    protected static Minecraft mc;
    private boolean entityShadows;

    public FramebufferShader(String string) {
        super(string);
    }

    public void startDraw(float f) {
        GlStateManager.func_179141_d();
        GlStateManager.func_179094_E();
        GlStateManager.func_179123_a();
        framebuffer = this.setupFrameBuffer(framebuffer);
        framebuffer.func_147614_f();
        framebuffer.func_147610_a(true);
        this.entityShadows = FramebufferShader.mc.field_71474_y.field_181151_V;
        FramebufferShader.mc.field_71474_y.field_181151_V = false;
    }

    public void stopDraw(Color color, float f, float f2) {
        FramebufferShader.mc.field_71474_y.field_181151_V = this.entityShadows;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        mc.func_147110_a().func_147610_a(true);
        this.red = (float)color.getRed() / 255.0f;
        this.green = (float)color.getGreen() / 255.0f;
        this.blue = (float)color.getBlue() / 255.0f;
        this.alpha = (float)color.getAlpha() / 255.0f;
        this.radius = f;
        this.quality = f2;
        FramebufferShader.mc.field_71460_t.func_175072_h();
        RenderHelper.func_74518_a();
        this.startShader();
        FramebufferShader.mc.field_71460_t.func_78478_c();
        this.drawFramebuffer(framebuffer);
        this.stopShader();
        FramebufferShader.mc.field_71460_t.func_175072_h();
        GlStateManager.func_179121_F();
        GlStateManager.func_179099_b();
    }

    public Framebuffer setupFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer != null) {
            framebuffer.func_147608_a();
        }
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        framebuffer = new Framebuffer(scaledResolution.func_78326_a(), scaledResolution.func_78328_b(), true);
        return framebuffer;
    }

    public void drawFramebuffer(Framebuffer framebuffer) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        GL11.glBindTexture(3553, framebuffer.field_147617_g);
        GL11.glBegin(7);
        GL11.glTexCoord2d(0.0, 1.0);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glTexCoord2d(0.0, 0.0);
        GL11.glVertex2d(0.0, scaledResolution.func_78328_b());
        GL11.glTexCoord2d(1.0, 0.0);
        GL11.glVertex2d(scaledResolution.func_78326_a(), scaledResolution.func_78328_b());
        GL11.glTexCoord2d(1.0, 1.0);
        GL11.glVertex2d(scaledResolution.func_78326_a(), 0.0);
        GL11.glEnd();
        GL20.glUseProgram(0);
    }

    static {
        mc = Minecraft.func_71410_x();
    }
}

