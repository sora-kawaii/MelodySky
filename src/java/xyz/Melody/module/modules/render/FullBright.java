package xyz.Melody.module.modules.render;

import java.awt.Color;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class FullBright extends Module {
   private float old;

   public FullBright() {
      super("FullBright", new String[]{"fbright", "brightness", "bright"}, ModuleType.Render);
      this.setColor((new Color(244, 255, 149)).getRGB());
      this.setModInfo("Night Vision.");
   }

   public void onEnable() {
      NotificationPublisher.queue("Test", "test", NotificationType.INFO, 114514);
      this.old = this.mc.gameSettings.gammaSetting;
   }

   @EventHandler
   private void onTick(EventTick e) {
      this.mc.gameSettings.gammaSetting = 1.5999999E7F;
   }

   public void onDisable() {
      this.mc.gameSettings.gammaSetting = this.old;
   }
}
