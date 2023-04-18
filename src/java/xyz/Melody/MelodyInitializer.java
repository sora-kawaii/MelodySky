/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.NetworkModHolder;

public final class MelodyInitializer {
    public static void initModList() {
        try {
            Loader loader = Loader.instance();
            Class<?> clazz = loader.getClass();
            Field field = clazz.getDeclaredField("mods");
            field.setAccessible(true);
            List list = loader.getModList().stream().collect(Collectors.toList());
            list.removeIf(modContainer -> modContainer.getName().toLowerCase().equals("melodysky"));
            field.set(loader, list);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void initModMap() {
        try {
            Loader loader = Loader.instance();
            Class<?> clazz = loader.getClass();
            Field field = clazz.getDeclaredField("namedMods");
            field.setAccessible(true);
            HashMap<String, ModContainer> hashMap = new HashMap<String, ModContainer>(loader.getIndexedModList());
            if (hashMap.containsKey("melodysky")) {
                hashMap.remove("melodysky");
            }
            field.set(loader, hashMap);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void initActivedMods() {
        try {
            Loader loader = Loader.instance();
            Class<?> clazz = loader.getClass();
            Field field = clazz.getDeclaredField("modController");
            field.setAccessible(true);
            LoadController loadController = (LoadController)field.get(loader);
            Class<?> clazz2 = loadController.getClass();
            Field field2 = clazz2.getDeclaredField("activeModList");
            field2.setAccessible(true);
            List list = Loader.instance().getActiveModList().stream().collect(Collectors.toList());
            list.removeIf(modContainer -> modContainer.getName().toLowerCase().equals("melodysky"));
            field2.set(loadController, list);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void initIServerMods() {
        try {
            NetworkRegistry networkRegistry = NetworkRegistry.INSTANCE;
            Class<?> clazz = ((Object)((Object)networkRegistry)).getClass();
            Field field = clazz.getDeclaredField("registry");
            field.setAccessible(true);
            HashMap<ModContainer, NetworkModHolder> hashMap = new HashMap<ModContainer, NetworkModHolder>(networkRegistry.registry());
            ModContainer modContainer = null;
            Set set = hashMap.keySet();
            for (ModContainer modContainer2 : set) {
                if (!modContainer2.getName().toLowerCase().equals("melodysky")) continue;
                modContainer = modContainer2;
            }
            hashMap.remove(modContainer);
            field.set((Object)networkRegistry, hashMap);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

