package xyz.Melody.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.ClickNew.NewClickGui;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.System.Managers.FileManager;
import xyz.Melody.System.Managers.ModuleManager;
import xyz.Melody.Utils.Helper;

public class Module {
   public String name;
   private String suffix;
   private int color;
   private String[] alias;
   private String modInfo;
   private boolean enabled;
   public boolean enabledOnStartup = false;
   private int key;
   public List values;
   public ModuleType type;
   private boolean removed;
   public Minecraft mc = Minecraft.getMinecraft();
   public ScaledResolution mainWindow;
   public static Random random = new Random();

   public Module(String name, String[] alias, ModuleType type) {
      this.mainWindow = new ScaledResolution(this.mc);
      this.name = name;
      this.alias = alias;
      this.type = type;
      this.suffix = "";
      this.key = 0;
      this.removed = false;
      this.enabled = false;
      this.values = new ArrayList();
      this.modInfo = "";
   }

   public Module(String name, ModuleType type) {
      this.mainWindow = new ScaledResolution(this.mc);
      this.name = name;
      this.alias = new String[0];
      this.type = type;
      this.suffix = "";
      this.key = 0;
      this.removed = false;
      this.enabled = false;
      this.values = new ArrayList();
      this.modInfo = "";
   }

   public String getName() {
      return this.name;
   }

   public String[] getAlias() {
      return this.alias;
   }

   public ModuleType getType() {
      return this.type;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean wasRemoved() {
      return this.removed;
   }

   public void setRemoved(boolean removed) {
      this.removed = removed;
   }

   public String getModInfo() {
      return this.modInfo;
   }

   public void setModInfo(String modInfo) {
      this.modInfo = modInfo;
   }

   public String getSuffix() {
      return this.suffix;
   }

   public void setSuffix(Object obj) {
      String suffix = obj.toString();
      if (suffix.isEmpty()) {
         this.suffix = suffix;
      } else {
         this.suffix = String.format("§7- §f%s§7", EnumChatFormatting.GRAY + suffix);
      }

   }

   public void setEnabled(boolean enabled) {
      if (!Client.instance.authManager.verified && this.getName() != "ClickGui" && this.getName() != "ClientCommands") {
         if (this.mc.theWorld != null && this.mc.thePlayer != null && this.mc.currentScreen instanceof NewClickGui) {
            Helper.sendMessage("MelodySky Will Not Work Cause of Failed to Verify Your UUID.");
         }

         this.enabled = false;
      } else {
         this.enabled = enabled;
         if (enabled) {
            this.onEnable();
            EventBus.getInstance().register(this);
            this.regFML(this);
            if (ModuleManager.loaded && this.getType() != ModuleType.QOL && this.getType() != ModuleType.Swapping && this.getName() != "ClickGui") {
               if (this.getType() == ModuleType.Macros) {
                  Helper.sendMessage("[Macro] " + EnumChatFormatting.DARK_AQUA + this.getName() + EnumChatFormatting.GRAY + " Now" + EnumChatFormatting.GREEN + " Enabled" + EnumChatFormatting.GRAY + ".");
               }

               this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
               NotificationPublisher.queue("Module", this.getName() + " Enabled", NotificationType.INFO, 1000);
            }
         } else {
            EventBus.getInstance().unregister(this);
            this.unregFML(this);
            if (ModuleManager.loaded && this.getType() != ModuleType.QOL && this.getType() != ModuleType.Swapping && this.getName() != "ClickGui") {
               if (this.getType() == ModuleType.Macros) {
                  Helper.sendMessage("[Macro] " + EnumChatFormatting.DARK_AQUA + this.getName() + EnumChatFormatting.GRAY + " Now" + EnumChatFormatting.RED + " Disabled" + EnumChatFormatting.GRAY + ".");
               }

               this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 0.8F));
               NotificationPublisher.queue("Module", this.getName() + " Disabled", NotificationType.INFO, 1000);
            }

            this.onDisable();
         }

      }
   }

   private void regFML(Object obj) {
      MinecraftForge.EVENT_BUS.register(obj);
   }

   private void unregFML(Object obj) {
      MinecraftForge.EVENT_BUS.unregister(obj);
   }

   public void setColor(int color) {
      this.color = color;
   }

   public int getColor() {
      return this.color;
   }

   protected void addValues(Value... values) {
      Value[] var5 = values;
      int var4 = values.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Value value = var5[var3];
         this.values.add(value);
      }

   }

   public List getValues() {
      return this.values;
   }

   public int getKey() {
      return this.key;
   }

   public void setKey(int key) {
      this.key = key;
      String content = "";
      Client.instance.getModuleManager();

      Module m;
      for(Iterator var4 = ModuleManager.getModules().iterator(); var4.hasNext(); content = content + String.format("%s:%s%s", m.getName(), Keyboard.getKeyName(m.getKey()), System.lineSeparator())) {
         m = (Module)var4.next();
      }

      FileManager.save("Binds.txt", content, false);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void makeCommand() {
      if (this.values.size() > 0) {
         String options = "";
         String other = "";
         Iterator var4 = this.values.iterator();

         Value v;
         while(var4.hasNext()) {
            v = (Value)var4.next();
            if (!(v instanceof Mode)) {
               if (options.isEmpty()) {
                  options = options + v.getName();
               } else {
                  options = options + String.format(", %s", v.getName());
               }
            }
         }

         var4 = this.values.iterator();

         while(true) {
            do {
               if (!var4.hasNext()) {
                  Client.instance.getCommandManager().add(new Module$1(this, this.name, this.alias, String.format("%s%s", options.isEmpty() ? "" : String.format("%s,", options), other.isEmpty() ? "" : String.format("%s", other)), "Setup this module"));
                  return;
               }

               v = (Value)var4.next();
            } while(!(v instanceof Mode));

            Mode mode = (Mode)v;
            Enum[] modes;
            int length = (modes = mode.getModes()).length;

            for(int i = 0; i < length; ++i) {
               Enum e = modes[i];
               if (other.isEmpty()) {
                  other = other + e.name().toLowerCase();
               } else {
                  other = other + String.format(", %s", e.name().toLowerCase());
               }
            }
         }
      }
   }
}
