package xyz.Melody.System.Managers.Dungeons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.StringUtils;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.System.Managers.Manager;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.PlayerListUtils;
import xyz.Melody.Utils.ScoreboardUtils;

public class DungeonListener implements Manager {
   private Minecraft mc = Minecraft.getMinecraft();
   public DungeonTypes floor;
   public boolean inBoss;
   public ArrayList teammates;
   public int secretsFound;
   public int cryptsFound;
   public boolean foundMimic;
   public int deaths;
   public boolean activeRun;
   public int score;
   private Pattern scorePattern;
   public List entryMessages;
   private int dicks;
   private Pattern numberPattern;

   public DungeonListener() {
      this.floor = DungeonTypes.NULL;
      this.inBoss = false;
      this.teammates = new ArrayList();
      this.secretsFound = 0;
      this.cryptsFound = 0;
      this.foundMimic = false;
      this.deaths = 0;
      this.activeRun = false;
      this.score = 0;
      this.scorePattern = Pattern.compile("Cleared: [0-9]{1,3}% \\((?<score>[0-9]+)\\)");
      this.entryMessages = Arrays.asList("[BOSS] Bonzo:", "[BOSS] Scarf:", "[BOSS] The Professor:", "[BOSS] Thorn:", "[BOSS] Livid:", "[BOSS] Sadan:", "[BOSS] Maxor:");
      this.dicks = 0;
      this.numberPattern = Pattern.compile("[^0-9.]");
   }

   public void init() {
      EventBus.getInstance().register(this);
   }

   public void reset() {
      this.floor = DungeonTypes.NULL;
      this.inBoss = false;
      this.secretsFound = 0;
      this.cryptsFound = 0;
      this.foundMimic = false;
      this.deaths = 0;
      this.activeRun = false;
      this.score = 0;
      this.teammates.clear();
   }

   @EventHandler
   public void onChatPacket(EventPacketRecieve event) {
      if (this.mc.theWorld != null && this.mc.thePlayer != null) {
         if (Client.inDungeons && event.getPacket() instanceof S02PacketChat && ((S02PacketChat)event.getPacket()).getType() != 2) {
            String text = StringUtils.stripControlCodes(((S02PacketChat)event.getPacket()).getChatComponent().getUnformattedText());
            if ("[NPC] Mort: Here, I found this map when I first entered the dungeon.".equals(text)) {
               this.updateTeammates(this.getTabList());
            }
         }

      }
   }

   @EventHandler
   public void onTick1(EventTick event) {
      if (this.mc.theWorld != null && this.mc.thePlayer != null) {
         if (this.dicks < 20) {
            ++this.dicks;
         } else if (Client.inDungeons) {
            this.updateFloor();
            List tabList = this.getTabList();
            if (tabList != null) {
               this.updateStats(tabList);
            }

            if (tabList != null && this.teammates.isEmpty()) {
               this.updateTeammates(tabList);
            }

            String scoreLine = ScoreboardUtils.getLineThatContains("Cleared: ");
            if (scoreLine != null) {
               Matcher matcher = this.scorePattern.matcher(scoreLine);
               if (matcher.matches()) {
                  String scoreString = matcher.group("score");
                  this.score = Integer.parseInt(scoreString);
               }
            }

            this.dicks = 0;
         }
      }
   }

   public void updateTeammates(List tabList) {
      this.teammates.clear();

      for(int i = 0; i < 5; ++i) {
         String text = StringUtils.stripControlCodes((String)tabList.get(1 + i * 4)).trim();
         String username = text.split(" ")[0];
         if (!Objects.equals(username, "")) {
            Iterator var5 = this.mc.theWorld.playerEntities.iterator();

            while(var5.hasNext()) {
               EntityPlayer playerEntity = (EntityPlayer)var5.next();
               if (playerEntity.getName().contains(username)) {
                  this.teammates.add(playerEntity);
               }
            }
         }
      }

   }

   public void updateFloor() {
      if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
         if (Client.inDungeons) {
            if (ScoreboardUtils.scoreboardContains("(F1)")) {
               this.floor = DungeonTypes.F1;
               Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F2)")) {
               this.floor = DungeonTypes.F2;
               Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F3)")) {
               this.floor = DungeonTypes.F3;
               Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F4)")) {
               this.floor = DungeonTypes.F4;
               Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F5)")) {
               this.floor = DungeonTypes.F5;
               Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F6)")) {
               this.floor = DungeonTypes.F6;
               Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(F7)")) {
               this.floor = DungeonTypes.F7;
               Client.isMMD = false;
            } else if (ScoreboardUtils.scoreboardContains("(M1)")) {
               this.floor = DungeonTypes.M1;
               Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M2)")) {
               this.floor = DungeonTypes.M2;
               Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M3)")) {
               this.floor = DungeonTypes.M3;
               Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M4)")) {
               this.floor = DungeonTypes.M4;
               Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M5)")) {
               this.floor = DungeonTypes.M5;
               Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M6)")) {
               this.floor = DungeonTypes.M6;
               Client.isMMD = true;
            } else if (ScoreboardUtils.scoreboardContains("(M7)")) {
               this.floor = DungeonTypes.M7;
               Client.isMMD = true;
            } else if ((ScoreboardUtils.scoreboardContains("Dragon") || ScoreboardUtils.scoreboardContains("No Alive Dragons")) && ScoreboardUtils.scoreboardContains("Time Elapsed:") && ScoreboardUtils.scoreboardContains("Cleard: ")) {
               this.floor = DungeonTypes.M7;
               Client.isMMD = true;
            } else {
               this.floor = DungeonTypes.NULL;
               Client.isMMD = false;
            }
         } else {
            this.floor = DungeonTypes.NULL;
            Client.isMMD = false;
         }

      }
   }

   private void updateStats(List tabList) {
      try {
         Iterator var2 = tabList.iterator();

         while(true) {
            while(var2.hasNext()) {
               String item = (String)var2.next();
               String justNumbers;
               if (item.contains("Deaths: ")) {
                  item = StringUtils.stripControlCodes(item);
                  justNumbers = this.removeAllExceptNumbersAndPeriods(item);
                  if (!justNumbers.isEmpty()) {
                     this.deaths = Integer.parseInt(justNumbers);
                  }
               } else if (item.contains("Secrets Found: ") && !item.contains("%")) {
                  item = StringUtils.stripControlCodes(item);
                  justNumbers = this.removeAllExceptNumbersAndPeriods(item);
                  if (!justNumbers.isEmpty()) {
                     this.secretsFound = Integer.parseInt(justNumbers);
                  }
               } else if (item.contains("Crypts: ")) {
                  item = StringUtils.stripControlCodes(item);
                  justNumbers = this.removeAllExceptNumbersAndPeriods(item);
                  if (!justNumbers.isEmpty()) {
                     this.cryptsFound = Integer.parseInt(justNumbers);
                  }
               }
            }

            return;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
         Client.instance.logger.error("Exception in class DungeonListener.");
      }
   }

   public List getTabList() {
      List tabList = PlayerListUtils.getTabListListStr();
      return !StringUtils.stripControlCodes((String)tabList.get(0)).contains("Party") ? null : tabList;
   }

   public boolean inFloor(DungeonTypes... floors) {
      DungeonTypes[] var2 = floors;
      int var3 = floors.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         DungeonTypes floorToCheck = var2[var4];
         if (floorToCheck == this.floor) {
            return true;
         }
      }

      return false;
   }

   public void debug() {
      if (Client.inDungeons) {
         Helper.sendMessage("Floor: " + this.floor.name());
         Helper.sendMessage("In Boss: " + this.inBoss);
         Helper.sendMessage("Teams:");
         Iterator var1 = this.teammates.iterator();

         while(var1.hasNext()) {
            EntityPlayer teammate = (EntityPlayer)var1.next();
            Helper.sendMessage("- " + teammate.getName());
         }
      } else {
         Helper.sendMessage("You must be in a dungeon to debug a dungeon!");
      }

   }

   public String removeAllExceptNumbersAndPeriods(String text) {
      return this.numberPattern.matcher(text).replaceAll("");
   }
}
