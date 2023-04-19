/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.common;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import xyz.Melody.Performance.FoamFix.api.IFoamFixHelper;
import xyz.Melody.Performance.FoamFix.common.FoamyBlockStateContainer;
import xyz.Melody.Performance.FoamFix.common.FoamyExtendedBlockStateContainer;

public final class FoamFixHelper
implements IFoamFixHelper {
    public BlockState createBlockState(Block block, IProperty ... properties) {
        return new FoamyBlockStateContainer(block, (IProperty<?>[])properties);
    }

    public BlockState createExtendedBlockState(Block block, IProperty[] properties, IUnlistedProperty[] unlistedProperties) {
        return new FoamyExtendedBlockStateContainer(block, (IProperty<?>[])properties, (IUnlistedProperty<?>[])unlistedProperties);
    }
}

