/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.StringUtils;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoEnchantTable
extends Module {
    private Mode<Enum> clickMode = new Mode("ClickMode", (Enum[])cm.values(), (Enum)cm.Middle);
    private Numbers<Double> delay = new Numbers<Double>("Delay", 150.0, 100.0, 500.0, 1.0);
    private Option<Boolean> chronomatron = new Option<Boolean>("Chronomatron", true);
    private Option<Boolean> ultrasequencer = new Option<Boolean>("Ultrasequencer", true);
    private Option<Boolean> superpairs = new Option<Boolean>("SuperPairsSolver", true);
    private Option<Boolean> bug = new Option<Boolean>("dEBuG", false);
    private TimerUtil timer = new TimerUtil();
    public expType currentType = expType.NONE;
    private boolean addedAll = false;
    private Map<Integer, ItemStack> superpairStacks = new HashMap<Integer, ItemStack>();
    private int lastSlotClicked = -1;
    private HashSet<Integer> successfulMatches = new HashSet();
    private HashSet<Integer> possibleMatches = new HashSet();
    private HashSet<Integer> powerupMatches = new HashSet();
    private List<String> chronomatronPattern = new ArrayList<String>();
    private int lastChronomatronRound = 0;
    private int chronomatronMouseClicks = 0;
    private Slot[] clickInOrderSlots = new Slot[36];
    private ArrayList<Slot> clickQueue = new ArrayList();
    private int windowId = 0;

    public AutoEnchantTable() {
        super("AutoExperiment", new String[]{"enchant"}, ModuleType.QOL);
        this.addValues(this.clickMode, this.delay, this.chronomatron, this.ultrasequencer, this.superpairs, this.bug);
        this.setModInfo("Auto Do Experimentation Table.");
    }

    @EventHandler
    private void tickContainer(EventTick event) {
        if (this.currentType != expType.NONE && this.clickMode.getValue() != cm.Middle) {
            this.mc.thePlayer.inventory.setItemStack(null);
        }
    }

    @EventHandler
    public void onGuiDraw(EventTick event) {
        Container container;
        GuiScreen gui = this.mc.currentScreen;
        if (gui instanceof GuiChest && (container = ((GuiChest)gui).inventorySlots) instanceof ContainerChest) {
            if (this.currentType == expType.NONE) {
                String chestName = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (chestName.startsWith("Ultrasequencer (")) {
                    this.currentType = expType.Ultrasequencer;
                } else if (chestName.startsWith("Chronomatron (")) {
                    this.currentType = expType.Chronomatron;
                } else if (chestName.startsWith("Superpairs (")) {
                    this.currentType = expType.Superpairs;
                }
            } else if (this.currentType != expType.NONE && !this.clickQueue.isEmpty() && this.timer.hasReached(((Double)this.delay.getValue()).longValue())) {
                this.clickSlot(this.clickQueue.get(0), true);
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
            List<Slot> invSlots = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
            if (this.currentType == expType.Ultrasequencer && ((Boolean)this.ultrasequencer.getValue()).booleanValue()) {
                if (invSlots.get(49).getStack() != null && invSlots.get(49).getStack().getDisplayName().contains("Remember the pattern!")) {
                    this.addedAll = false;
                    for (int i = 9; i <= 44; ++i) {
                        String itemName;
                        if (invSlots.get(i) == null || invSlots.get(i).getStack() == null || !(itemName = StringUtils.stripControlCodes(invSlots.get(i).getStack().getDisplayName())).matches("\\d+")) continue;
                        int number = Integer.parseInt(itemName);
                        this.clickInOrderSlots[number - 1] = invSlots.get(i);
                    }
                } else if (invSlots.get(49).getStack().getDisplayName().startsWith("\u00a77Timer: \u00a7a") && !this.addedAll) {
                    this.clickQueue.addAll(Arrays.stream(this.clickInOrderSlots).filter(Objects::nonNull).collect(Collectors.toList()));
                    this.clickInOrderSlots = new Slot[36];
                    this.addedAll = true;
                }
            }
        }
    }

    @EventHandler
    public void onGuiRender(EventTick event) {
        if (this.currentType != expType.Chronomatron) {
            return;
        }
        if (this.mc.currentScreen instanceof GuiChest) {
            GuiChest inventory = (GuiChest)this.mc.currentScreen;
            Container containerChest = inventory.inventorySlots;
            if (containerChest instanceof ContainerChest) {
                EntityPlayerSP player = this.mc.thePlayer;
                List<Slot> invSlots = containerChest.inventorySlots;
                if (((Boolean)this.chronomatron.getValue()).booleanValue() && this.currentType == expType.Chronomatron && player.inventory.getItemStack() == null && invSlots.size() > 48 && invSlots.get(49).getStack() != null) {
                    if (invSlots.get(49).getStack().getDisplayName().startsWith("\u00a77Timer: \u00a7a") && invSlots.get(4).getStack() != null) {
                        int i;
                        int round = invSlots.get((int)4).getStack().stackSize;
                        int timerSeconds = Integer.parseInt(StringUtils.stripControlCodes(invSlots.get(49).getStack().getDisplayName()).replaceAll("[^\\d]", ""));
                        if (round != this.lastChronomatronRound && timerSeconds == round + 2) {
                            this.lastChronomatronRound = round;
                            for (i = 10; i <= 43; ++i) {
                                ItemStack stack = invSlots.get(i).getStack();
                                if (stack == null || stack.getItem() != Item.getItemFromBlock(Blocks.stained_hardened_clay)) continue;
                                this.chronomatronPattern.add(stack.getDisplayName());
                                break;
                            }
                        }
                        if (this.chronomatronMouseClicks < this.chronomatronPattern.size() && player.inventory.getItemStack() == null) {
                            for (i = 10; i <= 43; ++i) {
                                ItemStack glass = invSlots.get(i).getStack();
                                if (glass == null || player.inventory.getItemStack() != null) continue;
                                Slot glassSlot = invSlots.get(i);
                                if (!glass.getDisplayName().equals(this.chronomatronPattern.get(this.chronomatronMouseClicks)) || !this.timer.hasReached((Double)this.delay.getValue())) continue;
                                this.clickSlot(glassSlot, false);
                                ++this.chronomatronMouseClicks;
                                break;
                            }
                        }
                    } else if (invSlots.get(49).getStack().getDisplayName().equals("\u00a7aRemember the pattern!")) {
                        this.chronomatronMouseClicks = 0;
                    }
                }
            }
        }
    }

    public ItemStack overrideStack(IInventory inventory, int slotIndex, ItemStack stack) {
        if (stack != null && stack.getDisplayName() != null && this.mc.currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest)this.mc.currentScreen;
            ContainerChest container = (ContainerChest)chest.inventorySlots;
            IInventory lower = container.getLowerChestInventory();
            if (lower != inventory) {
                return null;
            }
            if (this.currentType == expType.Superpairs && stack.getItem() == Item.getItemFromBlock(Blocks.stained_glass) && this.superpairStacks.containsKey(slotIndex)) {
                return this.superpairStacks.get(slotIndex);
            }
        }
        return null;
    }

    public boolean onStackRender(ItemStack stack, IInventory inventory, int slotIndex, int x, int y) {
        if (stack != null && stack.getDisplayName() != null && Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest)Minecraft.getMinecraft().currentScreen;
            ContainerChest container = (ContainerChest)chest.inventorySlots;
            IInventory lower = container.getLowerChestInventory();
            if (lower != inventory) {
                return false;
            }
            if (this.currentType == expType.Superpairs) {
                int meta = 0;
                if (stack.getItem() == Item.getItemFromBlock(Blocks.stained_glass) && this.superpairStacks.containsKey(slotIndex)) {
                    meta = this.possibleMatches.contains(slotIndex) ? 2 : 5;
                } else if (this.powerupMatches.contains(slotIndex)) {
                    meta = 11;
                } else if (this.successfulMatches.contains(slotIndex)) {
                    meta = 6;
                }
                if (meta > 0) {
                    RenderUtil.drawItemStack(new ItemStack(Item.getItemFromBlock(Blocks.stained_glass_pane), 1, meta - 1), x, y);
                }
            }
        }
        return false;
    }

    public boolean onStackClick(ItemStack stack, int windowId, int slotId, int mouseButtonClicked, int mode) {
        if (stack != null && stack.getDisplayName() != null && this.mc.currentScreen instanceof GuiChest && this.currentType == expType.Superpairs) {
            this.lastSlotClicked = slotId;
        }
        return false;
    }

    public void processInventoryContents() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest)Minecraft.getMinecraft().currentScreen;
            ContainerChest container = (ContainerChest)chest.inventorySlots;
            IInventory lower = container.getLowerChestInventory();
            if (this.currentType == expType.Superpairs) {
                this.successfulMatches.clear();
                this.possibleMatches.clear();
                this.powerupMatches.clear();
                block0: for (int index = 0; index < lower.getSizeInventory(); ++index) {
                    ItemStack stack = lower.getStackInSlot(index);
                    if (stack == null) continue;
                    if (stack.getItem() != Item.getItemFromBlock(Blocks.stained_glass) && stack.getItem() != Item.getItemFromBlock(Blocks.stained_glass_pane)) {
                        boolean oddMatches;
                        NBTTagCompound display;
                        this.superpairStacks.put(index, stack);
                        NBTTagCompound tag = stack.getTagCompound();
                        if (tag != null && (display = tag.getCompoundTag("display")).hasKey("Lore", 9)) {
                            NBTTagList list = display.getTagList("Lore", 8);
                            for (int i = 0; i < list.tagCount(); ++i) {
                                if (!list.getStringTagAt(i).toLowerCase().contains("powerup")) continue;
                                this.powerupMatches.add(index);
                                continue block0;
                            }
                        }
                        int numMatches = 0;
                        for (int index2 = 0; index2 < lower.getSizeInventory(); ++index2) {
                            ItemStack stack2 = lower.getStackInSlot(index2);
                            if (stack2 == null || !stack2.getDisplayName().equals(stack.getDisplayName()) || stack.getItem() != stack2.getItem() || stack.getItemDamage() != stack2.getItemDamage()) continue;
                            ++numMatches;
                        }
                        boolean bl = oddMatches = numMatches % 2 == 1;
                        if (oddMatches && index == this.lastSlotClicked || this.successfulMatches.contains(index)) continue;
                        for (int index2 = 0; index2 < lower.getSizeInventory(); ++index2) {
                            ItemStack stack2;
                            if (index == index2 || oddMatches && index2 == this.lastSlotClicked || (stack2 = lower.getStackInSlot(index2)) == null || !stack2.getDisplayName().equals(stack.getDisplayName()) || stack.getItem() != stack2.getItem() || stack.getItemDamage() != stack2.getItemDamage()) continue;
                            this.successfulMatches.add(index);
                            this.successfulMatches.add(index2);
                        }
                        continue;
                    }
                    if (!this.superpairStacks.containsKey(index) || this.superpairStacks.get(index) == null || this.possibleMatches.contains(index)) continue;
                    ItemStack stack1 = this.superpairStacks.get(index);
                    for (int index2 = 0; index2 < lower.getSizeInventory(); ++index2) {
                        if (index == index2 || !this.superpairStacks.containsKey(index2) || this.superpairStacks.get(index2) == null) continue;
                        ItemStack stack2 = this.superpairStacks.get(index2);
                        if (!stack1.getDisplayName().equals(stack2.getDisplayName()) || stack1.getItem() != stack2.getItem() || stack1.getItemDamage() != stack2.getItemDamage()) continue;
                        this.possibleMatches.add(index);
                        this.possibleMatches.add(index2);
                    }
                }
            } else {
                this.superpairStacks.clear();
                this.successfulMatches.clear();
                this.powerupMatches.clear();
                this.lastSlotClicked = -1;
            }
        }
    }

    @EventHandler
    public void onGuiClosed(EventTick event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (!(this.mc.currentScreen instanceof GuiChest)) {
            this.currentType = expType.NONE;
            this.addedAll = false;
            this.chronomatronMouseClicks = 0;
            this.lastChronomatronRound = 0;
            this.chronomatronPattern.clear();
            this.clickInOrderSlots = new Slot[36];
            this.clickQueue.clear();
        }
    }

    private void clickSlot(Slot slot, boolean remove) {
        if (this.clickMode.getValue() == cm.Middle) {
            this.clickSlot(slot, 2, 3, remove);
        }
        if (this.clickMode.getValue() == cm.Left) {
            this.clickSlot(slot, 0, 0, remove);
        }
        if (this.clickMode.getValue() == cm.Right) {
            this.clickSlot(slot, 1, 0, remove);
        }
    }

    private void clickSlot(Slot slot, int clickButton, int clickMode, boolean remove) {
        this.windowId = this.mc.thePlayer.openContainer.windowId;
        this.windowClick(this.windowId, slot, clickButton, clickMode);
        if (((Boolean)this.bug.getValue()).booleanValue()) {
            Helper.sendMessage("Clicked: " + slot.slotNumber);
        }
        this.timer.reset();
        if (remove) {
            this.clickQueue.remove(slot);
        }
    }

    private void windowClick(int windowId, Slot slot, int mouseButtonClicked, int mode) {
        short tid = this.mc.thePlayer.openContainer.getNextTransactionID(this.mc.thePlayer.inventory);
        ItemStack itemstack = slot.getStack();
        this.mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(windowId, slot.slotNumber, mouseButtonClicked, mode, itemstack, tid));
    }

    public static enum expType {
        NONE,
        Chronomatron,
        Ultrasequencer,
        Superpairs;

    }

    static enum cm {
        Middle,
        Left,
        Right;

    }
}

