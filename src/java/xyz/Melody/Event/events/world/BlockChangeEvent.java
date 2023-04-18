/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import xyz.Melody.Event.Event;

public class BlockChangeEvent
extends Event {
    private BlockPos position;
    private IBlockState oldBlock;
    private IBlockState newBlock;

    public BlockChangeEvent(BlockPos blockPos, IBlockState iBlockState, IBlockState iBlockState2) {
        this.position = blockPos;
        this.oldBlock = iBlockState;
        this.newBlock = iBlockState2;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public IBlockState getOldBlock() {
        return this.oldBlock;
    }

    public IBlockState getNewBlock() {
        return this.newBlock;
    }
}

