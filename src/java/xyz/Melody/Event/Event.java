/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event;

public abstract class Event {
    private boolean cancelled;
    public byte type;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean bl) {
        this.cancelled = bl;
    }

    public byte getType() {
        return this.type;
    }

    public void setType(byte by) {
        this.type = by;
    }
}

