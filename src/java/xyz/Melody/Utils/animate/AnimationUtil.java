package xyz.Melody.Utils.animate;

public class AnimationUtil {
   private static float defaultSpeed = 0.125F;

   public static float calculateCompensation(float target, float current, long delta, int speed) {
      float diff = current - target;
      if (delta < 1L) {
         delta = 1L;
      }

      double xD;
      if (diff > (float)speed) {
         xD = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
         current = (float)((double)current - xD);
         if (current < target) {
            current = target;
         }
      } else if (diff < (float)(-speed)) {
         xD = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
         current = (float)((double)current + xD);
         if (current > target) {
            current = target;
         }
      } else {
         current = target;
      }

      return current;
   }

   public static float mvoeUD(float current, float end, float minSpeed) {
      return moveUD(current, end, defaultSpeed, minSpeed);
   }

   public static float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
      float movement = (end - current) * smoothSpeed;
      if (movement > 0.0F) {
         movement = Math.max(minSpeed, movement);
         movement = Math.min(end - current, movement);
      } else if (movement < 0.0F) {
         movement = Math.min(-minSpeed, movement);
         movement = Math.max(end - current, movement);
      }

      return current + movement;
   }

   public static double animate(double target, double current, double speed) {
      boolean larger = target > current;
      if (speed < 0.0) {
         speed = 0.0;
      } else if (speed > 1.0) {
         speed = 1.0;
      }

      double dif = Math.max(target, current) - Math.min(target, current);
      double factor = dif * speed;
      if (factor < 0.1) {
         factor = 0.1;
      }

      current = larger ? current + factor : current - factor;
      return current;
   }
}
