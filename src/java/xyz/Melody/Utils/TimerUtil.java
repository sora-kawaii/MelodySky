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

    public final boolean elapsed(long milliseconds) {
        return this.getCurrentMS() - this.ms > milliseconds;
    }

    public final void resetStopWatch() {
        this.ms = this.getCurrentMS();
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double milliseconds) {
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public boolean delay(float milliSec) {
        return (float)(this.getTime() - this.lastMS) >= milliSec;
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

