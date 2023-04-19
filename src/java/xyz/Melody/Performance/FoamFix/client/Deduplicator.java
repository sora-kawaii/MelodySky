/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.client;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import gnu.trove.set.hash.TCustomHashSet;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import org.apache.logging.log4j.Logger;
import xyz.Melody.Performance.FoamFix.client.DeduplicatingStorageTrove;
import xyz.Melody.Performance.FoamFix.client.FoamyItemLayerModel;
import xyz.Melody.Performance.FoamFix.client.IDeduplicatingStorage;
import xyz.Melody.Performance.FoamFix.shared.FoamFixShared;
import xyz.Melody.Performance.FoamFix.util.HashingStrategies;
import xyz.Melody.Performance.FoamFix.util.MethodHandleHelper;

public final class Deduplicator {
    private static final Set<Class> BLACKLIST_CLASS = new TCustomHashSet<Class>(HashingStrategies.IDENTITY);
    private static final Set<Class> TRIM_ARRAYS_CLASSES = new TCustomHashSet<Class>(HashingStrategies.IDENTITY);
    private static final Map<Class, Set<MethodHandle[]>> CLASS_FIELDS = new IdentityHashMap<Class, Set<MethodHandle[]>>();
    private static final Map<Class, MethodHandle> COLLECTION_CONSTRUCTORS = new IdentityHashMap<Class, MethodHandle>();
    private static final MethodHandle FIELD_UNPACKED_DATA_GETTER = MethodHandleHelper.findFieldGetter(UnpackedBakedQuad.class, "unpackedData");
    private static final MethodHandle FIELD_UNPACKED_DATA_SETTER = MethodHandleHelper.findFieldSetter(UnpackedBakedQuad.class, "unpackedData");
    private static final MethodHandle IPAM_MW_TRANSFORMS_GETTER = MethodHandleHelper.findFieldGetter(IPerspectiveAwareModel.MapWrapper.class, "transforms");
    private static final MethodHandle IPAM_MW_TRANSFORMS_SETTER = MethodHandleHelper.findFieldSetter(IPerspectiveAwareModel.MapWrapper.class, "transforms");
    private static final MethodHandle BIM_TRANSFORMS_GETTER = MethodHandleHelper.findFieldGetter("net.minecraftforge.client.model.ItemLayerModel$BakedItemModel", "transforms");
    private static final MethodHandle BIM_TRANSFORMS_SETTER = MethodHandleHelper.findFieldSetter("net.minecraftforge.client.model.ItemLayerModel$BakedItemModel", "transforms");
    public int successfuls = 0;
    public int maxRecursion = 0;
    private final IDeduplicatingStorage<float[]> FLOATA_STORAGE = new DeduplicatingStorageTrove<float[]>(HashingStrategies.FLOAT_ARRAY);
    private final IDeduplicatingStorage<float[][]> FLOATAA_STORAGE = new DeduplicatingStorageTrove<float[][]>(HashingStrategies.FLOAT_ARRAY_ARRAY);
    private final IDeduplicatingStorage OBJECT_STORAGE = new DeduplicatingStorageTrove(HashingStrategies.GENERIC);
    private final IDeduplicatingStorage<ItemCameraTransforms> ICT_STORAGE = new DeduplicatingStorageTrove<ItemCameraTransforms>(HashingStrategies.ITEM_CAMERA_TRANSFORMS);
    private final Set<Object> deduplicatedObjects = new TCustomHashSet<Object>(HashingStrategies.IDENTITY);

    private boolean shouldCheckClass(Class c) {
        if (BLACKLIST_CLASS.contains(c)) {
            return false;
        }
        if (c.isPrimitive() || c.isEnum() || c.isArray() && !this.shouldCheckClass(c.getComponentType())) {
            BLACKLIST_CLASS.add(c);
            return false;
        }
        return true;
    }

    public void addObject(Object o) {
        this.OBJECT_STORAGE.deduplicate(o);
    }

    public void addObjects(Collection coll) {
        for (Object o : coll) {
            this.OBJECT_STORAGE.deduplicate(o);
        }
    }

    public Object deduplicate0(Object o) {
        Object n = o;
        int size = 0;
        if (o instanceof float[]) {
            size = 24 + ((float[])o).length * 4;
            n = this.FLOATA_STORAGE.deduplicate((float[])o);
        } else if (o instanceof float[][]) {
            size = 16 + ((float[][])o).length * 4;
            float[][] arr = this.FLOATAA_STORAGE.deduplicate((float[][])o);
            if (arr != o) {
                n = arr;
                this.successfuls += arr.length;
            } else {
                for (int i = 0; i < arr.length; ++i) {
                    arr[i] = (float[])this.deduplicate0(arr[i]);
                }
            }
        } else if (o instanceof float[][][]) {
            float[][][] arr = (float[][][])o;
            for (int i = 0; i < arr.length; ++i) {
                arr[i] = (float[][])this.deduplicate0(arr[i]);
            }
        } else if (o instanceof ImmutableList || o instanceof ImmutableSet || o instanceof ImmutableMap) {
            n = this.OBJECT_STORAGE.deduplicate(o);
        } else {
            Class<?> c = o.getClass();
            if (ResourceLocation.class == c || ModelResourceLocation.class == c) {
                size = 16;
                n = this.OBJECT_STORAGE.deduplicate(o);
            } else if (TRSRTransformation.class == c) {
                size = 257;
                n = this.OBJECT_STORAGE.deduplicate(o);
            } else if (ItemCameraTransforms.class == c) {
                size = 80;
                n = this.ICT_STORAGE.deduplicate((ItemCameraTransforms)o);
            } else {
                return null;
            }
        }
        if (n != o) {
            ++this.successfuls;
            FoamFixShared.ramSaved += size;
        }
        return n;
    }

    public Object deduplicateObject(Object o, int recursion) {
        block60: {
            Class<?> c;
            block63: {
                block62: {
                    Object b;
                    block61: {
                        block59: {
                            if (o == null || recursion > this.maxRecursion) {
                                return o;
                            }
                            c = o.getClass();
                            if (!this.shouldCheckClass(c)) {
                                return o;
                            }
                            if (!this.deduplicatedObjects.add(o)) {
                                return o;
                            }
                            if (o instanceof IBakedModel) {
                                Object toD;
                                Object to;
                                if (o instanceof IPerspectiveAwareModel.MapWrapper) {
                                    try {
                                        to = IPAM_MW_TRANSFORMS_GETTER.invoke(o);
                                        toD = this.deduplicate0(to);
                                        if (toD != null && to != toD) {
                                            IPAM_MW_TRANSFORMS_SETTER.invoke(o, toD);
                                        }
                                    }
                                    catch (Throwable t) {
                                        t.printStackTrace();
                                    }
                                } else if ("net.minecraftforge.client.model.ItemLayerModel$BakedItemModel".equals(c.getName())) {
                                    try {
                                        to = BIM_TRANSFORMS_GETTER.invoke(o);
                                        toD = this.deduplicate0(to);
                                        if (toD != null && to != toD) {
                                            BIM_TRANSFORMS_SETTER.invoke(o, toD);
                                        }
                                    }
                                    catch (Throwable t) {
                                        t.printStackTrace();
                                    }
                                }
                            }
                            if (c != UnpackedBakedQuad.class) break block59;
                            try {
                                float[][][] array = FIELD_UNPACKED_DATA_GETTER.invokeExact((UnpackedBakedQuad)o);
                                this.deduplicate0(array);
                            }
                            catch (Throwable t) {
                                t.printStackTrace();
                            }
                            break block60;
                        }
                        if (o instanceof ResourceLocation || o instanceof TRSRTransformation) {
                            return this.deduplicate0(o);
                        }
                        if (c == ItemCameraTransforms.class) {
                            Object d = this.deduplicate0(o);
                            if (d != o) {
                                return d;
                            }
                            return o;
                        }
                        if (!(o instanceof Item) && !(o instanceof Block) && !(o instanceof World) && !(o instanceof Entity) && !(o instanceof Logger) && !(o instanceof IRegistry)) break block61;
                        BLACKLIST_CLASS.add(c);
                        break block60;
                    }
                    if (!(o instanceof Optional)) break block62;
                    Optional opt = (Optional)o;
                    if (opt.isPresent() && (b = this.deduplicateObject(opt.get(), recursion + 1)) != null && b != opt.get()) {
                        return Optional.of(b);
                    }
                    break block60;
                }
                if (!(o instanceof Multimap)) break block63;
                if (o instanceof ImmutableMultimap) break block60;
                for (Object key : ((Multimap)o).keySet()) {
                    ArrayList arrayList = Lists.newArrayList(((Multimap)o).values());
                    for (int i = 0; i < arrayList.size(); ++i) {
                        arrayList.set(i, this.deduplicateObject(arrayList.get(i), recursion + 1));
                    }
                    ((Multimap)o).replaceValues(key, arrayList);
                }
                break block60;
            }
            if (o instanceof Map) {
                if (o instanceof ImmutableMap) {
                    ImmutableMap im = (ImmutableMap)o;
                    HashMap newMap = new HashMap();
                    boolean deduplicated = false;
                    Iterator i = ((ImmutableSet)im.keySet()).iterator();
                    while (i.hasNext()) {
                        Object key;
                        Object a = im.get(key = i.next());
                        Object b = this.deduplicateObject(a, recursion + 1);
                        newMap.put(key, b != null ? b : a);
                        if (b == null || b == a) continue;
                        deduplicated = true;
                    }
                    if (deduplicated) {
                        return ImmutableMap.copyOf(newMap);
                    }
                } else {
                    for (Object key : ((Map)o).keySet()) {
                        Object value = ((Map)o).get(key);
                        Object valueD = this.deduplicateObject(value, recursion + 1);
                        if (valueD == null || value == valueD) continue;
                        ((Map)o).put(key, valueD);
                    }
                }
            } else if (o instanceof List) {
                List<Object> il;
                if (o instanceof ImmutableList) {
                    il = (ImmutableList)o;
                    ArrayList<Object> newList = new ArrayList<Object>();
                    boolean deduplicated = false;
                    for (int i = 0; i < ((AbstractCollection)((Object)il)).size(); ++i) {
                        Object a = il.get(i);
                        Object b = this.deduplicateObject(a, recursion + 1);
                        newList.add(b != null ? b : a);
                        if (b == null || b == a) continue;
                        deduplicated = true;
                    }
                    if (deduplicated) {
                        return ImmutableList.copyOf(newList);
                    }
                } else {
                    il = (List)o;
                    for (int i = 0; i < il.size(); ++i) {
                        il.set(i, this.deduplicateObject(il.get(i), recursion + 1));
                    }
                }
            } else if (o instanceof Collection) {
                MethodHandle constructor;
                if (!COLLECTION_CONSTRUCTORS.containsKey(c)) {
                    try {
                        COLLECTION_CONSTRUCTORS.put(c, MethodHandles.publicLookup().findConstructor(c, MethodType.methodType(Void.TYPE)));
                    }
                    catch (Exception e) {
                        COLLECTION_CONSTRUCTORS.put(c, null);
                    }
                }
                if ((constructor = COLLECTION_CONSTRUCTORS.get(c)) != null) {
                    try {
                        Collection nc = constructor.invoke();
                        for (Object o1 : (Collection)o) {
                            nc.add(this.deduplicateObject(o1, recursion + 1));
                        }
                        return nc;
                    }
                    catch (Throwable nc) {
                        // empty catch block
                    }
                }
                for (Object o1 : (Collection)o) {
                    this.deduplicateObject(o1, recursion + 1);
                }
            } else if (c.isArray()) {
                for (int i = 0; i < Array.getLength(o); ++i) {
                    Object entry = Array.get(o, i);
                    Object entryD = this.deduplicateObject(entry, recursion + 1);
                    if (entryD == null || entry == entryD) continue;
                    Array.set(o, i, entryD);
                }
            } else {
                if (!CLASS_FIELDS.containsKey(c)) {
                    ImmutableSet.Builder fsBuilder = ImmutableSet.builder();
                    Class<?> cc = c;
                    do {
                        for (Field f : cc.getDeclaredFields()) {
                            f.setAccessible(true);
                            if ((f.getModifiers() & 8) != 0 || !this.shouldCheckClass(f.getType())) continue;
                            try {
                                fsBuilder.add((Object)new MethodHandle[]{MethodHandles.lookup().unreflectGetter(f), MethodHandles.lookup().unreflectSetter(f)});
                            }
                            catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } while ((cc = cc.getSuperclass()) != Object.class);
                    CLASS_FIELDS.put(c, (Set<MethodHandle[]>)((Object)fsBuilder.build()));
                }
                for (MethodHandle[] mh : CLASS_FIELDS.get(c)) {
                    try {
                        Object value = mh[0].invoke(o);
                        Object valueD = this.deduplicateObject(value, recursion + 1);
                        if (TRIM_ARRAYS_CLASSES.contains(c) && valueD instanceof ArrayList) {
                            ((ArrayList)valueD).trimToSize();
                        }
                        if (valueD == null || value == valueD) continue;
                        mh[1].invoke(o, valueD);
                    }
                    catch (IllegalAccessException value) {
                    }
                    catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
        return o;
    }

    static {
        TRIM_ARRAYS_CLASSES.add(FoamyItemLayerModel.DynamicItemModel.class);
        TRIM_ARRAYS_CLASSES.add(SimpleBakedModel.class);
        BLACKLIST_CLASS.add(FoamyItemLayerModel.Dynamic3DItemModel.class);
        BLACKLIST_CLASS.add(Object.class);
        BLACKLIST_CLASS.add(Class.class);
        BLACKLIST_CLASS.add(String.class);
        BLACKLIST_CLASS.add(Integer.class);
        BLACKLIST_CLASS.add(Long.class);
        BLACKLIST_CLASS.add(Byte.class);
        BLACKLIST_CLASS.add(Boolean.class);
        BLACKLIST_CLASS.add(Float.class);
        BLACKLIST_CLASS.add(Double.class);
        BLACKLIST_CLASS.add(Short.class);
        BLACKLIST_CLASS.add(TextureAtlasSprite.class);
        BLACKLIST_CLASS.add(ItemStack.class);
        BLACKLIST_CLASS.add(Gson.class);
        BLACKLIST_CLASS.add(ModelLoader.class);
        BLACKLIST_CLASS.add(Class.class);
        BLACKLIST_CLASS.add(BlockPart.class);
        BLACKLIST_CLASS.add(Minecraft.class);
        BLACKLIST_CLASS.add(BlockModelShapes.class);
        BLACKLIST_CLASS.add(ModelManager.class);
        BLACKLIST_CLASS.add(Logger.class);
        BLACKLIST_CLASS.add(Joiner.class);
        BLACKLIST_CLASS.add(Tessellator.class);
        BLACKLIST_CLASS.add(WorldRenderer.class);
        BLACKLIST_CLASS.add(Cache.class);
        BLACKLIST_CLASS.add(LoadingCache.class);
        BLACKLIST_CLASS.add(VertexFormatElement.class);
        BLACKLIST_CLASS.add(BakedQuad.class);
    }
}

