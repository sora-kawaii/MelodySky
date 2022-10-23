package xyz.Melody.GUI.Notification;

import java.awt.Color;
import net.minecraft.util.ResourceLocation;

public enum NotificationType {
   SUCCESS((new Color(6348946)).getRGB(), new ResourceLocation("Melody/notification/success.png")),
   INFO((new Color(6590631)).getRGB(), new ResourceLocation("Melody/notification/info.png")),
   WARN((new Color(6590631)).getRGB(), new ResourceLocation("Melody/notification/warning.png")),
   ERROR((new Color(16723759)).getRGB(), new ResourceLocation("Melody/notification/error.png"));

   private final int color;
   private final ResourceLocation icon;

   public ResourceLocation getIcon() {
      return this.icon;
   }

   private NotificationType(int color, ResourceLocation icon) {
      this.color = color;
      this.icon = icon;
   }

   public final int getColor() {
      return this.color;
   }
}
