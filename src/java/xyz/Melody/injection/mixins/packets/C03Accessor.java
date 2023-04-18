/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.packets;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={C03PacketPlayer.class})
public interface C03Accessor {
    @Accessor(value="x")
    public void setX(double var1);

    @Accessor(value="y")
    public void setY(double var1);

    @Accessor(value="z")
    public void setZ(double var1);

    @Accessor(value="yaw")
    public void setYaw(float var1);

    @Accessor(value="pitch")
    public void setPitch(float var1);

    @Accessor(value="onGround")
    public void setOnGround(boolean var1);
}

