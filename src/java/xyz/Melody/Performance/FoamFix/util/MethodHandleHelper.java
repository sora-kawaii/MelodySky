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

    public static MethodHandle findFieldGetter(Class clazz, String ... stringArray) {
        try {
            return MethodHandles.lookup().unreflectGetter(ReflectionHelper.findField(clazz, stringArray));
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
            return null;
        }
    }

    public static MethodHandle findFieldSetter(Class clazz, String ... stringArray) {
        try {
            return MethodHandles.lookup().unreflectSetter(ReflectionHelper.findField(clazz, stringArray));
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
            return null;
        }
    }

    public static MethodHandle findFieldGetter(String string, String ... stringArray) {
        try {
            return MethodHandleHelper.findFieldGetter(Class.forName(string), stringArray);
        }
        catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            return null;
        }
    }

    public static MethodHandle findFieldSetter(String string, String ... stringArray) {
        try {
            return MethodHandleHelper.findFieldSetter(Class.forName(string), stringArray);
        }
        catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            return null;
        }
    }
}

