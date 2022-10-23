package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;

public class LCPS extends HUDApi {
   private ArrayList clicks = new ArrayList();

   public LCPS() {
      super("LCPS", 5, 10);
      this.setEnabled(true);
   }

   @SubscribeEvent
   public void onClick(InputEvent.MouseInputEvent event) {
      if (Mouse.getEventButtonState() && this.mc.gameSettings.keyBindAttack.isKeyDown() && Mouse.getEventButton() == this.mc.gameSettings.keyBindAttack.getKeyCode() + 100) {
         this.clicks.add(System.currentTimeMillis());
      }

   }

   @EventHandler
   public void onRender(EventRender2D event) {
      if (!(this.mc.currentScreen instanceof HUDScreen)) {
         this.cpsRender();
      }
   }

   public void InScreenRender() {
      this.cpsRender();
   }

   private void cpsRender() {
      int c2 = (new Color(30, 30, 30, 100)).getRGB();
      CFontRenderer font = FontLoaders.NMSL20;
      RenderUtil.drawFastRoundedRect((float)this.x, (float)this.y, (float)(this.x + font.getStringWidth("LCPS: " + this.getLeftCPS()) + 8), (float)(this.y + font.getStringHeight("LCPS: " + this.getLeftCPS()) + 6), 1.0F, c2);
      FontLoaders.NMSL20.drawString("LCPS: " + this.getLeftCPS(), (float)(this.x + 4), (float)(this.y + 4), -1);
   }

   public int getLeftCPS() {
      long curTime = System.currentTimeMillis();
      this.clicks.removeIf((e) -> {
         return e + 1000L < curTime;
      });
      return this.clicks.size();
   }
}
