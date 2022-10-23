package xyz.Melody.Utils;

import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ArrayUtils {
   public static Object getRandomItem(List list) {
      return list.get((new Random()).nextInt(list.size()));
   }

   public static Object firstOrNull(Iterable iterable) {
      return Iterables.getFirst(iterable, (Object)null);
   }

   public static Object getFirstMatch(List list, Predicate predicate) {
      return list.stream().filter(predicate).findFirst().orElse((Object)null);
   }
}
