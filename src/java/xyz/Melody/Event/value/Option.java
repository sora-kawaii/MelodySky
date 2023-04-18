/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.value;

import xyz.Melody.Event.value.Value;

public class Option<V>
extends Value<V> {
    public Option(String string, V v) {
        super(string, string);
        this.setValue(v);
    }
}

