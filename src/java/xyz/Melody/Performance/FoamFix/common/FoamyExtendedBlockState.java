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

    public FoamyExtendedBlockState(PropertyValueMapper owner, Block block, ImmutableMap<IProperty, Comparable> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
        super(owner, block, properties);
        this.unlistedProperties = unlistedProperties;
    }

    public FoamyExtendedBlockState(PropertyValueMapper owner, Block block, ImmutableMap<IProperty, Comparable> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties, int value) {
        super(owner, block, properties);
        this.unlistedProperties = unlistedProperties;
        this.value = value;
    }

    @Override
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V propertyValue) {
        if (!this.getProperties().containsKey(property)) {
            throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.getBlock().getBlockState());
        }
        if (!property.getAllowedValues().contains(propertyValue)) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + this.value + " on block " + Block.blockRegistry.getNameForObject(this.getBlock()) + ", it is not an allowed value");
        }
        if (this.getProperties().get(property) == propertyValue) {
            return this;
        }
        int newValue = this.owner.withPropertyValue(this.value, property, propertyValue);
        if (newValue == -1) {
            throw new IllegalArgumentException("Cannot set property " + property + " because FoamFix could not find a mapping for it! Please reproduce without FoamFix first!");
        }
        IBlockState state = this.owner.getPropertyByValue(newValue);
        if (Iterables.all(this.unlistedProperties.values(), Predicates.equalTo(Optional.absent()))) {
            return state;
        }
        return new FoamyExtendedBlockState(this.owner, this.getBlock(), state.getProperties(), this.unlistedProperties, newValue);
    }

    @Override
    public <V> IExtendedBlockState withProperty(IUnlistedProperty<V> property, V value) {
        if (!this.unlistedProperties.containsKey(property)) {
            throw new IllegalArgumentException("Cannot set unlisted property " + property + " as it does not exist in " + this.getBlock().getBlockState());
        }
        if (!property.isValid(value)) {
            throw new IllegalArgumentException("Cannot set unlisted property " + property + " to " + value + " on block " + Block.blockRegistry.getNameForObject(this.getBlock()) + ", it is not an allowed value");
        }
        HashMap newMap = new HashMap(this.unlistedProperties);
        newMap.put(property, Optional.fromNullable(value));
        if (Iterables.all(newMap.values(), Predicates.equalTo(Optional.absent()))) {
            return (IExtendedBlockState)this.owner.getPropertyByValue(this.value);
        }
        return new FoamyExtendedBlockState(this.owner, this.getBlock(), this.getProperties(), ImmutableMap.copyOf(newMap), this.value);
    }

    @Override
    public Collection<IUnlistedProperty<?>> getUnlistedNames() {
        return Collections.unmodifiableCollection(this.unlistedProperties.keySet());
    }

    @Override
    public <V> V getValue(IUnlistedProperty<V> property) {
        if (!this.unlistedProperties.containsKey(property)) {
            throw new IllegalArgumentException("Cannot get unlisted property " + property + " as it does not exist in " + this.getBlock().getBlockState());
        }
        return property.getType().cast(this.unlistedProperties.get(property).orNull());
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

