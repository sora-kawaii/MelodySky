/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Particles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import xyz.Melody.GUI.Particles.Particle;
import xyz.Melody.GUI.Particles.RenderUtils;

public final class ParticleGenerator {
    private final List<Particle> particles = new ArrayList<Particle>();
    private final int amount;
    private int prevWidth;
    private int prevHeight;

    public ParticleGenerator(int amount) {
        this.amount = amount;
    }

    public void draw(int mouseX, int mouseY) {
        if (this.particles.isEmpty() || this.prevWidth != Minecraft.getMinecraft().displayWidth || this.prevHeight != Minecraft.getMinecraft().displayHeight) {
            this.particles.clear();
            this.create();
        }
        this.prevWidth = Minecraft.getMinecraft().displayWidth;
        this.prevHeight = Minecraft.getMinecraft().displayHeight;
        for (Particle particle : this.particles) {
            boolean mouseOver;
            particle.fall();
            particle.interpolation();
            int range = 60;
            boolean bl = mouseOver = (float)mouseX >= particle.x - (float)range && (float)mouseY >= particle.y - (float)range && (float)mouseX <= particle.x + (float)range && (float)mouseY <= particle.y + (float)range;
            if (mouseOver) {
                this.particles.stream().filter(part -> part.getX() > particle.getX() && part.getX() - particle.getX() < (float)range && particle.getX() - part.getX() < (float)range && (part.getY() > particle.getY() && part.getY() - particle.getY() < (float)range || particle.getY() > part.getY() && particle.getY() - part.getY() < (float)range)).forEach(connectable -> particle.connect(connectable.getX(), connectable.getY()));
            }
            RenderUtils.drawCircle(particle.getX(), particle.getY(), particle.size, new Color(255, 255, 255, 100).getRGB());
        }
    }

    private void create() {
        Random random = new Random();
        for (int i = 0; i < this.amount; ++i) {
            this.particles.add(new Particle(random.nextInt(Minecraft.getMinecraft().displayWidth), random.nextInt(Minecraft.getMinecraft().displayHeight)));
        }
    }
}

