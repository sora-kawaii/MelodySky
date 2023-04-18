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
    public void tick(EventTick eventTick) {
        if (!Client.inSkyblock) {
            return;
        }
        if (this.timer.hasReached(250.0)) {
            int n;
            IChatComponent iChatComponent = ((GuiPlayerTabAccessor)((Object)this.mc.field_71456_v.func_175181_h())).getFooter();
            String string = null;
            String string2 = null;
            if (iChatComponent != null) {
                string = iChatComponent.func_150254_d();
                string2 = Pattern.compile("(?i)\u00a7[0-9A-FK-ORZ]").matcher(string).replaceAll("");
            }
            if (string == null) {
                return;
            }
            Pattern pattern = Pattern.compile("Tonic I (?<min>[0-9.]+) Minutes");
            Pattern pattern2 = Pattern.compile("Tonic I (?<hour>[0-9.]+)h (?<min>[0-9.]+)m");
            Pattern pattern3 = Pattern.compile("Tonic I (?<min>[0-9.]+)m (?<sec>[0-9.]+)s");
            Pattern pattern4 = Pattern.compile("Tonic I (?<sec>[0-9.]+)s");
            Matcher matcher = pattern.matcher(string2);
            Matcher matcher2 = pattern2.matcher(string2);
            Matcher matcher3 = pattern3.matcher(string2);
            Matcher matcher4 = pattern4.matcher(string2);
            String string3 = null;
            String string4 = null;
            String string5 = null;
            while (matcher.find()) {
                string4 = matcher.group("min");
            }
            while (matcher2.find()) {
                string4 = matcher2.group("min");
                string3 = matcher2.group("hour");
            }
            while (matcher3.find()) {
                string4 = matcher3.group("min");
                string5 = matcher3.group("sec");
            }
            while (matcher4.find()) {
                string5 = matcher4.group("sec");
            }
            String string6 = null;
            if (string4 != null) {
                int n2;
                string6 = (Object)((Object)EnumChatFormatting.GREEN) + "Mushed Glowy Tonic: " + (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + string4 + (Object)((Object)EnumChatFormatting.GREEN) + " Min Left.";
                if (string3 != null) {
                    n = Integer.parseInt(string3) * 60;
                    n2 = Integer.parseInt(string4);
                    string6 = (Object)((Object)EnumChatFormatting.GREEN) + "Mushed Glowy Tonic: " + (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + (n + n2) + (Object)((Object)EnumChatFormatting.GREEN) + " Min Left.";
                }
                if (string5 != null) {
                    n = Integer.parseInt(string4) * 60;
                    n2 = Integer.parseInt(string5);
                    string6 = (Object)((Object)EnumChatFormatting.GREEN) + "Mushed Glowy Tonic: " + (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + (n + n2) + (Object)((Object)EnumChatFormatting.GREEN) + " Seconds Left.";
                }
            }
            if (string4 == null && string3 == null && string5 != null) {
                n = Integer.parseInt(string5);
                string6 = (Object)((Object)EnumChatFormatting.GREEN) + "Mushed Glowy Tonic: " + (Object)((Object)EnumChatFormatting.RED) + n + (Object)((Object)EnumChatFormatting.GREEN) + " Seconds Left.";
            }
            this.shabi = string6;
            if (string6 == null) {
                this.shabi = (Object)((Object)EnumChatFormatting.RED) + "Mushed Glowy Tonic Effect Not Actived.";
            }
            this.timer.reset();
        }
    }

    @EventHandler
    public void onRender(EventRender2D eventRender2D) {
        if (this.mc.field_71462_r instanceof HUDScreen) {
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
            CFontRenderer cFontRenderer = FontLoaders.NMSL22;
            RenderUtil.drawFastRoundedRect(this.x + 1, this.y, this.x + cFontRenderer.getStringWidth(this.shabi) + 6, this.y + 16, 2.0f, new Color(30, 30, 30, 130).getRGB());
            cFontRenderer.drawString(this.shabi, this.x + 4, this.y + 4, -1);
        }
    }
}

