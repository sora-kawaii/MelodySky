package xyz.Melody.module.balance;

import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Value;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class Reach extends Module {
   public Numbers size = new Numbers("Size", 3.5, 0.0, 6.0, 0.1);

   public Reach() {
      super("Reach", new String[]{"hitBox"}, ModuleType.Balance);
      this.addValues(new Value[]{this.size});
   }
}
