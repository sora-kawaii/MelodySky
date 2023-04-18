/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Managers.GaoNeng;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import xyz.Melody.Client;

public final class GaoNengManager {
    public static volatile Map<String, GaoNeng> loading_gaoNengs;
    public static String forUrl;
    public static Thread scanTimerThread;
    public static volatile Map<String, GaoNeng> gaoNengs;

    private static String getPlayerUUID(EntityOtherPlayerMP entityOtherPlayerMP) {
        if (entityOtherPlayerMP == null) {
            return "none";
        }
        String string = EntityPlayerMP.getUUID(entityOtherPlayerMP.getGameProfile()).toString();
        String string2 = string.replaceAll("-", "");
        return string2;
    }

    static {
        gaoNengs = new HashMap<String, GaoNeng>();
        loading_gaoNengs = new HashMap<String, GaoNeng>();
        forUrl = "https://tool.msirp.cn/api.txt";
    }

    public static void loadGaoNengs() throws IOException {
        String string = GaoNengManager.get(forUrl);
        String string2 = GaoNengManager.get(string);
        if (string2 == null) {
            Client.instance.logger.error("Result cannot be 'null'.");
            return;
        }
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(string2);
        if (jsonObject == null) {
            return;
        }
        JsonObject jsonObject2 = jsonObject.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject2.entrySet()) {
            String string3 = entry.getKey();
            GaoNeng gaoNeng = loading_gaoNengs.get(string3);
            boolean bl = gaoNeng == null;
            JsonObject jsonObject3 = entry.getValue().getAsJsonObject();
            String string4 = jsonObject3.get("Name").getAsString();
            String string5 = jsonObject3.get("UUID").getAsString();
            String string6 = jsonObject3.get("Reason").getAsString();
            String string7 = jsonObject3.get("Checker").getAsString();
            String string8 = jsonObject3.get("Rank").getAsString();
            String string9 = jsonObject3.get("Time").getAsString();
            String string10 = jsonObject3.get("Bilibili").getAsString();
            String string11 = jsonObject3.get("QQ").getAsString();
            String string12 = jsonObject3.get("Phone").getAsString();
            boolean bl2 = jsonObject3.get("Tag").getAsBoolean();
            if (!bl) continue;
            gaoNeng = new GaoNeng(string4, string5, string6, string7, string8, string9, string10, string11, string12, bl2);
            loading_gaoNengs.put(entry.getKey(), gaoNeng);
        }
    }

    private static String get(String string) throws IOException {
        String string2 = "";
        URL uRL = new URL(string);
        Object object = (HttpURLConnection)uRL.openConnection();
        ((URLConnection)object).setDoInput(true);
        ((HttpURLConnection)object).setRequestMethod("GET");
        ((URLConnection)object).setRequestProperty("User-Agent", "Mozilla/5.0");
        Object object2 = ((URLConnection)object).getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader((InputStream)object2, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String string3 = bufferedReader.readLine();
        while (string3 != null) {
            string2 = String.valueOf(String.valueOf(string2)) + string3 + "\n";
            string3 = bufferedReader.readLine();
        }
        try {
            bufferedReader.close();
            inputStreamReader.close();
            ((InputStream)object2).close();
            ((HttpURLConnection)object).disconnect();
        }
        catch (Exception exception) {
            object = new Throwable(exception.getMessage());
            object2 = new StackTraceElement[]{};
            ((Throwable)object).setStackTrace((StackTraceElement[])object2);
            ((Throwable)object).printStackTrace();
        }
        return string2;
    }

    public static void registerTimer() {
        scanTimerThread = new Thread(() -> {
            int n = 300000;
            GaoNengManager.load();
            while (true) {
                try {
                    while (true) {
                        Thread.sleep(n);
                        GaoNengManager.load();
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    continue;
                }
                break;
            }
        });
        scanTimerThread.setName("Melody -> GaoNeng Thread");
        scanTimerThread.start();
    }

    public static GaoNeng getIfIsGaoNeng(EntityPlayerSP entityPlayerSP) {
        if (gaoNengs.containsKey(EntityPlayerSP.getUUID(entityPlayerSP.getGameProfile()).toString().replaceAll("-", ""))) {
            return gaoNengs.get(EntityPlayerSP.getUUID(entityPlayerSP.getGameProfile()).toString().replaceAll("-", ""));
        }
        return null;
    }

    public static GaoNeng getIfIsGaoNeng(EntityOtherPlayerMP entityOtherPlayerMP) {
        if (gaoNengs.containsKey(GaoNengManager.getPlayerUUID(entityOtherPlayerMP))) {
            return gaoNengs.get(GaoNengManager.getPlayerUUID(entityOtherPlayerMP));
        }
        return null;
    }

    public static void load() {
        try {
            GaoNengManager.loadGaoNengs();
            gaoNengs = loading_gaoNengs;
            loading_gaoNengs = new HashMap<String, GaoNeng>();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static GaoNeng getIfIsGaoNeng(String string) {
        if (gaoNengs.containsKey(string)) {
            return gaoNengs.get(string);
        }
        return null;
    }

    public static class GaoNeng {
        private String rank;
        private boolean realBlackList;
        private String phone;
        private String reason;
        private String time;
        private String uuid;
        private String checker;
        private String bilibili;
        private String name;
        private String qq;

        public boolean isRealBlackList() {
            return this.realBlackList;
        }

        public String getName() {
            return this.name;
        }

        public String getUuid() {
            return this.uuid;
        }

        public GaoNeng(String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, boolean bl) {
            this.name = string;
            this.uuid = string2;
            this.reason = string3;
            this.checker = string4;
            this.rank = string5;
            this.time = string6;
            this.bilibili = string7;
            this.qq = string8;
            this.phone = string9;
            this.realBlackList = bl;
        }

        public String getReason() {
            return this.reason;
        }

        public String getTime() {
            return this.time;
        }

        public String getRank() {
            return this.rank;
        }

        public String getChecker() {
            return this.checker;
        }

        public String getQQ() {
            return this.qq;
        }

        public String getBilibili() {
            return this.bilibili;
        }

        public String getPhone() {
            return this.phone;
        }
    }
}

