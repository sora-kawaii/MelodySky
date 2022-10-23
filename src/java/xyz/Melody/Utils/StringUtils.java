package xyz.Melody.Utils;

import java.util.Arrays;

public final class StringUtils {
   public static String toCompleteString(String[] args, int start) {
      return args.length <= start ? "" : String.join(" ", (CharSequence[])Arrays.copyOfRange(args, start, args.length));
   }

   public static String replace(String string, String searchChars, String replaceChars) {
      if (!string.isEmpty() && !searchChars.isEmpty() && !searchChars.equals(replaceChars)) {
         if (replaceChars == null) {
            replaceChars = "";
         }

         int stringLength = string.length();
         int searchCharsLength = searchChars.length();
         StringBuilder stringBuilder = new StringBuilder(string);

         for(int i = 0; i < stringLength; ++i) {
            int start = stringBuilder.indexOf(searchChars, i);
            if (start == -1) {
               if (i == 0) {
                  return string;
               }

               return stringBuilder.toString();
            }

            stringBuilder.replace(start, start + searchCharsLength, replaceChars);
         }

         return stringBuilder.toString();
      } else {
         return string;
      }
   }
}
