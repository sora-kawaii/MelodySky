package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;

public class FPS extends HUDApi {
   public FPS() {
      super("FPS", 5, 90);
      this.setEnabled(true);
   }

   @EventHandler
   public void onRender(EventRender2D event) {
      if (!(this.mc.currentScreen instanceof HUDScreen)) {
         this.fpsRender();
      }
   }

   public void InScreenRender() {
      this.fpsRender();
   }

   private void fpsRender() {
      int c2 = (new Color(30, 30, 30, 100)).getRGB();
      CFontRenderer font = FontLoaders.NMSL20;
      RenderUtil.drawFastRoundedRect((float)this.x, (float)this.y, (float)(this.x + font.getStringWidth("FPS: " + Minecraft.getDebugFPS()) + 8), (float)(this.y + font.getStringHeight("FPS: " + Minecraft.getDebugFPS()) + 6), 1.0F, c2);
      FontLoaders.NMSL20.drawString("FPS: " + Minecraft.getDebugFPS(), (float)(this.x + 4), (float)(this.y + 4), -1);
   }
}
