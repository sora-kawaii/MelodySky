package xyz.Melody.module.modules.QOL.Dungeons;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.container.DrawSlotEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoSell extends Module {
   private Option DJ = new Option("DungeonJunks", true);
   private Option salvable = new Option("SalvageAble", false);
   private Option boooom = new Option("SuperBoooom", true);
   private Option rs = new Option("ReviveStones", true);
   private Option runes = new Option("Runes", true);
   private boolean inTradeMenu = false;
   private int tickCount = 0;
   private static final String[] dungeonJunk = new String[]{"Training Weight", "Healing VIII Splash Potion", "Healing 8 Slash Potion", "Premium Flesh", "Mimic Fragment", "Enchanted Rotten Flesh", "Enchanted Bone", "Defuse Kit", "Enchanted Ice", "Optic Lense", "Tripwire Hook", "Button", "Carpet", "Lever", "Journal Entry", "Sign"};

   public AutoSell() {
      super("AutoSell", new String[]{"as"}, ModuleType.Dungeons);
      this.addValues(new Value[]{this.DJ, this.salvable, this.boooom, this.runes, this.rs});
      this.setModInfo("Auto Sell Useless Items.");
   }

   @EventHandler
   public void onTick(EventTick event) {
      if (this.tickCount % 2 == 0 && Client.inSkyblock && this.inTradeMenu && this.mc.currentScreen instanceof GuiChest) {
         List chestInventory = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
         if (((Slot)chestInventory.get(49)).getStack() != null && ((Slot)chestInventory.get(49)).getStack().getItem() != Item.getItemFromBlock(Blocks.barrier)) {
            Iterator var3 = this.mc.thePlayer.inventoryContainer.inventorySlots.iterator();

            while(var3.hasNext()) {
               Slot slot = (Slot)var3.next();
               if (this.shouldSell(slot.getStack())) {
                  this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 45 + slot.slotNumber, 2, 0, this.mc.thePlayer);
                  break;
               }
            }
         }
      }

      ++this.tickCount;
   }

   @EventHandler
   public void onBackgroundRender(EventTick event) {
      GuiScreen gui = this.mc.currentScreen;
      if (gui instanceof GuiChest) {
         Container container = ((GuiChest)gui).inventorySlots;
         if (container instanceof ContainerChest) {
            String chestName = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
            this.inTradeMenu = chestName.equals("Trades");
         }
      }

   }

   @EventHandler
   public void onDrawSlot(DrawSlotEvent event) {
      if (this.inTradeMenu && this.shouldSell(event.slot.getStack())) {
         int x = event.slot.xDisplayPosition;
         int y = event.slot.yDisplayPosition;
         Gui.drawRect(x, y, x + 16, y + 16, (new Color(128, 0, 128, 120)).getRGB());
      }

   }

   private boolean shouldSell(ItemStack item) {
      if (item != null) {
         if ((Boolean)this.salvable.getValue() && AutoSalvage.shouldSalvage(item)) {
            return true;
         }

         if ((Boolean)this.boooom.getValue() && ItemUtils.getSkyBlockID(item).equals("SUPERBOOM_TNT")) {
            return true;
         }

         if ((Boolean)this.rs.getValue() && ItemUtils.getSkyBlockID(item).equals("REVIVE_STONE")) {
            return true;
         }

         if ((Boolean)this.runes.getValue() && item.getDisplayName().contains("Rune")) {
            return true;
         }

         if ((Boolean)this.DJ.getValue()) {
            String[] var2 = dungeonJunk;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               String name = var2[var4];
               if (item.getDisplayName().contains(name)) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}
