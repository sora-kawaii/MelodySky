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

    public FoamyBlockState(PropertyValueMapper owner, Block blockIn, ImmutableMap<IProperty, Comparable> propertiesIn) {
        super(blockIn, propertiesIn);
        this.owner = owner;
        this.properties = propertiesIn;
    }

    @Override
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
        Comparable comparable = this.properties.get(property);
        if (comparable == null) {
            throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.getBlock().getBlockState());
        }
        if (comparable == value) {
            return this;
        }
        IBlockState state = this.owner.withProperty(this.value, property, value);
        if (state == null) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.blockRegistry.getNameForObject(this.getBlock()) + ", it is not an allowed value");
        }
        return state;
    }

    @Override
    public void buildPropertyValueTable(Map<Map<IProperty, Comparable>, BlockState.StateImplementation> map) {
        this.value = this.owner.generateValue(this);
    }
}

