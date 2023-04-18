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
    private Minecraft mc = Minecraft.func_71410_x();

    private void setupUniforms(float f, float f2, float f3) {
        this.blurShader.setUniformi("textureIn", 0);
        this.blurShader.setUniformf("texelSize", 1.0f / (float)this.mc.field_71443_c, 1.0f / (float)this.mc.field_71440_d);
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
        int n = scaledResolution.func_78325_e();
        Scissor.start(f * (float)n, (float)this.mc.field_71440_d - f2 * (float)n - f4 * (float)n, f3 * (float)n, f4 * (float)n);
        this.renderBlur(f5);
        Scissor.end();
    }

    public void renderBlur(float f) {
        this.renderBlur(f, 1, 0, 0, 0);
        this.renderBlur(f, 0, 1, 0, 0);
    }

    private void renderBlur(float f, int n, int n2, int n3, int n4) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        this.framebuffer = GLUtils.createFrameBuffer(this.framebuffer);
        this.framebuffer.func_147614_f();
        this.framebuffer.func_147610_a(true);
        this.blurShader.init();
        this.setupUniforms(n, n2, f);
        GLUtils.bindTexture(this.mc.func_147110_a().field_147617_g);
        ShaderUtils.drawQuads();
        this.framebuffer.func_147609_e();
        this.blurShader.unload();
        this.mc.func_147110_a().func_147610_a(true);
        GLUtils.bindTexture(this.framebuffer.field_147617_g);
        ShaderUtils.drawQuads();
        GlStateManager.func_179117_G();
        GlStateManager.func_179144_i((int)0);
        GlStateManager.func_179121_F();
    }
}

