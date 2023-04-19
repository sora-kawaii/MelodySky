/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Menu;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import xyz.Melody.Client;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.TimerUtil;

public final class GuiHideMods
extends GuiConfig {
    private int shabiAlpha = 0;
    private int alpha = 0;
    private int contentAlpha = 0;
    private float titleY = 0.0f;
    private TimerUtil timer = new TimerUtil();
    private int continueAlpha = 0;

    @Override
    public void initGui() {
        this.alpha = 0;
        this.titleY = 0.0f;
        this.contentAlpha = 0;
        this.continueAlpha = 0;
        this.timer.reset();
        super.initGui();
    }

    public GuiHideMods(GuiScreen parentScreen) {
        super(parentScreen, new ConfigElement(Client.modsConfig.getCategory("general")).getChildElements(), "MelodySky", false, false, "Melody Sky Mods Hider");
        this.titleLine2 = "Select Which Mods Will Be Hidden.";
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Client.modsConfig.save();
    }

    @Override
    public void drawDefaultBackground() {
        BackgroundShader.BACKGROUND_SHADER.startShader();
        Tessellator instance = Tessellator.getInstance();
        WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(0.0, this.height, 0.0).endVertex();
        worldRenderer.pos(this.width, this.height, 0.0).endVertex();
        worldRenderer.pos(this.width, 0.0, 0.0).endVertex();
        worldRenderer.pos(0.0, 0.0, 0.0).endVertex();
        instance.draw();
        BackgroundShader.BACKGROUND_SHADER.stopShader();
        ParticleUtils.drawParticles();
    }
}

