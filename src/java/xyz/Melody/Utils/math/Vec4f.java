/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.math;

public final class Vec4f {
    public float x;
    public float y;
    public float w;
    public float h;

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getW() {
        return this.w;
    }

    public float getH() {
        return this.h;
    }

    public void setX(float f) {
        this.x = f;
    }

    public void setY(float f) {
        this.y = f;
    }

    public void setW(float f) {
        this.w = f;
    }

    public void setH(float f) {
        this.h = f;
    }

    public Vec4f(float f, float f2, float f3, float f4) {
        this.x = f;
        this.y = f2;
        this.w = f3;
        this.h = f4;
    }
}

