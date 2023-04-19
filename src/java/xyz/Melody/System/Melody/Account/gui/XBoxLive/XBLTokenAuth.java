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

    public XBLTokenAuth(GuiScreen previousScreen) {
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
            new Thread(this::lambda$actionPerformed$0, "XBLT Authenticator").start();
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

    private String getAccessToken(String xstsToken, String uhs) throws IOException {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token...";
        Client.instance.logger.info("Getting access token");
        URL url = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        JsonObject input = new JsonObject();
        input.addProperty("identityToken", "XBL3.0 x=" + uhs + ";" + xstsToken);
        this.write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), new Gson().toJson(input));
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(this.read(connection.getInputStream()));
        return jsonObject.get("access_token").getAsString();
    }

    public String[] getXSTSTokenAndUserHash(String xboxLiveToken) throws IOException {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS Token and User Info...";
        Client.instance.logger.info("Getting xsts token and user hash");
        URL ConnectUrl = new URL("https://xsts.auth.xboxlive.com/xsts/authorize");
        ArrayList<String> tokens = new ArrayList<String>();
        tokens.add(xboxLiveToken);
        JsonObject xbl_param = new JsonObject();
        JsonObject xbl_properties = new JsonObject();
        xbl_properties.addProperty("SandboxId", "RETAIL");
        xbl_properties.add("UserTokens", new JsonParser().parse(new Gson().toJson(tokens)));
        xbl_param.add("Properties", xbl_properties);
        xbl_param.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        xbl_param.addProperty("TokenType", "JWT");
        String param = new Gson().toJson(xbl_param);
        HttpURLConnection connection = (HttpURLConnection)ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        this.write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), param);
        JsonObject response_obj = (JsonObject)new JsonParser().parse(this.read(connection.getInputStream()));
        String token = response_obj.get("Token").getAsString();
        String uhs = response_obj.getAsJsonObject("DisplayClaims").getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString();
        return new String[]{token, uhs};
    }

    private void write(BufferedWriter writer, String s) throws IOException {
        writer.write(s);
        writer.close();
    }

    private String read(InputStream stream) throws IOException {
        String s;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        while ((s = reader.readLine()) != null) {
            stringBuilder.append(s);
        }
        stream.close();
        reader.close();
        return stringBuilder.toString();
    }

    private void lambda$actionPerformed$0() {
        try {
            String XBLToken = this.xbltField.getText();
            String[] xstsTokenAndHash = this.getXSTSTokenAndUserHash(XBLToken);
            String accessToken = this.getAccessToken(xstsTokenAndHash[0], xstsTokenAndHash[1]);
            this.status = "Logging in from AccessToken...";
            HttpURLConnection c = (HttpURLConnection)new URL("https://api.minecraftservices.com/minecraft/profile/").openConnection();
            c.setRequestProperty("Content-type", "application/json");
            c.setRequestProperty("Authorization", "Bearer " + accessToken);
            c.setDoOutput(true);
            JsonObject json = new JsonParser().parse(IOUtils.toString(c.getInputStream())).getAsJsonObject();
            String username = json.get("name").getAsString();
            String uuid = json.get("id").getAsString();
            String token = accessToken;
            ((MCA)((Object)this.mc)).setSession(new Session(username, uuid, token, "mojang"));
            this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + username;
            NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.mc.getSession().getUsername(), NotificationType.SUCCESS, 3000);
        }
        catch (Exception e) {
            this.status = (Object)((Object)EnumChatFormatting.RED) + "Failed: " + e.getMessage();
            e.printStackTrace();
        }
    }
}

