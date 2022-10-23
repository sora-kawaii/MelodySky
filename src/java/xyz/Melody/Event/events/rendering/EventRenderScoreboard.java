package xyz.Melody.Event.events.rendering;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import xyz.Melody.Event.Event;

public class EventRenderScoreboard extends Event {
   private ScoreObjective objective;
   private ScaledResolution scaledRes;

   public EventRenderScoreboard(ScoreObjective objective, ScaledResolution scaledRes) {
      this.objective = objective;
      this.scaledRes = scaledRes;
   }

   public ScoreObjective getObjective() {
      return this.objective;
   }

   public ScaledResolution getScaledRes() {
      return this.scaledRes;
   }
}
