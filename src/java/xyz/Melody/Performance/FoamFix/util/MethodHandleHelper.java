/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class MethodHandleHelper {
    private MethodHandleHelper() {
    }

    public static MethodHandle findFieldGetter(Class c, String ... names) {
        try {
            return MethodHandles.lookup().unreflectGetter(ReflectionHelper.findField(c, names));
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MethodHandle findFieldSetter(Class c, String ... names) {
        try {
            return MethodHandles.lookup().unreflectSetter(ReflectionHelper.findField(c, names));
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MethodHandle findFieldGetter(String s, String ... names) {
        try {
            return MethodHandleHelper.findFieldGetter(Class.forName(s), names);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MethodHandle findFieldSetter(String s, String ... names) {
        try {
            return MethodHandleHelper.findFieldSetter(Class.forName(s), names);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

