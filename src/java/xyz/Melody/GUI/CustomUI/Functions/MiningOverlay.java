package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.Client;
import xyz.Melody.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.PlayerListUtils;
import xyz.Melody.Utils.render.RenderUtil;

public class MiningOverlay extends HUDApi {
   private ArrayList clicks = new ArrayList();
   private String mithrilPowder = null;
   private String gemstonePowder = null;
   private int tickTimer = 0;

   public MiningOverlay() {
      super("MiningOverlay", 50, 50);
      this.setEnabled(true);
   }

   @EventHandler
   private void onTick(EventTick event) {
      SkyblockArea area = Client.instance.sbArea;
      if (this.tickTimer <= 20) {
         ++this.tickTimer;
      } else if (area.getCurrentArea() != SkyblockArea.Areas.Dwarven_Mines && area.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
         this.mithrilPowder = "Failed to Fetch Mithril Powder Info From The Player List.";
         this.gemstonePowder = "Failed to Fetch Gemstone Powder Info From The Player List.";
      } else {
         if (PlayerListUtils.tabContains("Mithril Powder:")) {
            this.mithrilPowder = PlayerListUtils.copyContainsLine("Mithril Powder:");
            this.mithrilPowder.replaceFirst(" ", "");
         } else {
            this.mithrilPowder = "Failed to Fetch Mithril Powder Info From The Player List.";
         }

         if (PlayerListUtils.tabContains("Gemstone Powder:")) {
            this.gemstonePowder = PlayerListUtils.copyContainsLine("Gemstone Powder:");
            this.gemstonePowder.replaceFirst(" ", "");
         } else {
            this.gemstonePowder = "Failed to Fetch Gemstone Powder Info From The Player List.";
         }

         this.tickTimer = 0;
      }
   }

   @EventHandler
   public void onRender(EventRender2D event) {
      if (!(this.mc.currentScreen instanceof HUDScreen)) {
         SkyblockArea area = Client.instance.sbArea;
         if (area.getCurrentArea() == SkyblockArea.Areas.Dwarven_Mines || area.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
            this.render();
         }

      }
   }

   public void InScreenRender() {
      this.render();
   }

   private void render() {
      int c2 = (new Color(30, 30, 30, 170)).getRGB();
      CFontRenderer font = FontLoaders.NMSL20;
      int longest = Math.max(font.getStringWidth(this.mithrilPowder), font.getStringWidth(" Pickaxe CD Ready: " + Client.pickaxeAbilityReady));
      int finalWidth = Math.max(longest, font.getStringWidth(this.gemstonePowder));
      RenderUtil.drawFastRoundedRect((float)this.x, (float)this.y, (float)(this.x + finalWidth + 10), (float)(this.y + 43), 1.0F, c2);
      RenderUtil.drawBorderedRect((float)this.x, (float)this.y, (float)(this.x + finalWidth + 10), (float)(this.y + 43), 1.5F, Client.pickaxeAbilityReady ? Colors.GREEN.c : Colors.RED.c, 0);
      FontLoaders.NMSL20.drawString(this.mithrilPowder, (float)(this.x + 2), (float)(this.y + 5), -1);
      FontLoaders.NMSL20.drawString(this.gemstonePowder, (float)(this.x + 2), (float)(this.y + 18), -1);
      FontLoaders.NMSL20.drawString(" Pickaxe CD Ready: " + (Client.pickaxeAbilityReady ? EnumChatFormatting.GREEN + "true" : EnumChatFormatting.RED + "false"), (float)(this.x + 2), (float)(this.y + 31), -1);
   }

   public int getRightCPS() {
      long curTime = System.currentTimeMillis();
      this.clicks.removeIf((e) -> {
         return e + 1000L < curTime;
      });
      return this.clicks.size();
   }
}
