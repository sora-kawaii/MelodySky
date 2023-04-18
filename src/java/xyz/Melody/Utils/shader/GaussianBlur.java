/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.shader;

import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import xyz.Melody.Utils.math.MathUtil;
import xyz.Melody.Utils.render.Scissor;
import xyz.Melody.Utils.render.gl.GLUtils;
import xyz.Melody.Utils.shader.ShaderUtils;

public final class GaussianBlur {
    private ShaderUtils blurShader = new ShaderUtils("Melody/GLSL/Shaders/gaussian.frag");
    private Framebuffer framebuffer = new Framebuffer(1, 1, false);
    private Minecraft mc = Minecraft.getMinecraft();

    private void setupUniforms(float f, float f2, float f3) {
        this.blurShader.setUniformi("textureIn", 0);
        this.blurShader.setUniformf("texelSize", 1.0f / (float)this.mc.displayWidth, 1.0f / (float)this.mc.displayHeight);
        this.blurShader.setUniformf("direction", f, f2);
        this.blurShader.setUniformf("radius", f3);
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(256);
        int n = 0;
        while ((float)n <= f3) {
            floatBuffer.put(MathUtil.calculateGaussianValue(n, f3 / 2.0f));
            ++n;
        }
        floatBuffer.rewind();
        GL20.glUniform1(this.blurShader.getUniform("weights"), floatBuffer);
    }

    public void blurArea(float f, float f2, float f3, float f4, float f5) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int n = scaledResolution.getScaleFactor();
        Scissor.start(f * (float)n, (float)this.mc.displayHeight - f2 * (float)n - f4 * (float)n, f3 * (float)n, f4 * (float)n);
        this.renderBlur(f5);
        Scissor.end();
    }

    public void renderBlur(float f) {
        this.renderBlur(f, 1, 0, 0, 0);
        this.renderBlur(f, 0, 1, 0, 0);
    }

    private void renderBlur(float f, int n, int n2, int n3, int n4) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        this.framebuffer = GLUtils.createFrameBuffer(this.framebuffer);
        this.framebuffer.framebufferClear();
        this.framebuffer.bindFramebuffer(true);
        this.blurShader.init();
        this.setupUniforms(n, n2, f);
        GLUtils.bindTexture(this.mc.getFramebuffer().framebufferTexture);
        ShaderUtils.drawQuads();
        this.framebuffer.unbindFramebuffer();
        this.blurShader.unload();
        this.mc.getFramebuffer().bindFramebuffer(true);
        GLUtils.bindTexture(this.framebuffer.framebufferTexture);
        ShaderUtils.drawQuads();
        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
        GlStateManager.popMatrix();
    }
}

