/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import xyz.Melody.Client;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.injection.mixins.shader.IMixinShaderGroup;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class MotionBlur
extends Module {
    private ResourceLocation location = new ResourceLocation("minecraft:shaders/post/motion_blur.json");
    private Minecraft mc = Minecraft.getMinecraft();
    private ShaderGroup shader;
    private float shaderBlur;
    public Numbers<Double> size = new Numbers<Double>("Blur Amount", 5.0, 1.0, 10.0, 0.5);

    public MotionBlur() {
        super("MotionBlur", ModuleType.Others);
        this.addValues(this.size);
    }

    public ShaderGroup getShader() {
        float f;
        if (this.shader == null) {
            this.shaderBlur = Float.NaN;
            try {
                this.shader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), this.location);
                this.shader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            }
            catch (JsonSyntaxException | IOException exception) {
                Client.instance.logger.error("Could not load motion blur shader", (Throwable)exception);
                return null;
            }
        }
        if (this.shaderBlur != (f = ((Double)this.size.getValue()).floatValue() / 12.0f)) {
            ((IMixinShaderGroup)((Object)this.shader)).getListShaders().forEach(shader -> {
                ShaderUniform shaderUniform = shader.getShaderManager().getShaderUniform("BlurFactor");
                if (shaderUniform != null) {
                    shaderUniform.set(f);
                }
            });
            this.shaderBlur = f;
        }
        return this.shader;
    }
}

