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
                float x0 = (float)i / (float)ATAN2_DIM;
                float y0 = (float)j / (float)ATAN2_DIM;
                FastTrig.atan2[j * FastTrig.ATAN2_DIM + i] = (float)Math.atan2(y0, x0);
            }
        }
    }

    public static final float atan2(double y, double x) {
        float add;
        float mul;
        if (x < 0.0) {
            if (y < 0.0) {
                x = -x;
                y = -y;
                mul = 1.0f;
            } else {
                x = -x;
                mul = -1.0f;
            }
            add = (float)(-Math.PI);
        } else {
            if (y < 0.0) {
                y = -y;
                mul = -1.0f;
            } else {
                mul = 1.0f;
            }
            add = 0.0f;
        }
        double invDiv = 1.0 / ((x < y ? y : x) * (double)INV_ATAN2_DIM_MINUS_1);
        int xi = (int)(x * invDiv);
        int yi = (int)(y * invDiv);
        return (atan2[yi * ATAN2_DIM + xi] + add) * mul;
    }
}

