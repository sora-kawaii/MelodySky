package xyz.Melody.GUI.DungeonMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.GUI.DungeonMap.elements.MapTile;
import xyz.Melody.GUI.DungeonMap.elements.rooms.Room;
import xyz.Melody.GUI.DungeonMap.elements.rooms.RoomStatus;
import xyz.Melody.GUI.DungeonMap.elements.rooms.RoomTile;
import xyz.Melody.GUI.DungeonMap.elements.rooms.RoomType;
import xyz.Melody.System.Managers.Dungeons.DungeonTypes;

public class MapController {
   private static Minecraft mc = Minecraft.getMinecraft();
   public static HashMap rooms = new HashMap();
   public static HashSet uniqueRooms = new HashSet();
   public static MapModel scannedMap;
   public static boolean isScanning = false;
   private static long lastScan = 0L;
   public static int[] startCorner = new int[]{5, 5};
   public static float multiplier = 1.6F;
   public static int roomSize = 16;
   private int ticks = 0;

   public static void calibrate() {
      Client.instance.log("Beginning calibration");
      switch (Client.instance.dungeonUtils.floor) {
         case F1:
            startCorner[0] = 22;
            startCorner[1] = 11;
            break;
         case F2:
         case F3:
            startCorner[0] = 11;
            startCorner[1] = 11;
            break;
         case F4:
            if (scannedMap.roomTiles.size() > 25) {
               startCorner[0] = 5;
               startCorner[1] = 16;
            }
            break;
         default:
            if (scannedMap.roomTiles.size() == 30) {
               startCorner[0] = 16;
               startCorner[1] = 5;
            } else if (scannedMap.roomTiles.size() == 25) {
               startCorner[0] = 11;
               startCorner[1] = 11;
            } else {
               startCorner[0] = 5;
               startCorner[1] = 5;
            }
      }

      if (!Client.instance.dungeonUtils.inFloor(DungeonTypes.F1, DungeonTypes.F2, DungeonTypes.F3, DungeonTypes.M1, DungeonTypes.M2, DungeonTypes.M3) && scannedMap.roomTiles.size() != 24) {
         roomSize = 16;
      } else {
         roomSize = 18;
      }

      multiplier = 32.0F / ((float)roomSize + 4.0F);
      Client.instance.log("Calibration finished");
      Client.instance.log("  startCorner: " + Arrays.toString(startCorner));
      Client.instance.log("  roomSize: " + roomSize);
      Client.instance.log("  multiplier: " + multiplier);
   }

   public static void loadRooms() {
      try {
         ResourceLocation roomsResource = new ResourceLocation("shadyaddons:dungeonscanner/new-rooms.json");
         InputStream inputStream = mc.getResourceManager().getResource(roomsResource).getInputStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
         JsonObject json = (new JsonParser()).parse(reader).getAsJsonObject();
         JsonArray roomsArray = json.getAsJsonArray("rooms");
         Iterator var5 = roomsArray.iterator();

         while(var5.hasNext()) {
            JsonElement roomElement = (JsonElement)var5.next();
            JsonObject roomObject = roomElement.getAsJsonObject();
            String type = roomObject.get("type").getAsString().toUpperCase();
            String name = roomObject.get("name").getAsString();
            int secrets = roomObject.get("secrets").getAsInt();
            JsonArray cores = roomObject.get("cores").getAsJsonArray();
            Room room = new Room(RoomType.valueOf(type), name, secrets);
            Iterator var13 = cores.iterator();

            while(var13.hasNext()) {
               JsonElement core = (JsonElement)var13.next();
               int coreNumber = core.getAsInt();
               rooms.put(coreNumber, room);
            }

            uniqueRooms.add(room);
         }
      } catch (Exception var16) {
         System.out.println("Error loading dungeon rooms");
         var16.printStackTrace();
      }

   }

   public static void printRooms() {
      Iterator var0 = uniqueRooms.iterator();

      while(var0.hasNext()) {
         Room roomTile = (Room)var0.next();
         System.out.println("Name: " + roomTile.name);
         System.out.println("Secrets: " + roomTile.secrets);
         System.out.println("Type: " + roomTile.type.name());
         System.out.println();
      }

   }

   public static void scan() {
      Client.instance.log("Beginning scan");

      try {
         lastScan = System.currentTimeMillis();
         (new Thread(() -> {
            isScanning = true;
            scannedMap = MapScanner.getScan();
            if (scannedMap.allLoaded) {
               calibrate();
            }

            isScanning = false;
         }, "MelodySky-DungeonScanner")).start();
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }

   private static boolean shouldScan() {
      return Client.inDungeons && !isScanning && System.currentTimeMillis() - lastScan >= 250L && Client.instance.dungeonUtils.floor != null && (scannedMap == null || !scannedMap.allLoaded);
   }

   @EventHandler
   public void onTick(EventTick event) {
      if (shouldScan()) {
         scan();
      }

      if (!Client.inDungeons) {
         scannedMap = null;
      }

      if (this.ticks < 10) {
         ++this.ticks;
      } else {
         if (scannedMap != null && scannedMap.allLoaded) {
            (new Thread(MapController::updateRoomStatuses)).start();
         }

         this.ticks = 0;
      }
   }

   public static void updateRoomStatuses() {
      MapData mapData = getMapData();
      if (mapData != null) {
         byte[] mapColors = mapData.colors;
         Client.instance.log("Updating room statuses");
         int startX = startCorner[0] + Math.floorDiv(roomSize, 2);
         int startZ = startCorner[1] + Math.floorDiv(roomSize, 2);
         int increment = Math.floorDiv(roomSize, 2) + 2;

         for(int x = 0; x <= 10; ++x) {
            for(int y = 0; y <= 10; ++y) {
               int mapX = startX + x * increment;
               int mapY = startZ + y * increment;
               if (mapX < 128 && mapY < 128) {
                  MapTile tile = scannedMap.elements[y][x];
                  if (tile != null) {
                     int color = Byte.toUnsignedInt(mapColors[(mapY << 7) + mapX]);
                     switch (color) {
                        case 0:
                        case 85:
                        case 119:
                           tile.status = RoomStatus.UNDISCOVERED;
                           break;
                        case 18:
                           if (!(tile instanceof RoomTile)) {
                              tile.status = RoomStatus.DISCOVERED;
                           } else if (((RoomTile)tile).room.type == RoomType.BLOOD) {
                              tile.status = RoomStatus.DISCOVERED;
                           } else if (((RoomTile)tile).room.type == RoomType.PUZZLE) {
                              tile.status = RoomStatus.FAILED;
                           }
                           break;
                        case 30:
                           if (tile instanceof RoomTile) {
                              if (((RoomTile)tile).room.type == RoomType.ENTRANCE) {
                                 tile.status = RoomStatus.DISCOVERED;
                              } else {
                                 tile.status = RoomStatus.GREEN;
                              }
                           }
                           break;
                        case 34:
                           tile.status = RoomStatus.CLEARED;
                           break;
                        default:
                           tile.status = RoomStatus.DISCOVERED;
                     }
                  }
               }
            }
         }

      }
   }

   private static MapData getMapData() {
      if (mc.thePlayer == null) {
         return null;
      } else if (mc.thePlayer.inventory == null) {
         return null;
      } else {
         ItemStack handheldMap = mc.thePlayer.inventory.getStackInSlot(8);
         return handheldMap != null && handheldMap.getItem() instanceof ItemMap && handheldMap.getDisplayName().contains("Magical Map") ? ((ItemMap)handheldMap.getItem()).getMapData(handheldMap, mc.theWorld) : null;
      }
   }
}
