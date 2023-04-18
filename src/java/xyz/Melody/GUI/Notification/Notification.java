/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.Translate;

public final class Notification {
    public static final int HEIGHT = 30;
    private final String title;
    private final String content;
    private final int time;
    private final NotificationType type;
    private final TimerUtil timer;
    private final Translate translate;
    private final CFontRenderer fontRenderer;
    public double scissorBoxWidth;
    public boolean showTime;

    public Notification(String string, String string2, NotificationType notificationType, CFontRenderer cFontRenderer, int n, boolean bl) {
        this.title = string;
        this.content = string2;
        this.time = n;
        this.type = notificationType;
        this.showTime = bl;
        this.timer = new TimerUtil();
        this.fontRenderer = cFontRenderer;
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        this.translate = new Translate((float)scaledResolution.getScaledWidth() - this.getWidth(), scaledResolution.getScaledHeight() - 40);
    }

    public Notification(String string, String string2, NotificationType notificationType, CFontRenderer cFontRenderer, int n) {
        this.title = string;
        this.content = string2;
        this.time = n;
        this.type = notificationType;
        this.showTime = false;
        this.timer = new TimerUtil();
        this.fontRenderer = cFontRenderer;
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        this.translate = new Translate((float)scaledResolution.getScaledWidth() - this.getWidth(), scaledResolution.getScaledHeight() - 40);
    }

    public float getWidth() {
        return Math.max(100, Math.max(this.fontRenderer.getStringWidth(this.title), this.fontRenderer.getStringWidth(this.content)) + 10);
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getContent() {
        return this.content;
    }

    public final int getTime() {
        return this.time;
    }

    public final double getDBTime() {
        double d = this.time / 1000;
        return d;
    }

    public final NotificationType getType() {
        return this.type;
    }

    public final TimerUtil getTimer() {
        return this.timer;
    }

    public final Translate getTranslate() {
        return this.translate;
    }
}

