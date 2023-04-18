/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.util;

import com.google.common.collect.Sets;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
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
        Field field = ReflectionHelper.findField(ModelLoaderRegistry.class, "cache");
        try {
            Map map = (Map)field.get(null);
            int n = 0;
            Client.instance.logger.info("Clearing ModelLoaderRegistry cache (" + map.size() + " items)...");
            for (ResourceLocation resourceLocation : Sets.newHashSet(map.keySet())) {
                map.remove(resourceLocation);
                ++n;
            }
            Client.instance.logger.info("Cleared " + n + " objects.");
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
    }

    static {
        GenericDeclaration genericDeclaration;
        Object object;
        MethodHandle methodHandle = null;
        try {
            object = Class.forName("net.minecraftforge.client.model.ModelLoaderRegistry");
            genericDeclaration = ((Class)object).getDeclaredMethod("getTextures", new Class[0]);
            ((Method)genericDeclaration).setAccessible(true);
            methodHandle = MethodHandles.lookup().unreflect((Method)genericDeclaration);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        MLR_GET_TEXTURES = methodHandle;
        object = null;
        try {
            genericDeclaration = Class.forName("net.minecraft.client.renderer.block.model.ModelBakery");
            Method method = ((Class)genericDeclaration).getDeclaredMethod("loadBlock", BlockStateMapper.class, Block.class, ResourceLocation.class);
            method.setAccessible(true);
            object = MethodHandles.lookup().unreflect(method);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        ML_LOAD_BLOCK = object;
    }
}

