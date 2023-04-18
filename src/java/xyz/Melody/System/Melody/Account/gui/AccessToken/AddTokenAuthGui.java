/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account.gui.AccessToken;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;
import xyz.Melody.Client;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.System.Melody.Account.altimpl.OriginalAlt;
import xyz.Melody.Utils.shader.GaussianBlur;

public final class AddTokenAuthGui
extends GuiScreen {
    private GuiScreen previousScreen;
    private String status = "Session:";
    private GuiTextField sessionField;
    private ScaledResolution sr;
    private GaussianBlur gb = new GaussianBlur();

    public AddTokenAuthGui(GuiScreen guiScreen) {
        this.previousScreen = guiScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.sr = new ScaledResolution(this.mc);
        this.sessionField = new GuiTextField(1, this.mc.fontRendererObj, this.sr.getScaledWidth() / 2 - 100, this.sr.getScaledHeight() / 2 - 30, 200, 20);
        this.sessionField.setMaxStringLength(Short.MAX_VALUE);
        this.sessionField.setFocused(true);
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
        this.mc.fontRendererObj.drawString("Input Format: 1.name:uuid:token 2.token", 20, 13, Color.WHITE.getRGB());
        this.mc.fontRendererObj.drawString("1.name:uuid:token ", 35, 24, Color.WHITE.getRGB());
        this.mc.fontRendererObj.drawString("2.token", 35, 35, Color.WHITE.getRGB());
        this.mc.fontRendererObj.drawString(this.status, this.sr.getScaledWidth() / 2 - this.mc.fontRendererObj.getStringWidth(this.status) / 2, this.sr.getScaledHeight() / 2 - 60, Color.WHITE.getRGB());
        this.sessionField.drawTextBox();
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.id == 997) {
            try {
                String string;
                String string2;
                String string3;
                String string4 = this.sessionField.getText();
                if (string4.contains(":")) {
                    string3 = string4.split(":")[0];
                    string2 = string4.split(":")[1];
                    string = string4.split(":")[2];
                } else {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("https://api.minecraftservices.com/minecraft/profile/").openConnection();
                    httpURLConnection.setRequestProperty("Content-type", "application/json");
                    httpURLConnection.setRequestProperty("Authorization", "Bearer " + this.sessionField.getText());
                    httpURLConnection.setDoOutput(true);
                    JsonObject jsonObject = new JsonParser().parse(IOUtils.toString(httpURLConnection.getInputStream())).getAsJsonObject();
                    string3 = jsonObject.get("name").getAsString();
                    string2 = jsonObject.get("id").getAsString();
                    string = string4;
                }
                Client.instance.getAccountManager().addAlt(new OriginalAlt(string3, string, string2, "mojang"));
                this.mc.displayGuiScreen(this.previousScreen);
            }
            catch (Exception exception) {
                this.status = "\u00a7cError: Couldn't set session (check mc logs)";
                exception.printStackTrace();
            }
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
        this.sessionField.textboxKeyTyped(c, n);
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

