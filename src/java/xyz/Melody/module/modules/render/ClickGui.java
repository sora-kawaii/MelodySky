package xyz.Melody.module.modules.render;

import java.awt.Color;
import xyz.Melody.GUI.ClickNew.NewClickGui;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class ClickGui extends Module {
   public ClickGui() {
      super("ClickGui", new String[]{"click"}, ModuleType.Render);
      this.setColor((new Color(244, 255, 149)).getRGB());
   }

   public void onEnable() {
      this.mc.displayGuiScreen(new NewClickGui());
      this.setEnabled(false);
   }
}
