/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.common;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import xyz.Melody.Performance.FoamFix.common.FoamyBlockState;
import xyz.Melody.Performance.FoamFix.common.PropertyValueMapper;

public final class FoamyBlockStateContainer
extends BlockState {
    public FoamyBlockStateContainer(Block block, IProperty<?> ... iPropertyArray) {
        super(block, iPropertyArray);
    }

    protected BlockState.StateImplementation createState(Block block, ImmutableMap<IProperty, Comparable> immutableMap, ImmutableMap<IUnlistedProperty<?>, Optional<?>> immutableMap2) {
        return new FoamyBlockState(PropertyValueMapper.getOrCreate(this), block, immutableMap);
    }
}

