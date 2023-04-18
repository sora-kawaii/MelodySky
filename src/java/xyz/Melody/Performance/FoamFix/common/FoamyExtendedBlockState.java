/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.common;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import xyz.Melody.Performance.FoamFix.common.FoamyBlockState;
import xyz.Melody.Performance.FoamFix.common.PropertyValueMapper;

public final class FoamyExtendedBlockState
extends FoamyBlockState
implements IExtendedBlockState {
    private final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties;

    public FoamyExtendedBlockState(PropertyValueMapper propertyValueMapper, Block block, ImmutableMap<IProperty, Comparable> immutableMap, ImmutableMap<IUnlistedProperty<?>, Optional<?>> immutableMap2) {
        super(propertyValueMapper, block, immutableMap);
        this.unlistedProperties = immutableMap2;
    }

    public FoamyExtendedBlockState(PropertyValueMapper propertyValueMapper, Block block, ImmutableMap<IProperty, Comparable> immutableMap, ImmutableMap<IUnlistedProperty<?>, Optional<?>> immutableMap2, int n) {
        super(propertyValueMapper, block, immutableMap);
        this.unlistedProperties = immutableMap2;
        this.value = n;
    }

    @Override
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> iProperty, V v) {
        if (!this.getProperties().containsKey(iProperty)) {
            throw new IllegalArgumentException("Cannot set property " + iProperty + " as it does not exist in " + this.getBlock().getBlockState());
        }
        if (!iProperty.getAllowedValues().contains(v)) {
            throw new IllegalArgumentException("Cannot set property " + iProperty + " to " + this.value + " on block " + Block.blockRegistry.getNameForObject(this.getBlock()) + ", it is not an allowed value");
        }
        if (this.getProperties().get(iProperty) == v) {
            return this;
        }
        int n = this.owner.withPropertyValue(this.value, iProperty, v);
        if (n == -1) {
            throw new IllegalArgumentException("Cannot set property " + iProperty + " because FoamFix could not find a mapping for it! Please reproduce without FoamFix first!");
        }
        IBlockState iBlockState = this.owner.getPropertyByValue(n);
        if (Iterables.all(this.unlistedProperties.values(), Predicates.equalTo(Optional.absent()))) {
            return iBlockState;
        }
        return new FoamyExtendedBlockState(this.owner, this.getBlock(), iBlockState.getProperties(), this.unlistedProperties, n);
    }

    @Override
    public <V> IExtendedBlockState withProperty(IUnlistedProperty<V> iUnlistedProperty, V v) {
        if (!this.unlistedProperties.containsKey(iUnlistedProperty)) {
            throw new IllegalArgumentException("Cannot set unlisted property " + iUnlistedProperty + " as it does not exist in " + this.getBlock().getBlockState());
        }
        if (!iUnlistedProperty.isValid(v)) {
            throw new IllegalArgumentException("Cannot set unlisted property " + iUnlistedProperty + " to " + v + " on block " + Block.blockRegistry.getNameForObject(this.getBlock()) + ", it is not an allowed value");
        }
        HashMap hashMap = new HashMap(this.unlistedProperties);
        hashMap.put(iUnlistedProperty, Optional.fromNullable(v));
        if (Iterables.all(hashMap.values(), Predicates.equalTo(Optional.absent()))) {
            return (IExtendedBlockState)this.owner.getPropertyByValue(this.value);
        }
        return new FoamyExtendedBlockState(this.owner, this.getBlock(), this.getProperties(), ImmutableMap.copyOf(hashMap), this.value);
    }

    @Override
    public Collection<IUnlistedProperty<?>> getUnlistedNames() {
        return Collections.unmodifiableCollection(this.unlistedProperties.keySet());
    }

    @Override
    public <V> V getValue(IUnlistedProperty<V> iUnlistedProperty) {
        if (!this.unlistedProperties.containsKey(iUnlistedProperty)) {
            throw new IllegalArgumentException("Cannot get unlisted property " + iUnlistedProperty + " as it does not exist in " + this.getBlock().getBlockState());
        }
        return iUnlistedProperty.getType().cast(this.unlistedProperties.get(iUnlistedProperty).orNull());
    }

    @Override
    public ImmutableMap<IUnlistedProperty<?>, Optional<?>> getUnlistedProperties() {
        return this.unlistedProperties;
    }

    @Override
    public IBlockState getClean() {
        return this.owner.getPropertyByValue(this.value);
    }
}

