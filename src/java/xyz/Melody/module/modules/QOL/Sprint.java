package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class Sprint extends Module {
   public Sprint() {
      super("Sprint", new String[]{"run"}, ModuleType.QOL);
      this.setColor((new Color(158, 205, 125)).getRGB());
      this.setModInfo("Toggle Sprint.");
   }

   @EventHandler
   private void onUpdate(EventPreUpdate event) {
      if (this.mc.thePlayer.getFoodStats().getFoodLevel() > 6 && this.mc.thePlayer.moveForward > 0.0F) {
         this.mc.thePlayer.setSprinting(true);
      }

   }
}
