package xyz.Melody.GUI.CustomUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import xyz.Melody.Event.EventBus;
import xyz.Melody.GUI.CustomUI.Functions.BigRat;
import xyz.Melody.GUI.CustomUI.Functions.CurrentServerInfo;
import xyz.Melody.GUI.CustomUI.Functions.CustomScoreboard;
import xyz.Melody.GUI.CustomUI.Functions.Day;
import xyz.Melody.GUI.CustomUI.Functions.DungeonMapXYSet;
import xyz.Melody.GUI.CustomUI.Functions.FPS;
import xyz.Melody.GUI.CustomUI.Functions.KeyStrokes;
import xyz.Melody.GUI.CustomUI.Functions.LCPS;
import xyz.Melody.GUI.CustomUI.Functions.MiningOverlay;
import xyz.Melody.GUI.CustomUI.Functions.RCPS;
import xyz.Melody.System.Managers.FileManager;
import xyz.Melody.System.Managers.Manager;

public class HUDManager implements Manager {
   public static boolean loaded = false;
   public static List apis = new ArrayList();

   public void init() {
      apis.add(new Day());
      apis.add(new BigRat());
      apis.add(new DungeonMapXYSet());
      apis.add(new MiningOverlay());
      apis.add(new CurrentServerInfo());
      apis.add(new KeyStrokes());
      apis.add(new CustomScoreboard());
      apis.add(new LCPS());
      apis.add(new RCPS());
      apis.add(new FPS());
      this.readXYE();
      EventBus.getInstance().register(this);
      loaded = true;
   }

   public static List getApis() {
      return apis;
   }

   public static HUDApi getApiByName(String name) {
      Iterator var1 = apis.iterator();

      HUDApi h;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         h = (HUDApi)var1.next();
      } while(!h.getName().equalsIgnoreCase(name));

      return h;
   }

   private void readXYE() {
      List hud = FileManager.read("HUD.txt");
      Iterator var2 = hud.iterator();

      while(var2.hasNext()) {
         String v = (String)var2.next();
         String name = v.split(":")[0];
         String x1 = v.split(":")[1];
         String x = x1.split(":")[0];
         String y1 = v.split(":")[2];
         String y = y1.split(":")[0];
         String e = v.split(":")[3];
         HUDApi m = getApiByName(name);
         if (m != null) {
            m.x = Integer.parseInt(x);
            m.y = Integer.parseInt(y);
            m.setEnabled(Boolean.parseBoolean(e));
         }
      }

   }
}
