/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.common;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import xyz.Melody.Performance.FoamFix.common.PropertyValueMapper;

public class FoamyBlockState
extends BlockState.StateImplementation {
    protected final PropertyValueMapper owner;
    protected final ImmutableMap<IProperty, Comparable> properties;
    protected int value;

    public FoamyBlockState(PropertyValueMapper propertyValueMapper, Block block, ImmutableMap<IProperty, Comparable> immutableMap) {
        super(block, immutableMap);
        this.owner = propertyValueMapper;
        this.properties = immutableMap;
    }

    @Override
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> iProperty, V v) {
        Comparable comparable = this.properties.get(iProperty);
        if (comparable == null) {
            throw new IllegalArgumentException("Cannot set property " + iProperty + " as it does not exist in " + this.getBlock().getBlockState());
        }
        if (comparable == v) {
            return this;
        }
        IBlockState iBlockState = this.owner.withProperty(this.value, iProperty, v);
        if (iBlockState == null) {
            throw new IllegalArgumentException("Cannot set property " + iProperty + " to " + v + " on block " + Block.blockRegistry.getNameForObject(this.getBlock()) + ", it is not an allowed value");
        }
        return iBlockState;
    }

    @Override
    public void buildPropertyValueTable(Map<Map<IProperty, Comparable>, BlockState.StateImplementation> map) {
        this.value = this.owner.generateValue(this);
    }
}

