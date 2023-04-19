/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.api;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public interface IFoamFixHelper {
    public BlockState createBlockState(Block var1, IProperty<?> ... var2);

    public BlockState createExtendedBlockState(Block var1, IProperty<?>[] var2, IUnlistedProperty<?>[] var3);

    public static class Default
    implements IFoamFixHelper {
        @Override
        public BlockState createBlockState(Block block, IProperty<?> ... properties) {
            return new BlockState(block, properties);
        }

        @Override
        public BlockState createExtendedBlockState(Block block, IProperty<?>[] properties, IUnlistedProperty<?>[] unlistedProperties) {
            return new ExtendedBlockState(block, (IProperty[])properties, unlistedProperties);
        }
    }
}

