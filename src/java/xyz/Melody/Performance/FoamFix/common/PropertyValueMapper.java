/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.common;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.MathHelper;

public final class PropertyValueMapper {
    private static final Comparator<IProperty> COMPARATOR_BIT_FITNESS = new Comparator<IProperty>(){

        @Override
        public int compare(IProperty first, IProperty second) {
            int diff1 = PropertyValueMapper.getPropertyEntry(first).bitSize - first.getAllowedValues().size();
            int diff2 = PropertyValueMapper.getPropertyEntry(second).bitSize - second.getAllowedValues().size();
            return diff1 - diff2;
        }

        @Override
        public int compare(Object object, Object object2) {
            return this.compare((IProperty)object, (IProperty)object2);
        }
    };
    private static final Map<IProperty, Entry> entryMap = new IdentityHashMap<IProperty, Entry>();
    private static final Map<BlockState, PropertyValueMapper> mapperMap = new IdentityHashMap<BlockState, PropertyValueMapper>();
    private final Entry[] entryList;
    private final TObjectIntMap<IProperty> entryPositionMap;
    private final IBlockState[] stateMap;

    public PropertyValueMapper(BlockState container) {
        Collection<IProperty> properties = container.getProperties();
        this.entryList = new Entry[properties.size()];
        ArrayList<IProperty> propertiesSortedFitness = new ArrayList<IProperty>(properties);
        propertiesSortedFitness.sort(COMPARATOR_BIT_FITNESS);
        int i = 0;
        for (IProperty p : propertiesSortedFitness) {
            this.entryList[i++] = PropertyValueMapper.getPropertyEntry(p);
        }
        this.entryPositionMap = new TObjectIntHashMap<IProperty>(10, 0.5f, -1);
        int bitPos = 0;
        Entry lastEntry = null;
        for (Entry ee : this.entryList) {
            this.entryPositionMap.put(ee.property, bitPos);
            bitPos += ee.bits;
            lastEntry = ee;
        }
        this.stateMap = lastEntry == null ? new IBlockState[1 << bitPos] : new IBlockState[(1 << bitPos - lastEntry.bits) * lastEntry.property.getAllowedValues().size()];
    }

    public static PropertyValueMapper getOrCreate(BlockState owner) {
        PropertyValueMapper e = mapperMap.get(owner);
        if (e == null) {
            e = new PropertyValueMapper(owner);
            mapperMap.put(owner, e);
        }
        return e;
    }

    protected static Entry getPropertyEntry(IProperty property) {
        Entry e = entryMap.get(property);
        if (e == null) {
            e = new Entry(property, null);
            entryMap.put(property, e);
        }
        return e;
    }

    public int generateValue(IBlockState state) {
        int bitPos = 0;
        int value = 0;
        for (Entry e : this.entryList) {
            value |= e.get(state.getValue(e.property)) << bitPos;
            bitPos += e.bits;
        }
        this.stateMap[value] = state;
        return value;
    }

    public <T extends Comparable<T>, V extends T> IBlockState withProperty(int value, IProperty<T> property, V propertyValue) {
        Entry e;
        int bitPos = this.entryPositionMap.get(property);
        if (bitPos >= 0 && (e = PropertyValueMapper.getPropertyEntry(property)) != null) {
            int nv = e.get(propertyValue);
            if (nv < 0) {
                return null;
            }
            value &= ~(e.bitSize - 1 << bitPos);
            return this.stateMap[value |= nv << bitPos];
        }
        return null;
    }

    public IBlockState getPropertyByValue(int value) {
        return this.stateMap[value];
    }

    public <T extends Comparable<T>, V extends T> int withPropertyValue(int value, IProperty<T> property, V propertyValue) {
        Entry e;
        int bitPos = this.entryPositionMap.get(property);
        if (bitPos >= 0 && (e = PropertyValueMapper.getPropertyEntry(property)) != null) {
            int nv = e.get(propertyValue);
            if (nv < 0) {
                return -1;
            }
            value &= ~(e.bitSize - 1 << bitPos);
            return value |= nv << bitPos;
        }
        return -1;
    }

    public static class Entry {
        private final IProperty property;
        private final TObjectIntMap values;
        private final int bitSize;
        private final int bits;

        private Entry(IProperty property) {
            this.property = property;
            this.values = new TObjectIntHashMap(10, 0.5f, -1);
            this.bitSize = MathHelper.roundUpToPowerOfTwo(property.getAllowedValues().size());
            int bits = 0;
            for (int b = this.bitSize; b != 0; b >>= 1) {
                ++bits;
            }
            this.bits = bits;
            int i = 0;
            for (Object o : property.getAllowedValues()) {
                this.values.put(o, i++);
            }
        }

        public int get(Object v) {
            return this.values.get(v);
        }

        Entry(IProperty x0, I x1) {
            this(x0);
        }
    }
}

