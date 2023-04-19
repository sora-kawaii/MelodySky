/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import java.lang.reflect.Method;

public final class MethodReflectionHelper {
    private Method method;

    public MethodReflectionHelper(Class<?> clazz, String methodName, String methodname2, Class<?> ... parameter) {
        try {
            try {
                this.method = clazz.getDeclaredMethod(methodName, parameter);
            }
            catch (NoSuchMethodException e2) {
                this.method = clazz.getDeclaredMethod(methodname2, parameter);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void invoke(Object instance) {
        try {
            this.method.setAccessible(true);
            this.method.invoke(instance, new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

