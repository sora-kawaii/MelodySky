package xyz.Melody.GUI.DungeonMap;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.GUI.DungeonMap.elements.MapTile;
import xyz.Melody.GUI.DungeonMap.elements.doors.DoorTile;
import xyz.Melody.GUI.DungeonMap.elements.rooms.RoomTile;
import xyz.Melody.GUI.DungeonMap.elements.rooms.RoomType;
import xyz.Melody.GUI.DungeonMap.elements.rooms.Separator;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.modules.QOL.Dungeons.DungeonMap;

public class MapView {
   private static Minecraft mc = Minecraft.getMinecraft();
   public static int tileSize = 8;
   public static int maxMapPx;
   public static int maxMapBlocks;
   private static int borderSize;
   private static ArrayList roomNamesDrawn;
   private static ResourceLocation icon_cross;
   private static ResourceLocation icon_whiteCheck;
   private static ResourceLocation icon_check;

   @EventHandler
   public void onRenderOverlay(EventRender2D event) {
      if (MapController.scannedMap != null && Client.inDungeons && !Client.instance.dungeonUtils.inBoss) {
         DungeonMap map = (DungeonMap)Client.instance.getModuleManager().getModuleByClass(DungeonMap.class);
         if (map.isEnabled()) {
            float scale = ((Double)map.scale.getValue()).floatValue() / 100.0F;
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate((float)(DungeonMap.mapX + (map.mapBorder.getValue() != DungeonMap.borders.None ? borderSize : 0)), (float)(DungeonMap.mapY + (map.mapBorder.getValue() != DungeonMap.borders.None ? borderSize : 0)), 0.0F);
            Gui.drawRect(0, 0, maxMapPx + tileSize * 2, maxMapPx + tileSize * 2, (new Color(10, 10, 10, 120)).getRGB());
            int rowNum;
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
                     borderColor = this.getChroma();
               }

               rowNum = maxMapPx + tileSize * 2;
               Gui.drawRect(-3, -3, rowNum + 3, 0, borderColor);
               Gui.drawRect(-3, -3, 0, rowNum + 3, borderColor);
               Gui.drawRect(-3, rowNum, rowNum + 3, rowNum + 3, borderColor);
               Gui.drawRect(rowNum, -3, rowNum + 3, rowNum + 3, borderColor);
            }

            GlStateManager.enableAlpha();
            GlStateManager.translate((float)tileSize, (float)tileSize, 0.0F);
            MapTile[][] elements = MapController.scannedMap.elements;

            int x;
            int y;
            for(rowNum = 0; rowNum < elements.length; ++rowNum) {
               MapTile[] row = elements[rowNum];

               for(int colNum = 0; colNum < row.length; ++colNum) {
                  MapTile tile = row[colNum];
                  if (tile != null) {
                     x = (int)(Math.ceil((double)((float)colNum / 2.0F)) * (double)tileSize * 3.0 + Math.floor((double)((float)colNum / 2.0F)) * (double)tileSize);
                     y = (int)(Math.ceil((double)((float)rowNum / 2.0F)) * (double)tileSize * 3.0 + Math.floor((double)((float)rowNum / 2.0F)) * (double)tileSize);
                     boolean colEven = colNum % 2 == 0;
                     boolean rowEven = rowNum % 2 == 0;
                     if (colEven && rowEven) {
                        Gui.drawRect(x, y, x + tileSize * 3, y + tileSize * 3, tile.color);
                        if (!drawCheckmark((RoomTile)tile, x, y)) {
                           drawRoomName((RoomTile)tile, x, y);
                        }
                     } else if (tile instanceof DoorTile) {
                        if (colEven) {
                           Gui.drawRect(x + tileSize, y, x + tileSize * 2, y + tileSize, tile.color);
                        } else {
                           Gui.drawRect(x, y + tileSize, x + tileSize, y + tileSize * 2, tile.color);
                        }
                     } else if (tile instanceof Separator) {
                        if (colEven) {
                           Gui.drawRect(x, y, x + tileSize * 3, y + tileSize, tile.color);
                        } else if (rowEven) {
                           Gui.drawRect(x, y, x + tileSize, y + tileSize * 3, tile.color);
                        } else {
                           Gui.drawRect(x, y, x + tileSize, y + tileSize, tile.color);
                        }
                     }
                  }
               }
            }

            int headSize = 10;
            Iterator var18 = Client.instance.dungeonUtils.teammates.iterator();

            while(var18.hasNext()) {
               EntityPlayer teammate = (EntityPlayer)var18.next();
               int playerX = (int)((float)(teammate.getPosition().getX() - MapScanner.xCorner) / (float)maxMapBlocks * (float)maxMapPx);
               x = (int)((float)(teammate.getPosition().getZ() - MapScanner.zCorner) / (float)maxMapBlocks * (float)maxMapPx);
               y = (int)(teammate.getRotationYawHead() - 180.0F);
               drawPlayerIcon(teammate, headSize, playerX - headSize / 2, x - headSize / 2, y);
            }

            GlStateManager.translate((float)(-tileSize), (float)(-tileSize), 0.0F);
            GlStateManager.translate((float)(-DungeonMap.mapX - (map.mapBorder.getValue() != DungeonMap.borders.None ? borderSize : 0)), (float)(-DungeonMap.mapY - (map.mapBorder.getValue() != DungeonMap.borders.None ? borderSize : 0)), 0.0F);
            GlStateManager.popMatrix();
            roomNamesDrawn.clear();
         }
      }
   }

   private static void drawPlayerIcon(EntityPlayer player, int size, int x, int y, int angle) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)x + (float)size / 2.0F, (float)y + (float)size / 2.0F, 0.0F);
      GlStateManager.rotate((float)angle, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate((float)(-x) - (float)size / 2.0F, (float)(-y) - (float)size / 2.0F, 0.0F);
      Gui.drawRect(x, y, x + size, y + size, Color.BLACK.getRGB());
      GlStateManager.color(255.0F, 255.0F, 255.0F);
      GlStateManager.translate(0.0F, 0.0F, 200.0F);
      RenderUtil.drawPlayerIcon(player, size - 2, x + 1, y + 1);
      GlStateManager.translate(0.0F, 0.0F, -200.0F);
      GlStateManager.popMatrix();
   }

   private static boolean drawCheckmark(RoomTile room, int x, int y) {
      ResourceLocation resourceLocation = null;
      switch (room.status) {
         case FAILED:
            resourceLocation = icon_cross;
            break;
         case CLEARED:
            resourceLocation = icon_whiteCheck;
            break;
         case GREEN:
            resourceLocation = icon_check;
            break;
         default:
            return false;
      }

      RenderUtil.drawImage(resourceLocation, (float)(x + (int)((double)tileSize * 0.75)), (float)(y + (int)((double)tileSize * 0.75)), (float)((int)((double)tileSize * 1.5)), (float)((int)((double)tileSize * 1.5)));
      return true;
   }

   private static void drawRoomName(RoomTile roomTile, int x, int y) {
      GlStateManager.pushMatrix();
      DungeonMap mapMod = (DungeonMap)Client.instance.getModuleManager().getModuleByClass(DungeonMap.class);
      if (mapMod.name.getValue() != DungeonMap.names.Null) {
         String name = null;
         if (mapMod.name.getValue() == DungeonMap.names.Important) {
            if (roomTile.room.type == RoomType.YELLOW || roomTile.room.type == RoomType.PUZZLE || roomTile.room.type == RoomType.TRAP) {
               name = (String)RoomLists.shortNames.get(roomTile.room.name);
               if (name == null) {
                  name = roomTile.room.name.replace(" ", "\n");
               }
            }
         } else if (mapMod.name.getValue() == DungeonMap.names.All) {
            if (roomNamesDrawn.contains(roomTile.room.name)) {
               return;
            }

            name = roomTile.room.name.replace(" ", "\n");
            roomNamesDrawn.add(roomTile.room.name);
         }

         if (name != null) {
            GlStateManager.translate(0.0F, 0.0F, 100.0F);
            mc.fontRendererObj.drawString(name, (int)((double)x + (double)tileSize * 1.5 - (double)(mc.fontRendererObj.getStringWidth(name) / 2)), (int)((double)y + (double)tileSize * 1.5), -1);
            GlStateManager.translate(0.0F, 0.0F, -100.0F);
            GlStateManager.popMatrix();
         }
      }
   }

   private static char getColorCode(int value, int orangeAfter, int greenAfter) {
      if (value < orangeAfter) {
         return 'c';
      } else {
         return (char)(value < greenAfter ? '6' : 'a');
      }
   }

   private int getChroma() {
      float hue = (float)(System.currentTimeMillis() % 3000L) / 3000.0F;
      return Color.getHSBColor(hue, 0.75F, 1.0F).getRGB();
   }

   static {
      maxMapPx = tileSize * 23;
      maxMapBlocks = 197;
      borderSize = 2;
      roomNamesDrawn = new ArrayList();
      icon_cross = new ResourceLocation("Melody/dungeons/cross.png");
      icon_whiteCheck = new ResourceLocation("Melody/dungeons/white_check.png");
      icon_check = new ResourceLocation("Melody/dungeons/check.png");
   }
}
