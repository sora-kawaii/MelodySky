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
import net.minecraft.block.Block;
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
            this.mc.field_71439_g.field_71071_by.func_70437_b(null);
        }
    }

    @EventHandler
    public void onGuiDraw(EventTick eventTick) {
        Container container;
        GuiScreen guiScreen = this.mc.field_71462_r;
        if (guiScreen instanceof GuiChest && (container = ((GuiChest)guiScreen).field_147002_h) instanceof ContainerChest) {
            if (this.currentType == expType.NONE) {
                String string = ((ContainerChest)container).func_85151_d().func_145748_c_().func_150260_c();
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
        EntityPlayerSP entityPlayerSP = this.mc.field_71439_g;
        if (this.mc.field_71462_r instanceof GuiChest) {
            if (entityPlayerSP == null) {
                return;
            }
            List list = ((GuiChest)this.mc.field_71462_r).field_147002_h.field_75151_b;
            if (this.currentType == expType.Ultrasequencer && ((Boolean)this.ultrasequencer.getValue()).booleanValue()) {
                if (((Slot)list.get(49)).func_75211_c() != null && ((Slot)list.get(49)).func_75211_c().func_82833_r().contains("Remember the pattern!")) {
                    this.addedAll = false;
                    for (int i = 9; i <= 44; ++i) {
                        String string;
                        if (list.get(i) == null || ((Slot)list.get(i)).func_75211_c() == null || !(string = StringUtils.func_76338_a((String)((Slot)list.get(i)).func_75211_c().func_82833_r())).matches("\\d+")) continue;
                        int n = Integer.parseInt(string);
                        this.clickInOrderSlots[n - 1] = (Slot)list.get(i);
                    }
                } else if (((Slot)list.get(49)).func_75211_c().func_82833_r().startsWith("\u00a77Timer: \u00a7a") && !this.addedAll) {
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
        if (this.mc.field_71462_r instanceof GuiChest) {
            GuiChest guiChest = (GuiChest)this.mc.field_71462_r;
            Container container = guiChest.field_147002_h;
            if (container instanceof ContainerChest) {
                EntityPlayerSP entityPlayerSP = this.mc.field_71439_g;
                List list = container.field_75151_b;
                if (((Boolean)this.chronomatron.getValue()).booleanValue() && this.currentType == expType.Chronomatron && entityPlayerSP.field_71071_by.func_70445_o() == null && list.size() > 48 && ((Slot)list.get(49)).func_75211_c() != null) {
                    if (((Slot)list.get(49)).func_75211_c().func_82833_r().startsWith("\u00a77Timer: \u00a7a") && ((Slot)list.get(4)).func_75211_c() != null) {
                        ItemStack itemStack;
                        int n;
                        int n2 = ((Slot)list.get((int)4)).func_75211_c().field_77994_a;
                        int n3 = Integer.parseInt(StringUtils.func_76338_a((String)((Slot)list.get(49)).func_75211_c().func_82833_r()).replaceAll("[^\\d]", ""));
                        if (n2 != this.lastChronomatronRound && n3 == n2 + 2) {
                            this.lastChronomatronRound = n2;
                            for (n = 10; n <= 43; ++n) {
                                itemStack = ((Slot)list.get(n)).func_75211_c();
                                if (itemStack == null || itemStack.func_77973_b() != Item.func_150898_a((Block)Blocks.field_150406_ce)) continue;
                                this.chronomatronPattern.add(itemStack.func_82833_r());
                                break;
                            }
                        }
                        if (this.chronomatronMouseClicks < this.chronomatronPattern.size() && entityPlayerSP.field_71071_by.func_70445_o() == null) {
                            for (n = 10; n <= 43; ++n) {
                                itemStack = ((Slot)list.get(n)).func_75211_c();
                                if (itemStack == null || entityPlayerSP.field_71071_by.func_70445_o() != null) continue;
                                Slot slot = (Slot)list.get(n);
                                if (!itemStack.func_82833_r().equals(this.chronomatronPattern.get(this.chronomatronMouseClicks)) || !this.timer.hasReached((Double)this.delay.getValue())) continue;
                                this.clickSlot(slot, false);
                                ++this.chronomatronMouseClicks;
                                break;
                            }
                        }
                    } else if (((Slot)list.get(49)).func_75211_c().func_82833_r().equals("\u00a7aRemember the pattern!")) {
                        this.chronomatronMouseClicks = 0;
                    }
                }
            }
        }
    }

    public ItemStack overrideStack(IInventory iInventory, int n, ItemStack itemStack) {
        if (itemStack != null && itemStack.func_82833_r() != null && this.mc.field_71462_r instanceof GuiChest) {
            GuiChest guiChest = (GuiChest)this.mc.field_71462_r;
            ContainerChest containerChest = (ContainerChest)guiChest.field_147002_h;
            IInventory iInventory2 = containerChest.func_85151_d();
            if (iInventory2 != iInventory) {
                return null;
            }
            if (this.currentType == expType.Superpairs && itemStack.func_77973_b() == Item.func_150898_a((Block)Blocks.field_150399_cn) && this.superpairStacks.containsKey(n)) {
                return this.superpairStacks.get(n);
            }
        }
        return null;
    }

    public boolean onStackRender(ItemStack itemStack, IInventory iInventory, int n, int n2, int n3) {
        if (itemStack != null && itemStack.func_82833_r() != null && Minecraft.func_71410_x().field_71462_r instanceof GuiChest) {
            GuiChest guiChest = (GuiChest)Minecraft.func_71410_x().field_71462_r;
            ContainerChest containerChest = (ContainerChest)guiChest.field_147002_h;
            IInventory iInventory2 = containerChest.func_85151_d();
            if (iInventory2 != iInventory) {
                return false;
            }
            if (this.currentType == expType.Superpairs) {
                int n4 = 0;
                if (itemStack.func_77973_b() == Item.func_150898_a((Block)Blocks.field_150399_cn) && this.superpairStacks.containsKey(n)) {
                    n4 = this.possibleMatches.contains(n) ? 2 : 5;
                } else if (this.powerupMatches.contains(n)) {
                    n4 = 11;
                } else if (this.successfulMatches.contains(n)) {
                    n4 = 6;
                }
                if (n4 > 0) {
                    RenderUtil.drawItemStack(new ItemStack(Item.func_150898_a((Block)Blocks.field_150397_co), 1, n4 - 1), n2, n3);
                }
            }
        }
        return false;
    }

    public boolean onStackClick(ItemStack itemStack, int n, int n2, int n3, int n4) {
        if (itemStack != null && itemStack.func_82833_r() != null && this.mc.field_71462_r instanceof GuiChest && this.currentType == expType.Superpairs) {
            this.lastSlotClicked = n2;
        }
        return false;
    }

    public void processInventoryContents() {
        if (Minecraft.func_71410_x().field_71462_r instanceof GuiChest) {
            GuiChest guiChest = (GuiChest)Minecraft.func_71410_x().field_71462_r;
            ContainerChest containerChest = (ContainerChest)guiChest.field_147002_h;
            IInventory iInventory = containerChest.func_85151_d();
            if (this.currentType == expType.Superpairs) {
                this.successfulMatches.clear();
                this.possibleMatches.clear();
                this.powerupMatches.clear();
                block0: for (int i = 0; i < iInventory.func_70302_i_(); ++i) {
                    Object object;
                    Object object2;
                    ItemStack itemStack = iInventory.func_70301_a(i);
                    if (itemStack == null) continue;
                    if (itemStack.func_77973_b() != Item.func_150898_a((Block)Blocks.field_150399_cn) && itemStack.func_77973_b() != Item.func_150898_a((Block)Blocks.field_150397_co)) {
                        int n;
                        int n2;
                        NBTTagCompound nBTTagCompound;
                        this.superpairStacks.put(i, itemStack);
                        object2 = itemStack.func_77978_p();
                        if (object2 != null && (nBTTagCompound = object2.func_74775_l("display")).func_150297_b("Lore", 9)) {
                            object = nBTTagCompound.func_150295_c("Lore", 8);
                            for (n2 = 0; n2 < object.func_74745_c(); ++n2) {
                                if (!object.func_150307_f(n2).toLowerCase().contains("powerup")) continue;
                                this.powerupMatches.add(i);
                                continue block0;
                            }
                        }
                        int n3 = 0;
                        for (n = 0; n < iInventory.func_70302_i_(); ++n) {
                            ItemStack itemStack2 = iInventory.func_70301_a(n);
                            if (itemStack2 == null || !itemStack2.func_82833_r().equals(itemStack.func_82833_r()) || itemStack.func_77973_b() != itemStack2.func_77973_b() || itemStack.func_77952_i() != itemStack2.func_77952_i()) continue;
                            ++n3;
                        }
                        int n4 = n = n3 % 2 == 1 ? 1 : 0;
                        if (n != 0 && i == this.lastSlotClicked || this.successfulMatches.contains(i)) continue;
                        for (n2 = 0; n2 < iInventory.func_70302_i_(); ++n2) {
                            ItemStack itemStack3;
                            if (i == n2 || n != 0 && n2 == this.lastSlotClicked || (itemStack3 = iInventory.func_70301_a(n2)) == null || !itemStack3.func_82833_r().equals(itemStack.func_82833_r()) || itemStack.func_77973_b() != itemStack3.func_77973_b() || itemStack.func_77952_i() != itemStack3.func_77952_i()) continue;
                            this.successfulMatches.add(i);
                            this.successfulMatches.add(n2);
                        }
                        continue;
                    }
                    if (!this.superpairStacks.containsKey(i) || this.superpairStacks.get(i) == null || this.possibleMatches.contains(i)) continue;
                    object2 = this.superpairStacks.get(i);
                    for (int j = 0; j < iInventory.func_70302_i_(); ++j) {
                        if (i == j || !this.superpairStacks.containsKey(j) || this.superpairStacks.get(j) == null) continue;
                        object = this.superpairStacks.get(j);
                        if (!object2.func_82833_r().equals(object.func_82833_r()) || object2.func_77973_b() != object.func_77973_b() || object2.func_77952_i() != object.func_77952_i()) continue;
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
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        if (!(this.mc.field_71462_r instanceof GuiChest)) {
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
        this.windowId = this.mc.field_71439_g.field_71070_bA.field_75152_c;
        this.windowClick(this.windowId, slot, n, n2);
        if (((Boolean)this.bug.getValue()).booleanValue()) {
            Helper.sendMessage("Clicked: " + slot.field_75222_d);
        }
        this.timer.reset();
        if (bl) {
            this.clickQueue.remove(slot);
        }
    }

    private void windowClick(int n, Slot slot, int n2, int n3) {
        short s = this.mc.field_71439_g.field_71070_bA.func_75136_a(this.mc.field_71439_g.field_71071_by);
        ItemStack itemStack = slot.func_75211_c();
        this.mc.func_147114_u().func_147297_a(new C0EPacketClickWindow(n, slot.field_75222_d, n2, n3, itemStack, s));
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

