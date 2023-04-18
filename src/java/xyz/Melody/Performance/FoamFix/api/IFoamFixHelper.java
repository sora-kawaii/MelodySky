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
        public BlockState createBlockState(Block block, IProperty<?> ... iPropertyArray) {
            return new BlockState(block, iPropertyArray);
        }

        @Override
        public BlockState createExtendedBlockState(Block block, IProperty<?>[] iPropertyArray, IUnlistedProperty<?>[] iUnlistedPropertyArray) {
            return new ExtendedBlockState(block, (IProperty[])iPropertyArray, iUnlistedPropertyArray);
        }
    }
}

