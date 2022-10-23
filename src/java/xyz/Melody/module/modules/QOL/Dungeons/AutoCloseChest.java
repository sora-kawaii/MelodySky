package xyz.Melody.module.modules.QOL.Dungeons;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoCloseChest extends Module {
   public AutoCloseChest() {
      super("AutoCloseChest", new String[]{"acc"}, ModuleType.Dungeons);
      this.setModInfo("Auto Close Secret Chests.");
   }

   @EventHandler
   public void onGuiDraw(EventTick event) {
      GuiScreen gui = this.mc.currentScreen;
      if (gui instanceof GuiChest && Client.inSkyblock && Client.inDungeons && this.getGuiName(gui).equals("Chest")) {
         this.mc.thePlayer.closeScreen();
      }

   }

   public String getGuiName(GuiScreen gui) {
      return gui instanceof GuiChest ? ((ContainerChest)((GuiChest)gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText() : "";
   }
}
