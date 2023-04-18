/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.rendering;

import xyz.Melody.Event.Event;

public class EventRender2D
extends Event {
    private float partialTicks;

    public EventRender2D(float f) {
        this.partialTicks = f;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public void setPartialTicks(float f) {
        this.partialTicks = f;
    }
}

