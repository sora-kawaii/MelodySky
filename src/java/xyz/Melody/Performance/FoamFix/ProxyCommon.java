/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.BiMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import xyz.Melody.Client;
import xyz.Melody.Performance.FoamFix.shared.FoamFixShared;

public class ProxyCommon {
    private void optimizeLaunchWrapper() {
        if (FoamFixShared.config.lwWeakenResourceCache) {
            Client.instance.logger.info("Weakening LaunchWrapper resource cache...");
            try {
                LaunchClassLoader launchClassLoader = (LaunchClassLoader)this.getClass().getClassLoader();
                Field field = ReflectionHelper.findField(LaunchClassLoader.class, "resourceCache");
                Map map = (Map)field.get(launchClassLoader);
                ConcurrentMap concurrentMap = CacheBuilder.newBuilder().weakValues().build().asMap();
                concurrentMap.putAll(map);
                field.set(launchClassLoader, concurrentMap);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void optimizeForgeRegistries() {
        try {
            int n = 0;
            int n2 = 0;
            Class<?> clazz = Class.forName("net.minecraftforge.fml.common.registry.PersistentRegistryManager$PersistentRegistry");
            Field field = clazz.getDeclaredField("registries");
            Field field2 = FMLControlledNamespacedRegistry.class.getDeclaredField("availabilityMap");
            Field field3 = BitSet.class.getDeclaredField("sizeIsSticky");
            Method method = BitSet.class.getDeclaredMethod("trimToSize", new Class[0]);
            field.setAccessible(true);
            field2.setAccessible(true);
            field3.setAccessible(true);
            method.setAccessible(true);
            for (Object obj : clazz.getEnumConstants()) {
                BiMap biMap = (BiMap)field.get(obj);
                for (FMLControlledNamespacedRegistry fMLControlledNamespacedRegistry : biMap.values()) {
                    BitSet bitSet = (BitSet)field2.get(fMLControlledNamespacedRegistry);
                    int n3 = bitSet.size();
                    if (n3 <= 65536) continue;
                    field3.set(bitSet, false);
                    method.invoke(bitSet, new Object[0]);
                    ++n;
                    n2 += n3 - bitSet.size() >> 3;
                }
            }
            FoamFixShared.ramSaved += n2;
            Client.instance.logger.info("Optimized " + n + " FML registries, saving " + n2 + " bytes.");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void preInit() {
    }

    public void init() {
    }

    public void postInit() {
        this.optimizeLaunchWrapper();
        if (FoamFixShared.config.geDynamicRegistrySizeScaling) {
            this.optimizeForgeRegistries();
        }
    }
}

