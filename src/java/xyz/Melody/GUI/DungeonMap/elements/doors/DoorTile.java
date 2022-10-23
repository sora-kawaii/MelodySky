package xyz.Melody.GUI.DungeonMap.elements.doors;

import xyz.Melody.GUI.DungeonMap.elements.MapTile;

public class DoorTile extends MapTile {
   public DoorType type;

   public DoorTile(DoorType type) {
      super(type.color);
      this.type = type;
   }

   public String toString() {
      return "Door (" + this.type.name() + ")";
   }
}
