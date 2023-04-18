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

    public void func_73863_a(int n, int n2, float f) {
        Calendar calendar = Calendar.getInstance();
        int n3 = calendar.get(11);
        int n4 = calendar.get(12);
        int n5 = calendar.get(13);
        String string = n3 + " : " + n4 + " : " + n5;
        ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
        CFontRenderer cFontRenderer = FontLoaders.CNMD35;
        CFontRenderer cFontRenderer2 = FontLoaders.NMSL28;
        this.func_146276_q_();
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
                        this.field_146297_k.func_147108_a(new MainMenu((int)this.opacity.getOpacity()));
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
        cFontRenderer.drawCenteredString("Click or Tap the Keyboard to Continue.", (float)scaledResolution.func_78326_a() / 2.0f, (float)scaledResolution.func_78328_b() / 2.0f - 15.0f - (float)this.Anitext, new Color(255, 255, 255, this.textAlpha).getRGB());
        cFontRenderer2.drawCenteredString(string, (float)scaledResolution.func_78326_a() / 2.0f, (float)scaledResolution.func_78328_b() / 2.0f + 10.0f - (float)this.Anitext, new Color(180, 180, 180, this.textAlpha).getRGB());
    }

    protected void func_73864_a(int n, int n2, int n3) throws IOException {
        this.shouldMainMenu = true;
    }

    public void func_146282_l() throws IOException {
        this.shouldMainMenu = true;
        super.func_146282_l();
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
    }

    public void func_146281_b() {
        this.field_146297_k.field_71460_t.func_175071_c();
    }
}

