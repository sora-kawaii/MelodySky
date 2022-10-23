package xyz.Melody.module.modules.QOL.Dungeons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventClickSlot;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoTerminals extends Module {
   private Option delayy = new Option("NoDelay", false);
   private Numbers delay = new Numbers("ClickDelay", 10.0, 1.0, 200.0, 1.0);
   private ArrayList clickQueue = new ArrayList(28);
   private long lastClickTime = 0L;
   private TerminalType currentTerminal;
   private int targetColorIndex;
   private int windowId;
   private int windowClicks;
   private boolean recalculate;
   private static int lastRowClicked;
   private Option debug;
   private Option maze;
   private Option numbers;
   private Option ca;
   private Option letter;
   private Option color;
   private Option cot;
   private Option sameColor;
   private String letterNeeded;
   static List colorOrder = Arrays.asList(14, 1, 4, 13, 11);
   private boolean foundColor;
   private int correctColor;

   public AutoTerminals() {
      super("AutoTerminals", new String[]{"at"}, ModuleType.Dungeons);
      this.currentTerminal = AutoTerminals.TerminalType.NONE;
      this.targetColorIndex = -1;
      this.windowId = 0;
      this.windowClicks = 0;
      this.recalculate = false;
      this.debug = new Option("Debug", false);
      this.maze = new Option("Maze", true);
      this.numbers = new Option("ClickInOrder", true);
      this.ca = new Option("CorrectAll", true);
      this.letter = new Option("Letter", true);
      this.color = new Option("SelectColor", true);
      this.cot = new Option("ClickOnTime", true);
      this.sameColor = new Option("SameColor", true);
      this.letterNeeded = null;
      this.foundColor = false;
      this.addValues(new Value[]{this.debug, this.delayy, this.delay, this.maze, this.numbers, this.ca, this.letter, this.color, this.cot, this.sameColor});
      this.setModInfo("Auto Do Terminals in F7/M7.");
   }

   @EventHandler
   private void onClickSlot(EventClickSlot event) {
      if ((Boolean)this.debug.getValue()) {
         Helper.sendMessage("WindowID: " + event.getWindowID() + ", SlotNumber: " + event.getSlotNumber() + ", Button: " + event.getButton() + ", Mode: " + event.getMode() + ", Player: " + event.getPlayer());
      }

   }

   @EventHandler
   public void onGuiDraw(EventTick event) {
      GuiScreen gui = this.mc.currentScreen;
      if (gui instanceof GuiChest) {
         Container container = ((GuiChest)gui).inventorySlots;
         if (container instanceof ContainerChest) {
            List invSlots = container.inventorySlots;
            if (this.currentTerminal == AutoTerminals.TerminalType.NONE) {
               String chestName = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
               if (chestName.equals("Navigate the maze!")) {
                  this.currentTerminal = AutoTerminals.TerminalType.MAZE;
               } else if (chestName.equals("Click in order!")) {
                  this.currentTerminal = AutoTerminals.TerminalType.NUMBERS;
               } else if (chestName.equals("Correct all the panes!")) {
                  this.currentTerminal = AutoTerminals.TerminalType.CORRECT_ALL;
               } else if (chestName.startsWith("What starts with: '")) {
                  this.currentTerminal = AutoTerminals.TerminalType.LETTER;
               } else if (chestName.startsWith("Select all the")) {
                  this.currentTerminal = AutoTerminals.TerminalType.COLOR;
               } else if (chestName.startsWith("Click the button on time!")) {
                  this.currentTerminal = AutoTerminals.TerminalType.TIMING;
               } else if (chestName.startsWith("Change all to same color!")) {
                  this.currentTerminal = AutoTerminals.TerminalType.CHANGEATSC;
               }
            }

            if (this.currentTerminal != AutoTerminals.TerminalType.NONE) {
               if (!this.clickQueue.isEmpty() && !this.recalculate) {
                  switch (this.currentTerminal) {
                     case MAZE:
                     case NUMBERS:
                     case CORRECT_ALL:
                        this.clickQueue.removeIf((slot) -> {
                           return ((Slot)invSlots.get(slot.slotNumber)).getHasStack() && ((Slot)invSlots.get(slot.slotNumber)).getStack().getItemDamage() == 5;
                        });
                        break;
                     case LETTER:
                     case COLOR:
                        this.clickQueue.removeIf((slot) -> {
                           return ((Slot)invSlots.get(slot.slotNumber)).getHasStack() && ((Slot)invSlots.get(slot.slotNumber)).getStack().isItemEnchanted();
                        });
                        break;
                     case CHANGEATSC:
                        this.clickQueue.removeIf((slot) -> {
                           return ((Slot)invSlots.get(slot.slotNumber)).getHasStack() && ((Slot)invSlots.get(slot.slotNumber)).getStack().getItemDamage() == this.targetColorIndex;
                        });
                     case TIMING:
                  }
               } else {
                  this.recalculate = this.getClicks((ContainerChest)container);
               }

               if (!this.clickQueue.isEmpty() && System.currentTimeMillis() - this.lastClickTime > ((Double)this.delay.getValue()).longValue()) {
                  this.clickSlot((Slot)this.clickQueue.get(0));
               }
            }
         }
      }

   }

   @EventHandler
   public void onMaxFans(EventTick event) {
      if (!this.foundColor && this.currentTerminal == AutoTerminals.TerminalType.CHANGEATSC) {
         EntityPlayerSP player = this.mc.thePlayer;
         if (this.mc.currentScreen instanceof GuiChest) {
            if (player == null) {
               return;
            }

            ContainerChest chest = (ContainerChest)player.openContainer;
            List invSlots = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
            String chestName = chest.getLowerChestInventory().getDisplayName().getUnformattedText().trim();
            if (chestName.equals("Change all to same color!")) {
               int red = 0;
               int orange = 0;
               int yellow = 0;
               int green = 0;
               int blue = 0;

               int max;
               for(max = 12; max <= 32; ++max) {
                  ItemStack stack = ((Slot)invSlots.get(max)).getStack();
                  if (stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane) && stack.getItemDamage() != 7) {
                     switch (stack.getItemDamage()) {
                        case 1:
                           ++orange;
                        case 2:
                        case 3:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        default:
                           break;
                        case 4:
                           ++yellow;
                           break;
                        case 11:
                           ++blue;
                           break;
                        case 13:
                           ++green;
                           break;
                        case 14:
                           ++red;
                     }
                  }
               }

               max = NumberUtils.max(new int[]{red, orange, yellow, green, blue});
               if (max == red) {
                  this.correctColor = 14;
               } else if (max == orange) {
                  this.correctColor = 1;
               } else if (max == yellow) {
                  this.correctColor = 4;
               } else if (max == green) {
                  this.correctColor = 13;
               } else {
                  this.correctColor = 11;
               }

               this.foundColor = true;
            }
         }

      }
   }

   @EventHandler
   public void onTick(EventTick event) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         if (!(this.mc.currentScreen instanceof GuiChest)) {
            this.reset();
         }

      }
   }

   private void reset() {
      this.currentTerminal = AutoTerminals.TerminalType.NONE;
      this.clickQueue.clear();
      this.windowClicks = 0;
      this.targetColorIndex = -1;
   }

   private boolean getClicks(ContainerChest container) {
      List invSlots = container.inventorySlots;
      String chestName = container.getLowerChestInventory().getDisplayName().getUnformattedText().trim();
      this.clickQueue.clear();
      int purpleSlot;
      Slot slot;
      ItemStack itemStack;
      int startSlot;
      int greenSlot;
      int endSlot;
      Iterator var23;
      int k;
      switch (this.currentTerminal) {
         case MAZE:
            if ((Boolean)this.maze.getValue()) {
               int[] mazeDirection = new int[]{-9, -1, 1, 9};
               boolean[] isStartSlot = new boolean[54];
               endSlot = -1;
               Iterator var34 = invSlots.iterator();

               while(var34.hasNext()) {
                  Slot slot = (Slot)var34.next();
                  ItemStack itemStack;
                  if (slot.inventory != this.mc.thePlayer.inventory && (itemStack = slot.getStack()) != null && itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane)) {
                     if (itemStack.getItemDamage() == 5) {
                        isStartSlot[slot.slotNumber] = true;
                     } else if (itemStack.getItemDamage() == 14) {
                        endSlot = slot.slotNumber;
                     }
                  }
               }

               for(k = 0; k < 54; ++k) {
                  if (isStartSlot[k]) {
                     boolean[] mazeVisited = new boolean[54];
                     startSlot = k;

                     while(startSlot != endSlot) {
                        boolean newSlotChosen = false;
                        int[] var11 = mazeDirection;
                        int var12 = mazeDirection.length;

                        for(int var13 = 0; var13 < var12; ++var13) {
                           int i = var11[var13];
                           int nextSlot = startSlot + i;
                           if (nextSlot >= 0 && nextSlot <= 53 && (i != -1 || startSlot % 9 != 0) && (i != 1 || startSlot % 9 != 8)) {
                              if (nextSlot == endSlot) {
                                 return false;
                              }

                              ItemStack itemStack;
                              if (!mazeVisited[nextSlot] && (itemStack = ((Slot)invSlots.get(nextSlot)).getStack()) != null && itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane) && itemStack.getItemDamage() == 0) {
                                 this.clickQueue.add((Slot)invSlots.get(nextSlot));
                                 startSlot = nextSlot;
                                 mazeVisited[nextSlot] = true;
                                 newSlotChosen = true;
                                 break;
                              }
                           }
                        }

                        if (!newSlotChosen) {
                           System.out.println("Maze calculation aborted");
                           return true;
                        }
                     }
                  }
               }

               return true;
            }
         case NUMBERS:
            if ((Boolean)this.numbers.getValue()) {
               greenSlot = 0;
               Slot[] slotOrder = new Slot[14];

               for(endSlot = 10; endSlot <= 25; ++endSlot) {
                  if (endSlot != 17 && endSlot != 18 && (itemStack = ((Slot)invSlots.get(endSlot)).getStack()) != null && itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane) && itemStack.stackSize < 15) {
                     if (itemStack.getItemDamage() == 14) {
                        slotOrder[itemStack.stackSize - 1] = (Slot)invSlots.get(endSlot);
                     } else if (itemStack.getItemDamage() == 5 && greenSlot < itemStack.stackSize) {
                        greenSlot = itemStack.stackSize;
                     }
                  }
               }

               this.clickQueue.addAll((Collection)Arrays.stream(slotOrder).filter(Objects::nonNull).collect(Collectors.toList()));
               if (this.clickQueue.size() != 14 - greenSlot) {
                  return true;
               }
               break;
            }
         case CORRECT_ALL:
            if ((Boolean)this.ca.getValue()) {
               Iterator var20 = invSlots.iterator();

               while(var20.hasNext()) {
                  Slot slot = (Slot)var20.next();
                  if (slot.inventory != this.mc.thePlayer.inventory && slot.slotNumber >= 9 && slot.slotNumber <= 35 && slot.slotNumber % 9 > 1 && slot.slotNumber % 9 < 7) {
                     ItemStack itemStack = slot.getStack();
                     if (itemStack == null) {
                        return true;
                     }

                     if (itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane) && itemStack.getItemDamage() == 14) {
                        this.clickQueue.add(slot);
                     }
                  }
               }
            }
            break;
         case LETTER:
            if ((Boolean)this.letter.getValue()) {
               if (chestName.length() > chestName.indexOf("'") + 1) {
                  char letter = chestName.charAt(chestName.indexOf("'") + 1);
                  if (this.letterNeeded != String.valueOf(letter)) {
                     this.letterNeeded = String.valueOf(letter);
                  }

                  var23 = invSlots.iterator();

                  while(var23.hasNext()) {
                     slot = (Slot)var23.next();
                     if (slot.inventory != this.mc.thePlayer.inventory) {
                        itemStack = slot.getStack();
                        if (itemStack == null) {
                           return true;
                        }

                        if (!itemStack.isItemEnchanted() && slot.slotNumber >= 9 && slot.slotNumber <= 44 && slot.slotNumber % 9 != 0 && slot.slotNumber % 9 != 8 && StringUtils.stripControlCodes(itemStack.getDisplayName()).startsWith(this.letterNeeded)) {
                           this.clickQueue.add(slot);
                        }
                     }
                  }
               }
               break;
            }
         case COLOR:
            if ((Boolean)this.color.getValue()) {
               String colorNeeded = null;
               EnumDyeColor[] var21 = EnumDyeColor.values();
               endSlot = var21.length;

               for(k = 0; k < endSlot; ++k) {
                  EnumDyeColor color = var21[k];
                  String colorName = color.getName().replaceAll(" ", "_").toUpperCase();
                  if (chestName.contains(colorName)) {
                     colorNeeded = color.getUnlocalizedName();
                     break;
                  }
               }

               if (colorNeeded != null) {
                  Helper.sendMessage(colorNeeded);
                  var23 = invSlots.iterator();

                  while(var23.hasNext()) {
                     slot = (Slot)var23.next();
                     if (slot.inventory != this.mc.thePlayer.inventory && slot.slotNumber >= 9 && slot.slotNumber <= 44 && slot.slotNumber % 9 != 0 && slot.slotNumber % 9 != 8) {
                        itemStack = slot.getStack();
                        if (itemStack == null) {
                           return true;
                        }

                        if (!itemStack.isItemEnchanted() && itemStack.getUnlocalizedName().contains(colorNeeded)) {
                           this.clickQueue.add(slot);
                        }
                     }
                  }
               }
               break;
            }
         case TIMING:
            if ((Boolean)this.cot.getValue() && System.currentTimeMillis() - this.lastClickTime > ((Double)this.delay.getValue()).longValue()) {
               greenSlot = -1;
               purpleSlot = -1;
               endSlot = 0;

               for(k = 1; k < 51; ++k) {
                  ItemStack stack = ((Slot)invSlots.get(k)).getStack();
                  if (stack != null) {
                     EnumDyeColor color = EnumDyeColor.byMetadata(stack.getItemDamage());
                     switch (color) {
                        case PURPLE:
                           if (purpleSlot == -1) {
                              purpleSlot = k % 9;
                           }
                           break;
                        case LIME:
                           Item item3 = stack.getItem();
                           if (item3 == Item.getItemFromBlock(Blocks.stained_glass_pane)) {
                              if (greenSlot == -1) {
                                 greenSlot = k % 9;
                              }
                           } else if (item3 == Item.getItemFromBlock(Blocks.stained_hardened_clay)) {
                              endSlot = k;
                           }
                     }
                  }
               }

               if (purpleSlot != -1 && endSlot != 0 && greenSlot == purpleSlot) {
                  this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, endSlot, 2, 0, this.mc.thePlayer);
                  this.lastClickTime = System.currentTimeMillis();
               }

               return true;
            }
            break;
         case CHANGEATSC:
            if ((Boolean)this.sameColor.getValue() && this.foundColor) {
               ArrayList slotOrder = new ArrayList();

               for(purpleSlot = 12; purpleSlot <= 32; ++purpleSlot) {
                  slot = (Slot)invSlots.get(purpleSlot);
                  itemStack = slot.getStack();
                  if (itemStack != null && itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane) && itemStack.getItemDamage() != 7) {
                     int distance = Math.abs(this.getDiff(itemStack.getItemDamage(), this.correctColor));
                     if (distance != 0) {
                        for(startSlot = 0; startSlot < distance; ++startSlot) {
                           slotOrder.add(slot);
                        }
                     }
                  }
               }

               this.clickQueue.addAll(slotOrder);
            }
      }

      return false;
   }

   public int getDiff(int color, int endColor) {
      int index = colorOrder.indexOf(color);
      int finalIndex = colorOrder.indexOf(endColor);
      if (index != -1 && finalIndex != -1) {
         return finalIndex < index ? finalIndex - index + 5 : finalIndex - index;
      } else {
         return 0;
      }
   }

   private void clickSlot(Slot slot) {
      this.clickSlot(slot, 2, 0);
   }

   private void clickSlot(Slot slot, int clickButton, int clickMode) {
      if (this.windowClicks == 0) {
         this.windowId = this.mc.thePlayer.openContainer.windowId;
      }

      this.mc.playerController.windowClick(this.windowId + this.windowClicks, slot.slotNumber, clickButton, clickMode, this.mc.thePlayer);
      this.lastClickTime = System.currentTimeMillis();
      if ((Boolean)this.delayy.getValue()) {
         ++this.windowClicks;
         this.clickQueue.remove(slot);
      }

   }

   private static enum TerminalType {
      MAZE,
      NUMBERS,
      CORRECT_ALL,
      LETTER,
      COLOR,
      TIMING,
      CHANGEATSC,
      NONE;
   }
}
