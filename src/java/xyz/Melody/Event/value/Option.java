/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.value;

import xyz.Melody.Event.value.Value;

public class Option<V>
extends Value<V> {
    public Option(String name, V enabled) {
        super(name, name);
        this.setValue(enabled);
    }
}

