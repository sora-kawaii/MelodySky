package xyz.Melody.GUI.DungeonMap.elements;

import java.awt.Color;

public abstract class DungeonColors {
   public static Color RED;
   public static Color BROWN;
   public static Color DARK_BROWN;
   public static Color BLACK;
   public static Color YELLOW;
   public static Color ORANGE;
   public static Color FAIRY_ROOM;
   public static Color PUZZLE_ROOM;
   public static Color GREEN;
   public static Color GRAY_ROOM;
   public static Color RARE_ROOM;
   public static Color WITHER_DOOR;

   static {
      RED = Color.RED;
      BROWN = new Color(9127187);
      DARK_BROWN = new Color(4924683);
      BLACK = Color.BLACK;
      YELLOW = new Color(16703232);
      ORANGE = new Color(14188340);
      FAIRY_ROOM = new Color(15629722);
      PUZZLE_ROOM = new Color(7733637);
      GREEN = new Color(32768);
      GRAY_ROOM = new Color(3684408);
      RARE_ROOM = Color.BLUE;
      WITHER_DOOR = DARK_BROWN;
   }
}
