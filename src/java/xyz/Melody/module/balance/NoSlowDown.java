package xyz.Melody.module.balance;

import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class NoSlowDown extends Module {
   public NoSlowDown() {
      super("NoSlowDown", new String[]{"ab", "autob", "ablock"}, ModuleType.Balance);
   }
}
