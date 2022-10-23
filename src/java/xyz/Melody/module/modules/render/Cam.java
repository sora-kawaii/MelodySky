package xyz.Melody.module.modules.render;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.Potion;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class Cam extends Module {
   public Option colorHurtCam = new Option("ColorHurtCam", true);
   public Option bht = new Option("NoHurtCam", true);
   public Option noFire = new Option("NoFireRender", false);
   public Option noBlindness = new Option("NoBlindness", true);
   public Option noClip = new Option("NoClip", false);

   public Cam() {
      super("Camera", ModuleType.Render);
      this.addValues(new Value[]{this.bht, this.colorHurtCam, this.noFire, this.noBlindness, this.noClip});
      this.setModInfo("Better Camera Render.");
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventHandler
   private void onTick() {
      if ((Boolean)this.noBlindness.getValue()) {
         if (this.mc.thePlayer.isPotionActive(Potion.blindness.getId())) {
            this.mc.thePlayer.removePotionEffect(Potion.blindness.getId());
         }

      }
   }

   @EventHandler
   private void onHurt(EventRender2D event) {
      if ((Boolean)this.bht.getValue()) {
         if ((Boolean)this.colorHurtCam.getValue()) {
            ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
            if (this.mc.thePlayer.hurtTime > 0) {
               RenderUtil.drawBorderedRect(0.0F, 0.0F, (float)sc.getScaledWidth(), (float)sc.getScaledHeight(), 10.0F, (new Color(25 * this.mc.thePlayer.hurtTime, 20, 20, 20 * this.mc.thePlayer.hurtTime)).getRGB(), (new Color(255, 255, 255, 1)).getRGB());
            }

         }
      }
   }
}
