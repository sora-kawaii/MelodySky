package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;

public class Day extends HUDApi {
   private float xxx = 0.0F;

   public Day() {
      super("DayCounter", 300, 200);
      this.setEnabled(true);
   }

   @EventHandler
   public void onRender(EventRender2D event) {
      if (!(this.mc.currentScreen instanceof HUDScreen)) {
         this.DAY();
      }
   }

   public void InScreenRender() {
      this.DAY();
   }

   private void DAY() {
      int c2 = (new Color(30, 30, 30, 100)).getRGB();
      CFontRenderer font = FontLoaders.NMSL20;
      RenderUtil.drawFastRoundedRect((float)this.x, (float)this.y, (float)(this.x + font.getStringWidth("Day " + this.mc.theWorld.getWorldTime() / 24000L) + 8), (float)(this.y + font.getStringHeight("Day " + this.mc.theWorld.getWorldTime() / 24000L) + 6), 1.0F, c2);
      FontLoaders.NMSL20.drawString("Day " + this.mc.theWorld.getWorldTime() / 24000L, (float)(this.x + 4), (float)(this.y + 4), -1);
   }
}
