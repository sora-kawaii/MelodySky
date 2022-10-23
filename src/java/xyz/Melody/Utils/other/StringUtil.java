package xyz.Melody.Utils.other;

import net.minecraft.client.Minecraft;

public class StringUtil {
   public static Minecraft mc = Minecraft.getMinecraft();

   public static String removeFormatting(String input) {
      return input.replaceAll("§[0-9a-fk-or]", "");
   }
}
