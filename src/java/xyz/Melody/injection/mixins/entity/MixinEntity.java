/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.entity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={Entity.class})
public abstract class MixinEntity {
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public float rotationPitch;
    @Shadow
    public float rotationYaw;
    @Shadow
    public boolean onGround;
}

