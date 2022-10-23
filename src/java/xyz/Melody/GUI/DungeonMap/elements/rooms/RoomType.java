package xyz.Melody.GUI.DungeonMap.elements.rooms;

import java.awt.Color;
import xyz.Melody.GUI.DungeonMap.elements.DungeonColors;

public enum RoomType {
   NORMAL(DungeonColors.BROWN),
   BLOOD(DungeonColors.RED),
   ENTRANCE(DungeonColors.GREEN),
   FAIRY(DungeonColors.FAIRY_ROOM),
   YELLOW(DungeonColors.YELLOW),
   RARE(DungeonColors.RARE_ROOM),
   TRAP(DungeonColors.ORANGE),
   PUZZLE(DungeonColors.PUZZLE_ROOM);

   public Color color;

   private RoomType(Color color) {
      this.color = color;
   }
}
