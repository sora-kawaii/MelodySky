/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.ClickNew;

import xyz.Melody.GUI.ClickNew.AnimationUtil;

public final class Opacity {
    private float opacity;
    private long lastMS;

    public Opacity(int n) {
        this.opacity = n;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float f) {
        long l2 = System.currentTimeMillis();
        long l3 = l2 - this.lastMS;
        this.opacity = AnimationUtil.calculateCompensation(f, this.opacity, l3, 20.0f);
        this.lastMS = l2;
    }

    public void interp(float f, float f2) {
        long l2 = System.currentTimeMillis();
        long l3 = l2 - this.lastMS;
        this.opacity = AnimationUtil.calculateCompensation(f, this.opacity, l3, f2);
        this.lastMS = l2;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float f) {
        this.opacity = f;
    }
}

