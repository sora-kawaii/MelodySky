package xyz.Melody.GUI.DungeonMap.elements.rooms;

import xyz.Melody.GUI.DungeonMap.elements.DungeonColors;
import xyz.Melody.GUI.DungeonMap.elements.MapTile;

public class Separator extends MapTile {
   public static Separator GENERIC = new Separator();

   public Separator() {
      super(DungeonColors.BROWN);
   }

   public String toString() {
      return "Room Separator";
   }
}
