package xyz.Melody;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MouseHelper;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.CustomUI.HUDManager;
import xyz.Melody.GUI.DungeonMap.MapController;
import xyz.Melody.GUI.DungeonMap.MapView;
import xyz.Melody.System.IRC.IRC;
import xyz.Melody.System.Managers.CommandManager;
import xyz.Melody.System.Managers.FileManager;
import xyz.Melody.System.Managers.FriendManager;
import xyz.Melody.System.Managers.ModuleManager;
import xyz.Melody.System.Managers.SkyblockListener;
import xyz.Melody.System.Managers.Auctions.AhBzManager;
import xyz.Melody.System.Managers.Authentication.AuthManager;
import xyz.Melody.System.Managers.Authentication.UserObj;
import xyz.Melody.System.Managers.Dungeons.DungeonListener;
import xyz.Melody.Utils.ReflectionUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.FMLModules.AlertsListener;
import xyz.Melody.module.FMLModules.ChatMonitor;
import xyz.Melody.module.FMLModules.DungeonAssist;
import xyz.Melody.module.FMLModules.IRCKeepAlive;
import xyz.Melody.module.modules.QOL.Swappings.ItemSwitcher;

public class Client {
   public static final String name = "Melody";
   public static final String version = "2.1.4 Build1";
   public static boolean devMode = false;
   public static boolean firstMenu;
   public static boolean firstLaunch = false;
   public static Client instance = new Client();
   public static Minecraft mc = Minecraft.getMinecraft();
   public Logger logger = LogManager.getLogger();
   public KeyBinding alt = new KeyBinding("key.command", 56, "key.categories.misc");
   private ModuleManager modulemanager;
   private CommandManager commandmanager;
   private FriendManager friendmanager;
   private HUDManager hudmanager;
   public DungeonListener dungeonUtils;
   public static String playerName = "";
   public static String userUUID = "";
   public static boolean ircNotVerified = false;
   public String[] rat = new String[]{"                   ..----.._                  ", "                .' .--.    '-.(O)_            ", "    '-.__.-'''=:|  ,  _)_ |__ . c'-..        ", "                 ''------'---''---'-'         "};
   public AuthManager authManager;
   public static boolean isMMD;
   public SkyblockArea sbArea;
   public boolean slayerBossSpawned;
   public static boolean stopping;
   private static boolean isUngrabbed = false;
   private static MouseHelper oldMouseHelper;
   private static boolean doesGameWantUngrab;
   public IRC irc;
   public Thread ircThread;
   public Thread ircConnectionThread;
   public boolean ircExeption;
   public static boolean vanillaMenu;
   public static boolean clientChat;
   public static boolean inDungeons;
   public static boolean inSkyblock;
   public static boolean pickaxeAbilityReady;

   public void start() {
      stopping = false;
      this.logger.info("[Melody] [CONSOLE] Starting Client Module Thread.");
      this.logger.info("[Melody] [AUTHENTICATION] Verifing User.");
      userUUID = mc.getSession().getProfile().getId().toString();
      this.authManager = new AuthManager(new UserObj(mc.getSession().getUsername(), mc.getSession().getProfile().getId().toString(), mc.getSession().getToken()));
      this.authManager.init();
      this.logger.info("[Melody] [AUTHENTICATION] User Verified: " + this.authManager.verified);
      (new SkyblockListener()).init();
      this.initFMLFeature();
      playerName = mc.getSession().getUsername();
      firstMenu = true;
      inDungeons = false;
      inSkyblock = false;
      this.slayerBossSpawned = false;
      FileManager.init();
      this.logger.info("[Melody] [CONSOLE] FileManager Initialized.");
      this.commandmanager = new CommandManager();
      this.commandmanager.init();
      this.logger.info("[Melody] [CONSOLE] CommandManager Initialized.");
      this.friendmanager = new FriendManager();
      this.friendmanager.init();
      this.logger.info("[Melody] [CONSOLE] FriendManager Initialized.");
      UISettings.init();
      UISettings.chatBackground = true;
      UISettings.chatTextShadow = true;
      UISettings.scoreboardBackground = true;
      this.readCustomName();
      this.readMenuMode();
      this.readUISettings();
      this.logger.info("[Melody] [CONSOLE] UI Settings Loaded.");
      this.hudmanager = new HUDManager();
      this.hudmanager.init();
      this.logger.info("[Melody] [CONSOLE] HUDManager Initialized.");
      this.ircConnectionThread = new Thread(() -> {
         this.irc = new IRC();
         this.irc.start();
      }, "Melody-IRC Connection Thread");
      this.ircConnectionThread.start();
      this.logger.info("[Melody] [CONSOLE] IRC Thread Started.");
      this.dungeonUtils = new DungeonListener();
      this.dungeonUtils.init();
      EventBus.getInstance().register(new MapController());
      EventBus.getInstance().register(new MapView());
      this.logger.info("[Melody] [CONSOLE] Dungeon System Initialized.");
      this.modulemanager = new ModuleManager();
      this.modulemanager.init();
      this.logger.info("[Melody] [CONSOLE] ModuleManager Initialized.");
      AhBzManager.registerTimer();
      this.sbArea = new SkyblockArea();
      isMMD = false;
      this.logger.info("[Melody] [CONSOLE] Client Module Thread Started.");
   }

   private void initFMLFeature() {
      this.logger.info("[Melody] [CONSOLE] Initializat FML Modules.");
      MinecraftForge.EVENT_BUS.register(this);
      MinecraftForge.EVENT_BUS.register(new DungeonAssist());
      MinecraftForge.EVENT_BUS.register(new AlertsListener());
      MinecraftForge.EVENT_BUS.register(new ChatMonitor());
      MinecraftForge.EVENT_BUS.register(new IRCKeepAlive());
   }

   public static void rightClick() {
      if (!ReflectionUtils.invoke(mc.getClass(), "func_147121_ag")) {
         ReflectionUtils.invoke(mc.getClass(), "rightClickMouse");
      }

   }

   public static void leftClick() {
      if (!ReflectionUtils.invoke(mc.getClass(), "func_147116_af")) {
         ReflectionUtils.invoke(mc.getClass(), "clickMouse");
      }

   }

   public static void drawTitle(String title, EnumChatFormatting color) {
      mc.ingameGUI.displayTitle(color + title, (String)null, 0, 2000, 10);
   }

   public static void middleClick() {
      if (!ReflectionUtils.invoke(mc.getClass(), "func_147112_ai")) {
         ReflectionUtils.invoke(mc.getClass(), "middleClickMouse");
      }

   }

   public HUDManager getHudmanager() {
      return this.hudmanager;
   }

   public ModuleManager getModuleManager() {
      return this.modulemanager;
   }

   public CommandManager getCommandManager() {
      return this.commandmanager;
   }

   public void stop() {
      this.logger.info("[Melody] [CONSOLE] Shutting Down...");
      this.logger.info("[Melody] [CONSOL] Quitting IRC...");
      if (this.irc != null) {
         this.irc.sendMessage("CLOSE");
      }

      stopping = true;
      this.logger.info("[Melody] [CONSOL] Reader and Writer Closed.");
      this.logger.info("[Melody] [CONSOL] IRC Thread Stopped.");
      this.logger.info("[Melody] [Config] Save Settings...");
      this.saveConfig();
      this.saveMenuMode();
      this.saveUISettings();
      this.logger.info("[Melody] [Config] Config Saved!");
      this.logger.info("[Melody] [CONSOLE] Client Module Thread Closed!");
   }

   public static void ungrabMouse() {
      if (mc.inGameHasFocus && !isUngrabbed) {
         if (oldMouseHelper == null) {
            oldMouseHelper = mc.mouseHelper;
         }

         mc.gameSettings.pauseOnLostFocus = false;
         doesGameWantUngrab = !Mouse.isGrabbed();
         oldMouseHelper.ungrabMouseCursor();
         mc.inGameHasFocus = true;
         mc.mouseHelper = new MouseHelper() {
            public void mouseXYChange() {
            }

            public void grabMouseCursor() {
               Client.doesGameWantUngrab = false;
            }

            public void ungrabMouseCursor() {
               Client.doesGameWantUngrab = true;
            }
         };
         isUngrabbed = true;
      }
   }

   public static void regrabMouse() {
      if (isUngrabbed) {
         mc.mouseHelper = oldMouseHelper;
         if (!doesGameWantUngrab) {
            mc.mouseHelper.grabMouseCursor();
         }

         oldMouseHelper = null;
         isUngrabbed = false;
      }
   }

   public void readConfig() {
      this.logger.info("[Melody] [CONSOLE] Start Loading Configs.");
      this.readCustomIS();
      this.readBinds();
      this.readEnabledMods();
      this.readModValues();
      this.readUISettings();
      this.logger.info("[Melody] [CONSOLE] Config Loaded Successfully.");
   }

   private void readMenuMode() {
      this.logger.info("[Melody] [Config] Loading MainMenu Settings.");
      List sets = FileManager.read("VanillaMenu.txt");
      Iterator var2 = sets.iterator();

      while(var2.hasNext()) {
         String sb = (String)var2.next();
         if (sb.equals("true")) {
            vanillaMenu = true;
         }

         if (sb.equals("false")) {
            vanillaMenu = false;
         }
      }

      this.logger.info("[Melody] [Config] Vanilla Menu: " + vanillaMenu + ".");
   }

   public void saveMenuMode() {
      String enabled = String.valueOf(vanillaMenu);
      FileManager.save("VanillaMenu.txt", enabled, false);
      this.logger.info("[Melody] [Config] MainMenu Mode Saved!");
   }

   private void readCustomName() {
      this.logger.info("[Melody] [Config] Loading MainMenu Settings.");
      List sets = FileManager.read("CustomName.txt");

      String sb;
      for(Iterator var2 = sets.iterator(); var2.hasNext(); playerName = sb) {
         sb = (String)var2.next();
      }

      this.logger.info("[Melody] [Config] Vanilla Menu: " + vanillaMenu + ".");
   }

   public void saveCustomName() {
      String enabled = playerName;
      FileManager.save("CustomName.txt", enabled, false);
      this.logger.info("[Melody] [Config] MainMenu Mode Saved!");
   }

   private void readUISettings() {
      this.logger.info("[Melody] [Config] Loading UI Settings.");
      List sets = FileManager.read("UI.txt");
      Iterator var2 = sets.iterator();

      while(var2.hasNext()) {
         switch ((String)var2.next()) {
            case "chatTextShadow":
               UISettings.chatTextShadow = false;
               break;
            case "chatBackground":
               UISettings.chatBackground = false;
               break;
            case "scoreboardBackground":
               UISettings.scoreboardBackground = false;
         }
      }

      this.logger.info("[Melody] [Config] Succefully Loaded UI Settings!");
   }

   public void saveUISettings() {
      String enabled = "";
      Iterator var2 = UISettings.settings.iterator();

      while(var2.hasNext()) {
         switch ((String)var2.next()) {
            case "chatTextShadow":
               if (!UISettings.chatTextShadow) {
                  enabled = enabled + String.format("%s%s", "chatTextShadow", System.lineSeparator());
               }
               break;
            case "chatBackground":
               if (!UISettings.chatBackground) {
                  enabled = enabled + String.format("%s%s", "chatBackground", System.lineSeparator());
               }
               break;
            case "scoreboardBackground":
               if (!UISettings.scoreboardBackground) {
                  enabled = enabled + String.format("%s%s", "scoreboardBackground", System.lineSeparator());
               }
         }
      }

      FileManager.save("UI.txt", enabled, false);
      this.logger.info("[Melody] [Config] UI Settings Saved!");
   }

   private void readCustomIS() {
      this.logger.info("[Melody] [Config] Loading Custom ItemSwitcher Values.");
      List cis = FileManager.read("CustomIS.txt");
      Iterator var2 = cis.iterator();

      while(var2.hasNext()) {
         String id = (String)var2.next();
         ItemSwitcher is = (ItemSwitcher)instance.getModuleManager().getModuleByClass(ItemSwitcher.class);
         is.setCustomItemID(id);
         this.logger.info("[Melody] [Config] Set Custom ItemSwitcher ItemID to " + id + " .");
      }

      this.logger.info("[Melody] [Config] Succefully Loaded CustomItemSwitcher!");
   }

   private void readBinds() {
      this.logger.info("[Melody] [Config] Loading Binds.");
      List binds = FileManager.read("Binds.txt");
      Iterator var2 = binds.iterator();

      while(var2.hasNext()) {
         String v = (String)var2.next();
         String name = v.split(":")[0];
         String bind = v.split(":")[1];
         Module m = ModuleManager.getModuleByName(name);
         if (m != null) {
            m.setKey(Keyboard.getKeyIndex(bind.toUpperCase()));
            this.logger.info("[Melody] [Config] " + m.getName() + " bound to " + Keyboard.getKeyIndex(bind.toUpperCase()));
         }
      }

      this.logger.info("[Melody] [Config] Succefully Loaded Binds!");
   }

   private void readEnabledMods() {
      this.logger.info("[Melody] [Config] Loading Enabled Modules.");
      List enabled = FileManager.read("Enabled.txt");
      Iterator var2 = enabled.iterator();

      while(var2.hasNext()) {
         String v = (String)var2.next();
         Module m = ModuleManager.getModuleByName(v);
         if (m != null) {
            m.enabledOnStartup = true;
            this.logger.info("[Melody] [Config] Set " + m.getName() + " EnabledOnStartUp True.");
         }
      }

      this.logger.info("[Melody] [Config] Succefully Loaded Enables!");
   }

   private void readModValues() {
      this.logger.info("[Melody] [Config] Loading Module Values.");
      List vals = FileManager.read("Values.txt");
      Iterator var2 = vals.iterator();

      while(true) {
         String v;
         String values;
         Module m;
         do {
            if (!var2.hasNext()) {
               this.logger.info("[Melody] [Config] Succefully Loaded Module Values.");
               return;
            }

            v = (String)var2.next();
            String name = v.split(":")[0];
            values = v.split(":")[1];
            m = ModuleManager.getModuleByName(name);
         } while(m == null);

         Iterator var7 = m.getValues().iterator();

         while(var7.hasNext()) {
            Value value = (Value)var7.next();
            if (value.getName().equalsIgnoreCase(values)) {
               if (value instanceof Option) {
                  value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                  this.logger.info("[Melody] [Config] " + m.getName() + " : " + value.getName() + " : " + Boolean.parseBoolean(v.split(":")[2]));
               } else if (value instanceof Numbers) {
                  value.setValue(Double.parseDouble(v.split(":")[2]));
                  this.logger.info("[Melody] [Config] " + m.getName() + " : " + value.getName() + " : " + Double.parseDouble(v.split(":")[2]));
               } else {
                  ((Mode)value).setMode(v.split(":")[2]);
               }
            }
         }
      }
   }

   public void saveConfig() {
      String values = "";
      instance.getModuleManager();
      Iterator var2 = ModuleManager.getModules().iterator();

      while(var2.hasNext()) {
         Module m = (Module)var2.next();

         Value v;
         for(Iterator var4 = m.getValues().iterator(); var4.hasNext(); values = values + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator())) {
            v = (Value)var4.next();
         }
      }

      FileManager.save("Values.txt", values, false);
      this.logger.info("[Melody] [Config] Module Value Saved!");
      String enabled = "";
      Iterator var7 = ModuleManager.getModules().iterator();

      while(var7.hasNext()) {
         Module m = (Module)var7.next();
         if (m.isEnabled()) {
            enabled = enabled + String.format("%s%s", m.getName(), System.lineSeparator());
         }
      }

      FileManager.save("Enabled.txt", enabled, false);
      this.logger.info("[Melody] [Config] Enabled Modules Saved!");
   }

   public void log(String string) {
      this.logger.info("[Melody] [Debug] " + string);
   }
}
