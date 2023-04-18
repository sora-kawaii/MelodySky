/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.value;

public abstract class Value<V> {
    private String displayName;
    private String name;
    private V value;

    public Value(String string, String string2) {
        this.displayName = string;
        this.name = string2;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.name;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V v) {
        this.value = v;
    }
}

