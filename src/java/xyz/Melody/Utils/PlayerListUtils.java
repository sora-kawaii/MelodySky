package xyz.Melody.Utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerListUtils {
   private static Minecraft mc = Minecraft.getMinecraft();
   private static final Ordering playerOrdering = Ordering.from(new PlayerComparator());
   public static final Ordering playerInfoOrdering2 = new Ordering() {
      public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
         ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
         ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
         return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != GameType.SPECTATOR, p_compare_2_.getGameType() != GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
      }
   };

   public static GuiPlayerTabOverlay getTabList() {
      return mc.ingameGUI.getTabList();
   }

   public static List getTabEntries() {
      return Minecraft.getMinecraft().thePlayer == null ? Collections.emptyList() : playerInfoOrdering2.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
   }

   public static List getTabListListStr() {
      return (List)getTabEntries().stream().map((playerInfo) -> {
         return Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(playerInfo);
      }).collect(Collectors.toList());
   }

   public static boolean tabContains(String str) {
      List players = playerOrdering.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
      Iterator var2 = players.iterator();

      String name;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         NetworkPlayerInfo info = (NetworkPlayerInfo)var2.next();
         name = net.minecraft.util.StringUtils.stripControlCodes(Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(info));
      } while(!name.contains(str));

      return true;
   }

   public static String copyContainsLine(String str) {
      List players = playerOrdering.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
      Iterator var2 = players.iterator();

      String name;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         NetworkPlayerInfo info = (NetworkPlayerInfo)var2.next();
         name = Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(info);
      } while(!name.contains(str));

      return name;
   }

   @SideOnly(Side.CLIENT)
   static class PlayerComparator implements Comparator {
      private PlayerComparator() {
      }

      public int compare(NetworkPlayerInfo o1, NetworkPlayerInfo o2) {
         ScorePlayerTeam team1 = o1.getPlayerTeam();
         ScorePlayerTeam team2 = o2.getPlayerTeam();
         return ComparisonChain.start().compareTrueFirst(o1.getGameType() != GameType.SPECTATOR, o2.getGameType() != GameType.SPECTATOR).compare(team1 != null ? team1.getRegisteredName() : "", team2 != null ? team2.getRegisteredName() : "").compare(o1.getGameProfile().getName(), o2.getGameProfile().getName()).result();
      }

      // $FF: synthetic method
      PlayerComparator(Object x0) {
         this();
      }
   }
}
