/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.value;

import xyz.Melody.Event.value.Value;

public class Mode<V extends Enum>
extends Value<V> {
    private V[] modes;

    public Mode(String string, V[] VArray, V v) {
        super(string, string);
        this.modes = VArray;
        this.setValue(v);
    }

    public V[] getModes() {
        return this.modes;
    }

    public String getModeAsString() {
        return ((Enum)this.getValue()).name();
    }

    public void setMode(String string) {
        for (V v : this.modes) {
            if (!((Enum)v).name().equalsIgnoreCase(string)) continue;
            this.setValue(v);
        }
    }

    public boolean isValid(String string) {
        for (V v : this.modes) {
            if (!((Enum)v).name().equalsIgnoreCase(string)) continue;
            return true;
        }
        return false;
    }
}

