package xyz.Melody.module.modules.QOL.Dungeons;

import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.CustomUI.HUDManager;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class DungeonMap extends Module {
   public Mode name;
   public Numbers scale;
   public Mode mapBorder;
   public static int mapX = 0;
   public static int mapY = 0;

   public DungeonMap() {
      super("DungeonMap", new String[]{"acc"}, ModuleType.Dungeons);
      this.name = new Mode("Show Name", DungeonMap.names.values(), DungeonMap.names.All);
      this.scale = new Numbers("Scale", 80.0, 10.0, 200.0, 1.0);
      this.mapBorder = new Mode("Border", DungeonMap.borders.values(), DungeonMap.borders.White);
      this.addValues(new Value[]{this.name, this.scale, this.mapBorder});
      this.setModInfo("Xray Map of Dungeons.");
   }

   @EventHandler
   private void tickXY(EventTick event) {
      mapX = HUDManager.getApiByName("DungeonMap").x;
      mapY = HUDManager.getApiByName("DungeonMap").y;
   }

   public static enum borders {
      None,
      Black,
      White,
      Rainbow;
   }

   public static enum names {
      Null,
      Important,
      All;
   }
}
