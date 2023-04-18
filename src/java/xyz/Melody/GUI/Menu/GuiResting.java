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
    public void drawScreen(int n, int n2, float f) {
        Calendar calendar = Calendar.getInstance();
        int n3 = calendar.get(11);
        int n4 = calendar.get(12);
        int n5 = calendar.get(13);
        String string = n3 + " : " + n4 + " : " + n5;
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        CFontRenderer cFontRenderer = FontLoaders.CNMD35;
        CFontRenderer cFontRenderer2 = FontLoaders.NMSL28;
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
        ParticleUtils.drawParticles(n, n2);
        cFontRenderer.drawCenteredString("Click or Tap the Keyboard to Continue.", (float)scaledResolution.getScaledWidth() / 2.0f, (float)scaledResolution.getScaledHeight() / 2.0f - 15.0f - (float)this.Anitext, new Color(255, 255, 255, this.textAlpha).getRGB());
        cFontRenderer2.drawCenteredString(string, (float)scaledResolution.getScaledWidth() / 2.0f, (float)scaledResolution.getScaledHeight() / 2.0f + 10.0f - (float)this.Anitext, new Color(180, 180, 180, this.textAlpha).getRGB());
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
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
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(0.0, this.height, 0.0).endVertex();
        worldRenderer.pos(this.width, this.height, 0.0).endVertex();
        worldRenderer.pos(this.width, 0.0, 0.0).endVertex();
        worldRenderer.pos(0.0, 0.0, 0.0).endVertex();
        tessellator.draw();
        BackgroundShader.BACKGROUND_SHADER.stopShader();
    }

    @Override
    public void onGuiClosed() {
        this.mc.entityRenderer.switchUseShader();
    }
}

