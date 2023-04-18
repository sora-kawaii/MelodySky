/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account.gui.XBoxLive;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;
import xyz.Melody.Client;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.shader.GaussianBlur;
import xyz.Melody.injection.mixins.client.MCA;

public final class XBLTokenAuth
extends GuiScreen {
    private GuiScreen previousScreen;
    private String status = "XBox Live Token:";
    private GuiTextField xbltField;
    private ScaledResolution sr;
    private GaussianBlur gb = new GaussianBlur();

    public XBLTokenAuth(GuiScreen guiScreen) {
        this.previousScreen = guiScreen;
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
                try {
                    String string = this.xbltField.getText();
                    String[] stringArray = this.getXSTSTokenAndUserHash(string);
                    String string2 = this.getAccessToken(stringArray[0], stringArray[1]);
                    this.status = "Logging in from AccessToken...";
                    HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("https://api.minecraftservices.com/minecraft/profile/").openConnection();
                    httpURLConnection.setRequestProperty("Content-type", "application/json");
                    httpURLConnection.setRequestProperty("Authorization", "Bearer " + string2);
                    httpURLConnection.setDoOutput(true);
                    JsonObject jsonObject = new JsonParser().parse(IOUtils.toString(httpURLConnection.getInputStream())).getAsJsonObject();
                    String string3 = jsonObject.get("name").getAsString();
                    String string4 = jsonObject.get("id").getAsString();
                    String string5 = string2;
                    ((MCA)((Object)this.mc)).setSession(new Session(string3, string4, string5, "mojang"));
                    this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + string3;
                    NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
                }
                catch (Exception exception) {
                    this.status = (Object)((Object)EnumChatFormatting.RED) + "Failed: " + exception.getMessage();
                    exception.printStackTrace();
                }
            }, "XBLT Authenticator").start();
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

    private String getAccessToken(String string, String string2) throws IOException {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token...";
        Client.instance.logger.info("Getting access token");
        URL uRL = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("identityToken", "XBL3.0 x=" + string2 + ";" + string);
        this.write(new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream())), new Gson().toJson(jsonObject));
        JsonObject jsonObject2 = (JsonObject)new JsonParser().parse(this.read(httpURLConnection.getInputStream()));
        return jsonObject2.get("access_token").getAsString();
    }

    public String[] getXSTSTokenAndUserHash(String string) throws IOException {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS Token and User Info...";
        Client.instance.logger.info("Getting xsts token and user hash");
        URL uRL = new URL("https://xsts.auth.xboxlive.com/xsts/authorize");
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(string);
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("SandboxId", "RETAIL");
        jsonObject2.add("UserTokens", new JsonParser().parse(new Gson().toJson(arrayList)));
        jsonObject.add("Properties", jsonObject2);
        jsonObject.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        jsonObject.addProperty("TokenType", "JWT");
        String string2 = new Gson().toJson(jsonObject);
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        this.write(new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream())), string2);
        JsonObject jsonObject3 = (JsonObject)new JsonParser().parse(this.read(httpURLConnection.getInputStream()));
        String string3 = jsonObject3.get("Token").getAsString();
        String string4 = jsonObject3.getAsJsonObject("DisplayClaims").getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString();
        return new String[]{string3, string4};
    }

    private void write(BufferedWriter bufferedWriter, String string) throws IOException {
        bufferedWriter.write(string);
        bufferedWriter.close();
    }

    private String read(InputStream inputStream) throws IOException {
        String string;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        while ((string = bufferedReader.readLine()) != null) {
            stringBuilder.append(string);
        }
        inputStream.close();
        bufferedReader.close();
        return stringBuilder.toString();
    }
}

