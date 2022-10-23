package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import net.minecraft.entity.Entity;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class InvisEntity extends Module {
   public InvisEntity() {
      super("InvisEntity", new String[]{"ie"}, ModuleType.QOL);
      this.setColor((new Color(244, 255, 149)).getRGB());
      this.setModInfo("Remove The Entity What You are Looking.");
   }

   @EventHandler
   private void onTick(EventTick e) {
      if (this.mc.objectMouseOver != null) {
         if (this.mc.objectMouseOver.entityHit != null) {
            if (Client.instance.alt.isPressed()) {
               Entity jb = this.mc.objectMouseOver.entityHit;
               this.mc.theWorld.removeEntity(jb);
            }

            this.setEnabled(false);
         }
      }
   }
}
