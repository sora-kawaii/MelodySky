package xyz.Melody.module.modules.QOL.MainWorld;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPostUpdate;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class MelodyPlayer extends Module {
   public boolean inHarp = false;
   public ArrayList lastInventory = new ArrayList();
   private boolean shab = false;

   public MelodyPlayer() {
      super("AutoMelody", new String[]{"am"}, ModuleType.QOL);
      this.setModInfo("Auto Play Melody.");
   }

   @EventHandler
   private void onTick(EventTick event) {
      if (this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChest && Client.inSkyblock && this.getGuiName(this.mc.currentScreen).startsWith("Harp -") && !this.shab) {
         this.lastInventory.clear();
         this.inHarp = true;
         this.shab = true;
      }

      if (this.mc.currentScreen == null || !(this.mc.currentScreen instanceof GuiChest) || !Client.inSkyblock && !this.getGuiName(this.mc.currentScreen).startsWith("Harp -") && this.shab) {
         this.shab = false;
      }

   }

   @EventHandler
   public void onTick(EventPostUpdate event) {
      if (this.inHarp && this.mc.thePlayer != null) {
         String inventoryName = this.getInventoryName();
         if (inventoryName == null || !inventoryName.startsWith("Harp -")) {
            this.inHarp = false;
            this.shab = false;
         }

         ArrayList thisInventory = new ArrayList();
         Iterator var4 = this.mc.thePlayer.openContainer.inventorySlots.iterator();

         Slot slot;
         while(var4.hasNext()) {
            slot = (Slot)var4.next();
            if (slot.getStack() != null) {
               thisInventory.add(slot.getStack().getItem());
            }
         }

         if (!this.lastInventory.toString().equals(thisInventory.toString())) {
            var4 = this.mc.thePlayer.openContainer.inventorySlots.iterator();

            while(var4.hasNext()) {
               slot = (Slot)var4.next();
               if (slot.getStack() != null && slot.getStack().getItem() instanceof ItemBlock && ((ItemBlock)slot.getStack().getItem()).getBlock() == Blocks.quartz_block) {
                  this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, slot.slotNumber, 2, 0, this.mc.thePlayer);
                  break;
               }
            }
         }

         this.lastInventory.clear();
         this.lastInventory.addAll(thisInventory);
      }
   }

   public String getGuiName(GuiScreen gui) {
      return gui instanceof GuiChest ? ((ContainerChest)((GuiChest)gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText() : "";
   }

   public String getInventoryName() {
      return this.mc.thePlayer != null && this.mc.theWorld != null ? ((Slot)this.mc.thePlayer.openContainer.inventorySlots.get(0)).inventory.getName() : "null";
   }
}
