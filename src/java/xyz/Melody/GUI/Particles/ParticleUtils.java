/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Particles;

import xyz.Melody.GUI.Particles.ParticleGenerator;

public final class ParticleUtils {
    private static final ParticleGenerator particleGenerator = new ParticleGenerator(100);

    public static void drawParticles(int mouseX, int mouseY) {
        particleGenerator.draw(mouseX, mouseY);
    }

    public static void drawParticles() {
        particleGenerator.draw(-10, -10);
    }
}

