package xyz.Melody.module.modules.QOL.Swappings;

import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoZombieSword extends Module {
   private Mode type;

   public AutoZombieSword() {
      super("AutoHeal", new String[]{"aheal"}, ModuleType.Swapping);
      this.type = new Mode("Sword", AutoZombieSword.swordType.values(), AutoZombieSword.swordType.FloridZombie);
      this.addValues(new Value[]{this.type});
      this.setModInfo("Auto Swap Zombie Swords and Use 5 Times.");
   }

   @EventHandler
   private void onTick(EventTick e) {
      for(int i = 0; i < 5; ++i) {
         switch (((Enum)this.type.getValue()).toString().toUpperCase()) {
            case "ZOMBIE":
               ItemUtils.useSBItem("ZOMBIE_SWORD");
               break;
            case "ORNATEZOMBIE":
               ItemUtils.useSBItem("ORNATE_ZOMBIE_SWORD");
               break;
            case "FLORIDZOMBIE":
               ItemUtils.useSBItem("FLORID_ZOMBIE_SWORD");
         }
      }

      Helper.sendMessage("AutoZombieSword Disabled.");
      this.setEnabled(false);
   }

   static enum swordType {
      FloridZombie,
      OrnateZombie,
      Zombie;
   }
}
