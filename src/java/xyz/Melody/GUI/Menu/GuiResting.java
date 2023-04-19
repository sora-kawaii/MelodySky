/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Menu;

import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import xyz.Melody.GUI.ClickNew.Opacity;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Menu.MainMenu;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.shader.GaussianBlur;

public final class GuiResting
extends GuiScreen {
    public boolean shouldMainMenu = false;
    private Opacity opacity = new Opacity(10);
    private TimerUtil timer = new TimerUtil();
    private ParticleUtils particle;
    private int textAlpha = 0;
    private double Anitext = 0.0;
    private GaussianBlur gb = new GaussianBlur();

    public GuiResting() {
        this.particle = new ParticleUtils();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(11);
        int min = c.get(12);
        int sec = c.get(13);
        String time = hour + " : " + min + " : " + sec;
        ScaledResolution sr = new ScaledResolution(this.mc);
        CFontRenderer titleFont = FontLoaders.CNMD35;
        CFontRenderer timeFont = FontLoaders.NMSL28;
        this.drawDefaultBackground();
        this.gb.renderBlur(this.opacity.getOpacity());
        this.opacity.interp(140.0f, 5.0f);
        if (this.opacity.getOpacity() == 140.0f) {
            if (this.shouldMainMenu) {
                if (this.textAlpha >= 16) {
                    this.textAlpha -= 16;
                    this.timer.reset();
                }
                if (this.textAlpha <= 16) {
                    this.textAlpha = 6;
                    if (this.timer.hasReached(300.0)) {
                        this.mc.displayGuiScreen(new MainMenu((int)this.opacity.getOpacity()));
                    }
                }
            } else {
                if (this.textAlpha < 170) {
                    this.textAlpha += 14;
                }
                if (this.textAlpha >= 170) {
                    this.textAlpha = 170;
                }
            }
        } else {
            this.textAlpha = 6;
        }
        ParticleUtils.drawParticles(mouseX, mouseY);
        titleFont.drawCenteredString("Click or Tap the Keyboard to Continue.", (float)sr.getScaledWidth() / 2.0f, (float)sr.getScaledHeight() / 2.0f - 15.0f - (float)this.Anitext, new Color(255, 255, 255, this.textAlpha).getRGB());
        timeFont.drawCenteredString(time, (float)sr.getScaledWidth() / 2.0f, (float)sr.getScaledHeight() / 2.0f + 10.0f - (float)this.Anitext, new Color(180, 180, 180, this.textAlpha).getRGB());
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.shouldMainMenu = true;
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        this.shouldMainMenu = true;
        super.handleKeyboardInput();
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
    }

    @Override
    public void onGuiClosed() {
        this.mc.entityRenderer.switchUseShader();
    }
}

