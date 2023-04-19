/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.value;

import xyz.Melody.Event.value.Value;

public class Mode<V extends Enum>
extends Value<V> {
    private V[] modes;

    public Mode(String name, V[] modes, V value) {
        super(name, name);
        this.modes = modes;
        this.setValue(value);
    }

    public V[] getModes() {
        return this.modes;
    }

    public String getModeAsString() {
        return ((Enum)this.getValue()).name();
    }

    public void setMode(String mode) {
        for (V e : this.modes) {
            if (!((Enum)e).name().equalsIgnoreCase(mode)) continue;
            this.setValue(e);
        }
    }

    public boolean isValid(String name) {
        for (V e : this.modes) {
            if (!((Enum)e).name().equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }
}

