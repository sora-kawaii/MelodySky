/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Menu;

import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import xyz.Melody.Client;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Menu.MainMenu;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.FadeUtil;
import xyz.Melody.Utils.shader.GaussianBlur;

public final class GuiWelcome
extends GuiScreen {
    private int shabiAlpha = 0;
    private int alpha = 0;
    private int contentAlpha = 0;
    private float titleY = 0.0f;
    private int enjoyAlpha = 0;
    private boolean shouldMainMenu;
    private TimerUtil timer = new TimerUtil();
    private TimerUtil timer2 = new TimerUtil();
    private TimerUtil timer3 = new TimerUtil();
    private int continueAlpha = 0;
    private GaussianBlur gblur = new GaussianBlur();

    public void func_73866_w_() {
        this.shouldMainMenu = false;
        this.alpha = 0;
        this.titleY = 0.0f;
        this.contentAlpha = 0;
        this.continueAlpha = 0;
        this.enjoyAlpha = 0;
        this.shabiAlpha = 0;
        this.timer.reset();
        this.timer2.reset();
        super.func_73866_w_();
    }

    public void func_73863_a(int n, int n2, float f) {
        CFontRenderer cFontRenderer = FontLoaders.CNMD35;
        CFontRenderer cFontRenderer2 = FontLoaders.CNMD45;
        CFontRenderer cFontRenderer3 = FontLoaders.NMSL22;
        CFontRenderer cFontRenderer4 = FontLoaders.CNMD30;
        this.func_146276_q_();
        this.gblur.renderBlur(140.0f);
        ParticleUtils.drawParticles(n, n2);
        if (Client.firstLaunch) {
            if (this.alpha < 210) {
                this.alpha += 3;
            }
            if (this.alpha >= 210 && this.titleY < (float)(this.field_146295_m / 3)) {
                this.titleY += (float)(this.field_146295_m / 100);
            }
            if (this.titleY >= (float)(this.field_146295_m / 3) && this.contentAlpha < 210) {
                this.contentAlpha += 7;
            }
            cFontRenderer2.drawCenteredString("Melody Skyblock", (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 2.0f - 3.0f - this.titleY, new Color(255, 255, 255, this.alpha).getRGB());
            float f2 = 25.0f;
            if (this.contentAlpha > 0) {
                cFontRenderer3.drawCenteredString("What is MelodySky?   This is a Mod that improves The Quality of Life of Hypixel Skyblock (QOL Mod).", (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 3.0f, new Color(255, 255, 255, this.contentAlpha).getRGB());
                cFontRenderer3.drawCenteredString("What Would This Offer?   Auto Fishing, Auto Experiment Table. Auto Terminals, Livid Finder. Client-Side Name Changing, Custom Rank.", (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 3.0f + 25.0f, new Color(255, 255, 255, this.contentAlpha).getRGB());
                cFontRenderer3.drawCenteredString("Mithril Nuker, Hardstone Nuker, Powder Chest Macro, Show Lowes Bin Data, Show Dungeon Chest Profit. And Client IRC Chatting.", (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 3.0f + 50.0f, new Color(255, 255, 255, this.contentAlpha).getRGB());
                cFontRenderer3.drawString("Tip 1 - Type '.bind clickgui rshift' to Set the Binding of Click Gui to Right Shift.", 280.0f, (float)this.field_146295_m / 3.0f + 50.0f + f2, new Color(249, 205, 173, this.contentAlpha).getRGB());
                cFontRenderer3.drawString("Tip 2 - In Click Gui, Left Click on a Module to Toggle, Right Click to Show Settings.", 280.0f, (float)this.field_146295_m / 3.0f + 75.0f + f2, new Color(249, 205, 173, this.contentAlpha).getRGB());
                cFontRenderer3.drawString("Tip 3 - Try 'Edit Locations' Button in the Left Bottom Position in Click Gui.", 280.0f, (float)this.field_146295_m / 3.0f + 100.0f + f2, new Color(249, 205, 173, this.contentAlpha).getRGB());
                cFontRenderer3.drawString("Tip 4 - Type '.help' to Show All Client Commands and Useage.", 280.0f, (float)this.field_146295_m / 3.0f + 125.0f + f2, new Color(249, 205, 173, this.contentAlpha).getRGB());
                if (this.timer2.hasReached(6000.0)) {
                    if (this.enjoyAlpha > 0 && this.enjoyAlpha < 210) {
                        cFontRenderer3.drawCenteredString("---==== Enjoy :) ====---", (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 3.0f + 175.0f, new Color(78, 128, 190, this.enjoyAlpha).getRGB());
                    }
                    if (this.enjoyAlpha >= 210) {
                        cFontRenderer3.drawCenteredString("---==== Enjoy :) ====---", (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 3.0f + 175.0f, new Color(78, 128, 190, this.enjoyAlpha).getRGB());
                    }
                    if (this.enjoyAlpha < 210) {
                        this.enjoyAlpha += 7;
                    }
                }
                if (this.timer.hasReached(11000.0)) {
                    if (this.continueAlpha > 0 && this.continueAlpha < 210) {
                        cFontRenderer4.drawCenteredString("Click To Continue.", (float)this.field_146294_l / 2.0f, this.field_146295_m - 100, new Color(255, 255, 255, this.continueAlpha).getRGB());
                    }
                    if (this.continueAlpha >= 210) {
                        cFontRenderer4.drawCenteredString("Click To Continue.", (float)this.field_146294_l / 2.0f, this.field_146295_m - 100, FadeUtil.fade(new Color(255, 255, 255, this.continueAlpha)).getRGB());
                    }
                    if (this.continueAlpha < 210) {
                        this.continueAlpha += 7;
                    }
                }
            }
        }
        if (!Client.firstLaunch) {
            if (!this.shouldMainMenu && this.shabiAlpha < 210) {
                this.shabiAlpha += 10;
            }
            if (this.shouldMainMenu && this.shabiAlpha > 10) {
                this.shabiAlpha -= 12;
                this.timer3.reset();
            }
            cFontRenderer.drawCenteredString("Welcome back to MelodySky", (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 2.0f - 3.0f, new Color(255, 255, 255, this.shabiAlpha).getRGB());
            if (this.shabiAlpha <= 10 && this.shouldMainMenu) {
                this.shabiAlpha = 6;
                if (this.timer3.hasReached(100.0)) {
                    Client.firstMenu = false;
                    this.field_146297_k.func_147108_a(new MainMenu(140));
                    this.timer3.reset();
                }
            }
        }
        super.func_73863_a(n, n2, f);
    }

    protected void func_73864_a(int n, int n2, int n3) throws IOException {
        if (Client.firstLaunch) {
            if (this.continueAlpha >= 210) {
                this.field_146297_k.func_147108_a(this);
                Client.firstLaunch = false;
            }
        } else {
            this.shouldMainMenu = true;
        }
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
}

