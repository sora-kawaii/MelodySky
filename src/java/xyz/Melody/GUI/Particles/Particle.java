/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Particles;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import xyz.Melody.GUI.Particles.RenderUtils;

class Particle {
    public float x;
    public float y;
    public final float size;
    private final float ySpeed = new Random().nextInt(5);
    private final float xSpeed = new Random().nextInt(5);
    private int height;
    private int width;

    Particle(int n, int n2) {
        this.x = n;
        this.y = n2;
        this.size = this.genRandom();
    }

    private float lint1(float f) {
        return 1.02f * (1.0f - f) + 1.0f * f;
    }

    private float lint2(float f) {
        return 1.02f + f * -0.01999998f;
    }

    void connect(float f, float f2) {
        RenderUtils.connectPoints(this.getX(), this.getY(), f, f2);
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int n) {
        this.height = n;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int n) {
        this.width = n;
    }

    public float getX() {
        return this.x;
    }

    public void setX(int n) {
        this.x = n;
    }

    public float getY() {
        return this.y;
    }

    public void setY(int n) {
        this.y = n;
    }

    void interpolation() {
        for (int i = 0; i <= 64; ++i) {
            float f;
            float f2 = (float)i / 64.0f;
            float f3 = this.lint1(f2);
            if (f3 == (f = this.lint2(f2))) continue;
            this.y -= f2;
            this.x -= f2;
        }
    }

    void fall() {
        Minecraft minecraft = Minecraft.func_71410_x();
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        this.y += this.ySpeed;
        this.x += this.xSpeed;
        if (this.y > (float)minecraft.field_71440_d) {
            this.y = 1.0f;
        }
        if (this.x > (float)minecraft.field_71443_c) {
            this.x = 1.0f;
        }
        if (this.x < 1.0f) {
            this.x = scaledResolution.func_78326_a();
        }
        if (this.y < 1.0f) {
            this.y = scaledResolution.func_78328_b();
        }
    }

    private float genRandom() {
        return (float)((double)0.3f + Math.random() * (double)1.3f);
    }
}

