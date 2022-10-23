package xyz.Melody.module.modules.QOL.Swappings;

import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class EndStoneSword extends Module {
   public EndStoneSword() {
      super("EndStoneSword", new String[]{"ess"}, ModuleType.Swapping);
      this.setModInfo("Auto Swap EndStone Sword.");
   }

   @EventHandler
   private void onTick(EventTick e) {
      if (ItemUtils.useSBItem("END_STONE_SWORD")) {
         Helper.sendMessage(EnumChatFormatting.RED + "Used EndStoneSword!");
      }

      this.setEnabled(false);
   }
}
