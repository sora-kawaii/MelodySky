package xyz.Melody.System.Managers;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventDrawText;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.ScoreboardUtils;
import xyz.Melody.Utils.other.StringUtil;

public class SkyblockListener implements Manager {
   private Minecraft mc = Minecraft.getMinecraft();
   private int ticks = 0;
   private int dickTimer = 0;
   private int idkTimer = 0;

   public void init() {
      EventBus.getInstance().register(this);
   }

   @EventHandler
   private void onFR(EventDrawText event) {
      event.setText(event.getText().replaceAll(this.mc.getSession().getUsername(), Client.playerName));
   }

   @EventHandler
   public void onTick(EventTick event) {
      if (this.ticks % 20 == 0) {
         if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            ScoreObjective scoreboardObj = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreboardObj != null) {
               Client.inSkyblock = StringUtil.removeFormatting(scoreboardObj.getDisplayName()).contains("SKYBLOCK");
            }

            if (ScoreboardUtils.scoreboardContains("The Catacombs")) {
               Client.inDungeons = true;
            } else if (ScoreboardUtils.scoreboardContains("Time Elapsed:") && ScoreboardUtils.scoreboardContains("Cleared: ")) {
               Client.inDungeons = true;
            } else {
               Client.inDungeons = false;
            }
         }

         this.ticks = 0;
      }

      ++this.ticks;
   }

   @EventHandler
   public void updateSBArea(EventTick event) {
      if (this.dickTimer <= 20) {
         ++this.dickTimer;
      } else {
         Client.instance.sbArea.updateCurrentArea();
         this.dickTimer = 0;
      }
   }

   @EventHandler
   public void updateSlayer(EventTick event) {
      if (this.idkTimer <= 20) {
         ++this.idkTimer;
      } else {
         if (ScoreboardUtils.scoreboardContains("Slay the boss!")) {
            Client.instance.slayerBossSpawned = true;
         } else {
            Client.instance.slayerBossSpawned = false;
         }

         this.idkTimer = 0;
      }
   }
}
