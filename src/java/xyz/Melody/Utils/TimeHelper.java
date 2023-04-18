/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

public final class TimeHelper {
    public long lastMs = 0L;

    public boolean isDelayComplete(long l2) {
        return System.currentTimeMillis() - this.lastMs > l2;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public void setLastMs(int n) {
        this.lastMs = System.currentTimeMillis() + (long)n;
    }

    public boolean hasReached(long l2) {
        return this.getCurrentMS() - this.lastMs >= l2;
    }

    public boolean hasReached(float f) {
        return (float)(this.getCurrentMS() - this.lastMs) >= f;
    }

    public boolean delay(double d) {
        return (double)(System.currentTimeMillis() - this.lastMs) >= d;
    }
}

