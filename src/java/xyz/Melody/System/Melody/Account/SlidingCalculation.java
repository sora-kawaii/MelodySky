/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account;

import org.lwjgl.input.Mouse;

public final class SlidingCalculation {
    private double current;
    private double added;
    private double minus;

    public SlidingCalculation() {
    }

    public SlidingCalculation(double d, double d2) {
        this.added = d;
        this.minus = d2;
    }

    public SlidingCalculation(double d, double d2, double d3) {
        this.current = d;
        this.added = d2;
        this.minus = d3;
    }

    public void calculation() {
        int n;
        if (Mouse.hasWheel() && (n = Mouse.getDWheel()) != 0) {
            this.current = n < 0 ? (this.current += this.added) : (this.current -= this.minus);
        }
    }

    public double getCurrent() {
        return this.current;
    }

    public void setCurrent(double d) {
        this.current = d;
    }

    public double getAdded() {
        return this.added;
    }

    public void setAdded(double d) {
        this.added = d;
    }

    public double getMinus() {
        return this.minus;
    }

    public void setMinus(double d) {
        this.minus = d;
    }
}

