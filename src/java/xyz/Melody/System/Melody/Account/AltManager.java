/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.util.StringUtils;
import xyz.Melody.System.Managers.Client.FileManager;
import xyz.Melody.System.Melody.Account.Alt;
import xyz.Melody.System.Melody.Account.I;
import xyz.Melody.System.Melody.Account.altimpl.MicrosoftAlt;
import xyz.Melody.System.Melody.Account.altimpl.OriginalAlt;
import xyz.Melody.System.Melody.Account.altimpl.XBLTokenAlt;
import xyz.Melody.injection.mixins.client.MCA;

public final class AltManager {
    private final ArrayList<Alt> altList;
    private final Gson gson;
    private final File ALT_FILE = new File(FileManager.getClientDir(), "Accounts.txt");
    private final JsonParser parser;

    public void addAlt(Alt alt) {
        this.altList.add(alt);
    }

    public static LoginStatus loginAlt(String string, String string2) throws AuthenticationException {
        if (StringUtils.isNullOrEmpty(string2)) {
            ((MCA)((Object)Minecraft.getMinecraft())).setSession(new Session(string, "", "", "mojang"));
            return LoginStatus.SUCCESS;
        }
        YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication yggdrasilUserAuthentication = (YggdrasilUserAuthentication)yggdrasilAuthenticationService.createUserAuthentication(Agent.MINECRAFT);
        yggdrasilUserAuthentication.setUsername(string);
        yggdrasilUserAuthentication.setPassword(string2);
        yggdrasilUserAuthentication.logIn();
        ((MCA)((Object)Minecraft.getMinecraft())).setSession(new Session(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang"));
        return LoginStatus.SUCCESS;
    }

    public void readAlt() {
        this.altList.clear();
        if (this.ALT_FILE.exists()) {
            try {
                final Iterator iterator = this.parser.parse(IOUtils.inputStreamToString(new FileInputStream(this.ALT_FILE), StandardCharsets.UTF_8)).getAsJsonArray().iterator();
                while (iterator.hasNext()) {
                    final JsonObject asJsonObject = iterator.next().getAsJsonObject();
                    final AccountEnum parse = AccountEnum.parse(asJsonObject.get("AltType").getAsString());
                    final String asString = asJsonObject.get("UserName").getAsString();
                    if (parse != null) {
                        switch (parse) {
                            case AccountEnum.OFFLINE: {
                                this.addAlt(new OfflineAlt(asString));
                                continue;
                            }
                            case AccountEnum.MICROSOFT: {
                                this.addAlt(new MicrosoftAlt(asString, asJsonObject.get("RefreshToken").getAsString(), asJsonObject.get("MSAToken").getAsString(), asJsonObject.get("XBLToken").getAsString(), asJsonObject.get("XSTS_1").getAsString(), asJsonObject.get("UHS").getAsString(), asJsonObject.get("AccessToken").getAsString(), asJsonObject.get("UUID").getAsString()));
                                continue;
                            }
                            case AccountEnum.ORIGINAL: {
                                this.addAlt(new OriginalAlt(asString, asJsonObject.get("AccessToken").getAsString(), asJsonObject.get("UUID").getAsString(), asJsonObject.get("Type").getAsString()));
                                continue;
                            }
                            case AccountEnum.XBLTOKEN: {
                                this.addAlt(new XBLTokenAlt(asString, asJsonObject.get("XBLToken").getAsString(), asJsonObject.get("XSTS_1").getAsString(), asJsonObject.get("UHS").getAsString(), asJsonObject.get("AccessToken").getAsString(), asJsonObject.get("UUID").getAsString(), asJsonObject.get("Type").getAsString()));
                                continue;
                            }
                        }
                    }
                }
            }
            catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public ArrayList<Alt> getAltList() {
        return this.altList;
    }

    public AltManager() {
        this.altList = new ArrayList();
        this.parser = new JsonParser();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    private void writeStringToFile(String string, File file) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);){
            fileOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
        }
    }

    public void saveAlt() {
        JsonArray jsonArray = new JsonArray();
        this.altList.sort(Comparator.comparingDouble(alt -> -alt.getAccountType().getWriteName().length()));
        for (Alt alt2 : this.altList) {
            Alt alt3;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("AltType", alt2.getAccountType().getWriteName());
            jsonObject.addProperty("UserName", alt2.getUserName());
            if (alt2 instanceof MicrosoftAlt) {
                alt3 = (MicrosoftAlt)alt2;
                jsonObject.addProperty("RefreshToken", ((MicrosoftAlt)alt3).getRefreshToken());
                jsonObject.addProperty("MSAToken", ((MicrosoftAlt)alt3).getMsToken());
                jsonObject.addProperty("XBLToken", ((MicrosoftAlt)alt3).getXblToken());
                jsonObject.addProperty("XSTS_1", ((MicrosoftAlt)alt3).getXstsToken_f());
                jsonObject.addProperty("UHS", ((MicrosoftAlt)alt3).getXstsToken_s());
                jsonObject.addProperty("AccessToken", ((MicrosoftAlt)alt3).getAccessToken());
                jsonObject.addProperty("UUID", ((MicrosoftAlt)alt3).getUUID());
            } else if (alt2 instanceof OriginalAlt) {
                alt3 = (OriginalAlt)alt2;
                jsonObject.addProperty("Type", ((OriginalAlt)alt3).getType());
                jsonObject.addProperty("AccessToken", ((OriginalAlt)alt3).getAccessToken());
                jsonObject.addProperty("UUID", ((OriginalAlt)alt3).getUUID());
            } else if (alt2 instanceof XBLTokenAlt) {
                alt3 = (XBLTokenAlt)alt2;
                jsonObject.addProperty("Type", ((XBLTokenAlt)alt3).getType());
                jsonObject.addProperty("XBLToken", ((XBLTokenAlt)alt3).getXblToken());
                jsonObject.addProperty("XSTS_1", ((XBLTokenAlt)alt3).getXstsToken_f());
                jsonObject.addProperty("UHS", ((XBLTokenAlt)alt3).getXstsToken_s());
                jsonObject.addProperty("AccessToken", ((XBLTokenAlt)alt3).getAccessToken());
                jsonObject.addProperty("UUID", ((XBLTokenAlt)alt3).getUUID());
            }
            jsonArray.add(jsonObject);
        }
        try {
            String string = this.gson.toJson(jsonArray);
            this.writeStringToFile(string, this.ALT_FILE);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static enum LoginStatus {
        FAILED,
        SUCCESS,
        EXCEPTION{
            private Exception exception;

            public Exception getException() {
                return this.exception;
            }

            public void setException(Exception exception) {
                this.exception = exception;
            }
        };


        LoginStatus(I i) {
            this();
        }

        private LoginStatus() {
        }
    }
}

