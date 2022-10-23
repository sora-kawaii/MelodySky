package xyz.Melody.GUI.Font;

import java.awt.Font;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import xyz.Melody.Client;

public abstract class FontLoaders {
   public static CFontRenderer NMSL10 = new CFontRenderer(getNMSL(10), true);
   public static CFontRenderer NMSL11 = new CFontRenderer(getNMSL(11), true);
   public static CFontRenderer NMSL12 = new CFontRenderer(getNMSL(12), true);
   public static CFontRenderer NMSL13 = new CFontRenderer(getNMSL(13), true);
   public static CFontRenderer NMSL14 = new CFontRenderer(getNMSL(14), true);
   public static CFontRenderer NMSL15 = new CFontRenderer(getNMSL(15), true);
   public static CFontRenderer NMSL16 = new CFontRenderer(getNMSL(16), true);
   public static CFontRenderer NMSL18 = new CFontRenderer(getNMSL(18), true);
   public static CFontRenderer NMSL19 = new CFontRenderer(getNMSL(19), true);
   public static CFontRenderer NMSL20 = new CFontRenderer(getNMSL(20), true);
   public static CFontRenderer NMSL21 = new CFontRenderer(getNMSL(21), true);
   public static CFontRenderer NMSL22 = new CFontRenderer(getNMSL(22), true);
   public static CFontRenderer NMSL24 = new CFontRenderer(getNMSL(24), true);
   public static CFontRenderer NMSL25 = new CFontRenderer(getNMSL(25), true);
   public static CFontRenderer NMSL26 = new CFontRenderer(getNMSL(26), true);
   public static CFontRenderer NMSL35 = new CFontRenderer(getNMSL(35), true);
   public static CFontRenderer NMSL40 = new CFontRenderer(getNMSL(40), true);
   public static CFontRenderer NMSL45 = new CFontRenderer(getNMSL(45), true);
   public static CFontRenderer NMSL28 = new CFontRenderer(getNMSL(28), true);
   public static CFontRenderer CNMD10 = new CFontRenderer(getCNMD(10), true);
   public static CFontRenderer CNMD11 = new CFontRenderer(getCNMD(11), true);
   public static CFontRenderer CNMD12 = new CFontRenderer(getCNMD(12), true);
   public static CFontRenderer CNMD13 = new CFontRenderer(getCNMD(13), true);
   public static CFontRenderer CNMD14 = new CFontRenderer(getCNMD(14), true);
   public static CFontRenderer CNMD15 = new CFontRenderer(getCNMD(15), true);
   public static CFontRenderer CNMD16 = new CFontRenderer(getCNMD(16), true);
   public static CFontRenderer CNMD18 = new CFontRenderer(getCNMD(18), true);
   public static CFontRenderer CNMD19 = new CFontRenderer(getCNMD(19), true);
   public static CFontRenderer CNMD20 = new CFontRenderer(getCNMD(20), true);
   public static CFontRenderer CNMD21 = new CFontRenderer(getCNMD(21), true);
   public static CFontRenderer CNMD22 = new CFontRenderer(getCNMD(22), true);
   public static CFontRenderer CNMD24 = new CFontRenderer(getCNMD(24), true);
   public static CFontRenderer CNMD25 = new CFontRenderer(getCNMD(25), true);
   public static CFontRenderer CNMD26 = new CFontRenderer(getCNMD(26), true);
   public static CFontRenderer CNMD30 = new CFontRenderer(getCNMD(30), true);
   public static CFontRenderer CNMD31 = new CFontRenderer(getCNMD(31), true);
   public static CFontRenderer CNMD32 = new CFontRenderer(getCNMD(32), true);
   public static CFontRenderer CNMD33 = new CFontRenderer(getCNMD(33), true);
   public static CFontRenderer CNMD34 = new CFontRenderer(getCNMD(34), true);
   public static CFontRenderer CNMD35 = new CFontRenderer(getCNMD(35), true);
   public static CFontRenderer CNMD40 = new CFontRenderer(getCNMD(40), true);
   public static CFontRenderer CNMD45 = new CFontRenderer(getCNMD(45), true);
   public static CFontRenderer CNMD28 = new CFontRenderer(getCNMD(28), true);

   private static void log(String font) {
      Client.instance.logger.info("[Melody] [Font] Loading Font: " + font + ".");
   }

   private static Font getNMSL(int size) {
      Font font;
      try {
         font = new Font("Tahoma", 0, size);
         log("Tahoma " + size);
      } catch (Exception var3) {
         var3.printStackTrace();
         System.out.println("Error loading font");
         font = new Font("default", 0, size);
      }

      return font;
   }

   private static Font getCNMD(int size) {
      Font font;
      try {
         InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Melody/Fonts/CNMD.ttf")).getInputStream();
         font = Font.createFont(0, is);
         font = font.deriveFont(0, (float)size);
         log("Montserrat Regular " + size);
      } catch (Exception var3) {
         var3.printStackTrace();
         System.out.println("Error loading font");
         font = new Font("default", 0, size);
      }

      return font;
   }
}
