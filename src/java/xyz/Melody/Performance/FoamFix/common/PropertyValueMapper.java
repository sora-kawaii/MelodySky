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
        public int compare(IProperty iProperty, IProperty iProperty2) {
            int n = PropertyValueMapper.getPropertyEntry(iProperty).bitSize - iProperty.func_177700_c().size();
            int n2 = PropertyValueMapper.getPropertyEntry(iProperty2).bitSize - iProperty2.func_177700_c().size();
            return n - n2;
        }
    };
    private static final Map<IProperty, Entry> entryMap = new IdentityHashMap<IProperty, Entry>();
    private static final Map<BlockState, PropertyValueMapper> mapperMap = new IdentityHashMap<BlockState, PropertyValueMapper>();
    private final Entry[] entryList;
    private final TObjectIntMap<IProperty> entryPositionMap;
    private final IBlockState[] stateMap;

    /*
     * WARNING - void declaration
     */
    public PropertyValueMapper(BlockState blockState) {
        void var6_9;
        Collection collection = blockState.func_177623_d();
        this.entryList = new Entry[collection.size()];
        ArrayList<IProperty> arrayList = new ArrayList<IProperty>(collection);
        arrayList.sort(COMPARATOR_BIT_FITNESS);
        int n = 0;
        for (IProperty object2 : arrayList) {
            this.entryList[n++] = PropertyValueMapper.getPropertyEntry(object2);
        }
        this.entryPositionMap = new TObjectIntHashMap<IProperty>(10, 0.5f, -1);
        int n2 = 0;
        Object var6_8 = null;
        for (Entry entry : this.entryList) {
            this.entryPositionMap.put(entry.property, n2);
            n2 += entry.bits;
            Entry entry2 = entry;
        }
        this.stateMap = var6_9 == null ? new IBlockState[1 << n2] : new IBlockState[(1 << n2 - ((Entry)var6_9).bits) * ((Entry)var6_9).property.func_177700_c().size()];
    }

    public static PropertyValueMapper getOrCreate(BlockState blockState) {
        PropertyValueMapper propertyValueMapper = mapperMap.get(blockState);
        if (propertyValueMapper == null) {
            propertyValueMapper = new PropertyValueMapper(blockState);
            mapperMap.put(blockState, propertyValueMapper);
        }
        return propertyValueMapper;
    }

    protected static Entry getPropertyEntry(IProperty iProperty) {
        Entry entry = entryMap.get(iProperty);
        if (entry == null) {
            entry = new Entry(iProperty);
            entryMap.put(iProperty, entry);
        }
        return entry;
    }

    public int generateValue(IBlockState iBlockState) {
        int n = 0;
        int n2 = 0;
        for (Entry entry : this.entryList) {
            n2 |= entry.get(iBlockState.func_177229_b(entry.property)) << n;
            n += entry.bits;
        }
        this.stateMap[n2] = iBlockState;
        return n2;
    }

    public <T extends Comparable<T>, V extends T> IBlockState withProperty(int n, IProperty<T> iProperty, V v) {
        Entry entry;
        int n2 = this.entryPositionMap.get(iProperty);
        if (n2 >= 0 && (entry = PropertyValueMapper.getPropertyEntry(iProperty)) != null) {
            int n3 = entry.get(v);
            if (n3 < 0) {
                return null;
            }
            n &= ~(entry.bitSize - 1 << n2);
            return this.stateMap[n |= n3 << n2];
        }
        return null;
    }

    public IBlockState getPropertyByValue(int n) {
        return this.stateMap[n];
    }

    public <T extends Comparable<T>, V extends T> int withPropertyValue(int n, IProperty<T> iProperty, V v) {
        Entry entry;
        int n2 = this.entryPositionMap.get(iProperty);
        if (n2 >= 0 && (entry = PropertyValueMapper.getPropertyEntry(iProperty)) != null) {
            int n3 = entry.get(v);
            if (n3 < 0) {
                return -1;
            }
            n &= ~(entry.bitSize - 1 << n2);
            return n |= n3 << n2;
        }
        return -1;
    }

    public static class Entry {
        private final IProperty property;
        private final TObjectIntMap values;
        private final int bitSize;
        private final int bits;

        private Entry(IProperty iProperty) {
            this.property = iProperty;
            this.values = new TObjectIntHashMap(10, 0.5f, -1);
            this.bitSize = MathHelper.func_151236_b((int)iProperty.func_177700_c().size());
            int n = 0;
            for (int i = this.bitSize; i != 0; i >>= 1) {
                ++n;
            }
            this.bits = n;
            int n2 = 0;
            for (Object e : iProperty.func_177700_c()) {
                this.values.put(e, n2++);
            }
        }

        public int get(Object object) {
            return this.values.get(object);
        }
    }
}

