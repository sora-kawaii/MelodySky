package xyz.Melody.module.balance;

import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class NoHitDelay extends Module {
   public NoHitDelay() {
      super("NoHitDelay", new String[]{"hitdelay"}, ModuleType.Balance);
      this.setModInfo("1.8 Mouse Clicking QOL.");
   }
}
