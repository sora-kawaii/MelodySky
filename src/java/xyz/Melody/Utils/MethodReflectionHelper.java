/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import java.lang.reflect.Method;

public final class MethodReflectionHelper {
    private Method method;

    public MethodReflectionHelper(Class<?> clazz, String string, String string2, Class<?> ... classArray) {
        try {
            try {
                this.method = clazz.getDeclaredMethod(string, classArray);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                this.method = clazz.getDeclaredMethod(string2, classArray);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    public void invoke(Object object) {
        try {
            this.method.setAccessible(true);
            this.method.invoke(object, new Object[0]);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

