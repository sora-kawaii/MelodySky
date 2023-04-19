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

    public void render(float mouseX, float mouseY) {
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float xOffset = mouseX;
        float yOffset = mouseY;
        this.particles.size();
        while (this.particles.size() < sr.getScaledWidth() / 19) {
            this.particles.add(new Particle(sr, new Random().nextFloat() * 2.0f + 2.0f, new Random().nextFloat() * 5.0f + 5.0f));
        }
        ArrayList<Particle> toremove = Lists.newArrayList();
        for (Particle p : this.particles) {
            if (p.opacity < 32.0f) {
                p.opacity += 2.0f;
            }
            if (p.opacity > 32.0f) {
                p.opacity = 32.0f;
            }
            Color c = new Color(255, 255, 255, (int)p.opacity);
            RenderUtil.drawFilledCircle(p.x + (float)Math.sin(p.ticks / 2.0f) * 50.0f + -xOffset / 5.0f, p.ticks * p.speed * p.ticks / 10.0f + -yOffset / 5.0f, p.radius * (p.opacity / 32.0f), c);
            p.ticks = (float)((double)p.ticks + 0.05);
            if (!(p.ticks * p.speed * p.ticks / 10.0f + -yOffset / 5.0f > (float)sr.getScaledHeight() || p.ticks * p.speed * p.ticks / 10.0f + -yOffset / 5.0f < 0.0f || (double)p.x + Math.sin(p.ticks / 2.0f) * 50.0 + (double)(-xOffset / 5.0f) > (double)sr.getScaledWidth()) && !((double)p.x + Math.sin(p.ticks / 2.0f) * 50.0 + (double)(-xOffset / 5.0f) < 0.0)) continue;
            toremove.add(p);
        }
        this.particles.removeAll(toremove);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        this.lastMouseX = GLUtils.getMouseX();
        this.lastMouseY = GLUtils.getMouseY();
    }
}

