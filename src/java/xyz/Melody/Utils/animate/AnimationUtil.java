/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.animate;

public final class AnimationUtil {
    private static float defaultSpeed = 0.125f;

    public static float calculateCompensation(float f, float f2, long l2, int n) {
        float f3 = f2 - f;
        if (l2 < 1L) {
            l2 = 1L;
        }
        if (f3 > (float)n) {
            double d = (double)((long)n * l2 / 16L) < 0.25 ? 0.5 : (double)((long)n * l2 / 16L);
            if ((f2 = (float)((double)f2 - d)) < f) {
                f2 = f;
            }
        } else if (f3 < (float)(-n)) {
            double d = (double)((long)n * l2 / 16L) < 0.25 ? 0.5 : (double)((long)n * l2 / 16L);
            if ((f2 = (float)((double)f2 + d)) > f) {
                f2 = f;
            }
        } else {
            f2 = f;
        }
        return f2;
    }

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

    public static double animate(double d, double d2, double d3) {
        boolean bl;
        boolean bl2 = bl = d > d2;
        if (d3 < 0.0) {
            d3 = 0.0;
        } else if (d3 > 1.0) {
            d3 = 1.0;
        }
        double d4 = Math.max(d, d2) - Math.min(d, d2);
        double d5 = d4 * d3;
        if (d5 < 0.1) {
            d5 = 0.1;
        }
        d2 = bl ? (d2 = d2 + d5) : (d2 = d2 - d5);
        return d2;
    }
}

