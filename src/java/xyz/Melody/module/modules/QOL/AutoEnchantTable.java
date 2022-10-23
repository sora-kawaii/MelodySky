package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoEnchantTable extends Module {
   private Numbers delay = new Numbers("Delay", 150.0, 100.0, 500.0, 1.0);
   private Option chronomatron = new Option("Chronomatron", true);
   private Option ultrasequencer = new Option("Ultrasequencer", true);
   private Option superpairs = new Option("SuperPairsSolver", true);
   private Option bug = new Option("dEBuG", false);
   private TimerUtil timer = new TimerUtil();
   private expType currentType;
   private boolean addedAll;
   private ItemStack[] experimentTableSlots;
   private List chronomatronPattern;
   private int lastChronomatronRound;
   private int chronomatronMouseClicks;
   private Slot[] clickInOrderSlots;
   private ArrayList clickQueue;
   private int windowId;

   public AutoEnchantTable() {
      super("AutoExperiment", new String[]{"enchant"}, ModuleType.QOL);
      this.currentType = AutoEnchantTable.expType.NONE;
      this.addedAll = false;
      this.experimentTableSlots = new ItemStack[54];
      this.chronomatronPattern = new ArrayList();
      this.lastChronomatronRound = 0;
      this.chronomatronMouseClicks = 0;
      this.clickInOrderSlots = new Slot[36];
      this.clickQueue = new ArrayList();
      this.windowId = 0;
      this.addValues(new Value[]{this.delay, this.chronomatron, this.ultrasequencer, this.superpairs, this.bug});
      this.setModInfo("Auto Do Experimentation Table.");
   }

   @EventHandler
   public void onGuiDraw(EventTick event) {
      GuiScreen gui = this.mc.currentScreen;
      if (gui instanceof GuiChest) {
         Container container = ((GuiChest)gui).inventorySlots;
         if (container instanceof ContainerChest) {
            if (this.currentType == AutoEnchantTable.expType.NONE) {
               String chestName = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
               if (chestName.startsWith("Ultrasequencer (")) {
                  this.currentType = AutoEnchantTable.expType.Ultrasequencer;
               } else if (chestName.startsWith("Chronomatron (")) {
                  this.currentType = AutoEnchantTable.expType.Chronomatron;
               } else if (chestName.startsWith("Superpairs (")) {
                  this.currentType = AutoEnchantTable.expType.Superpairs;
               }
            } else if (this.currentType != AutoEnchantTable.expType.NONE && !this.clickQueue.isEmpty() && this.timer.hasReached((double)((Double)this.delay.getValue()).longValue())) {
               this.clickSlot((Slot)this.clickQueue.get(0), true);
            }
         }
      }

   }

   @EventHandler
   public void onTick(EventTick event) {
      EntityPlayerSP player = this.mc.thePlayer;
      if (this.mc.currentScreen instanceof GuiChest) {
         if (player == null) {
            return;
         }

         List invSlots = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
         if (this.currentType == AutoEnchantTable.expType.Ultrasequencer && (Boolean)this.ultrasequencer.getValue()) {
            if (((Slot)invSlots.get(49)).getStack() != null && ((Slot)invSlots.get(49)).getStack().getDisplayName().contains("Remember the pattern!")) {
               this.addedAll = false;

               for(int i = 9; i <= 44; ++i) {
                  if (invSlots.get(i) != null && ((Slot)invSlots.get(i)).getStack() != null) {
                     String itemName = StringUtils.stripControlCodes(((Slot)invSlots.get(i)).getStack().getDisplayName());
                     if (itemName.matches("\\d+")) {
                        int number = Integer.parseInt(itemName);
                        this.clickInOrderSlots[number - 1] = (Slot)invSlots.get(i);
                     }
                  }
               }
            } else if (((Slot)invSlots.get(49)).getStack().getDisplayName().startsWith("§7Timer: §a") && !this.addedAll) {
               this.clickQueue.addAll((Collection)Arrays.stream(this.clickInOrderSlots).filter(Objects::nonNull).collect(Collectors.toList()));
               this.clickInOrderSlots = new Slot[36];
               this.addedAll = true;
            }
         }
      }

   }

   @SubscribeEvent
   public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
      if (this.currentType == AutoEnchantTable.expType.Chronomatron) {
         if (this.mc.currentScreen instanceof GuiChest) {
            GuiChest inventory = (GuiChest)event.gui;
            Container containerChest = inventory.inventorySlots;
            if (containerChest instanceof ContainerChest) {
               EntityPlayerSP player = this.mc.thePlayer;
               List invSlots = containerChest.inventorySlots;
               if ((Boolean)this.chronomatron.getValue() && this.currentType == AutoEnchantTable.expType.Chronomatron && player.inventory.getItemStack() == null && invSlots.size() > 48 && ((Slot)invSlots.get(49)).getStack() != null) {
                  if (((Slot)invSlots.get(49)).getStack().getDisplayName().startsWith("§7Timer: §a") && ((Slot)invSlots.get(4)).getStack() != null) {
                     int round = ((Slot)invSlots.get(4)).getStack().stackSize;
                     int timerSeconds = Integer.parseInt(StringUtils.stripControlCodes(((Slot)invSlots.get(49)).getStack().getDisplayName()).replaceAll("[^\\d]", ""));
                     int i;
                     ItemStack glass;
                     if (round != this.lastChronomatronRound && timerSeconds == round + 2) {
                        this.lastChronomatronRound = round;

                        for(i = 10; i <= 43; ++i) {
                           glass = ((Slot)invSlots.get(i)).getStack();
                           if (glass != null && glass.getItem() == Item.getItemFromBlock(Blocks.stained_hardened_clay)) {
                              this.chronomatronPattern.add(glass.getDisplayName());
                              break;
                           }
                        }
                     }

                     if (this.chronomatronMouseClicks < this.chronomatronPattern.size() && player.inventory.getItemStack() == null) {
                        for(i = 10; i <= 43; ++i) {
                           glass = ((Slot)invSlots.get(i)).getStack();
                           if (glass != null && player.inventory.getItemStack() == null) {
                              Slot glassSlot = (Slot)invSlots.get(i);
                              if (glass.getDisplayName().equals(this.chronomatronPattern.get(this.chronomatronMouseClicks)) && this.timer.hasReached((Double)this.delay.getValue())) {
                                 this.clickSlot(glassSlot, false);
                                 ++this.chronomatronMouseClicks;
                                 break;
                              }
                           }
                        }
                     }
                  } else if (((Slot)invSlots.get(49)).getStack().getDisplayName().equals("§aRemember the pattern!")) {
                     this.chronomatronMouseClicks = 0;
                  }
               }
            }
         }

      }
   }

   @SubscribeEvent
   public void onTooltip(ItemTooltipEvent event) {
      if (Client.inSkyblock) {
         if ((Boolean)this.superpairs.getValue()) {
            if (event.toolTip != null) {
               ItemStack item = event.itemStack;
               EntityPlayerSP player = this.mc.thePlayer;
               if (this.mc.currentScreen instanceof GuiChest) {
                  ContainerChest chest = (ContainerChest)player.openContainer;
                  IInventory inv = chest.getLowerChestInventory();
                  String chestName = inv.getDisplayName().getUnformattedText();
                  if (chestName.contains("Superpairs (")) {
                     if (Item.getIdFromItem(item.getItem()) != 95) {
                        return;
                     }

                     if (item.getDisplayName().contains("Click any button") || item.getDisplayName().contains("Click a second button") || item.getDisplayName().contains("Next button is instantly rewarded") || item.getDisplayName().contains("Stained Glass")) {
                        Slot slot = ((GuiChest)this.mc.currentScreen).getSlotUnderMouse();
                        ItemStack itemStack = this.experimentTableSlots[slot.getSlotIndex()];
                        if (itemStack == null) {
                           return;
                        }

                        String itemName = itemStack.getDisplayName();
                        if (event.toolTip.stream().anyMatch((x) -> {
                           return StringUtils.stripControlCodes(x).equals(StringUtils.stripControlCodes(itemName));
                        })) {
                           return;
                        }

                        event.toolTip.removeIf((x) -> {
                           x = StringUtils.stripControlCodes(x);
                           return x.equals("minecraft:stained_glass") ? true : x.startsWith("NBT: ");
                        });
                        event.toolTip.add(itemName);
                        event.toolTip.add(itemStack.getItem().getRegistryName());
                     }
                  }
               }

            }
         }
      }
   }

   @EventHandler
   public void onSuperPairsTick(EventTick event) {
      if ((Boolean)this.superpairs.getValue()) {
         if (this.mc.currentScreen instanceof GuiChest) {
            List invSlots = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
            if (this.currentType == AutoEnchantTable.expType.Superpairs) {
               for(int i = 0; i < 53; ++i) {
                  ItemStack itemStack = ((Slot)invSlots.get(i)).getStack();
                  if (itemStack != null) {
                     String itemName = itemStack.getDisplayName();
                     if (Item.getIdFromItem(itemStack.getItem()) != 95 && Item.getIdFromItem(itemStack.getItem()) != 160 && !itemName.contains("Instant Find") && !itemName.contains("Gained +")) {
                        if (itemName.contains("Enchanted Book")) {
                           itemName = (String)itemStack.getTooltip(this.mc.thePlayer, false).get(3);
                        }

                        if (itemStack.stackSize > 1) {
                           itemName = itemStack.stackSize + " " + itemName;
                        }

                        if (this.experimentTableSlots[i] == null) {
                           this.experimentTableSlots[i] = itemStack.copy().setStackDisplayName(itemName);
                        }
                     }
                  }
               }
            }
         }

      }
   }

   @SubscribeEvent
   public void onGuiDrawn(GuiScreenEvent.BackgroundDrawnEvent event) {
      if ((Boolean)this.superpairs.getValue()) {
         if (this.mc.currentScreen instanceof GuiChest) {
            GuiChest inventory = (GuiChest)event.gui;
            Container containerChest = inventory.inventorySlots;
            if (containerChest instanceof ContainerChest) {
               List invSlots = containerChest.inventorySlots;
               int chestSize = inventory.inventorySlots.inventorySlots.size();
               if (this.currentType == AutoEnchantTable.expType.Superpairs) {
                  HashMap matches = new HashMap();

                  for(int i = 0; i < 53; ++i) {
                     ItemStack itemStack = this.experimentTableSlots[i];
                     if (itemStack != null) {
                        String itemName = itemStack.getDisplayName();
                        String keyName = itemName + itemStack.getUnlocalizedName();
                        matches.computeIfAbsent(keyName, (k) -> {
                           return new HashSet();
                        });
                        ((HashSet)matches.get(keyName)).add(i);
                     }
                  }

                  Color[] colors = new Color[]{new Color(255, 0, 0, 100), new Color(0, 0, 255, 100), new Color(100, 179, 113, 100), new Color(255, 114, 255, 100), new Color(255, 199, 87, 100), new Color(119, 105, 198, 100), new Color(135, 199, 112, 100), new Color(240, 37, 240, 100), new Color(178, 132, 190, 100), new Color(63, 135, 163, 100), new Color(146, 74, 10, 100), new Color(255, 255, 255, 100), new Color(217, 252, 140, 100), new Color(255, 82, 82, 100)};
                  Iterator colorIterator = Arrays.stream(colors).iterator();
                  matches.forEach((itemNamex, slotSet) -> {
                     if (slotSet.size() >= 2) {
                        ArrayList slots = new ArrayList();
                        slotSet.forEach((slotNum) -> {
                           slots.add(invSlots.get(slotNum));
                        });
                        Color color = (Color)colorIterator.next();
                        slots.forEach((slot) -> {
                           RenderUtil.drawOnSlot(chestSize, slot.xDisplayPosition, slot.yDisplayPosition, color.getRGB());
                        });
                     }
                  });
               }
            }
         }

      }
   }

   @SubscribeEvent
   public void onGuiOpen(GuiOpenEvent event) {
      this.experimentTableSlots = new ItemStack[54];
   }

   @EventHandler
   public void onGuiClosed(EventTick event) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         if (!(this.mc.currentScreen instanceof GuiChest)) {
            this.currentType = AutoEnchantTable.expType.NONE;
            this.addedAll = false;
            this.chronomatronMouseClicks = 0;
            this.lastChronomatronRound = 0;
            this.chronomatronPattern.clear();
            this.clickInOrderSlots = new Slot[36];
            this.clickQueue.clear();
         }

      }
   }

   private void clickSlot(Slot slot, boolean remove) {
      this.clickSlot(slot, 2, 0, remove);
   }

   private void clickSlot(Slot slot, int clickButton, int clickMode, boolean remove) {
      this.windowId = this.mc.thePlayer.openContainer.windowId;
      this.mc.playerController.windowClick(this.windowId, slot.slotNumber, clickButton, clickMode, this.mc.thePlayer);
      if ((Boolean)this.bug.getValue()) {
         Helper.sendMessage("Clicked: " + slot.slotNumber);
      }

      this.timer.reset();
      if (remove) {
         this.clickQueue.remove(slot);
      }

   }

   static enum expType {
      NONE,
      Chronomatron,
      Ultrasequencer,
      Superpairs;
   }
}
