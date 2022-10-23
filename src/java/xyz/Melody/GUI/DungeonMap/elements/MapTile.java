package xyz.Melody.GUI.DungeonMap.elements;

import java.awt.Color;
import xyz.Melody.GUI.DungeonMap.elements.rooms.RoomStatus;

public abstract class MapTile {
   public int color;
   public RoomStatus status;

   public MapTile(Color color) {
      this.status = RoomStatus.UNDISCOVERED;
      this.color = color.getRGB();
   }
}
