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
        this.x = new Random().nextFloat() * (float)scaledResolution.getScaledWidth();
        this.y = new Random().nextFloat() * (float)scaledResolution.getScaledHeight();
        this.ticks = new Random().nextFloat() * (float)scaledResolution.getScaledHeight() / 10.0f;
        this.radius = f;
        this.speed = f2;
    }
}

