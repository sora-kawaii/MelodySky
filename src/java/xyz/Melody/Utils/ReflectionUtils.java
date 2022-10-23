package xyz.Melody.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;

public class ReflectionUtils {
   public static boolean invoke(Class _class, String methodName) {
      try {
         Method method = _class.getDeclaredMethod(methodName);
         method.setAccessible(true);
         method.invoke(Minecraft.getMinecraft());
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public static Object field(Object object, String name) {
      try {
         Field field = object.getClass().getDeclaredField(name);
         field.setAccessible(true);
         return field.get(object);
      } catch (Exception var3) {
         return null;
      }
   }
}
