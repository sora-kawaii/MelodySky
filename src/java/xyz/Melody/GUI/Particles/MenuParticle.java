/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Particles;

import java.util.ArrayList;
import java.util.Random;

public final class MenuParticle {
    public double x;
    public double y;
    public double preX;
    public double preY;
    public double depth;
    public double motionX;
    public double motionY;
    public float alpha = 1.0f;
    public boolean remove = false;
    public boolean followMouse = false;
    public Random rand = new Random();
    public boolean alphaDecay = false;

    public MenuParticle(double d, double d2, double d3, boolean bl) {
        this.x = d;
        this.y = d2;
        this.depth = d3;
        this.followMouse = bl;
    }

    public void update(int n, int n2, ArrayList<MenuParticle> arrayList) {
        if (this.followMouse) {
            float f = (float)Math.toDegrees(Math.atan2((double)n2 - this.y, (double)n - this.x));
            if (f < 0.0f) {
                f += 360.0f;
            }
            double d = Math.cos(Math.toRadians(f));
            double d2 = Math.sin(Math.toRadians(f));
            this.motionX += d * 1.0;
            this.motionY += d2 * 1.0;
        }
        this.preX = this.x;
        this.preY = this.y;
        this.x += this.motionX;
        this.y += this.motionY;
        this.motionY *= 0.985;
        this.motionX *= 0.985;
        if (this.alphaDecay) {
            this.alpha *= 0.995f;
            this.motionX *= 1.1;
            this.motionY *= 1.1;
        }
    }

    public MenuParticle addMotion(double d, double d2) {
        this.motionX += d;
        this.motionY += d2;
        return this;
    }
}

