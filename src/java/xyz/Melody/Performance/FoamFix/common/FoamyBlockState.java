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
    protected final ImmutableMap<IProperty, Comparable> field_177237_b;
    protected int value;

    public FoamyBlockState(PropertyValueMapper propertyValueMapper, Block block, ImmutableMap<IProperty, Comparable> immutableMap) {
        super(block, immutableMap);
        this.owner = propertyValueMapper;
        this.field_177237_b = immutableMap;
    }

    public <T extends Comparable<T>, V extends T> IBlockState func_177226_a(IProperty<T> iProperty, V v) {
        Comparable comparable = this.field_177237_b.get(iProperty);
        if (comparable == null) {
            throw new IllegalArgumentException("Cannot set property " + iProperty + " as it does not exist in " + this.func_177230_c().func_176194_O());
        }
        if (comparable == v) {
            return this;
        }
        IBlockState iBlockState = this.owner.withProperty(this.value, iProperty, v);
        if (iBlockState == null) {
            throw new IllegalArgumentException("Cannot set property " + iProperty + " to " + v + " on block " + Block.field_149771_c.func_177774_c(this.func_177230_c()) + ", it is not an allowed value");
        }
        return iBlockState;
    }

    public void func_177235_a(Map<Map<IProperty, Comparable>, BlockState.StateImplementation> map) {
        this.value = this.owner.generateValue(this);
    }
}

