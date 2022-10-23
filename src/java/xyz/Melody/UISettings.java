package xyz.Melody;

import java.util.ArrayList;

public class UISettings {
   public static ArrayList settings = new ArrayList();
   public static boolean chatTextShadow;
   public static boolean chatBackground;
   public static boolean scoreboardBackground;

   public static void init() {
      settings.add("chatTextShadow");
      settings.add("chatBackground");
      settings.add("scoreboardBackground");
   }
}
