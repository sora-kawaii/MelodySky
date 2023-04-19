/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.rendering;

import xyz.Melody.Event.Event;

public class EventDrawText
extends Event {
    private String text;

    public EventDrawText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

