/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.Player;

import xyz.Melody.Event.Event;

public class EventPostUpdate
extends Event {
    private float yaw;
    private float pitch;

    public EventPostUpdate(float f, float f2) {
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
}

