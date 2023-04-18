/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.Player;

import xyz.Melody.Event.Event;

public class EventPreUpdate
extends Event {
    private float yaw;
    private float pitch;
    public double x;
    public double y;
    public double z;
    private boolean ground;

    public EventPreUpdate(float f, float f2, double d, double d2, double d3, boolean bl) {
        this.yaw = f;
        this.pitch = f2;
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.ground = bl;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double d) {
        this.x = d;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double d) {
        this.z = d;
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

    public double getY() {
        return this.y;
    }

    public void setY(double d) {
        this.y = d;
    }

    public boolean isOnground() {
        return this.ground;
    }

    public void setOnground(boolean bl) {
        this.ground = bl;
    }
}

