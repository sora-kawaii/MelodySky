package xyz.Melody.GUI.DungeonMap;

import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.StringUtils;
import xyz.Melody.Client;
import xyz.Melody.GUI.DungeonMap.elements.MapTile;
import xyz.Melody.GUI.DungeonMap.elements.doors.DoorTile;
import xyz.Melody.GUI.DungeonMap.elements.doors.DoorType;
import xyz.Melody.GUI.DungeonMap.elements.rooms.Room;
import xyz.Melody.GUI.DungeonMap.elements.rooms.RoomTile;
import xyz.Melody.GUI.DungeonMap.elements.rooms.RoomType;
import xyz.Melody.GUI.DungeonMap.elements.rooms.Separator;

public class MapScanner {
   private static Minecraft mc = Minecraft.getMinecraft();
   public static int xCorner = -200;
   public static int zCorner = -200;
   public static int halfRoom = 16;
   private static boolean isScanning = false;

   public static MapModel getScan() {
      MapModel map = new MapModel();

      for(int col = 0; col <= 10; ++col) {
         for(int row = 0; row <= 10; ++row) {
            int x = -185 + col * 16;
            int z = -185 + row * 16;
            if (!mc.theWorld.getChunkFromChunkCoords(x >> 4, z >> 4).isLoaded()) {
               Client.instance.log("Chunk at x" + x + " z" + z + "is not loaded");
               map.allLoaded = false;
            }

            if (!isColumnAir(x, z)) {
               boolean rowEven = row % 2 == 0;
               boolean colEven = col % 2 == 0;
               if (rowEven && colEven) {
                  RoomTile roomTile = getRoomTile(x, z);
                  map.elements[row][col] = roomTile;
                  if (roomTile != null && roomTile.room != null) {
                     if (!map.uniqueRooms.contains(roomTile.room)) {
                        map.totalSecrets += roomTile.room.secrets;
                     }

                     map.uniqueRooms.add(roomTile.room);
                  }

                  map.roomTiles.add(roomTile);
               } else if (!rowEven && !colEven) {
                  if (map.elements[row - 1][col - 1] instanceof RoomTile) {
                     map.elements[row][col] = Separator.GENERIC;
                  }
               } else if (isDoor(x, z)) {
                  map.elements[row][col] = getDoor(x, z);
               } else {
                  MapTile tileToCheck = map.elements[rowEven ? row : row - 1][rowEven ? col - 1 : col];
                  if (tileToCheck instanceof RoomTile) {
                     if (((RoomTile)tileToCheck).room.type == RoomType.ENTRANCE) {
                        map.elements[row][col] = new DoorTile(DoorType.ENTRANCE);
                     } else {
                        map.elements[row][col] = Separator.GENERIC;
                     }
                  }
               }
            }
         }
      }

      return map;
   }

   @Nullable
   private static RoomTile getRoomTile(int x, int z) {
      int core = getCore(x, z);
      Room room = (Room)MapController.rooms.get(core);
      return room == null ? null : new RoomTile(room);
   }

   private static boolean isDoor(int x, int z) {
      boolean xPlus4 = isColumnAir(x + 4, z);
      boolean xMinus4 = isColumnAir(x - 4, z);
      boolean zPlus4 = isColumnAir(x, z + 4);
      boolean zMinus4 = isColumnAir(x, z - 4);
      return xPlus4 && xMinus4 && !zPlus4 && !zMinus4 || !xPlus4 && !xMinus4 && zPlus4 && zMinus4;
   }

   private static DoorTile getDoor(int x, int z) {
      IBlockState blockState = mc.theWorld.getBlockState(new BlockPos(x, 69, z));
      Block block = blockState.getBlock();
      DoorType type = null;
      if (block == Blocks.coal_block) {
         type = DoorType.WITHER;
      }

      if (block == Blocks.monster_egg) {
         type = DoorType.ENTRANCE;
      }

      if (block == Blocks.stained_hardened_clay && Blocks.stained_hardened_clay.getMetaFromState(blockState) == 14) {
         type = DoorType.BLOOD;
      }

      if (type == null) {
         type = DoorType.NORMAL;
      }

      return new DoorTile(type);
   }

   public static int getCore(int x, int z) {
      ArrayList blocks = new ArrayList();

      for(int y = 140; y >= 12; --y) {
         int id = Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
         if (id != 5 && id != 54) {
            blocks.add(id);
         }
      }

      return StringUtils.join(blocks.toArray()).hashCode();
   }

   private static boolean isColumnAir(int x, int z) {
      for(int y = 12; y < 140; ++y) {
         if (!mc.theWorld.isAirBlock(new BlockPos(x, y, z))) {
            return false;
         }
      }

      return true;
   }

   private static void setBlock(Block block, int x, int z) {
      mc.theWorld.setBlockState(new BlockPos(x, 255, z), block.getDefaultState());
   }
}
