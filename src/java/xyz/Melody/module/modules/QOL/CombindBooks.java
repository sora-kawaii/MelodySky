/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
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
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class CombindBooks
extends Module {
    private static final Map<String, Integer> books = new ConcurrentHashMap<String, Integer>();
    public static boolean threadRunning;
    private Numbers<Double> delay = new Numbers<Double>("Delay", 200.0, 100.0, 1000.0, 10.0);

    public CombindBooks() {
        super("CombindBooks", new String[]{"cb"}, ModuleType.QOL);
        this.addValues(this.delay);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.setModInfo("Auto Combind Enchant Books.");
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        books.clear();
        threadRunning = false;
    }

    @EventHandler
    public void onGuiDraw(EventTick event) {
        String chestName;
        Container container;
        GuiScreen gui = this.mc.currentScreen;
        if (gui instanceof GuiChest && (container = ((GuiChest)gui).inventorySlots) instanceof ContainerChest && (chestName = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText()).contains("Anvil")) {
            List<Slot> chestInventory = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
            this.combineBooks(chestInventory);
        }
    }

    public void combineBooks(List<Slot> invSlots) {
        if (threadRunning) {
            return;
        }
        for (int i = 54; i <= 89; ++i) {
            int value;
            NBTTagCompound extraAttr;
            NBTTagCompound enchantments;
            Slot slot = invSlots.get(i);
            if (slot.getStack() == null || slot.getStack().getItem() != Items.enchanted_book || (enchantments = (extraAttr = ItemUtils.getExtraAttributes(slot.getStack())).getCompoundTag("enchantments")).getKeySet().size() != 1) continue;
            if (books.containsKey(enchantments.toString()) && books.get(enchantments.toString()) != i) {
                if (invSlots.get(books.get(enchantments.toString())).getStack() != null) {
                    AtomicInteger atomicInteger = new AtomicInteger(i);
                    threadRunning = true;
                    String pair = enchantments.toString();
                    new Thread(() -> this.lambda$combineBooks$0(pair, invSlots, atomicInteger)).start();
                    return;
                }
                books.remove(enchantments.toString());
                continue;
            }
            try {
                value = Integer.parseInt(String.valueOf(enchantments.toString().charAt(enchantments.toString().indexOf(":") + 2)));
            }
            catch (Exception e) {
                value = Integer.parseInt(String.valueOf(enchantments.toString().charAt(enchantments.toString().indexOf(":") + 1)));
            }
            if (enchantments.toString().contains("feather_falling") || enchantments.toString().contains("infinite_quiver") ? value >= 10 : value >= 5) continue;
            books.put(enchantments.toString(), i);
        }
    }

    private int fix(String name, List<Slot> invSlots) {
        for (int i = 54; i <= 89; ++i) {
            NBTTagCompound extraAttr;
            NBTTagCompound enchantments;
            Slot slot = invSlots.get(i);
            if (slot.getStack() == null || slot.getStack().getItem() != Items.enchanted_book || (enchantments = (extraAttr = ItemUtils.getExtraAttributes(slot.getStack())).getCompoundTag("enchantments")).getKeySet().size() != 1 || !enchantments.toString().equals(name)) continue;
            return i;
        }
        return 0;
    }

    public void sleep(int sleeptime) {
        try {
            Thread.sleep(sleeptime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void lambda$combineBooks$0(String pair, List invSlots, AtomicInteger atomicInteger) {
        this.sleep(150 + ((Double)this.delay.getValue()).intValue());
        if (this.mc.currentScreen != null) {
            this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, books.get(pair), 0, 1, this.mc.thePlayer);
        }
        books.remove(pair);
        this.sleep(300 + ((Double)this.delay.getValue()).intValue());
        if (((Slot)invSlots.get(atomicInteger.get())).getStack() == null) {
            atomicInteger.set(this.fix(pair, invSlots));
        }
        if (this.mc.currentScreen != null) {
            this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, atomicInteger.get(), 0, 1, this.mc.thePlayer);
        }
        while (((Slot)invSlots.get(13)).getStack().getItem() != Items.enchanted_book) {
            if (this.mc.currentScreen != null) continue;
            return;
        }
        this.sleep(50);
        if (this.mc.currentScreen != null) {
            this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 22, 2, 3, this.mc.thePlayer);
        }
        this.sleep(250 + ((Double)this.delay.getValue()).intValue());
        if (this.mc.currentScreen != null) {
            this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 13, 0, 1, this.mc.thePlayer);
        }
        this.sleep(50);
        threadRunning = false;
    }
}

