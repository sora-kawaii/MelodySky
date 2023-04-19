/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.packets;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={S12PacketEntityVelocity.class})
public interface S12Accessor {
    @Accessor(value="motionX")
    public void setMotionX(int var1);

    @Accessor(value="motionY")
    public void setMotionY(int var1);

    @Accessor(value="motionZ")
    public void setMotionZ(int var1);
}

