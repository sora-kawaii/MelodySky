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
    public void onOpenGui(GuiOpenEvent guiOpenEvent) {
        books.clear();
        threadRunning = false;
    }

    @EventHandler
    public void onGuiDraw(EventTick eventTick) {
        String string;
        Container container;
        GuiScreen guiScreen = this.mc.currentScreen;
        if (guiScreen instanceof GuiChest && (container = ((GuiChest)guiScreen).inventorySlots) instanceof ContainerChest && (string = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText()).contains("Anvil")) {
            List<Slot> list = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
            this.combineBooks(list);
        }
    }

    public void combineBooks(List<Slot> list) {
        if (threadRunning) {
            return;
        }
        for (int i = 54; i <= 89; ++i) {
            int n;
            NBTTagCompound nBTTagCompound;
            NBTTagCompound nBTTagCompound2;
            Slot slot = list.get(i);
            if (slot.getStack() == null || slot.getStack().getItem() != Items.enchanted_book || (nBTTagCompound2 = (nBTTagCompound = ItemUtils.getExtraAttributes(slot.getStack())).getCompoundTag("enchantments")).getKeySet().size() != 1) continue;
            if (books.containsKey(nBTTagCompound2.toString()) && books.get(nBTTagCompound2.toString()) != i) {
                if (list.get(books.get(nBTTagCompound2.toString())).getStack() != null) {
                    AtomicInteger atomicInteger = new AtomicInteger(i);
                    threadRunning = true;
                    String string = nBTTagCompound2.toString();
                    new Thread(() -> {
                        this.sleep(150 + ((Double)this.delay.getValue()).intValue());
                        if (this.mc.currentScreen != null) {
                            this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, books.get(string), 0, 1, this.mc.thePlayer);
                        }
                        books.remove(string);
                        this.sleep(300 + ((Double)this.delay.getValue()).intValue());
                        if (((Slot)list.get(atomicInteger.get())).getStack() == null) {
                            atomicInteger.set(this.fix(string, list));
                        }
                        if (this.mc.currentScreen != null) {
                            this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, atomicInteger.get(), 0, 1, this.mc.thePlayer);
                        }
                        while (((Slot)list.get(13)).getStack().getItem() != Items.enchanted_book) {
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
                    }).start();
                    return;
                }
                books.remove(nBTTagCompound2.toString());
                continue;
            }
            try {
                n = Integer.parseInt(String.valueOf(nBTTagCompound2.toString().charAt(nBTTagCompound2.toString().indexOf(":") + 2)));
            }
            catch (Exception exception) {
                n = Integer.parseInt(String.valueOf(nBTTagCompound2.toString().charAt(nBTTagCompound2.toString().indexOf(":") + 1)));
            }
            if (nBTTagCompound2.toString().contains("feather_falling") || nBTTagCompound2.toString().contains("infinite_quiver") ? n >= 10 : n >= 5) continue;
            books.put(nBTTagCompound2.toString(), i);
        }
    }

    private int fix(String string, List<Slot> list) {
        for (int i = 54; i <= 89; ++i) {
            NBTTagCompound nBTTagCompound;
            NBTTagCompound nBTTagCompound2;
            Slot slot = list.get(i);
            if (slot.getStack() == null || slot.getStack().getItem() != Items.enchanted_book || (nBTTagCompound2 = (nBTTagCompound = ItemUtils.getExtraAttributes(slot.getStack())).getCompoundTag("enchantments")).getKeySet().size() != 1 || !nBTTagCompound2.toString().equals(string)) continue;
            return i;
        }
        return 0;
    }

    public void sleep(int n) {
        try {
            Thread.sleep(n);
        }
        catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}

