/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import xyz.Melody.Event.Event;

public class DrawSlotEvent
extends Event {
    public Container container;
    public Slot slot;

    public DrawSlotEvent(Container container, Slot slot) {
        this.container = container;
        this.slot = slot;
    }
}

