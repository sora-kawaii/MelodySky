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
    private Minecraft mc = Minecraft.func_71410_x();
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
                this.shader = new ShaderGroup(this.mc.func_110434_K(), this.mc.func_110442_L(), this.mc.func_147110_a(), this.location);
                this.shader.func_148026_a(this.mc.field_71443_c, this.mc.field_71440_d);
            }
            catch (JsonSyntaxException | IOException exception) {
                Client.instance.logger.error("Could not load motion blur shader", (Throwable)exception);
                return null;
            }
        }
        if (this.shaderBlur != (f = ((Double)this.size.getValue()).floatValue() / 12.0f)) {
            ((IMixinShaderGroup)((Object)this.shader)).getListShaders().forEach(shader -> {
                ShaderUniform shaderUniform = shader.func_148043_c().func_147991_a("BlurFactor");
                if (shaderUniform != null) {
                    shaderUniform.func_148090_a(f);
                }
            });
            this.shaderBlur = f;
        }
        return this.shader;
    }
}

