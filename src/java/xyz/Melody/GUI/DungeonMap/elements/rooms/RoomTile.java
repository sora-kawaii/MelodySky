package xyz.Melody.GUI.DungeonMap.elements.rooms;

import xyz.Melody.GUI.DungeonMap.elements.MapTile;

public class RoomTile extends MapTile {
   public Room room;

   public RoomTile(Room room) {
      super(room.type.color);
      this.room = room;
   }
}
