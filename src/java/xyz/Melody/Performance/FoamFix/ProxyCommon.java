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
                LaunchClassLoader loader = (LaunchClassLoader)this.getClass().getClassLoader();
                Field resourceCacheField = ReflectionHelper.findField(LaunchClassLoader.class, "resourceCache");
                Map oldResourceCache = (Map)resourceCacheField.get(loader);
                ConcurrentMap newResourceCache = CacheBuilder.newBuilder().weakValues().build().asMap();
                newResourceCache.putAll(oldResourceCache);
                resourceCacheField.set(loader, newResourceCache);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void optimizeForgeRegistries() {
        try {
            int optimizedRegs = 0;
            int optimizedSavings = 0;
            Class<?> persistentRegistryClass = Class.forName("net.minecraftforge.fml.common.registry.PersistentRegistryManager$PersistentRegistry");
            Field biMapField = persistentRegistryClass.getDeclaredField("registries");
            Field availMapField = FMLControlledNamespacedRegistry.class.getDeclaredField("availabilityMap");
            Field sizeStickyField = BitSet.class.getDeclaredField("sizeIsSticky");
            Method trimToSizeMethod = BitSet.class.getDeclaredMethod("trimToSize", new Class[0]);
            biMapField.setAccessible(true);
            availMapField.setAccessible(true);
            sizeStickyField.setAccessible(true);
            trimToSizeMethod.setAccessible(true);
            for (Object registryHolder : persistentRegistryClass.getEnumConstants()) {
                BiMap biMap = (BiMap)biMapField.get(registryHolder);
                for (FMLControlledNamespacedRegistry registry : biMap.values()) {
                    BitSet availMap = (BitSet)availMapField.get(registry);
                    int size = availMap.size();
                    if (size <= 65536) continue;
                    sizeStickyField.set(availMap, false);
                    trimToSizeMethod.invoke(availMap, new Object[0]);
                    ++optimizedRegs;
                    optimizedSavings += size - availMap.size() >> 3;
                }
            }
            FoamFixShared.ramSaved += optimizedSavings;
            Client.instance.logger.info("Optimized " + optimizedRegs + " FML registries, saving " + optimizedSavings + " bytes.");
        }
        catch (Exception e) {
            e.printStackTrace();
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

