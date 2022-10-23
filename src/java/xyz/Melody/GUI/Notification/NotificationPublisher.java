package xyz.Melody.GUI.Notification;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.AnimationUtil;
import xyz.Melody.Utils.animate.Translate;
import xyz.Melody.Utils.render.RenderUtil;

public final class NotificationPublisher {
   private static final List NOTIFICATIONS = new CopyOnWriteArrayList();
   public static TimerUtil timer;

   public static void publish(ScaledResolution sr) {
      if (!NOTIFICATIONS.isEmpty()) {
         int srScaledHeight = sr.getScaledHeight() - 14;
         int scaledWidth = sr.getScaledWidth();
         int y = srScaledHeight - 30;
         CFontRenderer fr = FontLoaders.NMSL20;
         CFontRenderer fr1 = FontLoaders.NMSL16;

         for(Iterator var6 = NOTIFICATIONS.iterator(); var6.hasNext(); y -= 30) {
            Notification notification = (Notification)var6.next();
            Translate translate = notification.getTranslate();
            float width = notification.getWidth();
            if (!notification.getTimer().hasReached((double)notification.getTime())) {
               notification.scissorBoxWidth = AnimationUtil.animate((double)width, notification.scissorBoxWidth, 0.1);
               translate.interpolate((float)scaledWidth - width, (float)y, 0.15);
            } else {
               notification.scissorBoxWidth = AnimationUtil.animate(0.0, notification.scissorBoxWidth, 0.1);
               if (notification.scissorBoxWidth < 1.0) {
                  NOTIFICATIONS.remove(notification);
               }

               y += 30;
            }

            float translateX = translate.getX();
            float translateY = translate.getY();
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.prepareScissorBox((float)((double)scaledWidth - notification.scissorBoxWidth - (double)(notification.getWidth() / 4.0F)) - 1.0F, translateY, (float)scaledWidth + notification.getWidth() / 4.0F, translateY + 30.0F);
            Gui.drawRect((int)(translateX - notification.getWidth() / 4.0F - 5.0F), (int)translateY, scaledWidth, (int)(translateY + 30.0F), -1879048192);
            Gui.drawRect((int)translateX, (int)(translateY + 30.0F - 2.0F), (int)(translateX + width * (float)(((long)notification.getTime() - notification.getTimer().getElapsedTime()) / (long)notification.getTime())), (int)(translateY + 30.0F), notification.getType().getColor());
            RenderUtil.drawImage(notification.getType().getIcon(), translateX - 10.0F - width / 9.0F, translateY + 5.0F, 20.0F, 20.0F);
            fr.drawStringWithShadow(notification.getTitle(), (double)(translateX + 3.0F), (double)(translateY + 4.0F), -1);
            fr1.drawStringWithShadow(notification.getContent(), (double)(translateX + 3.0F), (double)(translateY + 17.0F), -1);
            GL11.glDisable(3089);
            GL11.glPopMatrix();
         }

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
}
