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
        GuiScreen guiScreen = this.mc.field_71462_r;
        if (guiScreen instanceof GuiChest && (container = ((GuiChest)guiScreen).field_147002_h) instanceof ContainerChest && (string = ((ContainerChest)container).func_85151_d().func_145748_c_().func_150260_c()).contains("Anvil")) {
            List list = ((GuiChest)this.mc.field_71462_r).field_147002_h.field_75151_b;
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
            if (slot.func_75211_c() == null || slot.func_75211_c().func_77973_b() != Items.field_151134_bR || (nBTTagCompound2 = (nBTTagCompound = ItemUtils.getExtraAttributes(slot.func_75211_c())).func_74775_l("enchantments")).func_150296_c().size() != 1) continue;
            if (books.containsKey(nBTTagCompound2.toString()) && books.get(nBTTagCompound2.toString()) != i) {
                if (list.get(books.get(nBTTagCompound2.toString())).func_75211_c() != null) {
                    AtomicInteger atomicInteger = new AtomicInteger(i);
                    threadRunning = true;
                    String string = nBTTagCompound2.toString();
                    new Thread(() -> {
                        this.sleep(150 + ((Double)this.delay.getValue()).intValue());
                        if (this.mc.field_71462_r != null) {
                            this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, books.get(string), 0, 1, this.mc.field_71439_g);
                        }
                        books.remove(string);
                        this.sleep(300 + ((Double)this.delay.getValue()).intValue());
                        if (((Slot)list.get(atomicInteger.get())).func_75211_c() == null) {
                            atomicInteger.set(this.fix(string, list));
                        }
                        if (this.mc.field_71462_r != null) {
                            this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, atomicInteger.get(), 0, 1, this.mc.field_71439_g);
                        }
                        while (((Slot)list.get(13)).func_75211_c().func_77973_b() != Items.field_151134_bR) {
                            if (this.mc.field_71462_r != null) continue;
                            return;
                        }
                        this.sleep(50);
                        if (this.mc.field_71462_r != null) {
                            this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, 22, 2, 3, this.mc.field_71439_g);
                        }
                        this.sleep(250 + ((Double)this.delay.getValue()).intValue());
                        if (this.mc.field_71462_r != null) {
                            this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, 13, 0, 1, this.mc.field_71439_g);
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
            if (slot.func_75211_c() == null || slot.func_75211_c().func_77973_b() != Items.field_151134_bR || (nBTTagCompound2 = (nBTTagCompound = ItemUtils.getExtraAttributes(slot.func_75211_c())).func_74775_l("enchantments")).func_150296_c().size() != 1 || !nBTTagCompound2.toString().equals(string)) continue;
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

