package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class CombindBooks extends Module {
   private static final Map books = new ConcurrentHashMap();
   private static boolean threadRunning;
   private Numbers delay = new Numbers("Delay", 200.0, 100.0, 1000.0, 10.0);

   public CombindBooks() {
      super("CombindBooks", new String[]{"cb"}, ModuleType.QOL);
      this.addValues(new Value[]{this.delay});
      this.setColor((new Color(158, 205, 125)).getRGB());
      this.setModInfo("Auto Combind Enchant Books.");
   }

   @SubscribeEvent
   public void onOpenGui(GuiOpenEvent event) {
      books.clear();
      threadRunning = false;
   }

   @EventHandler
   public void onGuiDraw(EventTick event) {
      GuiScreen gui = this.mc.currentScreen;
      if (gui instanceof GuiChest) {
         Container container = ((GuiChest)gui).inventorySlots;
         if (container instanceof ContainerChest) {
            String chestName = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
            if (chestName.contains("Anvil")) {
               List chestInventory = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
               this.combineBooks(chestInventory);
            }
         }
      }

   }

   public void combineBooks(List invSlots) {
      if (!threadRunning) {
         for(int i = 54; i <= 89; ++i) {
            Slot slot = (Slot)invSlots.get(i);
            if (slot.getStack() != null && slot.getStack().getItem() == Items.enchanted_book) {
               NBTTagCompound extraAttr = ItemUtils.getExtraAttributes(slot.getStack());
               NBTTagCompound enchantments = extraAttr.getCompoundTag("enchantments");
               if (enchantments.getKeySet().size() == 1) {
                  if (books.containsKey(enchantments.toString()) && (Integer)books.get(enchantments.toString()) != i) {
                     if (((Slot)invSlots.get((Integer)books.get(enchantments.toString()))).getStack() != null) {
                        AtomicInteger atomicInteger = new AtomicInteger(i);
                        threadRunning = true;
                        String pair = enchantments.toString();
                        (new Thread(() -> {
                           this.sleep(150 + ((Double)this.delay.getValue()).intValue());
                           if (this.mc.currentScreen != null) {
                              this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, (Integer)books.get(pair), 0, 1, this.mc.thePlayer);
                           }

                           books.remove(pair);
                           this.sleep(300 + ((Double)this.delay.getValue()).intValue());
                           if (((Slot)invSlots.get(atomicInteger.get())).getStack() == null) {
                              atomicInteger.set(this.fix(pair, invSlots));
                           }

                           if (this.mc.currentScreen != null) {
                              this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, atomicInteger.get(), 0, 1, this.mc.thePlayer);
                           }

                           do {
                              if (((Slot)invSlots.get(13)).getStack().getItem() == Items.enchanted_book) {
                                 this.sleep(50);
                                 if (this.mc.currentScreen != null) {
                                    this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 22, 0, 0, this.mc.thePlayer);
                                 }

                                 this.sleep(250 + ((Double)this.delay.getValue()).intValue());
                                 if (this.mc.currentScreen != null) {
                                    this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 13, 0, 1, this.mc.thePlayer);
                                 }

                                 this.sleep(50);
                                 threadRunning = false;
                                 return;
                              }
                           } while(this.mc.currentScreen != null);

                        })).start();
                        return;
                     }

                     books.remove(enchantments.toString());
                  } else {
                     int value;
                     try {
                        value = Integer.parseInt(String.valueOf(enchantments.toString().charAt(enchantments.toString().indexOf(":") + 2)));
                     } catch (Exception var8) {
                        value = Integer.parseInt(String.valueOf(enchantments.toString().charAt(enchantments.toString().indexOf(":") + 1)));
                     }

                     if (!enchantments.toString().contains("feather_falling") && !enchantments.toString().contains("infinite_quiver")) {
                        if (value >= 5) {
                           continue;
                        }
                     } else if (value >= 10) {
                        continue;
                     }

                     books.put(enchantments.toString(), i);
                  }
               }
            }
         }

      }
   }

   private int fix(String name, List invSlots) {
      for(int i = 54; i <= 89; ++i) {
         Slot slot = (Slot)invSlots.get(i);
         if (slot.getStack() != null && slot.getStack().getItem() == Items.enchanted_book) {
            NBTTagCompound extraAttr = ItemUtils.getExtraAttributes(slot.getStack());
            NBTTagCompound enchantments = extraAttr.getCompoundTag("enchantments");
            if (enchantments.getKeySet().size() == 1 && enchantments.toString().equals(name)) {
               return i;
            }
         }
      }

      return 0;
   }

   public void sleep(int sleeptime) {
      try {
         Thread.sleep((long)sleeptime);
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

   }
}
