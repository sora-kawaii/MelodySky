package xyz.Melody.Utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import xyz.Melody.Utils.other.StringUtil;

public class ScoreboardUtils {
   public static String cleanSB(String scoreboard) {
      char[] nvString = net.minecraft.util.StringUtils.stripControlCodes(scoreboard).toCharArray();
      StringBuilder cleaned = new StringBuilder();
      char[] var3 = nvString;
      int var4 = nvString.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char c = var3[var5];
         if (c > 20 && c < 127) {
            cleaned.append(c);
         }
      }

      return cleaned.toString();
   }

   public static List getScoreboard() {
      ArrayList lines = new ArrayList();
      if (Minecraft.getMinecraft().theWorld == null) {
         return lines;
      } else {
         Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
         if (scoreboard == null) {
            return lines;
         } else {
            ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
            if (objective == null) {
               return lines;
            } else {
               Collection scores = scoreboard.getSortedScores(objective);
               List list = (List)scores.stream().filter((input) -> {
                  return input != null && input.getPlayerName() != null && !input.getPlayerName().startsWith("#");
               }).collect(Collectors.toList());
               Collection scores = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, scores.size() - 15)) : list;
               Iterator var5 = ((Collection)scores).iterator();

               while(var5.hasNext()) {
                  Score score = (Score)var5.next();
                  ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                  lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
               }

               return lines;
            }
         }
      }
   }

   public static int getLinesNumber() {
      return getScoreboard().size();
   }

   public static boolean scoreboardContains(String string) {
      boolean result = false;
      List scoreboard = getScoreboard();
      Iterator var3 = scoreboard.iterator();

      while(var3.hasNext()) {
         String line = (String)var3.next();
         line = cleanSB(line);
         if (StringUtil.removeFormatting(line).contains(string)) {
            result = true;
            break;
         }
      }

      return result;
   }

   public static String getLineThatContains(String string) {
      String result = null;
      List scoreboard = getScoreboard();
      Iterator var3 = scoreboard.iterator();

      while(var3.hasNext()) {
         String line = (String)var3.next();
         if ((line = cleanSB(line)).contains(string)) {
            result = line;
            break;
         }
      }

      return result;
   }
}
