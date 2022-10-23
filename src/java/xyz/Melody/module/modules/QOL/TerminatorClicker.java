package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import org.lwjgl.input.Mouse;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class TerminatorClicker extends Module {
   private TimerUtil timer = new TimerUtil();
   private Option juju = new Option("Juju", false);
   private Numbers cps = new Numbers("CPS", 14.0, 10.0, 20.0, 0.1);

   public TerminatorClicker() {
      super("TerminatorClicker", new String[]{"tc"}, ModuleType.QOL);
      this.addValues(new Value[]{this.juju, this.cps});
      this.setColor((new Color(191, 191, 191)).getRGB());
      this.setModInfo("Auto Right Click Terminator.");
   }

   @EventHandler
   private void onTick(EventTick event) {
      if (this.mc.thePlayer.getHeldItem() != null) {
         String itemID = ItemUtils.getSkyBlockID(this.mc.thePlayer.getHeldItem());
         if (itemID.equals("TERMINATOR") || (Boolean)this.juju.getValue() && itemID.equals("JUJU_SHORTBOW")) {
            float rdelay = 1000.0F / ((Double)this.cps.getValue()).floatValue();
            if (Mouse.isButtonDown(1) && this.timer.delay(rdelay) && this.mc.currentScreen == null) {
               Client.rightClick();
               this.timer.reset();
            }
         }

      }
   }
}
