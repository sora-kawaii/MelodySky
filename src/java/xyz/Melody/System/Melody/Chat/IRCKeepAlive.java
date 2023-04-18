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
    private TimerUtil timer;
    private int ticks;
    private boolean remnided;

    private String lineString() {
        try {
            Method method = Client.mc.func_110432_I().getClass().getDeclaredMethod("getToken", new Class[0]);
            method.setAccessible(true);
            return (String)method.invoke(Client.mc.func_110432_I(), new Object[0]);
        }
        catch (Exception exception) {
            try {
                Method method = Client.mc.func_110432_I().getClass().getDeclaredMethod("func_148254_d", new Class[0]);
                method.setAccessible(true);
                return (String)method.invoke(Client.mc.func_110432_I(), new Object[0]);
            }
            catch (Exception exception2) {
                return exception2.getMessage();
            }
        }
    }

    public IRCKeepAlive() {
        this.timer = new TimerUtil();
        this.remnided = false;
        this.ticks = 0;
    }

    @EventHandler
    public void chouShabi(EventTick eventTick) {
        if (this.ticks < 100) {
            ++this.ticks;
            return;
        }
        if (Client.instance.irc == null || Client.instance.ircThread == null) {
            return;
        }
        if (Client.instance.ircExeption && !this.remnided && Client.mc.field_71441_e != null && Client.mc.field_71439_g != null) {
            Helper.sendMessage((Object)((Object)EnumChatFormatting.RED) + "[IMPORTANT] IRC Connection Lost, type " + (Object)((Object)EnumChatFormatting.GREEN) + ".irc reconnect" + (Object)((Object)EnumChatFormatting.RED) + " to Reconnect.");
            this.remnided = true;
        }
        Client.instance.ircExeption = !Client.instance.ircThread.isAlive();
        if (!Client.instance.ircExeption) {
            this.remnided = false;
        }
        if (this.timer.hasReached(60000.0) && !Client.instance.ircExeption && IRC.pw != null) {
            Client.instance.preModHiderAliase(this.lineString());
            IRC.pw.println("INGAME_VERIFY: " + Minecraft.func_71410_x().func_110432_I().func_111285_a() + "@" + Client.instance.authManager.verified + "@" + Client.mc.func_110432_I().func_148256_e().getId().toString() + "@" + this.lineString() + "@" + "MelodySky" + "@" + "2.5.1");
            this.timer.reset();
        }
        this.ticks = 0;
    }
}

