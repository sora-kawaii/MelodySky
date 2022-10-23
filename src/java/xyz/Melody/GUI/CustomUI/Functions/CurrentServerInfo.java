package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;

public class CurrentServerInfo extends HUDApi {
   public CurrentServerInfo() {
      super("Area", 5, 50);
      this.setEnabled(true);
   }

   @EventHandler
   public void onRender(EventRender2D event) {
      if (!(this.mc.currentScreen instanceof HUDScreen)) {
         this.render();
      }
   }

   public void InScreenRender() {
      this.render();
   }

   private void render() {
      int c2 = (new Color(30, 30, 30, 100)).getRGB();
      if (Client.inSkyblock) {
         CFontRenderer font = FontLoaders.NMSL20;
         if (!Client.inDungeons) {
            RenderUtil.drawFastRoundedRect((float)this.x, (float)this.y, (float)(this.x + font.getStringWidth("Area: " + Client.instance.sbArea.getCurrentArea()) + 8), (float)(this.y + 10 + 6), 1.0F, c2);
            FontLoaders.NMSL20.drawString("Area: " + Client.instance.sbArea.getCurrentArea(), (float)(this.x + 4), (float)(this.y + 4), -1);
         } else if (Client.inDungeons) {
            if (Client.inDungeons) {
               int row = 0;
               FontLoaders.NMSL18.drawString("Score: " + Client.instance.dungeonUtils.score, (float)(this.x + 3), (float)(this.y + 3), -1);
               FontLoaders.NMSL18.drawString("Mimic: " + Client.instance.dungeonUtils.foundMimic, (float)(this.x + 4), (float)(this.y + 13), -1);
               FontLoaders.NMSL18.drawString("Secrets Found:" + Client.instance.dungeonUtils.secretsFound, (float)(this.x + 3), (float)(this.y + 23), -1);
               FontLoaders.NMSL18.drawString("Crypts:" + Client.instance.dungeonUtils.cryptsFound, (float)(this.x + 3), (float)(this.y + 33), -1);
               FontLoaders.NMSL18.drawString("Deaths:" + Client.instance.dungeonUtils.deaths, (float)(this.x + 3), (float)(this.y + 43), -1);
               FontLoaders.NMSL18.drawString("Teams:", (float)(this.x + 3), (float)(this.y + 53), -1);

               for(Iterator var4 = Client.instance.dungeonUtils.teammates.iterator(); var4.hasNext(); ++row) {
                  EntityPlayer teammate = (EntityPlayer)var4.next();
                  FontLoaders.NMSL18.drawString(" - " + teammate.getName(), (float)(this.x + 3), (float)(this.y + 63 + row * 10), -1);
               }

               FontLoaders.NMSL18.drawString("Floor: " + Client.instance.dungeonUtils.floor.name(), (float)(this.x + 3), (float)(this.y + 63 + row * 10), -1);
               FontLoaders.NMSL18.drawString("Master: " + Client.isMMD, (float)(this.x + 3), (float)(this.y + 73 + row * 10), -1);
               FontLoaders.NMSL18.drawString("In Boss: " + Client.instance.dungeonUtils.inBoss, (float)(this.x + 3), (float)(this.y + 83 + row * 10), -1);
            } else {
               FontLoaders.NMSL20.drawString("Unexpected Error.", (float)(this.x + 4), (float)(this.y + 4), -1);
            }
         }

      }
   }
}
