package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;

public class BigRat extends HUDApi {
   private float xxx = 0.0F;

   public BigRat() {
      super("RainbowRat", 10, 200);
      this.setEnabled(true);
   }

   @EventHandler
   public void onRender(EventRender2D event) {
      if (!(this.mc.currentScreen instanceof HUDScreen)) {
         this.RAT();
      }
   }

   public void InScreenRender() {
      this.RAT();
   }

   private void RAT() {
      float width = (float)(new ScaledResolution(this.mc)).getScaledWidth();
      if (this.xxx < width) {
         ++this.xxx;
      }

      if (this.xxx >= width) {
         this.xxx = -200.0F;
      }

      if (this.mc.currentScreen instanceof HUDScreen) {
         this.xxx = 0.0F;
      }

      this.mc.fontRendererObj.drawString("§l" + Client.instance.rat[0], (int)this.xxx, this.y + 2, this.rainbow());
      this.mc.fontRendererObj.drawString("§l" + Client.instance.rat[1], (int)this.xxx, this.y + 14, this.rainbow());
      this.mc.fontRendererObj.drawString("§l" + Client.instance.rat[2], (int)this.xxx, this.y + 26, this.rainbow());
      this.mc.fontRendererObj.drawString("§l" + Client.instance.rat[3], (int)this.xxx, this.y + 38, this.rainbow());
   }

   private int rainbow() {
      float hue = (float)(System.currentTimeMillis() % 3000L) / 3000.0F;
      return Color.getHSBColor(hue, 0.75F, 1.0F).getRGB();
   }
}
