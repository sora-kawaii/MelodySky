/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import net.minecraft.client.Minecraft;
import xyz.Melody.Client;
import xyz.Melody.System.Managers.Manager;
import xyz.Melody.System.Melody.Authentication.UserObj;
import xyz.Melody.Utils.other.DisplayUtils;

public final class AuthManager
implements Manager {
    public boolean verified;
    private Thread authDaemonThread;
    private Minecraft mc = Minecraft.getMinecraft();
    private UserObj user;

    public String getChecked() throws Exception {
        return this.get();
    }

    public UserObj getUser() {
        return this.user;
    }

    public boolean authMe(String string, String string2) {
        StackTraceElement[] stackTraceElementArray;
        Throwable throwable;
        Client.instance.authenticatingUser = true;
        boolean bl = false;
        boolean bl2 = false;
        DisplayUtils.init();
        while (DisplayUtils.t.isAlive()) {
            Thread.sleep(10L);
        }
        try {
            Client.instance.authDaemon();
            bl = this.verified;
        }
        catch (Exception exception) {
            throwable = new Throwable(exception.getMessage());
            stackTraceElementArray = new StackTraceElement[]{};
            throwable.setStackTrace(stackTraceElementArray);
            throwable.printStackTrace();
        }
        Client.instance.logger.info("m1Verified: " + bl);
        if (!bl) {
            Client.instance.logger.info("[Melody] [AUTHENTICATION] Trying Auth Method 2.");
            try {
                String string3 = this.getChecked();
                Client.instance.authDaemon();
                if (string3.contains(string)) {
                    bl2 = true;
                }
            }
            catch (Exception exception) {
                throwable = new Throwable(exception.getMessage());
                stackTraceElementArray = new StackTraceElement[]{};
                throwable.setStackTrace(stackTraceElementArray);
                throwable.printStackTrace();
            }
            Client.instance.logger.info("m2Verified: " + bl2);
        }
        if (bl || bl2) {
            this.verified = true;
            Client.instance.authenticatingUser = false;
            return true;
        }
        Client.instance.authenticatingUser = false;
        return false;
    }

    public String get() throws IOException {
        String string = "";
        HttpURLConnection httpURLConnection = (HttpURLConnection)Client.path.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        Object object = httpURLConnection.getInputStream();
        Object object2 = new InputStreamReader((InputStream)object, "utf-8");
        BufferedReader bufferedReader = new BufferedReader((Reader)object2);
        String string2 = bufferedReader.readLine();
        while (string2 != null) {
            string = String.valueOf(String.valueOf(string)) + string2 + "\n";
            string2 = bufferedReader.readLine();
        }
        try {
            bufferedReader.close();
            ((InputStreamReader)object2).close();
            ((InputStream)object).close();
            httpURLConnection.disconnect();
        }
        catch (Exception exception) {
            object = new Throwable(exception.getMessage());
            object2 = new StackTraceElement[]{};
            ((Throwable)object).setStackTrace((StackTraceElement[])object2);
            ((Throwable)object).printStackTrace();
        }
        return string;
    }

    public void setUser(UserObj userObj) {
        this.user = userObj;
    }

    @Override
    public void init() {
        this.authMe(this.user.getUuid(), this.user.getName());
    }
}

