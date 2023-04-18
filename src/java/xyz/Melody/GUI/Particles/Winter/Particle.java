/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Particles.Winter;

import java.util.Random;
import net.minecraft.client.gui.ScaledResolution;

public final class Particle {
    public float x;
    public float y;
    public float radius;
    public float speed;
    public float ticks;
    public float opacity;

    public Particle(ScaledResolution scaledResolution, float f, float f2) {
        this.x = new Random().nextFloat() * (float)scaledResolution.func_78326_a();
        this.y = new Random().nextFloat() * (float)scaledResolution.func_78328_b();
        this.ticks = new Random().nextFloat() * (float)scaledResolution.func_78328_b() / 10.0f;
        this.radius = f;
        this.speed = f2;
    }
}

