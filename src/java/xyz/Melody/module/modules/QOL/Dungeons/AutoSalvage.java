/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.container.DrawSlotEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoSalvage
extends Module {
    private boolean inSalvageGui = false;
    private boolean salvaging = false;
    private int tickCount = 0;
    private int currentSlot = 0;

    public AutoSalvage() {
        super("AutoSalvage", new String[]{"as"}, ModuleType.Dungeons);
        this.setModInfo("Auto Salvage Items.");
    }

    @EventHandler
    public void onTick(EventTick eventTick) {
        if (this.tickCount % 5 == 0 && this.inSalvageGui && this.salvaging && this.mc.field_71462_r instanceof GuiChest) {
            List list = ((GuiChest)this.mc.field_71462_r).field_147002_h.field_75151_b;
            if (list != null && ((Slot)list.get(31)).func_75211_c() != null && ((Slot)list.get(22)).func_75211_c() != null) {
                if (list.get(22) != null && ((Slot)list.get(22)).func_75211_c() != null & ((Slot)list.get(31)).func_75211_c().func_77973_b() == Item.func_150898_a((Block)Blocks.field_150406_ce)) {
                    this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, 31, 0, 0, this.mc.field_71439_g);
                    Helper.sendMessage("");
                }
                if (list.get(22) != null && ((Slot)list.get(22)).func_75211_c() != null && list.get(22) != null && ((Slot)list.get(22)).func_75211_c() != null && ((Slot)list.get(31)).func_75211_c().func_77973_b() == Item.func_150898_a((Block)Blocks.field_150461_bJ)) {
                    this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, 31, 0, 0, this.mc.field_71439_g);
                }
            }
            if (((Slot)list.get(22)).func_75211_c() == null) {
                ArrayList<Slot> arrayList = new ArrayList<Slot>(this.mc.field_71439_g.field_71069_bz.field_75151_b);
                arrayList.removeIf(slot -> !AutoSalvage.shouldSalvage(slot.func_75211_c()));
                if (arrayList.isEmpty()) {
                    this.salvaging = false;
                } else {
                    this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71070_bA.field_75152_c, 45 + ((Slot)arrayList.get((int)0)).field_75222_d, 0, 1, this.mc.field_71439_g);
                    this.currentSlot = 45 + ((Slot)arrayList.get((int)0)).field_75222_d;
                }
            }
        }
        ++this.tickCount;
    }

    @EventHandler
    public void onBackgroundRender(EventTick eventTick) {
        GuiScreen guiScreen = this.mc.field_71462_r;
        if (guiScreen instanceof GuiChest) {
            Container container = ((GuiChest)guiScreen).field_147002_h;
            if (container instanceof ContainerChest) {
                String string = ((ContainerChest)container).func_85151_d().func_145748_c_().func_150260_c();
                this.inSalvageGui = string.equals("Salvage Item");
                this.salvaging = this.inSalvageGui;
            }
        } else {
            this.inSalvageGui = false;
        }
    }

    @EventHandler
    public void onDrawSlot(DrawSlotEvent drawSlotEvent) {
        int n;
        int n2;
        if (this.inSalvageGui && AutoSalvage.shouldSalvage(drawSlotEvent.slot.func_75211_c())) {
            n2 = drawSlotEvent.slot.field_75223_e;
            n = drawSlotEvent.slot.field_75221_f;
            Gui.func_73734_a((int)n2, (int)n, (int)(n2 + 16), (int)(n + 16), (int)new Color(0, 255, 255, 120).getRGB());
        }
        if (this.inSalvageGui && drawSlotEvent.slot.field_75222_d == this.currentSlot) {
            n2 = drawSlotEvent.slot.field_75223_e;
            n = drawSlotEvent.slot.field_75221_f;
            Gui.func_73734_a((int)n2, (int)n, (int)(n2 + 16), (int)(n + 16), (int)new Color(0, 105, 255, 120).getRGB());
        }
    }

    public static boolean shouldSalvage(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        NBTTagCompound nBTTagCompound = itemStack.func_179543_a("ExtraAttributes", false);
        if (nBTTagCompound == null) {
            return false;
        }
        if (!nBTTagCompound.func_74764_b("baseStatBoostPercentage") || nBTTagCompound.func_74764_b("dungeon_item_level")) {
            return false;
        }
        return !ItemUtils.getSkyBlockID(itemStack).equals("ICE_SPRAY_WAND");
    }
}

