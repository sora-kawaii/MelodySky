/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account.gui.RefreshToken;

import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import xyz.Melody.Client;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.System.Melody.Account.altimpl.MicrosoftAlt;
import xyz.Melody.System.Melody.Account.microsoft.MicrosoftLogin;
import xyz.Melody.Utils.shader.GaussianBlur;

public final class AddRefreshAuth
extends GuiScreen {
    private GuiScreen previousScreen;
    public String status = "Refresh Token:";
    private GuiTextField xbltField;
    private ScaledResolution sr;
    private GaussianBlur gb = new GaussianBlur();

    public AddRefreshAuth(GuiScreen guiScreen) {
        this.previousScreen = guiScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.sr = new ScaledResolution(this.mc);
        this.xbltField = new GuiTextField(1, this.mc.fontRendererObj, this.sr.getScaledWidth() / 2 - 100, this.sr.getScaledHeight() / 2 - 30, 200, 20);
        this.xbltField.setMaxStringLength(Short.MAX_VALUE);
        this.xbltField.setFocused(true);
        this.buttonList.add(new GuiButton(997, this.sr.getScaledWidth() / 2 - 100, this.sr.getScaledHeight() / 2, 200, 20, "Add"));
        this.buttonList.add(new GuiButton(999, this.sr.getScaledWidth() / 2 - 100, this.sr.getScaledHeight() / 2 + 30, 200, 20, "Back"));
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.mc.fontRendererObj.drawString(this.status, this.sr.getScaledWidth() / 2 - this.mc.fontRendererObj.getStringWidth(this.status) / 2, this.sr.getScaledHeight() / 2 - 60, Color.WHITE.getRGB());
        this.xbltField.drawTextBox();
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.id == 997) {
            new Thread(() -> {
                this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Loggingin...";
                try {
                    MicrosoftLogin microsoftLogin = new MicrosoftLogin(this.xbltField.getText(), this);
                    if (microsoftLogin.logged) {
                        Client.instance.getAccountManager().addAlt(new MicrosoftAlt(microsoftLogin.userName, microsoftLogin.refreshToken, microsoftLogin.msToken, microsoftLogin.xblToken, microsoftLogin.xsts1, microsoftLogin.xsts2, microsoftLogin.accessToken, microsoftLogin.uuid));
                        this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + this.mc.getSession().getUsername();
                    }
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                    this.status = (Object)((Object)EnumChatFormatting.RED) + "Failed. " + iOException.getClass().getName() + ": " + iOException.getMessage();
                }
            }, "RefreshToken Authenticator").start();
        }
        if (guiButton.id == 999) {
            try {
                this.mc.displayGuiScreen(this.previousScreen);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        super.actionPerformed(guiButton);
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        this.xbltField.textboxKeyTyped(c, n);
        if (1 == n) {
            this.mc.displayGuiScreen(this.previousScreen);
        } else {
            super.keyTyped(c, n);
        }
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
        this.gb.renderBlur(140.0f);
    }
}

