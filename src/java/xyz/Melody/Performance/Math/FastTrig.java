/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.Math;

public final class FastTrig {
    private static final int ATAN2_BITS = 8;
    private static final int ATAN2_BITS2 = 16;
    private static final int ATAN2_MASK = 65535;
    private static final int ATAN2_COUNT = 65536;
    private static final int ATAN2_DIM = (int)Math.sqrt(65536.0);
    private static final float INV_ATAN2_DIM_MINUS_1 = 1.0f / (float)(ATAN2_DIM - 1);
    private static final float[] atan2 = new float[65536];

    public static void init() {
        for (int i = 0; i < ATAN2_DIM; ++i) {
            for (int j = 0; j < ATAN2_DIM; ++j) {
                float f = (float)i / (float)ATAN2_DIM;
                float f2 = (float)j / (float)ATAN2_DIM;
                FastTrig.atan2[j * FastTrig.ATAN2_DIM + i] = (float)Math.atan2(f2, f);
            }
        }
    }

    public static final float atan2(double d, double d2) {
        float f;
        float f2;
        if (d2 < 0.0) {
            if (d < 0.0) {
                d2 = -d2;
                d = -d;
                f2 = 1.0f;
            } else {
                d2 = -d2;
                f2 = -1.0f;
            }
            f = (float)(-Math.PI);
        } else {
            if (d < 0.0) {
                d = -d;
                f2 = -1.0f;
            } else {
                f2 = 1.0f;
            }
            f = 0.0f;
        }
        double d3 = 1.0 / ((d2 < d ? d : d2) * (double)INV_ATAN2_DIM_MINUS_1);
        int n = (int)(d2 * d3);
        int n2 = (int)(d * d3);
        return (atan2[n2 * ATAN2_DIM + n] + f) * f2;
    }
}

