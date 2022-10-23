package xyz.Melody.module.balance;

import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Value;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class HitBox extends Module {
   public Numbers size = new Numbers("Size", 0.2, 0.1, 1.0, 0.1);

   public HitBox() {
      super("HitBox", new String[]{"hitBox"}, ModuleType.Balance);
      this.addValues(new Value[]{this.size});
   }
}
