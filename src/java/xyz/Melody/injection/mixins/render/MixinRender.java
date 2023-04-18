/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Render.class})
public abstract class MixinRender<T extends Entity> {
    @Shadow
    protected abstract void renderName(T var1, double var2, double var4, double var6);

    @Overwrite
    public void doRender(T t, double d, double d2, double d3, float f, float f2) {
        this.renderName(t, d, d2, d3);
    }
}

