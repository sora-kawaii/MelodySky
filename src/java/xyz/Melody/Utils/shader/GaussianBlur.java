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

    private void setupUniforms(float dir1, float dir2, float radius) {
        this.blurShader.setUniformi("textureIn", 0);
        this.blurShader.setUniformf("texelSize", 1.0f / (float)this.mc.displayWidth, 1.0f / (float)this.mc.displayHeight);
        this.blurShader.setUniformf("direction", dir1, dir2);
        this.blurShader.setUniformf("radius", radius);
        FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        int i = 0;
        while ((float)i <= radius) {
            weightBuffer.put(MathUtil.calculateGaussianValue(i, radius / 2.0f));
            ++i;
        }
        weightBuffer.rewind();
        GL20.glUniform1(this.blurShader.getUniform("weights"), weightBuffer);
    }

    public void blurArea(float x, float y, float width, float height, float radius) {
        ScaledResolution scale = new ScaledResolution(this.mc);
        int factor = scale.getScaleFactor();
        Scissor.start(x * (float)factor, (float)this.mc.displayHeight - y * (float)factor - height * (float)factor, width * (float)factor, height * (float)factor);
        this.renderBlur(radius);
        Scissor.end();
    }

    public void renderBlur(float radius) {
        this.renderBlur(radius, 1, 0, 0, 0);
        this.renderBlur(radius, 0, 1, 0, 0);
    }

    private void renderBlur(float radius, int fhd, int fvd, int shd, int svd) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        this.framebuffer = GLUtils.createFrameBuffer(this.framebuffer);
        this.framebuffer.framebufferClear();
        this.framebuffer.bindFramebuffer(true);
        this.blurShader.init();
        this.setupUniforms(fhd, fvd, radius);
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

