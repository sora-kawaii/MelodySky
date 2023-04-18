/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.rendering;

import xyz.Melody.Event.Event;

public class EventDrawText
extends Event {
    private String text;

    public EventDrawText(String string) {
        this.text = string;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        this.text = string;
    }
}

