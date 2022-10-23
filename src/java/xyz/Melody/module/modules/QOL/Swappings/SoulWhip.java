package xyz.Melody.module.modules.QOL.Swappings;

import org.lwjgl.input.Mouse;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventKey;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class SoulWhip extends Module {
   private boolean shouldSwitch;
   private TimerUtil timer = new TimerUtil();
   private Mode mode;
   private Option mouse;
   private Option both;

   public SoulWhip() {
      super("SoulWhip", new String[]{"aots"}, ModuleType.Swapping);
      this.mode = new Mode("Mouse", SoulWhip.ddd.values(), SoulWhip.ddd.Right);
      this.mouse = new Option("MouseClick", false);
      this.both = new Option("Both", false);
      this.addValues(new Value[]{this.mode, this.mouse, this.both});
      this.setModInfo("Auto Swap And Use Soul Whip.");
   }

   @EventHandler
   private void onMouse(EventTick e) {
      if (this.mc.currentScreen == null) {
         int dik = this.mode.getValue() == SoulWhip.ddd.Left ? 0 : 1;
         if (Mouse.isButtonDown(dik) && ((Boolean)this.mouse.getValue() || (Boolean)this.both.getValue())) {
            this.shouldSwitch = true;
         }

      }
   }

   @EventHandler
   private void onTick(EventTick e) {
      if (this.shouldSwitch && this.timer.hasReached(200.0)) {
         ItemUtils.useSBItem("SOUL_WHIP");
         this.timer.reset();
      }

      this.shouldSwitch = false;
   }

   @EventHandler
   private void onKey(EventKey event) {
      if (event.getKey() == this.getKey() && (!(Boolean)this.mouse.getValue() || (Boolean)this.both.getValue())) {
         this.shouldSwitch = true;
         this.setEnabled(true);
      }

   }

   static enum ddd {
      Left,
      Right;
   }
}
