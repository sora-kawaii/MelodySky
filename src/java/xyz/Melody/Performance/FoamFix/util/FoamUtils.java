/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.util;

import com.google.common.collect.Sets;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import xyz.Melody.Client;

public final class FoamUtils {
    public static final MethodHandle MLR_GET_TEXTURES;
    public static final MethodHandle ML_LOAD_BLOCK;

    private FoamUtils() {
    }

    public static void wipeModelLoaderRegistryCache() {
        Field resourceCacheField = ReflectionHelper.findField(ModelLoaderRegistry.class, "cache");
        try {
            Map oldResourceCache = (Map)resourceCacheField.get(null);
            int itemsCleared = 0;
            Client.instance.logger.info("Clearing ModelLoaderRegistry cache (" + oldResourceCache.size() + " items)...");
            for (ResourceLocation r : Sets.newHashSet(oldResourceCache.keySet())) {
                oldResourceCache.remove(r);
                ++itemsCleared;
            }
            Client.instance.logger.info("Cleared " + itemsCleared + " objects.");
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static {
        MethodHandle MLR_GET_TEXTURES_TMP = null;
        try {
            Class<?> k = Class.forName("net.minecraftforge.client.model.ModelLoaderRegistry");
            Method m = k.getDeclaredMethod("getTextures", new Class[0]);
            m.setAccessible(true);
            MLR_GET_TEXTURES_TMP = MethodHandles.lookup().unreflect(m);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        MLR_GET_TEXTURES = MLR_GET_TEXTURES_TMP;
        MethodHandle ML_LOAD_BLOCK_TMP = null;
        try {
            Class<?> k = Class.forName("net.minecraft.client.renderer.block.model.ModelBakery");
            Method m = k.getDeclaredMethod("loadBlock", BlockStateMapper.class, Block.class, ResourceLocation.class);
            m.setAccessible(true);
            ML_LOAD_BLOCK_TMP = MethodHandles.lookup().unreflect(m);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ML_LOAD_BLOCK = ML_LOAD_BLOCK_TMP;
    }
}

