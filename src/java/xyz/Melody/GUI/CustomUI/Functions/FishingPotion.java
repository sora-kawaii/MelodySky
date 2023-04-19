/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.injection.mixins.gui.GuiPlayerTabAccessor;

public final class FishingPotion
extends HUDApi {
    private TimerUtil timer = new TimerUtil();
    private String shabi = "";

    public FishingPotion() {
        super("FishingPotion", 50, 30);
        this.setEnabled(true);
    }

    @EventHandler
    public void tick(EventTick event) {
        if (!Client.inSkyblock) {
            return;
        }
        if (this.timer.hasReached(250.0)) {
            IChatComponent tabFooterChatComponent = ((GuiPlayerTabAccessor)((Object)this.mc.ingameGUI.getTabList())).getFooter();
            String tabFooterString = null;
            String strippedTabFooterString = null;
            if (tabFooterChatComponent != null) {
                tabFooterString = tabFooterChatComponent.getFormattedText();
                strippedTabFooterString = Pattern.compile("(?i)\u00a7[0-9A-FK-ORZ]").matcher(tabFooterString).replaceAll("");
            }
            if (tabFooterString == null) {
                return;
            }
            Pattern sb = Pattern.compile("Tonic I (?<min>[0-9.]+) Minutes");
            Pattern sb1 = Pattern.compile("Tonic I (?<hour>[0-9.]+)h (?<min>[0-9.]+)m");
            Pattern sb2 = Pattern.compile("Tonic I (?<min>[0-9.]+)m (?<sec>[0-9.]+)s");
            Pattern sb3 = Pattern.compile("Tonic I (?<sec>[0-9.]+)s");
            Matcher matcher = sb.matcher(strippedTabFooterString);
            Matcher matcher1 = sb1.matcher(strippedTabFooterString);
            Matcher matcher2 = sb2.matcher(strippedTabFooterString);
            Matcher matcher3 = sb3.matcher(strippedTabFooterString);
            String hour = null;
            String minute = null;
            String sec = null;
            while (matcher.find()) {
                minute = matcher.group("min");
            }
            while (matcher1.find()) {
                minute = matcher1.group("min");
                hour = matcher1.group("hour");
            }
            while (matcher2.find()) {
                minute = matcher2.group("min");
                sec = matcher2.group("sec");
            }
            while (matcher3.find()) {
                sec = matcher3.group("sec");
            }
            String ps = null;
            if (minute != null) {
                ps = (Object)((Object)EnumChatFormatting.GREEN) + "Mushed Glowy Tonic: " + (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + minute + (Object)((Object)EnumChatFormatting.GREEN) + " Min Left.";
                if (hour != null) {
                    int htm = Integer.parseInt(hour) * 60;
                    int m = Integer.parseInt(minute);
                    ps = (Object)((Object)EnumChatFormatting.GREEN) + "Mushed Glowy Tonic: " + (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + (htm + m) + (Object)((Object)EnumChatFormatting.GREEN) + " Min Left.";
                }
                if (sec != null) {
                    int mts = Integer.parseInt(minute) * 60;
                    int s = Integer.parseInt(sec);
                    ps = (Object)((Object)EnumChatFormatting.GREEN) + "Mushed Glowy Tonic: " + (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + (mts + s) + (Object)((Object)EnumChatFormatting.GREEN) + " Seconds Left.";
                }
            }
            if (minute == null && hour == null && sec != null) {
                int s = Integer.parseInt(sec);
                ps = (Object)((Object)EnumChatFormatting.GREEN) + "Mushed Glowy Tonic: " + (Object)((Object)EnumChatFormatting.RED) + s + (Object)((Object)EnumChatFormatting.GREEN) + " Seconds Left.";
            }
            this.shabi = ps;
            if (ps == null) {
                this.shabi = (Object)((Object)EnumChatFormatting.RED) + "Mushed Glowy Tonic Effect Not Actived.";
            }
            this.timer.reset();
        }
    }

    @EventHandler
    public void onRender(EventRender2D event) {
        if (this.mc.currentScreen instanceof HUDScreen) {
            return;
        }
        this.render();
    }

    @Override
    public void InScreenRender() {
        this.render();
    }

    private void render() {
        if (this.shabi != null) {
            CFontRenderer cfr = FontLoaders.NMSL22;
            RenderUtil.drawFastRoundedRect(this.x + 1, this.y, this.x + cfr.getStringWidth(this.shabi) + 6, this.y + 16, 2.0f, new Color(30, 30, 30, 130).getRGB());
            cfr.drawString(this.shabi, this.x + 4, this.y + 4, -1);
        }
    }
}

