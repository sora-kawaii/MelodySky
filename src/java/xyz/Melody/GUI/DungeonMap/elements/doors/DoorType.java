package xyz.Melody.GUI.DungeonMap.elements.doors;

import java.awt.Color;
import xyz.Melody.GUI.DungeonMap.elements.DungeonColors;

public enum DoorType {
   WITHER(DungeonColors.WITHER_DOOR),
   BLOOD(DungeonColors.RED),
   ENTRANCE(DungeonColors.GREEN),
   NORMAL(DungeonColors.BROWN);

   public Color color;

   private DoorType(Color color) {
      this.color = color;
   }
}
