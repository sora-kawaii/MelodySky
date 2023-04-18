/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.animate;

import xyz.Melody.Utils.animate.AnimationUtil;

public final class Translate {
    private float x;
    private float y;
    private long lastMS;

    public Translate(float f, float f2) {
        this.x = f;
        this.y = f2;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float f, float f2, int n, int n2) {
        long l2 = System.currentTimeMillis();
        long l3 = l2 - this.lastMS;
        this.lastMS = l2;
        int n3 = (int)(Math.abs(f - this.x) * 0.51f);
        int n4 = (int)(Math.abs(f2 - this.y) * 0.51f);
        this.x = AnimationUtil.calculateCompensation(f, this.x, l3, n3);
        this.y = AnimationUtil.calculateCompensation(f2, this.y, l3, n4);
    }

    public void interpolate(float f, float f2, double d) {
        long l2 = System.currentTimeMillis();
        long l3 = l2 - this.lastMS;
        this.lastMS = l2;
        double d2 = 0.0;
        double d3 = 0.0;
        if (d != 0.0) {
            d2 = (double)(Math.abs(f - this.x) * 0.35f) / (10.0 / d);
            d3 = (double)(Math.abs(f2 - this.y) * 0.35f) / (10.0 / d);
        }
        this.x = AnimationUtil.calculateCompensation(f, this.x, l3, (int)d2);
        this.y = AnimationUtil.calculateCompensation(f2, this.y, l3, (int)d3);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float f) {
        this.x = f;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float f) {
        this.y = f;
    }
}

