/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.ClickNew;

import xyz.Melody.GUI.ClickNew.AnimationUtil;

public final class Opacity {
    private float opacity;
    private long lastMS;

    public Opacity(int opacity) {
        this.opacity = opacity;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float targetOpacity) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.opacity = AnimationUtil.calculateCompensation(targetOpacity, this.opacity, delta, 20.0f);
        this.lastMS = currentMS;
    }

    public void interp(float targetOpacity, float speed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.opacity = AnimationUtil.calculateCompensation(targetOpacity, this.opacity, delta, speed);
        this.lastMS = currentMS;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
}

