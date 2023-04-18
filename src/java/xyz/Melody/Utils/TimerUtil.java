/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import java.util.Date;

public final class TimerUtil {
    private long lastMS;
    private long ms = this.getCurrentMS();

    public final long getElapsedTime() {
        return this.getCurrentMS() - this.ms;
    }

    public final boolean elapsed(long l2) {
        return this.getCurrentMS() - this.ms > l2;
    }

    public final void resetStopWatch() {
        this.ms = this.getCurrentMS();
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double d) {
        return (double)(this.getCurrentMS() - this.lastMS) >= d;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public boolean delay(float f) {
        return (float)(this.getTime() - this.lastMS) >= f;
    }

    public long getLastMS() {
        return this.lastMS;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public static long curTime() {
        return new Date().getTime();
    }
}

