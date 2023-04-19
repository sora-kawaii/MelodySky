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

    public Translate(float x, float y) {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float targetX, float targetY, int xSpeed, int ySpeed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        int deltaX = (int)(Math.abs(targetX - this.x) * 0.51f);
        int deltaY = (int)(Math.abs(targetY - this.y) * 0.51f);
        this.x = AnimationUtil.calculateCompensation(targetX, this.x, delta, deltaX);
        this.y = AnimationUtil.calculateCompensation(targetY, this.y, delta, deltaY);
    }

    public void interpolate(float targetX, float targetY, double speed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        double deltaX = 0.0;
        double deltaY = 0.0;
        if (speed != 0.0) {
            deltaX = (double)(Math.abs(targetX - this.x) * 0.35f) / (10.0 / speed);
            deltaY = (double)(Math.abs(targetY - this.y) * 0.35f) / (10.0 / speed);
        }
        this.x = AnimationUtil.calculateCompensation(targetX, this.x, delta, (int)deltaX);
        this.y = AnimationUtil.calculateCompensation(targetY, this.y, delta, (int)deltaY);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

