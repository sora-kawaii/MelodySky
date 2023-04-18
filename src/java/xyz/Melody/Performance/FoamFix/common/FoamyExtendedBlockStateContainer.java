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
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import xyz.Melody.Performance.FoamFix.common.FoamyBlockState;
import xyz.Melody.Performance.FoamFix.common.FoamyExtendedBlockState;
import xyz.Melody.Performance.FoamFix.common.PropertyValueMapper;

public final class FoamyExtendedBlockStateContainer
extends ExtendedBlockState {
    public FoamyExtendedBlockStateContainer(Block block, IProperty<?>[] iPropertyArray, IUnlistedProperty<?>[] iUnlistedPropertyArray) {
        super(block, (IProperty[])iPropertyArray, iUnlistedPropertyArray);
    }

    @Override
    protected BlockState.StateImplementation createState(Block block, ImmutableMap<IProperty, Comparable> immutableMap, ImmutableMap<IUnlistedProperty<?>, Optional<?>> immutableMap2) {
        if (immutableMap2 == null || immutableMap2.isEmpty()) {
            return new FoamyBlockState(PropertyValueMapper.getOrCreate(this), block, immutableMap);
        }
        return new FoamyExtendedBlockState(PropertyValueMapper.getOrCreate(this), block, immutableMap, immutableMap2);
    }
}

