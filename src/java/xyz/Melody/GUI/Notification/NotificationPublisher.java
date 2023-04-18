/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Notification;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Notification.Notification;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.AnimationUtil;
import xyz.Melody.Utils.animate.Translate;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.render.Scissor;
import xyz.Melody.Utils.shader.GaussianBlur;

public final class NotificationPublisher {
    private static final List<Notification> NOTIFICATIONS = new CopyOnWriteArrayList<Notification>();
    public static TimerUtil timer;
    private static GaussianBlur gb;

    public static void publish(ScaledResolution scaledResolution) {
        if (NOTIFICATIONS.isEmpty()) {
            return;
        }
        int n = scaledResolution.func_78328_b() - 14;
        int n2 = scaledResolution.func_78326_a();
        int n3 = n - 30;
        CFontRenderer cFontRenderer = FontLoaders.NMSL20;
        CFontRenderer cFontRenderer2 = FontLoaders.NMSL16;
        for (Notification notification : NOTIFICATIONS) {
            float f;
            float f2;
            float f3;
            Translate translate = notification.getTranslate();
            float f4 = notification.getWidth() + 10.0f;
            if (!notification.getTimer().elapsed(notification.getTime()) && !notification.getTimer().elapsed(500L)) {
                notification.scissorBoxWidth = AnimationUtil.animate(f4, notification.scissorBoxWidth, 0.2);
            } else if (notification.getTimer().elapsed(notification.getTime())) {
                notification.scissorBoxWidth = AnimationUtil.animate(0.0, notification.scissorBoxWidth, 0.2);
                if (notification.scissorBoxWidth < 10.0) {
                    NOTIFICATIONS.remove(notification);
                }
                n3 += 32;
            }
            if (!notification.getTimer().elapsed(notification.getTime())) {
                translate.interpolate((float)n2 - f4, n3, 5.0);
            }
            float f5 = translate.getX();
            float f6 = translate.getY();
            float f7 = (float)(notification.scissorBoxWidth * 1.4);
            ScaledResolution scaledResolution2 = new ScaledResolution(Minecraft.func_71410_x());
            int n4 = scaledResolution2.func_78325_e();
            if (!notification.getTimer().elapsed(notification.getTime())) {
                f3 = f5 + notification.getWidth() * 1.25f - f7;
                f2 = n2;
                f = f6;
                float f8 = f6 + 30.0f;
                Scissor.start(f3 * (float)n4, f * (float)n4, f2 * (float)n4, f8 * (float)n4);
                gb.renderBlur(35.0f);
                Scissor.end();
            }
            f3 = f5 + notification.getWidth() * 1.25f - f7;
            RenderUtil.drawFastRoundedRect(f3, f6, n2, f6 + 30.0f, 2.0f, new Color(128, 128, 128, 90).getRGB());
            f2 = (f4 + 40.0f) / (float)notification.getTime();
            RenderUtil.drawFastRoundedRect(f3, f6 + 30.0f - 1.0f, f3 + f2 * (float)notification.getTimer().getElapsedTime(), f6 + 30.0f + 1.0f, 2.0f, notification.getType().getColor());
            RenderUtil.drawImage(notification.getType().getIcon(), f5 + notification.getWidth() * 1.25f - f7 + 7.0f, f6 + 5.0f, 20.0f, 20.0f);
            f = f5 + notification.getWidth() * 1.25f - f7 + 7.0f + 25.0f;
            cFontRenderer.drawString(notification.getTitle(), f + 3.0f, f6 + 5.0f, -1);
            cFontRenderer2.drawString(notification.getContent(), f + 3.0f, f6 + 18.0f, -1);
            n3 -= 32;
        }
    }

    public static void queue(String string, String string2, NotificationType notificationType, int n, boolean bl) {
        CFontRenderer cFontRenderer = FontLoaders.NMSL16;
        NOTIFICATIONS.add(new Notification(string, string2, notificationType, cFontRenderer, n, bl));
    }

    public static void queue(String string, String string2, NotificationType notificationType, int n) {
        CFontRenderer cFontRenderer = FontLoaders.NMSL16;
        NOTIFICATIONS.add(new Notification(string, string2, notificationType, cFontRenderer, n));
    }

    static {
        gb = new GaussianBlur();
    }
}

