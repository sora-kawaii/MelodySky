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
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        framebuffer = this.setupFrameBuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        this.entityShadows = FramebufferShader.mc.gameSettings.entityShadows;
        FramebufferShader.mc.gameSettings.entityShadows = false;
    }

    public void stopDraw(Color color, float f, float f2) {
        FramebufferShader.mc.gameSettings.entityShadows = this.entityShadows;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        mc.getFramebuffer().bindFramebuffer(true);
        this.red = (float)color.getRed() / 255.0f;
        this.green = (float)color.getGreen() / 255.0f;
        this.blue = (float)color.getBlue() / 255.0f;
        this.alpha = (float)color.getAlpha() / 255.0f;
        this.radius = f;
        this.quality = f2;
        FramebufferShader.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        this.startShader();
        FramebufferShader.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(framebuffer);
        this.stopShader();
        FramebufferShader.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public Framebuffer setupFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer != null) {
            framebuffer.deleteFramebuffer();
        }
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        framebuffer = new Framebuffer(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), true);
        return framebuffer;
    }

    public void drawFramebuffer(Framebuffer framebuffer) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glBindTexture(3553, framebuffer.framebufferTexture);
        GL11.glBegin(7);
        GL11.glTexCoord2d(0.0, 1.0);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glTexCoord2d(0.0, 0.0);
        GL11.glVertex2d(0.0, scaledResolution.getScaledHeight());
        GL11.glTexCoord2d(1.0, 0.0);
        GL11.glVertex2d(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        GL11.glTexCoord2d(1.0, 1.0);
        GL11.glVertex2d(scaledResolution.getScaledWidth(), 0.0);
        GL11.glEnd();
        GL20.glUseProgram(0);
    }

    static {
        mc = Minecraft.getMinecraft();
    }
}

