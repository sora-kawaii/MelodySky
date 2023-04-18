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

    private boolean shouldCheckClass(Class clazz) {
        if (BLACKLIST_CLASS.contains(clazz)) {
            return false;
        }
        if (clazz.isPrimitive() || clazz.isEnum() || clazz.isArray() && !this.shouldCheckClass(clazz.getComponentType())) {
            BLACKLIST_CLASS.add(clazz);
            return false;
        }
        return true;
    }

    public void addObject(Object object) {
        this.OBJECT_STORAGE.deduplicate(object);
    }

    public void addObjects(Collection collection) {
        for (Object e : collection) {
            this.OBJECT_STORAGE.deduplicate(e);
        }
    }

    public Object deduplicate0(Object object) {
        Object object2 = object;
        int n = 0;
        if (object instanceof float[]) {
            n = 24 + ((float[])object).length * 4;
            object2 = this.FLOATA_STORAGE.deduplicate((float[])object);
        } else if (object instanceof float[][]) {
            n = 16 + ((float[][])object).length * 4;
            float[][] fArray = this.FLOATAA_STORAGE.deduplicate((float[][])object);
            if (fArray != object) {
                object2 = fArray;
                this.successfuls += fArray.length;
            } else {
                for (int i = 0; i < fArray.length; ++i) {
                    fArray[i] = (float[])this.deduplicate0(fArray[i]);
                }
            }
        } else if (object instanceof float[][][]) {
            float[][][] fArray = (float[][][])object;
            for (int i = 0; i < fArray.length; ++i) {
                fArray[i] = (float[][])this.deduplicate0(fArray[i]);
            }
        } else if (object instanceof ImmutableList || object instanceof ImmutableSet || object instanceof ImmutableMap) {
            object2 = this.OBJECT_STORAGE.deduplicate(object);
        } else {
            Class<?> clazz = object.getClass();
            if (ResourceLocation.class == clazz || ModelResourceLocation.class == clazz) {
                n = 16;
                object2 = this.OBJECT_STORAGE.deduplicate(object);
            } else if (TRSRTransformation.class == clazz) {
                n = 257;
                object2 = this.OBJECT_STORAGE.deduplicate(object);
            } else if (ItemCameraTransforms.class == clazz) {
                n = 80;
                object2 = this.ICT_STORAGE.deduplicate((ItemCameraTransforms)object);
            } else {
                return null;
            }
        }
        if (object2 != object) {
            ++this.successfuls;
            FoamFixShared.ramSaved += n;
        }
        return object2;
    }

    public Object deduplicateObject(Object object, int n) {
        block60: {
            Object object222;
            Object object3;
            Class<?> clazz;
            block63: {
                block62: {
                    block61: {
                        block59: {
                            if (object == null || n > this.maxRecursion) {
                                return object;
                            }
                            clazz = object.getClass();
                            if (!this.shouldCheckClass(clazz)) {
                                return object;
                            }
                            if (!this.deduplicatedObjects.add(object)) {
                                return object;
                            }
                            if (object instanceof IBakedModel) {
                                if (object instanceof IPerspectiveAwareModel.MapWrapper) {
                                    try {
                                        object3 = IPAM_MW_TRANSFORMS_GETTER.invoke(object);
                                        object222 = this.deduplicate0(object3);
                                        if (object222 != null && object3 != object222) {
                                            IPAM_MW_TRANSFORMS_SETTER.invoke(object, object222);
                                        }
                                    }
                                    catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                } else if ("net.minecraftforge.client.model.ItemLayerModel$BakedItemModel".equals(clazz.getName())) {
                                    try {
                                        object3 = BIM_TRANSFORMS_GETTER.invoke(object);
                                        object222 = this.deduplicate0(object3);
                                        if (object222 != null && object3 != object222) {
                                            BIM_TRANSFORMS_SETTER.invoke(object, object222);
                                        }
                                    }
                                    catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                }
                            }
                            if (clazz != UnpackedBakedQuad.class) break block59;
                            try {
                                object3 = FIELD_UNPACKED_DATA_GETTER.invokeExact((UnpackedBakedQuad)object);
                                this.deduplicate0(object3);
                            }
                            catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                            break block60;
                        }
                        if (object instanceof ResourceLocation || object instanceof TRSRTransformation) {
                            return this.deduplicate0(object);
                        }
                        if (clazz == ItemCameraTransforms.class) {
                            object3 = this.deduplicate0(object);
                            if (object3 != object) {
                                return object3;
                            }
                            return object;
                        }
                        if (!(object instanceof Item) && !(object instanceof Block) && !(object instanceof World) && !(object instanceof Entity) && !(object instanceof Logger) && !(object instanceof IRegistry)) break block61;
                        BLACKLIST_CLASS.add(clazz);
                        break block60;
                    }
                    if (!(object instanceof Optional)) break block62;
                    object3 = (Optional)object;
                    if (((Optional)object3).isPresent() && (object222 = this.deduplicateObject(((Optional)object3).get(), n + 1)) != null && object222 != ((Optional)object3).get()) {
                        return Optional.of(object222);
                    }
                    break block60;
                }
                if (!(object instanceof Multimap)) break block63;
                if (object instanceof ImmutableMultimap) break block60;
                for (Object object222 : ((Multimap)object).keySet()) {
                    ArrayList arrayList = Lists.newArrayList(((Multimap)object).values());
                    for (int i = 0; i < arrayList.size(); ++i) {
                        arrayList.set(i, this.deduplicateObject(arrayList.get(i), n + 1));
                    }
                    ((Multimap)object).replaceValues(object222, arrayList);
                }
                break block60;
            }
            if (object instanceof Map) {
                if (object instanceof ImmutableMap) {
                    boolean v;
                    object3 = (ImmutableMap)object;
                    object222 = new HashMap();
                    boolean bl = false;
                    Iterator iterator = ((ImmutableSet)((ImmutableMap)object3).keySet()).iterator();
                    while (iterator.hasNext()) {
                        Object e;
                        Object v2 = ((ImmutableMap)object3).get(e = iterator.next());
                        Object object2 = this.deduplicateObject(v2, n + 1);
                        object222.put(e, object2 != null ? object2 : v2);
                        if (object2 == null || object2 == v2) continue;
                        v = true;
                    }
                    if (v) {
                        return ImmutableMap.copyOf(object222);
                    }
                } else {
                    for (Object object222 : ((Map)object).keySet()) {
                        Object bl = ((Map)object).get(object222);
                        Object object9 = this.deduplicateObject(bl, n + 1);
                        if (object9 == null || bl == object9) continue;
                        ((Map)object).put(object222, object9);
                    }
                }
            } else if (object instanceof List) {
                if (object instanceof ImmutableList) {
                    boolean e;
                    object3 = (ImmutableList)object;
                    object222 = new ArrayList();
                    boolean bl = false;
                    for (int i = 0; i < ((AbstractCollection)object3).size(); ++i) {
                        Object e2 = object3.get(i);
                        Object object4 = this.deduplicateObject(e2, n + 1);
                        object222.add(object4 != null ? object4 : e2);
                        if (object4 == null || object4 == e2) continue;
                        e = true;
                    }
                    if (e) {
                        return ImmutableList.copyOf(object222);
                    }
                } else {
                    object3 = (List)object;
                    for (int i = 0; i < object3.size(); ++i) {
                        object3.set(i, (Object)this.deduplicateObject(object3.get(i), n + 1));
                    }
                }
            } else if (object instanceof Collection) {
                if (!COLLECTION_CONSTRUCTORS.containsKey(clazz)) {
                    try {
                        COLLECTION_CONSTRUCTORS.put(clazz, MethodHandles.publicLookup().findConstructor(clazz, MethodType.methodType(Void.TYPE)));
                    }
                    catch (Exception exception) {
                        COLLECTION_CONSTRUCTORS.put(clazz, null);
                    }
                }
                if ((object3 = COLLECTION_CONSTRUCTORS.get(clazz)) != null) {
                    try {
                        object222 = object3.invoke();
                        for (Object e : (Collection)object) {
                            object222.add(this.deduplicateObject(e, n + 1));
                        }
                        return object222;
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
                for (Object object8 : (Collection)object) {
                    this.deduplicateObject(object8, n + 1);
                }
            } else if (clazz.isArray()) {
                for (int i = 0; i < Array.getLength(object); ++i) {
                    object222 = Array.get(object, i);
                    Object illegalAccessException = this.deduplicateObject(object222, n + 1);
                    if (illegalAccessException == null || object222 == illegalAccessException) continue;
                    Array.set(object, i, illegalAccessException);
                }
            } else {
                if (!CLASS_FIELDS.containsKey(clazz)) {
                    object3 = ImmutableSet.builder();
                    object222 = clazz;
                    do {
                        for (Field field : object222.getDeclaredFields()) {
                            field.setAccessible(true);
                            if ((field.getModifiers() & 8) != 0 || !this.shouldCheckClass(field.getType())) continue;
                            try {
                                ((ImmutableSet.Builder)object3).add((Object)new MethodHandle[]{MethodHandles.lookup().unreflectGetter(field), MethodHandles.lookup().unreflectSetter(field)});
                            }
                            catch (IllegalAccessException illegalAccessException) {
                                illegalAccessException.printStackTrace();
                            }
                        }
                    } while ((object222 = object222.getSuperclass()) != Object.class);
                    CLASS_FIELDS.put(clazz, (Set<MethodHandle[]>)((Object)((ImmutableSet.Builder)object3).build()));
                }
                for (Object object222 : CLASS_FIELDS.get(clazz)) {
                    try {
                        Object object5 = object222[0].invoke(object);
                        Object object6 = this.deduplicateObject(object5, n + 1);
                        if (TRIM_ARRAYS_CLASSES.contains(clazz) && object6 instanceof ArrayList) {
                            ((ArrayList)object6).trimToSize();
                        }
                        if (object6 == null || object5 == object6) continue;
                        object222[1].invoke(object, object6);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                    }
                    catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }
        return object;
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

