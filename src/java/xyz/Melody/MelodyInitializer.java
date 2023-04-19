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
            Loader LJ = Loader.instance();
            Class<?> testClass = LJ.getClass();
            Field field = testClass.getDeclaredField("mods");
            field.setAccessible(true);
            List dick = LJ.getModList().stream().collect(Collectors.toList());
            dick.removeIf(c -> c.getName().toLowerCase().equals("melodysky"));
            field.set(LJ, dick);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initModMap() {
        try {
            Loader LJ = Loader.instance();
            Class<?> testClass = LJ.getClass();
            Field field = testClass.getDeclaredField("namedMods");
            field.setAccessible(true);
            HashMap<String, ModContainer> dick = new HashMap<String, ModContainer>(LJ.getIndexedModList());
            if (dick.containsKey("melodysky")) {
                dick.remove("melodysky");
            }
            field.set(LJ, dick);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initActivedMods() {
        try {
            Loader loader = Loader.instance();
            Class<?> clz = loader.getClass();
            Field modController = clz.getDeclaredField("modController");
            modController.setAccessible(true);
            LoadController loadController = (LoadController)modController.get(loader);
            Class<?> testClass = loadController.getClass();
            Field field = testClass.getDeclaredField("activeModList");
            field.setAccessible(true);
            List activeModList = Loader.instance().getActiveModList().stream().collect(Collectors.toList());
            activeModList.removeIf(c -> c.getName().toLowerCase().equals("melodysky"));
            field.set(loadController, activeModList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initIServerMods() {
        try {
            NetworkRegistry nr = NetworkRegistry.INSTANCE;
            Class<?> testClass = ((Object)((Object)nr)).getClass();
            Field field = testClass.getDeclaredField("registry");
            field.setAccessible(true);
            HashMap<ModContainer, NetworkModHolder> registry = new HashMap<ModContainer, NetworkModHolder>(nr.registry());
            ModContainer melodyContainer = null;
            Set keySet = registry.keySet();
            for (ModContainer mod : keySet) {
                if (!mod.getName().toLowerCase().equals("melodysky")) continue;
                melodyContainer = mod;
            }
            registry.remove(melodyContainer);
            field.set((Object)nr, registry);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

