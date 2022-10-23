package xyz.Melody.GUI.DungeonMap;

import java.util.ArrayList;
import java.util.HashSet;
import xyz.Melody.GUI.DungeonMap.elements.MapTile;

public class MapModel {
   public MapTile[][] elements = new MapTile[11][11];
   public ArrayList roomTiles = new ArrayList();
   public HashSet uniqueRooms = new HashSet();
   public boolean allLoaded = true;
   public int totalSecrets = 0;
}
