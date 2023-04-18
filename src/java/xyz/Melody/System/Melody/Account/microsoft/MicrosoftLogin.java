/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account.microsoft;

import chrriis.dj.nativeswing.NSOption;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.Client;
import xyz.Melody.System.Melody.Account.GuiAltManager;
import xyz.Melody.System.Melody.Account.gui.RefreshToken.AddRefreshAuth;
import xyz.Melody.System.Melody.Account.gui.RefreshToken.RefreshTokenAuth;
import xyz.Melody.Utils.Browser;
import xyz.Melody.Utils.TimerUtil;

public final class MicrosoftLogin
implements Closeable {
    private String CLIENT_ID = "a1cabc43-47f4-464d-b869-19857d22d739";
    private String REDIRECT_URI = "http://localhost:9810/login";
    private String SCOPE = "XboxLive.signin%20offline_access";
    private String URL = "https://login.live.com/oauth20_authorize.srf?client_id=<client_id>&redirect_uri=<redirect_uri>&response_type=code&display=touch&scope=<scope>".replace("<client_id>", this.CLIENT_ID).replace("<redirect_uri>", this.REDIRECT_URI).replace("<scope>", this.SCOPE);
    public volatile String uuid = null;
    public volatile String userName = null;
    public volatile String accessToken = null;
    public volatile String refreshToken = null;
    public volatile String msToken = null;
    public volatile String xblToken = null;
    public volatile String xsts1 = null;
    public volatile String xsts2 = null;
    public volatile boolean initDone = false;
    public volatile boolean logged = false;
    public volatile int stage = 0;
    public volatile String status = "";
    public HttpServer httpServer;
    private final MicrosoftHttpHandler handler;
    private TimerUtil timer = new TimerUtil();
    public Browser bruhSir;
    public boolean useSystemBrowser;

    public MicrosoftLogin(boolean bl) throws Exception {
        this.stage = 0;
        this.useSystemBrowser = bl;
        if (!this.useSystemBrowser) {
            this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Initializing...";
            this.bruhSir = new Browser("https://account.xbox.com/account/signout", "Initializing...", true, false, true, new NSOption[0]);
            Thread.sleep(2000L);
            this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Initialized.";
        }
        this.initDone = true;
        Thread.sleep(1500L);
        this.timer.reset();
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Waitting...";
        this.handler = new MicrosoftHttpHandler(this);
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", 9810), 0);
        this.httpServer.createContext("/login", this.handler);
        this.httpServer.start();
        this.show();
    }

    private void show() throws Exception {
        if (this.bruhSir != null) {
            this.bruhSir.close();
        }
        if (!this.useSystemBrowser) {
            this.bruhSir = new Browser(this.URL, "Microsoft Login", true, true, true, new NSOption[0]);
        } else {
            Desktop.getDesktop().browse(new URI(this.URL));
        }
    }

    public MicrosoftLogin(String string, GuiScreen guiScreen) throws IOException {
        String string2;
        String[] stringArray;
        String string3;
        String string4;
        this.refreshToken = string;
        this.httpServer = null;
        this.handler = null;
        this.stage = 0;
        if (guiScreen instanceof GuiAltManager && guiScreen != null) {
            ((GuiAltManager)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Microsoft Token From Refresh Token...";
            string4 = this.getMicrosoftTokenFromRefreshToken(string);
            ((GuiAltManager)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XboxLive Token...";
            string3 = this.getXBoxLiveToken(string4);
            ((GuiAltManager)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS & UHS...";
            stringArray = this.getXSTSTokenAndUserHash(string3);
            ((GuiAltManager)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token...";
            string2 = this.getAccessToken(stringArray[0], stringArray[1]);
            ((GuiAltManager)guiScreen).status = "Logging in from Access Token...";
        } else if (guiScreen instanceof AddRefreshAuth && guiScreen != null) {
            ((AddRefreshAuth)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Microsoft Token From Refresh Token...";
            string4 = this.getMicrosoftTokenFromRefreshToken(string);
            ((AddRefreshAuth)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XboxLive Token...";
            string3 = this.getXBoxLiveToken(string4);
            ((AddRefreshAuth)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS & UHS...";
            stringArray = this.getXSTSTokenAndUserHash(string3);
            ((AddRefreshAuth)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token...";
            string2 = this.getAccessToken(stringArray[0], stringArray[1]);
            ((AddRefreshAuth)guiScreen).status = "Logging in from Access Token...";
        } else if (guiScreen instanceof RefreshTokenAuth && guiScreen != null) {
            ((RefreshTokenAuth)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Microsoft Token From Refresh Token...";
            string4 = this.getMicrosoftTokenFromRefreshToken(string);
            ((RefreshTokenAuth)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XboxLive Token...";
            string3 = this.getXBoxLiveToken(string4);
            ((RefreshTokenAuth)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS & UHS...";
            stringArray = this.getXSTSTokenAndUserHash(string3);
            ((RefreshTokenAuth)guiScreen).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token...";
            string2 = this.getAccessToken(stringArray[0], stringArray[1]);
            ((RefreshTokenAuth)guiScreen).status = "Logging in from Access Token...";
        } else {
            string4 = this.getMicrosoftTokenFromRefreshToken(string);
            string3 = this.getXBoxLiveToken(string4);
            stringArray = this.getXSTSTokenAndUserHash(string3);
            string2 = this.getAccessToken(stringArray[0], stringArray[1]);
        }
        URL uRL = new URL("https://api.minecraftservices.com/minecraft/profile");
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + string2);
        String string5 = this.read(httpURLConnection.getInputStream());
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(string5);
        String string6 = jsonObject.get("id").getAsString();
        String string7 = jsonObject.get("name").getAsString();
        this.refreshToken = string;
        this.msToken = string4;
        this.xblToken = string3;
        this.xsts1 = stringArray[0];
        this.xsts2 = stringArray[1];
        this.uuid = string6;
        this.userName = string7;
        this.accessToken = string2;
        this.logged = true;
    }

    @Override
    public void close() {
        if (this.httpServer != null) {
            this.httpServer.stop(0);
        }
    }

    private String getAccessToken(String string, String string2) throws IOException {
        this.stage = 4;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token.";
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

    public String getMicrosoftTokenFromRefreshToken(String string) throws IOException {
        this.stage = 1;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Microsoft Token From Refresh Token.";
        Client.instance.logger.info("Getting microsoft token from refresh token");
        URL uRL = new URL("https://login.live.com/oauth20_token.srf");
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        String string2 = "client_id=" + this.CLIENT_ID + "&refresh_token=" + string + "&grant_type=refresh_token&redirect_uri=" + this.REDIRECT_URI;
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        this.write(new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream())), string2);
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(this.read(httpURLConnection.getInputStream()));
        return jsonObject.get("access_token").getAsString();
    }

    public String[] getMicrosoftTokenAndRefreshToken(String string) throws IOException {
        this.stage = 1;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Microsoft Token.";
        Client.instance.logger.info("Getting microsoft token");
        URL uRL = new URL("https://login.live.com/oauth20_token.srf");
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        String string2 = "client_id=" + this.CLIENT_ID + "&code=" + string + "&grant_type=authorization_code&redirect_uri=" + this.REDIRECT_URI + "&scope=" + this.SCOPE;
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        this.write(new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream())), string2);
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(this.read(httpURLConnection.getInputStream()));
        return new String[]{jsonObject.get("access_token").getAsString(), jsonObject.get("refresh_token").getAsString()};
    }

    public String getXBoxLiveToken(String string) throws IOException {
        this.stage = 2;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XboxLive Token.";
        Client.instance.logger.info("Getting xbox live token");
        URL uRL = new URL("https://user.auth.xboxlive.com/user/authenticate");
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("AuthMethod", "RPS");
        jsonObject2.addProperty("SiteName", "user.auth.xboxlive.com");
        jsonObject2.addProperty("RpsTicket", "d=" + string);
        jsonObject.add("Properties", jsonObject2);
        jsonObject.addProperty("RelyingParty", "http://auth.xboxlive.com");
        jsonObject.addProperty("TokenType", "JWT");
        String string2 = new Gson().toJson(jsonObject);
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        this.write(new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream())), string2);
        JsonObject jsonObject3 = (JsonObject)new JsonParser().parse(this.read(httpURLConnection.getInputStream()));
        return jsonObject3.get("Token").getAsString();
    }

    public String[] getXSTSTokenAndUserHash(String string) throws IOException {
        this.stage = 3;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS Token and User Info.";
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

    private class MicrosoftHttpHandler
    implements HttpHandler {
        private boolean got = false;
        private MicrosoftLogin msLog;

        public MicrosoftHttpHandler(MicrosoftLogin microsoftLogin2) {
            this.msLog = microsoftLogin2;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String string;
            if (!this.got && (string = httpExchange.getRequestURI().getQuery()).contains("code")) {
                this.got = true;
                String string2 = string.split("code=")[1];
                String[] stringArray = MicrosoftLogin.this.getMicrosoftTokenAndRefreshToken(string2);
                String string3 = MicrosoftLogin.this.getXBoxLiveToken(stringArray[0]);
                String[] stringArray2 = MicrosoftLogin.this.getXSTSTokenAndUserHash(string3);
                String string4 = MicrosoftLogin.this.getAccessToken(stringArray2[0], stringArray2[1]);
                MicrosoftLogin.this.stage = 5;
                URL uRL = new URL("https://api.minecraftservices.com/minecraft/profile");
                HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Authorization", "Bearer " + string4);
                String string5 = MicrosoftLogin.this.read(httpURLConnection.getInputStream());
                JsonObject jsonObject = (JsonObject)new JsonParser().parse(string5);
                String string6 = jsonObject.get("id").getAsString();
                String string7 = jsonObject.get("name").getAsString();
                this.msLog.msToken = stringArray[0];
                this.msLog.xblToken = string3;
                this.msLog.xsts1 = stringArray2[0];
                this.msLog.xsts2 = stringArray2[1];
                this.msLog.uuid = string6;
                this.msLog.userName = string7;
                this.msLog.accessToken = string4;
                this.msLog.refreshToken = stringArray[1];
                this.msLog.logged = true;
                MicrosoftLogin.this.bruhSir.close();
            }
        }
    }
}

