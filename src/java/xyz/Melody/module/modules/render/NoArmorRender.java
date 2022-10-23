package xyz.Melody.module.modules.render;

import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class NoArmorRender extends Module {
   public Option selfOnly = new Option("SelfOnly", true);
   public Option chead = new Option("HideCustomHead", true);
   public Option armor = new Option("HideArmor", true);
   public Option arrows = new Option("HideArrows", true);

   public NoArmorRender() {
      super("NoArmorRender", new String[]{"armor"}, ModuleType.Render);
      this.addValues(new Value[]{this.selfOnly, this.chead, this.armor, this.arrows});
      this.setModInfo("Armor Invisible, Hide Arrows on Your Body.");
   }
}
