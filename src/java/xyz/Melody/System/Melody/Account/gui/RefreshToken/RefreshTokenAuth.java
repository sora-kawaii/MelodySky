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
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.System.Melody.Account.microsoft.MicrosoftLogin;
import xyz.Melody.Utils.shader.GaussianBlur;
import xyz.Melody.injection.mixins.client.MCA;

public final class RefreshTokenAuth
extends GuiScreen {
    private GuiScreen previousScreen;
    public String status = "Refresh Token:";
    private GuiTextField xbltField;
    private ScaledResolution sr;
    private GaussianBlur gb = new GaussianBlur();

    public RefreshTokenAuth(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.sr = new ScaledResolution(this.mc);
        this.xbltField = new GuiTextField(1, this.mc.fontRendererObj, this.sr.getScaledWidth() / 2 - 100, this.sr.getScaledHeight() / 2 - 30, 200, 20);
        this.xbltField.setMaxStringLength(Short.MAX_VALUE);
        this.xbltField.setFocused(true);
        this.buttonList.add(new GuiButton(997, this.sr.getScaledWidth() / 2 - 100, this.sr.getScaledHeight() / 2, 200, 20, "Login"));
        this.buttonList.add(new GuiButton(999, this.sr.getScaledWidth() / 2 - 100, this.sr.getScaledHeight() / 2 + 30, 200, 20, "Back"));
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.fontRendererObj.drawString(this.status, this.sr.getScaledWidth() / 2 - this.mc.fontRendererObj.getStringWidth(this.status) / 2, this.sr.getScaledHeight() / 2 - 60, Color.WHITE.getRGB());
        this.xbltField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 997) {
            new Thread(this::lambda$actionPerformed$0, "RefreshToken Authenticator").start();
        }
        if (button.id == 999) {
            try {
                this.mc.displayGuiScreen(this.previousScreen);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.xbltField.textboxKeyTyped(typedChar, keyCode);
        if (1 == keyCode) {
            this.mc.displayGuiScreen(this.previousScreen);
        } else {
            super.keyTyped(typedChar, keyCode);
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
        this.gb.renderBlur(140.0f);
    }

    private void lambda$actionPerformed$0() {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Loggingin...";
        try {
            MicrosoftLogin microsoftLogin = new MicrosoftLogin(this.xbltField.getText(), this);
            if (microsoftLogin.logged) {
                ((MCA)((Object)this.mc)).setSession(new Session(microsoftLogin.userName, microsoftLogin.uuid, microsoftLogin.accessToken, "mojang"));
                this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + this.mc.getSession().getUsername();
                NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
            this.status = (Object)((Object)EnumChatFormatting.RED) + "Failed. " + ioException.getClass().getName() + ": " + ioException.getMessage();
        }
    }
}

