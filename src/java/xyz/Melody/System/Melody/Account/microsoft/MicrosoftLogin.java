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
    public String uuid = null;
    public String userName = null;
    public String accessToken = null;
    public String refreshToken = null;
    public String msToken = null;
    public String xblToken = null;
    public String xsts1 = null;
    public String xsts2 = null;
    public boolean initDone = false;
    public boolean logged = false;
    public int stage = 0;
    public String status = "";
    public HttpServer httpServer;
    private final MicrosoftHttpHandler handler;
    private TimerUtil timer = new TimerUtil();
    public Browser bruhSir;
    public boolean useSystemBrowser;

    public MicrosoftLogin(boolean sysb) throws Exception {
        this.stage = 0;
        this.useSystemBrowser = sysb;
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

    public MicrosoftLogin(String refreshToken, GuiScreen am) throws IOException {
        String accessToken;
        String[] xstsTokenAndHash;
        String xBoxLiveToken;
        String microsoftTokenAndRefreshToken;
        this.refreshToken = refreshToken;
        this.httpServer = null;
        this.handler = null;
        this.stage = 0;
        if (am instanceof GuiAltManager && am != null) {
            ((GuiAltManager)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Microsoft Token From Refresh Token...";
            microsoftTokenAndRefreshToken = this.getMicrosoftTokenFromRefreshToken(refreshToken);
            ((GuiAltManager)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XboxLive Token...";
            xBoxLiveToken = this.getXBoxLiveToken(microsoftTokenAndRefreshToken);
            ((GuiAltManager)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS & UHS...";
            xstsTokenAndHash = this.getXSTSTokenAndUserHash(xBoxLiveToken);
            ((GuiAltManager)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token...";
            accessToken = this.getAccessToken(xstsTokenAndHash[0], xstsTokenAndHash[1]);
            ((GuiAltManager)am).status = "Logging in from Access Token...";
        } else if (am instanceof AddRefreshAuth && am != null) {
            ((AddRefreshAuth)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Microsoft Token From Refresh Token...";
            microsoftTokenAndRefreshToken = this.getMicrosoftTokenFromRefreshToken(refreshToken);
            ((AddRefreshAuth)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XboxLive Token...";
            xBoxLiveToken = this.getXBoxLiveToken(microsoftTokenAndRefreshToken);
            ((AddRefreshAuth)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS & UHS...";
            xstsTokenAndHash = this.getXSTSTokenAndUserHash(xBoxLiveToken);
            ((AddRefreshAuth)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token...";
            accessToken = this.getAccessToken(xstsTokenAndHash[0], xstsTokenAndHash[1]);
            ((AddRefreshAuth)am).status = "Logging in from Access Token...";
        } else if (am instanceof RefreshTokenAuth && am != null) {
            ((RefreshTokenAuth)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Microsoft Token From Refresh Token...";
            microsoftTokenAndRefreshToken = this.getMicrosoftTokenFromRefreshToken(refreshToken);
            ((RefreshTokenAuth)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XboxLive Token...";
            xBoxLiveToken = this.getXBoxLiveToken(microsoftTokenAndRefreshToken);
            ((RefreshTokenAuth)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS & UHS...";
            xstsTokenAndHash = this.getXSTSTokenAndUserHash(xBoxLiveToken);
            ((RefreshTokenAuth)am).status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token...";
            accessToken = this.getAccessToken(xstsTokenAndHash[0], xstsTokenAndHash[1]);
            ((RefreshTokenAuth)am).status = "Logging in from Access Token...";
        } else {
            microsoftTokenAndRefreshToken = this.getMicrosoftTokenFromRefreshToken(refreshToken);
            xBoxLiveToken = this.getXBoxLiveToken(microsoftTokenAndRefreshToken);
            xstsTokenAndHash = this.getXSTSTokenAndUserHash(xBoxLiveToken);
            accessToken = this.getAccessToken(xstsTokenAndHash[0], xstsTokenAndHash[1]);
        }
        URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        String read = this.read(connection.getInputStream());
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(read);
        String uuid = jsonObject.get("id").getAsString();
        String userName = jsonObject.get("name").getAsString();
        this.refreshToken = refreshToken;
        this.msToken = microsoftTokenAndRefreshToken;
        this.xblToken = xBoxLiveToken;
        this.xsts1 = xstsTokenAndHash[0];
        this.xsts2 = xstsTokenAndHash[1];
        this.uuid = uuid;
        this.userName = userName;
        this.accessToken = accessToken;
        this.logged = true;
    }

    @Override
    public void close() {
        if (this.httpServer != null) {
            this.httpServer.stop(0);
        }
    }

    private String getAccessToken(String xstsToken, String uhs) throws IOException {
        this.stage = 4;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Access Token.";
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

    public String getMicrosoftTokenFromRefreshToken(String refreshToken) throws IOException {
        this.stage = 1;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Microsoft Token From Refresh Token.";
        Client.instance.logger.info("Getting microsoft token from refresh token");
        URL url = new URL("https://login.live.com/oauth20_token.srf");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        String param = "client_id=" + this.CLIENT_ID + "&refresh_token=" + refreshToken + "&grant_type=refresh_token&redirect_uri=" + this.REDIRECT_URI;
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        this.write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), param);
        JsonObject response_obj = (JsonObject)new JsonParser().parse(this.read(connection.getInputStream()));
        return response_obj.get("access_token").getAsString();
    }

    public String[] getMicrosoftTokenAndRefreshToken(String code) throws IOException {
        this.stage = 1;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting Microsoft Token.";
        Client.instance.logger.info("Getting microsoft token");
        URL url = new URL("https://login.live.com/oauth20_token.srf");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        String param = "client_id=" + this.CLIENT_ID + "&code=" + code + "&grant_type=authorization_code&redirect_uri=" + this.REDIRECT_URI + "&scope=" + this.SCOPE;
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        this.write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), param);
        JsonObject response_obj = (JsonObject)new JsonParser().parse(this.read(connection.getInputStream()));
        return new String[]{response_obj.get("access_token").getAsString(), response_obj.get("refresh_token").getAsString()};
    }

    public String getXBoxLiveToken(String microsoftToken) throws IOException {
        this.stage = 2;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XboxLive Token.";
        Client.instance.logger.info("Getting xbox live token");
        URL connectUrl = new URL("https://user.auth.xboxlive.com/user/authenticate");
        JsonObject xbl_param = new JsonObject();
        JsonObject xbl_properties = new JsonObject();
        xbl_properties.addProperty("AuthMethod", "RPS");
        xbl_properties.addProperty("SiteName", "user.auth.xboxlive.com");
        xbl_properties.addProperty("RpsTicket", "d=" + microsoftToken);
        xbl_param.add("Properties", xbl_properties);
        xbl_param.addProperty("RelyingParty", "http://auth.xboxlive.com");
        xbl_param.addProperty("TokenType", "JWT");
        String param = new Gson().toJson(xbl_param);
        HttpURLConnection connection = (HttpURLConnection)connectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        this.write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), param);
        JsonObject response_obj = (JsonObject)new JsonParser().parse(this.read(connection.getInputStream()));
        return response_obj.get("Token").getAsString();
    }

    public String[] getXSTSTokenAndUserHash(String xboxLiveToken) throws IOException {
        this.stage = 3;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Getting XSTS Token and User Info.";
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

    private class MicrosoftHttpHandler
    implements HttpHandler {
        private boolean got = false;
        private MicrosoftLogin msLog;

        public MicrosoftHttpHandler(MicrosoftLogin msLog) {
            this.msLog = msLog;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String query;
            if (!this.got && (query = httpExchange.getRequestURI().getQuery()).contains("code")) {
                this.got = true;
                String code = query.split("code=")[1];
                String[] microsoftTokenAndRefreshToken = MicrosoftLogin.this.getMicrosoftTokenAndRefreshToken(code);
                String xBoxLiveToken = MicrosoftLogin.this.getXBoxLiveToken(microsoftTokenAndRefreshToken[0]);
                String[] xstsTokenAndHash = MicrosoftLogin.this.getXSTSTokenAndUserHash(xBoxLiveToken);
                String accessToken = MicrosoftLogin.this.getAccessToken(xstsTokenAndHash[0], xstsTokenAndHash[1]);
                MicrosoftLogin.this.stage = 5;
                URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                String read = MicrosoftLogin.this.read(connection.getInputStream());
                JsonObject jsonObject = (JsonObject)new JsonParser().parse(read);
                String uuid = jsonObject.get("id").getAsString();
                String userName = jsonObject.get("name").getAsString();
                this.msLog.msToken = microsoftTokenAndRefreshToken[0];
                this.msLog.xblToken = xBoxLiveToken;
                this.msLog.xsts1 = xstsTokenAndHash[0];
                this.msLog.xsts2 = xstsTokenAndHash[1];
                this.msLog.uuid = uuid;
                this.msLog.userName = userName;
                this.msLog.accessToken = accessToken;
                this.msLog.refreshToken = microsoftTokenAndRefreshToken[1];
                this.msLog.logged = true;
                MicrosoftLogin.this.bruhSir.close();
            }
        }
    }
}

