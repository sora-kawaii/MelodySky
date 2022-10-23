package xyz.Melody.module.modules.QOL.Swappings;

import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class WitherImpact extends Module {
   private Mode mode;

   public WitherImpact() {
      super("WitherImpact", new String[]{"wi"}, ModuleType.Swapping);
      this.mode = new Mode("Sword", WitherImpact.swords.values(), WitherImpact.swords.Hyperion);
      this.addValues(new Value[]{this.mode});
      this.setModInfo("Auto Swap Wither Impact Swords.");
   }

   @EventHandler
   private void onTick(EventTick e) {
      switch (((Enum)this.mode.getValue()).toString().toUpperCase()) {
         case "HYPERION":
            ItemUtils.useSBItem("HYPERION");
            break;
         case "SCYLLA":
            ItemUtils.useSBItem("SCYLLA");
            break;
         case "ASTRAEA":
            ItemUtils.useSBItem("ASTRAEA");
            break;
         case "VALKYRIE":
            ItemUtils.useSBItem("VALKYRIE");
            break;
         case "NECRONBLADE":
            ItemUtils.useSBItem("NECRON_BLADE");
      }

      this.setEnabled(false);
   }

   static enum swords {
      Hyperion,
      Scylla,
      Astraea,
      Valkyrie,
      NecronBlade;
   }
}
