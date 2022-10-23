package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.modules.QOL.Dungeons.DungeonMap;

public class DungeonMapXYSet extends HUDApi {
   public static int tileSize = 8;
   public static int maxMapPx;

   public DungeonMapXYSet() {
      super("DungeonMap", 20, 20);
      this.setEnabled(true);
   }

   public void InScreenRender() {
      DungeonMap map = (DungeonMap)Client.instance.getModuleManager().getModuleByClass(DungeonMap.class);
      float scale = ((Double)map.scale.getValue()).floatValue() / 100.0F;
      int borderSize = 2;
      GL11.glPushMatrix();
      GlStateManager.scale(scale, scale, scale);
      GlStateManager.translate((double)this.x * (1.0 + (double)scale * 0.55) + (double)(map.mapBorder.getValue() != DungeonMap.borders.None ? 3 : 0), (double)this.y * (1.0 + (double)scale * 0.55) + (double)(map.mapBorder.getValue() != DungeonMap.borders.None ? 3 : 0), 0.0);
      RenderUtil.drawFastRoundedRect(0.0F, 0.0F, (float)(maxMapPx + tileSize * 2), (float)(maxMapPx + tileSize * 2), 2.0F, (new Color(10, 10, 10, 120)).getRGB());
      if (map.mapBorder.getValue() != DungeonMap.borders.None) {
         int borderColor = 0;
         switch (((Enum)map.mapBorder.getValue()).toString()) {
            case "None":
            default:
               break;
            case "White":
               borderColor = Colors.WHITE.c;
               break;
            case "Black":
               borderColor = Colors.BLACK.c;
               break;
            case "Rainbow":
               borderColor = RenderUtil.rainbow((long)this.mc.thePlayer.ticksExisted, 1.0F, 1.0F).getRGB();
         }

         int mapWidth = 200;
         Gui.drawRect(-borderSize, -borderSize, mapWidth + borderSize, 0, borderColor);
         Gui.drawRect(-borderSize, -borderSize, 0, mapWidth + borderSize, borderColor);
         Gui.drawRect(-borderSize, mapWidth, mapWidth + borderSize, mapWidth + borderSize, borderColor);
         Gui.drawRect(mapWidth, -borderSize, mapWidth + borderSize, mapWidth + borderSize, borderColor);
      }

      GlStateManager.translate((float)(-tileSize), (float)(-tileSize), 0.0F);
      GlStateManager.translate((float)(-DungeonMap.mapX - (map.mapBorder.getValue() != DungeonMap.borders.None ? borderSize : 0)), (float)(-DungeonMap.mapY - (map.mapBorder.getValue() != DungeonMap.borders.None ? borderSize : 0)), 0.0F);
      GL11.glPopMatrix();
   }

   static {
      maxMapPx = tileSize * 23;
   }
}
