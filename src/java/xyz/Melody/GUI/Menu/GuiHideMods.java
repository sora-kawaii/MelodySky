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

    public void func_73866_w_() {
        this.alpha = 0;
        this.titleY = 0.0f;
        this.contentAlpha = 0;
        this.continueAlpha = 0;
        this.timer.reset();
        super.func_73866_w_();
    }

    public GuiHideMods(GuiScreen guiScreen) {
        super(guiScreen, new ConfigElement(Client.modsConfig.getCategory("general")).getChildElements(), "MelodySky", false, false, "Melody Sky Mods Hider");
        this.titleLine2 = "Select Which Mods Will Be Hidden.";
    }

    public void func_146281_b() {
        super.func_146281_b();
        Client.modsConfig.save();
    }

    public void func_146276_q_() {
        BackgroundShader.BACKGROUND_SHADER.startShader();
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(0.0, this.field_146295_m, 0.0).func_181675_d();
        worldRenderer.func_181662_b(this.field_146294_l, this.field_146295_m, 0.0).func_181675_d();
        worldRenderer.func_181662_b(this.field_146294_l, 0.0, 0.0).func_181675_d();
        worldRenderer.func_181662_b(0.0, 0.0, 0.0).func_181675_d();
        tessellator.func_78381_a();
        BackgroundShader.BACKGROUND_SHADER.stopShader();
        ParticleUtils.drawParticles();
    }
}

