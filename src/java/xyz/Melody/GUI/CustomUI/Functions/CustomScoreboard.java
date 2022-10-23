package xyz.Melody.GUI.CustomUI.Functions;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.UISettings;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.rendering.EventRenderScoreboard;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;

public class CustomScoreboard extends HUDApi {
   private ScoreObjective obj;
   private ScaledResolution scale;
   private float maxWidth = 0.0F;
   private float maxHeight = 0.0F;
   CFontRenderer ufr;

   public CustomScoreboard() {
      super("ScoreBoard", (new ScaledResolution(Minecraft.getMinecraft())).getScaledWidth() - 150, (new ScaledResolution(Minecraft.getMinecraft())).getScaledHeight() - 100);
      this.ufr = FontLoaders.NMSL19;
      this.setEnabled(true);
   }

   @EventHandler
   public void onRenderScoreboard(EventRenderScoreboard event) {
      this.obj = event.getObjective();
      this.scale = event.getScaledRes();
   }

   @EventHandler
   private void onDraw(EventRender2D event) {
      if (!(this.mc.currentScreen instanceof HUDScreen)) {
         this.superJiba(this.obj, this.scale);
      }
   }

   public void InScreenRender() {
      this.superJiba(this.obj, this.scale);
   }

   private void superJiba(ScoreObjective objective, ScaledResolution scaledRes) {
      if (objective != null && objective.getScoreboard() != null) {
         Scoreboard scoreboard = objective.getScoreboard();
         Collection collection = scoreboard.getSortedScores(objective);
         List list = Lists.newArrayList(Iterables.filter(collection, new Predicate() {
            public boolean apply(Score p_apply_1_) {
               return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }
         }));
         ArrayList collection;
         if (list.size() > 15) {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
         } else {
            collection = list;
         }

         int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());

         String s;
         for(Iterator var7 = collection.iterator(); var7.hasNext(); i = Math.max(i, this.getFontRenderer().getStringWidth(s))) {
            Score score = (Score)var7.next();
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
         }

         this.maxWidth = (float)i;
         int i1 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
         int j1 = scaledRes.getScaledHeight() / 2 + i1 / 3;
         int l1 = this.x;
         int j = 0;
         Iterator var11 = collection.iterator();

         while(var11.hasNext()) {
            Score score1 = (Score)var11.next();
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
            int k = this.y - j * this.getFontRenderer().FONT_HEIGHT;
            int l = this.x + i;
            this.maxHeight = (float)(j1 - j * this.getFontRenderer().FONT_HEIGHT);
            if (!UISettings.scoreboardBackground) {
               Gui.drawRect(l1 - 2, k, l, k + this.getFontRenderer().FONT_HEIGHT, (new Color(0, 0, 0, 0)).getRGB());
            } else {
               Gui.drawRect(l1 - 2, k, l, k + this.getFontRenderer().FONT_HEIGHT, 1342177280);
            }

            this.getFontRenderer().drawString(s1, l1, k, 553648127);
            this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 553648127);
            if (j == collection.size()) {
               String s3 = objective.getDisplayName();
               if (UISettings.scoreboardBackground) {
                  Gui.drawRect(l1 - 2, k - this.getFontRenderer().FONT_HEIGHT - 1, l, k - 1, 1610612736);
                  Gui.drawRect(l1 - 2, k - 1, l, k, 1342177280);
               }

               this.getFontRenderer().drawString(s3, l1 + i / 2 - this.getFontRenderer().getStringWidth(s3) / 2, k - this.getFontRenderer().FONT_HEIGHT, 553648127);
            }
         }

      }
   }

   private FontRenderer getFontRenderer() {
      return this.mc.fontRendererObj;
   }
}
