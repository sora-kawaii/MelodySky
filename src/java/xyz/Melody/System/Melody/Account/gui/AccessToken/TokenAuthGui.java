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
import net.minecraft.util.Session;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.shader.GaussianBlur;
import xyz.Melody.injection.mixins.client.MCA;

public final class TokenAuthGui
extends GuiScreen {
    private GuiScreen previousScreen;
    private String status = "Session:";
    private GuiTextField sessionField;
    private ScaledResolution sr;
    private GaussianBlur gb = new GaussianBlur();

    public TokenAuthGui(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.sr = new ScaledResolution(this.mc);
        this.sessionField = new GuiTextField(1, this.mc.fontRendererObj, this.sr.getScaledWidth() / 2 - 100, this.sr.getScaledHeight() / 2 - 30, 200, 20);
        this.sessionField.setMaxStringLength(Short.MAX_VALUE);
        this.sessionField.setFocused(true);
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
        this.mc.fontRendererObj.drawString("Input Format: 1.name:uuid:token 2.token", 20, 13, Color.WHITE.getRGB());
        this.mc.fontRendererObj.drawString("1.name:uuid:token ", 35, 24, Color.WHITE.getRGB());
        this.mc.fontRendererObj.drawString("2.token", 35, 35, Color.WHITE.getRGB());
        this.mc.fontRendererObj.drawString(this.status, this.sr.getScaledWidth() / 2 - this.mc.fontRendererObj.getStringWidth(this.status) / 2, this.sr.getScaledHeight() / 2 - 60, Color.WHITE.getRGB());
        this.sessionField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 997) {
            try {
                String token;
                String uuid;
                String username;
                String session = this.sessionField.getText();
                if (session.contains(":")) {
                    username = session.split(":")[0];
                    uuid = session.split(":")[1];
                    token = session.split(":")[2];
                } else {
                    HttpURLConnection c = (HttpURLConnection)new URL("https://api.minecraftservices.com/minecraft/profile/").openConnection();
                    c.setRequestProperty("Content-type", "application/json");
                    c.setRequestProperty("Authorization", "Bearer " + this.sessionField.getText());
                    c.setDoOutput(true);
                    JsonObject json = new JsonParser().parse(IOUtils.toString(c.getInputStream())).getAsJsonObject();
                    username = json.get("name").getAsString();
                    uuid = json.get("id").getAsString();
                    token = session;
                    NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
                }
                ((MCA)((Object)this.mc)).setSession(new Session(username, uuid, token, "mojang"));
                this.mc.displayGuiScreen(this.previousScreen);
            }
            catch (Exception e) {
                this.status = "\u00a7cError: Couldn't set session (check mc logs)";
                e.printStackTrace();
            }
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
        this.sessionField.textboxKeyTyped(typedChar, keyCode);
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
}

