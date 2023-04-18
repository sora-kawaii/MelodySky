/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Particles.Winter;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import xyz.Melody.GUI.Particles.Winter.Particle;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.render.gl.GLUtils;

public final class ParticleEngine {
    public ArrayList<Particle> particles = new ArrayList();
    public float lastMouseX;
    public float lastMouseY;

    public void render(float f, float f2) {
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        float f3 = f;
        float f4 = f2;
        this.particles.size();
        while (this.particles.size() < scaledResolution.getScaledWidth() / 19) {
            this.particles.add(new Particle(scaledResolution, new Random().nextFloat() * 2.0f + 2.0f, new Random().nextFloat() * 5.0f + 5.0f));
        }
        ArrayList<Particle> arrayList = Lists.newArrayList();
        for (Particle particle : this.particles) {
            if (particle.opacity < 32.0f) {
                particle.opacity += 2.0f;
            }
            if (particle.opacity > 32.0f) {
                particle.opacity = 32.0f;
            }
            Color color = new Color(255, 255, 255, (int)particle.opacity);
            RenderUtil.drawFilledCircle(particle.x + (float)Math.sin(particle.ticks / 2.0f) * 50.0f + -f3 / 5.0f, particle.ticks * particle.speed * particle.ticks / 10.0f + -f4 / 5.0f, particle.radius * (particle.opacity / 32.0f), color);
            particle.ticks = (float)((double)particle.ticks + 0.05);
            if (!(particle.ticks * particle.speed * particle.ticks / 10.0f + -f4 / 5.0f > (float)scaledResolution.getScaledHeight() || particle.ticks * particle.speed * particle.ticks / 10.0f + -f4 / 5.0f < 0.0f || (double)particle.x + Math.sin(particle.ticks / 2.0f) * 50.0 + (double)(-f3 / 5.0f) > (double)scaledResolution.getScaledWidth()) && !((double)particle.x + Math.sin(particle.ticks / 2.0f) * 50.0 + (double)(-f3 / 5.0f) < 0.0)) continue;
            arrayList.add(particle);
        }
        this.particles.removeAll(arrayList);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        this.lastMouseX = GLUtils.getMouseX();
        this.lastMouseY = GLUtils.getMouseY();
    }
}

