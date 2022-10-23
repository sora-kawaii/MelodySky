package xyz.Melody.GUI.DungeonMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class RoomLists {
   public static HashMap shortNames = new HashMap() {
      {
         this.put("Old Trap", "Old");
         this.put("New Trap", "New");
         this.put("Boulder", "Box");
         this.put("Creeper Beams", "Beams");
         this.put("Teleport Maze", "Maze");
         this.put("Ice Path", "S.Fish");
         this.put("Ice Fill", "Fill");
         this.put("Tic Tac Toe", "TTT");
         this.put("Water Board", "Water");
         this.put("Bomb Defuse", "Bomb");
         this.put("Three Weirdos", "3 Men");
         this.put("King Midas", "Midas");
         this.put("Shadow Assassin", "SA");
      }
   };
   public static ArrayList slowRooms = new ArrayList(Arrays.asList("Mines", "Spider", "Pit", "Crypt", "Wizard", "Cathedral"));
}
