/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.animate;

public abstract class Animation {
    protected int time;
    protected boolean enabling;

    public void update() {
        this.time = this.enabling ? ++this.time : --this.time;
        if (this.time < 0) {
            this.time = 0;
        }
        if (this.time > this.getMaxTime()) {
            this.time = this.getMaxTime();
        }
    }

    public void reset() {
        this.time = 0;
    }

    public int getMaxTime() {
        return 10;
    }

    public int getTime() {
        return this.time;
    }

    public void on() {
        this.enabling = true;
    }

    public void toggle() {
        this.enabling = !this.enabling;
    }

    public void toggle(boolean bl) {
        this.enabling = bl;
    }

    public void off() {
        this.enabling = false;
    }

    public boolean isEnabled() {
        return this.enabling;
    }

    public abstract void render();
}

