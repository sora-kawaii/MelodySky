/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.rendering;

import xyz.Melody.Event.Event;

public class EventRender3D
extends Event {
    private float ticks;
    private boolean isUsingShaders;

    public EventRender3D() {
    }

    public EventRender3D(float f) {
        this.ticks = f;
    }

    public float getPartialTicks() {
        return this.ticks;
    }

    public boolean isUsingShaders() {
        return this.isUsingShaders;
    }
}

