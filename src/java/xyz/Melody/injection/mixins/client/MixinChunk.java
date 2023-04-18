/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.world.BlockChangeEvent;

@Mixin(value={Chunk.class})
public abstract class MixinChunk {
    @Shadow
    public abstract IBlockState getBlockState(BlockPos var1);

    @Inject(method="setBlockState", at={@At(value="HEAD")})
    private void onBlockChange(BlockPos blockPos, IBlockState iBlockState, CallbackInfoReturnable<IBlockState> callbackInfoReturnable) {
        IBlockState iBlockState2 = this.getBlockState(blockPos);
        if (iBlockState2 != iBlockState && Minecraft.getMinecraft().theWorld != null) {
            EventBus.getInstance().call(new BlockChangeEvent(blockPos, iBlockState2, iBlockState));
        }
    }
}

