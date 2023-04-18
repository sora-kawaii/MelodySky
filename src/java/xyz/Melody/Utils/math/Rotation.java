/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.math;

public final class Rotation {
    private float yaw;
    private float pitch;

    public Rotation(float f, float f2) {
        this.yaw = f;
        this.pitch = f2;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float f) {
        this.yaw = f;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float f) {
        this.pitch = f;
    }

    public void addYaw(float f) {
        this.yaw += f;
    }

    public void addPitch(float f) {
        this.pitch += f;
    }

    public float getValue() {
        return Math.abs(this.yaw) + Math.abs(this.pitch);
    }

    public String toString() {
        return "Rotation{yaw=" + this.yaw + ", pitch=" + this.pitch + '}';
    }
}

