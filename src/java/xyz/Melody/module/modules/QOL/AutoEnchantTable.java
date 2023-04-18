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
    private void tickContainer(EventTick eventTick) {
        if (this.currentType != expType.NONE && this.clickMode.getValue() != cm.Middle) {
            this.mc.thePlayer.inventory.setItemStack(null);
        }
    }

    @EventHandler
    public void onGuiDraw(EventTick eventTick) {
        Container container;
        GuiScreen guiScreen = this.mc.currentScreen;
        if (guiScreen instanceof GuiChest && (container = ((GuiChest)guiScreen).inventorySlots) instanceof ContainerChest) {
            if (this.currentType == expType.NONE) {
                String string = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (string.startsWith("Ultrasequencer (")) {
                    this.currentType = expType.Ultrasequencer;
                } else if (string.startsWith("Chronomatron (")) {
                    this.currentType = expType.Chronomatron;
                } else if (string.startsWith("Superpairs (")) {
                    this.currentType = expType.Superpairs;
                }
            } else if (this.currentType != expType.NONE && !this.clickQueue.isEmpty() && this.timer.hasReached(((Double)this.delay.getValue()).longValue())) {
                this.clickSlot(this.clickQueue.get(0), true);
            }
        }
    }

    @EventHandler
    public void onTick(EventTick eventTick) {
        EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
        if (this.mc.currentScreen instanceof GuiChest) {
            if (entityPlayerSP == null) {
                return;
            }
            List<Slot> list = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
            if (this.currentType == expType.Ultrasequencer && ((Boolean)this.ultrasequencer.getValue()).booleanValue()) {
                if (list.get(49).getStack() != null && list.get(49).getStack().getDisplayName().contains("Remember the pattern!")) {
                    this.addedAll = false;
                    for (int i = 9; i <= 44; ++i) {
                        String string;
                        if (list.get(i) == null || list.get(i).getStack() == null || !(string = StringUtils.stripControlCodes(list.get(i).getStack().getDisplayName())).matches("\\d+")) continue;
                        int n = Integer.parseInt(string);
                        this.clickInOrderSlots[n - 1] = list.get(i);
                    }
                } else if (list.get(49).getStack().getDisplayName().startsWith("\u00a77Timer: \u00a7a") && !this.addedAll) {
                    this.clickQueue.addAll(Arrays.stream(this.clickInOrderSlots).filter(Objects::nonNull).collect(Collectors.toList()));
                    this.clickInOrderSlots = new Slot[36];
                    this.addedAll = true;
                }
            }
        }
    }

    @EventHandler
    public void onGuiRender(EventTick eventTick) {
        if (this.currentType != expType.Chronomatron) {
            return;
        }
        if (this.mc.currentScreen instanceof GuiChest) {
            GuiChest guiChest = (GuiChest)this.mc.currentScreen;
            Container container = guiChest.inventorySlots;
            if (container instanceof ContainerChest) {
                EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
                List<Slot> list = container.inventorySlots;
                if (((Boolean)this.chronomatron.getValue()).booleanValue() && this.currentType == expType.Chronomatron && entityPlayerSP.inventory.getItemStack() == null && list.size() > 48 && list.get(49).getStack() != null) {
                    if (list.get(49).getStack().getDisplayName().startsWith("\u00a77Timer: \u00a7a") && list.get(4).getStack() != null) {
                        ItemStack itemStack;
                        int n;
                        int n2 = list.get((int)4).getStack().stackSize;
                        int n3 = Integer.parseInt(StringUtils.stripControlCodes(list.get(49).getStack().getDisplayName()).replaceAll("[^\\d]", ""));
                        if (n2 != this.lastChronomatronRound && n3 == n2 + 2) {
                            this.lastChronomatronRound = n2;
                            for (n = 10; n <= 43; ++n) {
                                itemStack = list.get(n).getStack();
                                if (itemStack == null || itemStack.getItem() != Item.getItemFromBlock(Blocks.stained_hardened_clay)) continue;
                                this.chronomatronPattern.add(itemStack.getDisplayName());
                                break;
                            }
                        }
                        if (this.chronomatronMouseClicks < this.chronomatronPattern.size() && entityPlayerSP.inventory.getItemStack() == null) {
                            for (n = 10; n <= 43; ++n) {
                                itemStack = list.get(n).getStack();
                                if (itemStack == null || entityPlayerSP.inventory.getItemStack() != null) continue;
                                Slot slot = list.get(n);
                                if (!itemStack.getDisplayName().equals(this.chronomatronPattern.get(this.chronomatronMouseClicks)) || !this.timer.hasReached((Double)this.delay.getValue())) continue;
                                this.clickSlot(slot, false);
                                ++this.chronomatronMouseClicks;
                                break;
                            }
                        }
                    } else if (list.get(49).getStack().getDisplayName().equals("\u00a7aRemember the pattern!")) {
                        this.chronomatronMouseClicks = 0;
                    }
                }
            }
        }
    }

    public ItemStack overrideStack(IInventory iInventory, int n, ItemStack itemStack) {
        if (itemStack != null && itemStack.getDisplayName() != null && this.mc.currentScreen instanceof GuiChest) {
            GuiChest guiChest = (GuiChest)this.mc.currentScreen;
            ContainerChest containerChest = (ContainerChest)guiChest.inventorySlots;
            IInventory iInventory2 = containerChest.getLowerChestInventory();
            if (iInventory2 != iInventory) {
                return null;
            }
            if (this.currentType == expType.Superpairs && itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass) && this.superpairStacks.containsKey(n)) {
                return this.superpairStacks.get(n);
            }
        }
        return null;
    }

    public boolean onStackRender(ItemStack itemStack, IInventory iInventory, int n, int n2, int n3) {
        if (itemStack != null && itemStack.getDisplayName() != null && Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest guiChest = (GuiChest)Minecraft.getMinecraft().currentScreen;
            ContainerChest containerChest = (ContainerChest)guiChest.inventorySlots;
            IInventory iInventory2 = containerChest.getLowerChestInventory();
            if (iInventory2 != iInventory) {
                return false;
            }
            if (this.currentType == expType.Superpairs) {
                int n4 = 0;
                if (itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass) && this.superpairStacks.containsKey(n)) {
                    n4 = this.possibleMatches.contains(n) ? 2 : 5;
                } else if (this.powerupMatches.contains(n)) {
                    n4 = 11;
                } else if (this.successfulMatches.contains(n)) {
                    n4 = 6;
                }
                if (n4 > 0) {
                    RenderUtil.drawItemStack(new ItemStack(Item.getItemFromBlock(Blocks.stained_glass_pane), 1, n4 - 1), n2, n3);
                }
            }
        }
        return false;
    }

    public boolean onStackClick(ItemStack itemStack, int n, int n2, int n3, int n4) {
        if (itemStack != null && itemStack.getDisplayName() != null && this.mc.currentScreen instanceof GuiChest && this.currentType == expType.Superpairs) {
            this.lastSlotClicked = n2;
        }
        return false;
    }

    public void processInventoryContents() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest guiChest = (GuiChest)Minecraft.getMinecraft().currentScreen;
            ContainerChest containerChest = (ContainerChest)guiChest.inventorySlots;
            IInventory iInventory = containerChest.getLowerChestInventory();
            if (this.currentType == expType.Superpairs) {
                this.successfulMatches.clear();
                this.possibleMatches.clear();
                this.powerupMatches.clear();
                block0: for (int i = 0; i < iInventory.getSizeInventory(); ++i) {
                    Object object;
                    Object object2;
                    ItemStack itemStack = iInventory.getStackInSlot(i);
                    if (itemStack == null) continue;
                    if (itemStack.getItem() != Item.getItemFromBlock(Blocks.stained_glass) && itemStack.getItem() != Item.getItemFromBlock(Blocks.stained_glass_pane)) {
                        int n;
                        int n2;
                        NBTTagCompound nBTTagCompound;
                        this.superpairStacks.put(i, itemStack);
                        object2 = itemStack.getTagCompound();
                        if (object2 != null && (nBTTagCompound = ((NBTTagCompound)object2).getCompoundTag("display")).hasKey("Lore", 9)) {
                            object = nBTTagCompound.getTagList("Lore", 8);
                            for (n2 = 0; n2 < ((NBTTagList)object).tagCount(); ++n2) {
                                if (!((NBTTagList)object).getStringTagAt(n2).toLowerCase().contains("powerup")) continue;
                                this.powerupMatches.add(i);
                                continue block0;
                            }
                        }
                        int n3 = 0;
                        for (n = 0; n < iInventory.getSizeInventory(); ++n) {
                            ItemStack itemStack2 = iInventory.getStackInSlot(n);
                            if (itemStack2 == null || !itemStack2.getDisplayName().equals(itemStack.getDisplayName()) || itemStack.getItem() != itemStack2.getItem() || itemStack.getItemDamage() != itemStack2.getItemDamage()) continue;
                            ++n3;
                        }
                        int n4 = n = n3 % 2 == 1 ? 1 : 0;
                        if (n != 0 && i == this.lastSlotClicked || this.successfulMatches.contains(i)) continue;
                        for (n2 = 0; n2 < iInventory.getSizeInventory(); ++n2) {
                            ItemStack itemStack3;
                            if (i == n2 || n != 0 && n2 == this.lastSlotClicked || (itemStack3 = iInventory.getStackInSlot(n2)) == null || !itemStack3.getDisplayName().equals(itemStack.getDisplayName()) || itemStack.getItem() != itemStack3.getItem() || itemStack.getItemDamage() != itemStack3.getItemDamage()) continue;
                            this.successfulMatches.add(i);
                            this.successfulMatches.add(n2);
                        }
                        continue;
                    }
                    if (!this.superpairStacks.containsKey(i) || this.superpairStacks.get(i) == null || this.possibleMatches.contains(i)) continue;
                    object2 = this.superpairStacks.get(i);
                    for (int j = 0; j < iInventory.getSizeInventory(); ++j) {
                        if (i == j || !this.superpairStacks.containsKey(j) || this.superpairStacks.get(j) == null) continue;
                        object = this.superpairStacks.get(j);
                        if (!((ItemStack)object2).getDisplayName().equals(((ItemStack)object).getDisplayName()) || ((ItemStack)object2).getItem() != ((ItemStack)object).getItem() || ((ItemStack)object2).getItemDamage() != ((ItemStack)object).getItemDamage()) continue;
                        this.possibleMatches.add(i);
                        this.possibleMatches.add(j);
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
    public void onGuiClosed(EventTick eventTick) {
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

    private void clickSlot(Slot slot, boolean bl) {
        if (this.clickMode.getValue() == cm.Middle) {
            this.clickSlot(slot, 2, 3, bl);
        }
        if (this.clickMode.getValue() == cm.Left) {
            this.clickSlot(slot, 0, 0, bl);
        }
        if (this.clickMode.getValue() == cm.Right) {
            this.clickSlot(slot, 1, 0, bl);
        }
    }

    private void clickSlot(Slot slot, int n, int n2, boolean bl) {
        this.windowId = this.mc.thePlayer.openContainer.windowId;
        this.windowClick(this.windowId, slot, n, n2);
        if (((Boolean)this.bug.getValue()).booleanValue()) {
            Helper.sendMessage("Clicked: " + slot.slotNumber);
        }
        this.timer.reset();
        if (bl) {
            this.clickQueue.remove(slot);
        }
    }

    private void windowClick(int n, Slot slot, int n2, int n3) {
        short s = this.mc.thePlayer.openContainer.getNextTransactionID(this.mc.thePlayer.inventory);
        ItemStack itemStack = slot.getStack();
        this.mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(n, slot.slotNumber, n2, n3, itemStack, s));
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

