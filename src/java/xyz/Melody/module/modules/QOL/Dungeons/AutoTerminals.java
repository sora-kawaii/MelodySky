/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import java.util.ArrayList;
import java.util.Arrays;
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
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventClickSlot;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoTerminals
extends Module {
    private Option<Boolean> delayy = new Option<Boolean>("NoDelay", true);
    private Numbers<Double> delay = new Numbers<Double>("ClickDelay", 120.0, 1.0, 200.0, 1.0);
    private TimerUtil timer = new TimerUtil();
    private ArrayList<Slot> clickQueue = new ArrayList(28);
    private long lastClickTime = 0L;
    public TerminalType currentTerminal = TerminalType.NONE;
    private int windowId = 0;
    private int windowClicks = 0;
    private boolean recalculate = false;
    private Option<Boolean> debug = new Option<Boolean>("Debug", false);
    private Option<Boolean> maze = new Option<Boolean>("Maze", true);
    private Option<Boolean> numbers = new Option<Boolean>("ClickInOrder", true);
    private Option<Boolean> ca = new Option<Boolean>("CorrectAll", true);
    private Option<Boolean> letter = new Option<Boolean>("Letter", true);
    private Option<Boolean> color = new Option<Boolean>("SelectColor", true);
    private Option<Boolean> cot = new Option<Boolean>("ClickOnTime", true);
    private Option<Boolean> sameColor = new Option<Boolean>("SameColor", true);
    private String letterNeeded = null;
    private int targetColorIndex = -1;
    static List<Integer> colorOrder = Arrays.asList(14, 1, 4, 13, 11);
    private boolean foundColor = false;
    private int correctColor;

    public AutoTerminals() {
        super("AutoTerminals", new String[]{"at"}, ModuleType.Dungeons);
        this.addValues(this.debug, this.delayy, this.delay, this.maze, this.numbers, this.ca, this.letter, this.color, this.cot, this.sameColor);
        this.setModInfo("Auto Do Terminals in F7/M7.");
    }

    @EventHandler
    private void onClickSlot(EventClickSlot event) {
        if (((Boolean)this.debug.getValue()).booleanValue()) {
            Helper.sendMessage("WindowID: " + event.getWindowID() + ", SlotNumber: " + event.getSlotNumber() + ", Button: " + event.getButton() + ", Mode: " + event.getMode() + ", Player: " + event.getPlayer());
        }
    }

    @EventHandler
    public void onGuiDraw(EventTick event) {
        Container container;
        GuiScreen gui = this.mc.currentScreen;
        if (gui instanceof GuiChest && (container = ((GuiChest)gui).inventorySlots) instanceof ContainerChest) {
            List<Slot> invSlots = container.inventorySlots;
            if (this.currentTerminal == TerminalType.NONE) {
                String chestName = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (chestName.equals("Navigate the maze!")) {
                    this.currentTerminal = TerminalType.MAZE;
                } else if (chestName.equals("Click in order!")) {
                    this.currentTerminal = TerminalType.NUMBERS;
                } else if (chestName.equals("Correct all the panes!")) {
                    this.currentTerminal = TerminalType.CORRECT_ALL;
                } else if (chestName.startsWith("What starts with: '")) {
                    this.currentTerminal = TerminalType.LETTER;
                } else if (chestName.startsWith("Select all the")) {
                    this.currentTerminal = TerminalType.COLOR;
                } else if (chestName.startsWith("Click the button on time!")) {
                    this.currentTerminal = TerminalType.TIMING;
                } else if (chestName.startsWith("Change all to same color!")) {
                    this.currentTerminal = TerminalType.CHANGEATSC;
                }
            }
            if (this.currentTerminal != TerminalType.NONE) {
                if (this.currentTerminal == TerminalType.TIMING && ((Boolean)this.cot.getValue()).booleanValue()) {
                    this.timingClicks((ContainerChest)container);
                }
                if (this.clickQueue.isEmpty() || this.recalculate) {
                    this.recalculate = this.getClicks((ContainerChest)container);
                } else {
                    switch (this.currentTerminal) {
                        case MAZE: 
                        case NUMBERS: 
                        case CORRECT_ALL: {
                            this.clickQueue.removeIf(slot -> ((Slot)invSlots.get(slot.slotNumber)).getHasStack() && ((Slot)invSlots.get(slot.slotNumber)).getStack().getItemDamage() == 5);
                            break;
                        }
                        case LETTER: 
                        case COLOR: {
                            this.clickQueue.removeIf(slot -> ((Slot)invSlots.get(slot.slotNumber)).getHasStack() && ((Slot)invSlots.get(slot.slotNumber)).getStack().isItemEnchanted());
                            break;
                        }
                        case CHANGEATSC: {
                            this.clickQueue.removeIf(arg_0 -> this.lambda$onGuiDraw$2(invSlots, arg_0));
                            break;
                        }
                    }
                }
                if (!this.clickQueue.isEmpty() && System.currentTimeMillis() - this.lastClickTime > ((Double)this.delay.getValue()).longValue()) {
                    this.clickSlot(this.clickQueue.get(0));
                }
            }
        }
    }

    @EventHandler
    public void onMaxFans(EventTick event) {
        if (this.foundColor || this.currentTerminal != TerminalType.CHANGEATSC) {
            return;
        }
        EntityPlayerSP player = this.mc.thePlayer;
        if (this.mc.currentScreen instanceof GuiChest) {
            if (player == null) {
                return;
            }
            ContainerChest chest = (ContainerChest)player.openContainer;
            List<Slot> invSlots = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
            String chestName = chest.getLowerChestInventory().getDisplayName().getUnformattedText().trim();
            if (chestName.equals("Change all to same color!")) {
                int red = 0;
                int orange = 0;
                int yellow = 0;
                int green = 0;
                int blue = 0;
                block7: for (int i = 12; i <= 32; ++i) {
                    ItemStack stack = invSlots.get(i).getStack();
                    if (stack == null || stack.getItem() != Item.getItemFromBlock(Blocks.stained_glass_pane) || stack.getItemDamage() == 7) continue;
                    switch (stack.getItemDamage()) {
                        case 1: {
                            ++orange;
                            continue block7;
                        }
                        case 4: {
                            ++yellow;
                            continue block7;
                        }
                        case 11: {
                            ++blue;
                            continue block7;
                        }
                        case 13: {
                            ++green;
                            continue block7;
                        }
                        case 14: {
                            ++red;
                        }
                    }
                }
                int max = NumberUtils.max(new int[]{red, orange, yellow, green, blue});
                this.correctColor = max == red ? 14 : (max == orange ? 1 : (max == yellow ? 4 : (max == green ? 13 : 11)));
                this.foundColor = true;
            }
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (!(this.mc.currentScreen instanceof GuiChest)) {
            this.reset();
        }
    }

    private void reset() {
        this.currentTerminal = TerminalType.NONE;
        this.clickQueue.clear();
        this.foundColor = false;
        this.correctColor = 0;
        this.windowClicks = 0;
    }

    private boolean getClicks(ContainerChest container) {
        List invSlots = container.inventorySlots;
        String chestName = container.getLowerChestInventory().getDisplayName().getUnformattedText().trim();
        this.clickQueue.clear();
        switch (this.currentTerminal) {
            case MAZE: {
                if (((Boolean)this.maze.getValue()).booleanValue()) {
                    int[] mazeDirection = new int[]{-9, -1, 1, 9};
                    boolean[] isStartSlot = new boolean[54];
                    int endSlot = -1;
                    for (Slot slot : invSlots) {
                        ItemStack itemStack;
                        if (slot.inventory == this.mc.thePlayer.inventory || (itemStack = slot.getStack()) == null || itemStack.getItem() != Item.getItemFromBlock(Blocks.stained_glass_pane)) continue;
                        if (itemStack.getItemDamage() == 5) {
                            isStartSlot[slot.slotNumber] = true;
                            continue;
                        }
                        if (itemStack.getItemDamage() != 14) continue;
                        endSlot = slot.slotNumber;
                    }
                    for (int slot = 0; slot < 54; ++slot) {
                        if (!isStartSlot[slot]) continue;
                        boolean[] mazeVisited = new boolean[54];
                        int startSlot = slot;
                        while (startSlot != endSlot) {
                            boolean newSlotChosen = false;
                            for (int i : mazeDirection) {
                                ItemStack itemStack;
                                int nextSlot = startSlot + i;
                                if (nextSlot < 0 || nextSlot > 53 || i == -1 && startSlot % 9 == 0 || i == 1 && startSlot % 9 == 8) continue;
                                if (nextSlot == endSlot) {
                                    return false;
                                }
                                if (mazeVisited[nextSlot] || (itemStack = ((Slot)invSlots.get(nextSlot)).getStack()) == null || itemStack.getItem() != Item.getItemFromBlock(Blocks.stained_glass_pane) || itemStack.getItemDamage() != 0) continue;
                                this.clickQueue.add((Slot)invSlots.get(nextSlot));
                                startSlot = nextSlot;
                                mazeVisited[nextSlot] = true;
                                newSlotChosen = true;
                                break;
                            }
                            if (newSlotChosen) continue;
                            System.out.println("Maze calculation aborted");
                            return true;
                        }
                    }
                    return true;
                }
            }
            case NUMBERS: {
                if (((Boolean)this.numbers.getValue()).booleanValue()) {
                    int neededClick = 0;
                    Slot[] slotOrder = new Slot[14];
                    for (int i = 10; i <= 25; ++i) {
                        ItemStack itemStack;
                        if (i == 17 || i == 18 || (itemStack = ((Slot)invSlots.get(i)).getStack()) == null || itemStack.getItem() != Item.getItemFromBlock(Blocks.stained_glass_pane) || itemStack.stackSize >= 15) continue;
                        if (itemStack.getItemDamage() == 14) {
                            slotOrder[itemStack.stackSize - 1] = (Slot)invSlots.get(i);
                            continue;
                        }
                        if (itemStack.getItemDamage() != 5 || neededClick >= itemStack.stackSize) continue;
                        neededClick = itemStack.stackSize;
                    }
                    this.clickQueue.addAll(Arrays.stream(slotOrder).filter(Objects::nonNull).collect(Collectors.toList()));
                    if (this.clickQueue.size() == 14 - neededClick) break;
                    return true;
                }
            }
            case CORRECT_ALL: {
                if (!((Boolean)this.ca.getValue()).booleanValue()) break;
                for (Slot slot : invSlots) {
                    if (slot.inventory == this.mc.thePlayer.inventory || slot.slotNumber < 9 || slot.slotNumber > 35 || slot.slotNumber % 9 <= 1 || slot.slotNumber % 9 >= 7) continue;
                    ItemStack itemStack = slot.getStack();
                    if (itemStack == null) {
                        return true;
                    }
                    if (itemStack.getItem() != Item.getItemFromBlock(Blocks.stained_glass_pane) || itemStack.getItemDamage() != 14) continue;
                    this.clickQueue.add(slot);
                }
                break;
            }
            case LETTER: {
                if (((Boolean)this.letter.getValue()).booleanValue()) {
                    if (chestName.length() <= chestName.indexOf("'") + 1) break;
                    char letter = chestName.charAt(chestName.indexOf("'") + 1);
                    if (this.letterNeeded != String.valueOf(letter)) {
                        this.letterNeeded = String.valueOf(letter);
                    }
                    for (Slot slot : invSlots) {
                        if (slot.inventory == this.mc.thePlayer.inventory) continue;
                        ItemStack itemStack = slot.getStack();
                        if (itemStack == null) {
                            return true;
                        }
                        if (itemStack.isItemEnchanted() || slot.slotNumber < 9 || slot.slotNumber > 44 || slot.slotNumber % 9 == 0 || slot.slotNumber % 9 == 8 || !StringUtils.stripControlCodes(itemStack.getDisplayName()).startsWith(this.letterNeeded)) continue;
                        this.clickQueue.add(slot);
                    }
                    break;
                }
            }
            case COLOR: {
                if (((Boolean)this.color.getValue()).booleanValue()) {
                    String colorNeeded = null;
                    for (EnumDyeColor color : EnumDyeColor.values()) {
                        String colorName = color.getName().replaceAll(" ", "_").toUpperCase();
                        if (!chestName.contains(colorName)) continue;
                        colorNeeded = color.getUnlocalizedName();
                        break;
                    }
                    if (colorNeeded == null) break;
                    Helper.sendMessage(colorNeeded);
                    for (Slot slot : invSlots) {
                        if (slot.inventory == this.mc.thePlayer.inventory || slot.slotNumber < 9 || slot.slotNumber > 44 || slot.slotNumber % 9 == 0 || slot.slotNumber % 9 == 8) continue;
                        ItemStack itemStack = slot.getStack();
                        if (itemStack == null) {
                            return true;
                        }
                        if (itemStack.isItemEnchanted() || !itemStack.getUnlocalizedName().contains(colorNeeded)) continue;
                        this.clickQueue.add(slot);
                    }
                    break;
                }
            }
            case TIMING: {
                if (!((Boolean)this.cot.getValue()).booleanValue()) break;
                break;
            }
            case CHANGEATSC: {
                if (!((Boolean)this.sameColor.getValue()).booleanValue() || !this.foundColor) break;
                ArrayList<Slot> slotOrder = new ArrayList<Slot>();
                for (int i = 12; i <= 32; ++i) {
                    int distance;
                    Slot slot = (Slot)invSlots.get(i);
                    ItemStack stack = slot.getStack();
                    if (stack == null || stack.getItem() != Item.getItemFromBlock(Blocks.stained_glass_pane) || stack.getItemDamage() == 7 || (distance = Math.abs(this.getDiff(stack.getItemDamage(), this.correctColor))) == 0) continue;
                    for (int dick = 0; dick < distance; ++dick) {
                        slotOrder.add(slot);
                    }
                }
                this.clickQueue.addAll(slotOrder);
                break;
            }
        }
        return false;
    }

    private void timingClicks(ContainerChest container) {
        if (System.currentTimeMillis() - this.lastClickTime > ((Double)this.delay.getValue()).longValue()) {
            List invSlots = container.inventorySlots;
            int greenSlot = -1;
            int purpleSlot = -1;
            int clickSlot2 = 0;
            Slot slotNeedClick = null;
            block4: for (int k = 1; k < 51; ++k) {
                ItemStack stack = ((Slot)invSlots.get(k)).getStack();
                if (stack == null) continue;
                EnumDyeColor color = EnumDyeColor.byMetadata(stack.getItemDamage());
                switch (color) {
                    case PURPLE: {
                        if (purpleSlot != -1) continue block4;
                        purpleSlot = k % 9;
                        continue block4;
                    }
                    case LIME: {
                        Item item3 = stack.getItem();
                        if (item3 == Item.getItemFromBlock(Blocks.stained_glass_pane)) {
                            if (greenSlot != -1) continue block4;
                            greenSlot = k % 9;
                            continue block4;
                        }
                        if (item3 != Item.getItemFromBlock(Blocks.stained_hardened_clay)) continue block4;
                        clickSlot2 = k;
                        slotNeedClick = (Slot)invSlots.get(k);
                        continue block4;
                    }
                }
            }
            if (purpleSlot != -1 && clickSlot2 != 0 && greenSlot == purpleSlot && slotNeedClick != null) {
                this.windowClick(this.mc.thePlayer.openContainer.windowId, slotNeedClick, 2, 3);
                this.lastClickTime = System.currentTimeMillis();
            }
        }
    }

    public int getDiff(int color, int endColor) {
        int index = colorOrder.indexOf(color);
        int finalIndex = colorOrder.indexOf(endColor);
        if (index == -1 || finalIndex == -1) {
            return 0;
        }
        if (finalIndex < index) {
            return finalIndex - index + 5;
        }
        return finalIndex - index;
    }

    private void clickSlot(Slot slot) {
        this.clickSlot(slot, 2, 3);
    }

    private void clickSlot(Slot slot, int clickButton, int clickMode) {
        if (this.windowClicks == 0) {
            this.windowId = this.mc.thePlayer.openContainer.windowId;
        }
        this.windowClick(this.windowId + this.windowClicks, slot, clickButton, clickMode);
        this.lastClickTime = System.currentTimeMillis();
        this.timer.reset();
        if (((Boolean)this.delayy.getValue()).booleanValue()) {
            ++this.windowClicks;
            this.clickQueue.remove(slot);
        }
    }

    private void windowClick(int windowId, Slot slot, int mouseButtonClicked, int mode) {
        short tid = this.mc.thePlayer.openContainer.getNextTransactionID(this.mc.thePlayer.inventory);
        ItemStack itemstack = slot.getStack();
        this.mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(windowId, slot.slotNumber, mouseButtonClicked, mode, itemstack, tid));
    }

    private boolean lambda$onGuiDraw$2(List invSlots, Slot slot) {
        return ((Slot)invSlots.get(slot.slotNumber)).getHasStack() && ((Slot)invSlots.get(slot.slotNumber)).getStack().getItemDamage() == this.targetColorIndex;
    }

    public static enum TerminalType {
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

