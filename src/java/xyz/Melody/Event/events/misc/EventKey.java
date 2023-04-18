/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.misc;

import xyz.Melody.Event.Event;

public class EventKey
extends Event {
    private int key;

    public EventKey(int n) {
        this.key = n;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int n) {
        this.key = n;
    }
}

