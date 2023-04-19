/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.ShaderBG.Shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;
import xyz.Melody.GUI.ClickNew.Opacity;
import xyz.Melody.GUI.ShaderBG.Shader;

public final class BackgroundShader
extends Shader {
    public static final BackgroundShader BACKGROUND_SHADER = new BackgroundShader();
    private Opacity op = new Opacity(0);

    public BackgroundShader() {
        super("pixelBG.frag");
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("iResolution");
        this.setupUniform("time");
        this.setupUniform("iMouse");
    }

    @Override
    public void updateUniforms() {
        int timeID;
        int mouse;
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int resolutionID = this.getUniform("iResolution");
        if (resolutionID > -1) {
            GL20.glUniform2f(resolutionID, (float)scaledResolution.getScaledWidth() * 2.0f, (float)scaledResolution.getScaledHeight() * 2.0f);
        }
        if ((mouse = this.getUniform("iMouse")) > -1) {
            GL20.glUniform4f(mouse, this.op.getOpacity() * 10.0f, (float)new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() * 1.2f, 0.0f, 0.0f);
        }
        if ((timeID = this.getUniform("time")) > -1) {
            GL20.glUniform1f(timeID, this.op.getOpacity());
        }
        this.op.interp(this.op.getOpacity() + 0.05f, 0.01f);
    }
}

