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

    @Override
    public void initGui() {
        this.shouldMainMenu = false;
        this.alpha = 0;
        this.titleY = 0.0f;
        this.contentAlpha = 0;
        this.continueAlpha = 0;
        this.enjoyAlpha = 0;
        this.shabiAlpha = 0;
        this.timer.reset();
        this.timer2.reset();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CFontRenderer font = FontLoaders.CNMD35;
        CFontRenderer titleFont = FontLoaders.CNMD45;
        CFontRenderer contentFont = FontLoaders.NMSL22;
        CFontRenderer continueFont = FontLoaders.CNMD30;
        this.drawDefaultBackground();
        this.gblur.renderBlur(140.0f);
        ParticleUtils.drawParticles(mouseX, mouseY);
        if (Client.firstLaunch) {
            if (this.alpha < 210) {
                this.alpha += 3;
            }
            if (this.alpha >= 210 && this.titleY < (float)(this.height / 3)) {
                this.titleY += (float)(this.height / 100);
            }
            if (this.titleY >= (float)(this.height / 3) && this.contentAlpha < 210) {
                this.contentAlpha += 7;
            }
            titleFont.drawCenteredString("Melody Skyblock", (float)this.width / 2.0f, (float)this.height / 2.0f - 3.0f - this.titleY, new Color(255, 255, 255, this.alpha).getRGB());
            float tipsAppendY = 25.0f;
            if (this.contentAlpha > 0) {
                contentFont.drawCenteredString("What is MelodySky?   This is a Mod that improves The Quality of Life of Hypixel Skyblock (QOL Mod).", (float)this.width / 2.0f, (float)this.height / 3.0f, new Color(255, 255, 255, this.contentAlpha).getRGB());
                contentFont.drawCenteredString("What Would This Offer?   Auto Fishing, Auto Experiment Table. Auto Terminals, Livid Finder. Client-Side Name Changing, Custom Rank.", (float)this.width / 2.0f, (float)this.height / 3.0f + 25.0f, new Color(255, 255, 255, this.contentAlpha).getRGB());
                contentFont.drawCenteredString("Mithril Nuker, Hardstone Nuker, Powder Chest Macro, Show Lowes Bin Data, Show Dungeon Chest Profit. And Client IRC Chatting.", (float)this.width / 2.0f, (float)this.height / 3.0f + 50.0f, new Color(255, 255, 255, this.contentAlpha).getRGB());
                contentFont.drawString("Tip 1 - Type '.bind clickgui rshift' to Set the Binding of Click Gui to Right Shift.", 280.0f, (float)this.height / 3.0f + 50.0f + tipsAppendY, new Color(249, 205, 173, this.contentAlpha).getRGB());
                contentFont.drawString("Tip 2 - In Click Gui, Left Click on a Module to Toggle, Right Click to Show Settings.", 280.0f, (float)this.height / 3.0f + 75.0f + tipsAppendY, new Color(249, 205, 173, this.contentAlpha).getRGB());
                contentFont.drawString("Tip 3 - Try 'Edit Locations' Button in the Left Bottom Position in Click Gui.", 280.0f, (float)this.height / 3.0f + 100.0f + tipsAppendY, new Color(249, 205, 173, this.contentAlpha).getRGB());
                contentFont.drawString("Tip 4 - Type '.help' to Show All Client Commands and Useage.", 280.0f, (float)this.height / 3.0f + 125.0f + tipsAppendY, new Color(249, 205, 173, this.contentAlpha).getRGB());
                if (this.timer2.hasReached(6000.0)) {
                    if (this.enjoyAlpha > 0 && this.enjoyAlpha < 210) {
                        contentFont.drawCenteredString("---==== Enjoy :) ====---", (float)this.width / 2.0f, (float)this.height / 3.0f + 175.0f, new Color(78, 128, 190, this.enjoyAlpha).getRGB());
                    }
                    if (this.enjoyAlpha >= 210) {
                        contentFont.drawCenteredString("---==== Enjoy :) ====---", (float)this.width / 2.0f, (float)this.height / 3.0f + 175.0f, new Color(78, 128, 190, this.enjoyAlpha).getRGB());
                    }
                    if (this.enjoyAlpha < 210) {
                        this.enjoyAlpha += 7;
                    }
                }
                if (this.timer.hasReached(11000.0)) {
                    if (this.continueAlpha > 0 && this.continueAlpha < 210) {
                        continueFont.drawCenteredString("Click To Continue.", (float)this.width / 2.0f, this.height - 100, new Color(255, 255, 255, this.continueAlpha).getRGB());
                    }
                    if (this.continueAlpha >= 210) {
                        continueFont.drawCenteredString("Click To Continue.", (float)this.width / 2.0f, this.height - 100, FadeUtil.fade(new Color(255, 255, 255, this.continueAlpha)).getRGB());
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
            font.drawCenteredString("Welcome back to MelodySky", (float)this.width / 2.0f, (float)this.height / 2.0f - 3.0f, new Color(255, 255, 255, this.shabiAlpha).getRGB());
            if (this.shabiAlpha <= 10 && this.shouldMainMenu) {
                this.shabiAlpha = 6;
                if (this.timer3.hasReached(100.0)) {
                    Client.firstMenu = false;
                    this.mc.displayGuiScreen(new MainMenu(140));
                    this.timer3.reset();
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (Client.firstLaunch) {
            if (this.continueAlpha >= 210) {
                this.mc.displayGuiScreen(this);
                Client.firstLaunch = false;
            }
        } else {
            this.shouldMainMenu = true;
        }
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
}

