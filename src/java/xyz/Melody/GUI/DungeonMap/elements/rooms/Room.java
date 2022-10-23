package xyz.Melody.GUI.DungeonMap.elements.rooms;

public class Room {
   public RoomType type;
   public String name;
   public int secrets;

   public Room(RoomType type, String name, int secrets) {
      this.type = type;
      this.name = name;
      this.secrets = secrets;
   }

   public String toString() {
      return this.name;
   }
}
