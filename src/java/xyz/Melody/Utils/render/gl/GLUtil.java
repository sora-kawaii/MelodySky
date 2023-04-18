/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.render.gl;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public final class GLUtil {
    private static final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();

    public static void setGLCap(int n, boolean bl) {
        glCapMap.put(n, GL11.glGetBoolean(n));
        if (bl) {
            GL11.glEnable(n);
        } else {
            GL11.glDisable(n);
        }
    }

    private static void revertGLCap(int n) {
        Boolean bl = glCapMap.get(n);
        if (bl != null) {
            if (bl.booleanValue()) {
                GL11.glEnable(n);
            } else {
                GL11.glDisable(n);
            }
        }
    }

    public static void glEnable(int n) {
        GLUtil.setGLCap(n, true);
    }

    public static void glDisable(int n) {
        GLUtil.setGLCap(n, false);
    }

    public static void revertAllCaps() {
        for (Integer n : glCapMap.keySet()) {
            GLUtil.revertGLCap(n);
        }
    }
}

