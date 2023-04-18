/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.ClickNew;

public final class AnimationUtil {
    private static float defaultSpeed = 0.125f;

    public static float mvoeUD(float f, float f2, float f3) {
        return AnimationUtil.moveUD(f, f2, defaultSpeed, f3);
    }

    public static float moveUD(float f, float f2, float f3, float f4) {
        float f5 = (f2 - f) * f3;
        if (f5 > 0.0f) {
            f5 = Math.max(f4, f5);
            f5 = Math.min(f2 - f, f5);
        } else if (f5 < 0.0f) {
            f5 = Math.min(-f4, f5);
            f5 = Math.max(f2 - f, f5);
        }
        return f + f5;
    }

    public static float calculateCompensation(float f, float f2, long l2, float f3) {
        float f4 = f2 - f;
        if (l2 < 1L) {
            l2 = 1L;
        }
        if (f4 > f3) {
            double d = (double)(f3 * (float)l2 / 16.0f) < 0.25 ? 0.5 : (double)(f3 * (float)l2 / 16.0f);
            if ((f2 -= (float)d) < f) {
                f2 = f;
            }
        } else if (f4 < -f3) {
            double d = (double)(f3 * (float)l2 / 16.0f) < 0.25 ? 0.5 : (double)(f3 * (float)l2 / 16.0f);
            if ((f2 = (float)((double)f2 + d)) > f) {
                f2 = f;
            }
        } else {
            f2 = f;
        }
        return f2;
    }
}

