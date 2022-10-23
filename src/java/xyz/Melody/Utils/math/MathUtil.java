package xyz.Melody.Utils.math;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import xyz.Melody.Utils.Helper;

public class MathUtil {
   public static Random random = new Random();

   public static double toDecimalLength(double in, int places) {
      return Double.parseDouble(String.format("%." + places + "f", in));
   }

   public static double round(double in, int places) {
      places = (int)MathHelper.clamp_double((double)places, 0.0, 2.147483647E9);
      return Double.parseDouble(String.format("%." + places + "f", in));
   }

   public static boolean parsable(String s, byte type) {
      try {
         switch (type) {
            case 0:
               Short.parseShort(s);
               break;
            case 1:
               Byte.parseByte(s);
               break;
            case 2:
               Integer.parseInt(s);
               break;
            case 3:
               Float.parseFloat(s);
               break;
            case 4:
               Double.parseDouble(s);
               break;
            case 5:
               Long.parseLong(s);
         }

         return true;
      } catch (NumberFormatException var3) {
         var3.printStackTrace();
         return false;
      }
   }

   public static double square(double in) {
      return in * in;
   }

   public static double randomDouble(double min, double max) {
      return ThreadLocalRandom.current().nextDouble(min, max);
   }

   public static double getBaseMovementSpeed() {
      double baseSpeed = 0.2873;
      if (Helper.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
         int amplifier = Helper.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
         baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
      }

      return baseSpeed;
   }

   public static double getHighestOffset(double max) {
      for(double i = 0.0; i < max; i += 0.01) {
         int[] arrn = new int[]{-2, -1, 0, 1, 2};
         int[] arrn2 = arrn;
         int n = arrn.length;

         for(int n2 = 0; n2 < n; ++n2) {
            int offset = arrn2[n2];
            if (Helper.mc.theWorld.getCollidingBoundingBoxes(Helper.mc.thePlayer, Helper.mc.thePlayer.getEntityBoundingBox().offset(Helper.mc.thePlayer.motionX * (double)offset, i, Helper.mc.thePlayer.motionZ * (double)offset)).size() > 0) {
               return i - 0.01;
            }
         }
      }

      return max;
   }

   public static class NumberType {
      public static final byte SHORT = 0;
      public static final byte BYTE = 1;
      public static final byte INT = 2;
      public static final byte FLOAT = 3;
      public static final byte DOUBLE = 4;
      public static final byte LONG = 5;

      public static byte getByType(Class cls) {
         if (cls == Short.class) {
            return 0;
         } else if (cls == Byte.class) {
            return 1;
         } else if (cls == Integer.class) {
            return 2;
         } else if (cls == Float.class) {
            return 3;
         } else if (cls == Double.class) {
            return 4;
         } else {
            return (byte)(cls == Long.class ? 5 : -1);
         }
      }
   }
}
