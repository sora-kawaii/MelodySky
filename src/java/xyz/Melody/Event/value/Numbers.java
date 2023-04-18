/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.value;

import xyz.Melody.Event.value.Value;

public class Numbers<T extends Number>
extends Value<T> {
    private String name;
    public T min;
    public T max;
    public T inc;
    private boolean integer;

    public Numbers(String string, T t, T t2, T t3, T t4) {
        super(string, string);
        this.setValue(t);
        this.min = t2;
        this.max = t3;
        this.inc = t4;
        this.integer = false;
    }

    public T getMinimum() {
        return this.min;
    }

    public T getMaximum() {
        return this.max;
    }

    public void setIncrement(T t) {
        this.inc = t;
    }

    public T getIncrement() {
        return this.inc;
    }

    public String getId() {
        return this.name;
    }
}

