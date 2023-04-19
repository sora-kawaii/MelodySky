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

    public static void publish(ScaledResolution sr) {
        if (NOTIFICATIONS.isEmpty()) {
            return;
        }
        int srScaledHeight = sr.getScaledHeight() - 14;
        int scaledWidth = sr.getScaledWidth();
        int y = srScaledHeight - 30;
        CFontRenderer fr = FontLoaders.NMSL20;
        CFontRenderer fr1 = FontLoaders.NMSL16;
        for (Notification notification : NOTIFICATIONS) {
            Translate translate = notification.getTranslate();
            float width = notification.getWidth() + 10.0f;
            if (!notification.getTimer().elapsed(notification.getTime()) && !notification.getTimer().elapsed(500L)) {
                notification.scissorBoxWidth = AnimationUtil.animate(width, notification.scissorBoxWidth, 0.2);
            } else if (notification.getTimer().elapsed(notification.getTime())) {
                notification.scissorBoxWidth = AnimationUtil.animate(0.0, notification.scissorBoxWidth, 0.2);
                if (notification.scissorBoxWidth < 10.0) {
                    NOTIFICATIONS.remove(notification);
                }
                y += 32;
            }
            if (!notification.getTimer().elapsed(notification.getTime())) {
                translate.interpolate((float)scaledWidth - width, y, 5.0);
            }
            float translateX = translate.getX();
            float translateY = translate.getY();
            float appX = (float)(notification.scissorBoxWidth * 1.4);
            ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
            int factor = scale.getScaleFactor();
            if (!notification.getTimer().elapsed(notification.getTime())) {
                float right = translateX + notification.getWidth() * 1.25f - appX;
                float right1 = scaledWidth;
                float height = translateY;
                float height1 = translateY + 30.0f;
                Scissor.start(right * (float)factor, height * (float)factor, right1 * (float)factor, height1 * (float)factor);
                gb.renderBlur(35.0f);
                Scissor.end();
            }
            float jbx = translateX + notification.getWidth() * 1.25f - appX;
            RenderUtil.drawFastRoundedRect(jbx, translateY, scaledWidth, translateY + 30.0f, 2.0f, new Color(128, 128, 128, 90).getRGB());
            float timer = (width + 40.0f) / (float)notification.getTime();
            RenderUtil.drawFastRoundedRect(jbx, translateY + 30.0f - 1.0f, jbx + timer * (float)notification.getTimer().getElapsedTime(), translateY + 30.0f + 1.0f, 2.0f, notification.getType().getColor());
            RenderUtil.drawImage(notification.getType().getIcon(), translateX + notification.getWidth() * 1.25f - appX + 7.0f, translateY + 5.0f, 20.0f, 20.0f);
            float shabi = translateX + notification.getWidth() * 1.25f - appX + 7.0f + 25.0f;
            fr.drawString(notification.getTitle(), shabi + 3.0f, translateY + 5.0f, -1);
            fr1.drawString(notification.getContent(), shabi + 3.0f, translateY + 18.0f, -1);
            y -= 32;
        }
    }

    public static void queue(String title, String content, NotificationType type, int time, boolean showTime) {
        CFontRenderer fr = FontLoaders.NMSL16;
        NOTIFICATIONS.add(new Notification(title, content, type, fr, time, showTime));
    }

    public static void queue(String title, String content, NotificationType type, int time) {
        CFontRenderer fr = FontLoaders.NMSL16;
        NOTIFICATIONS.add(new Notification(title, content, type, fr, time));
    }

    static {
        gb = new GaussianBlur();
    }
}

