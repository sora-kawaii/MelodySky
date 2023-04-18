/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
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
        if (this.tickCount % 5 == 0 && this.inSalvageGui && this.salvaging && this.mc.currentScreen instanceof GuiChest) {
            List<Slot> list = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
            if (list != null && list.get(31).getStack() != null && list.get(22).getStack() != null) {
                if (list.get(22) != null && list.get(22).getStack() != null & list.get(31).getStack().getItem() == Item.getItemFromBlock(Blocks.stained_hardened_clay)) {
                    this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 31, 0, 0, this.mc.thePlayer);
                    Helper.sendMessage("");
                }
                if (list.get(22) != null && list.get(22).getStack() != null && list.get(22) != null && list.get(22).getStack() != null && list.get(31).getStack().getItem() == Item.getItemFromBlock(Blocks.beacon)) {
                    this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 31, 0, 0, this.mc.thePlayer);
                }
            }
            if (list.get(22).getStack() == null) {
                ArrayList<Slot> arrayList = new ArrayList<Slot>(this.mc.thePlayer.inventoryContainer.inventorySlots);
                arrayList.removeIf(slot -> !AutoSalvage.shouldSalvage(slot.getStack()));
                if (arrayList.isEmpty()) {
                    this.salvaging = false;
                } else {
                    this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 45 + arrayList.get((int)0).slotNumber, 0, 1, this.mc.thePlayer);
                    this.currentSlot = 45 + arrayList.get((int)0).slotNumber;
                }
            }
        }
        ++this.tickCount;
    }

    @EventHandler
    public void onBackgroundRender(EventTick eventTick) {
        GuiScreen guiScreen = this.mc.currentScreen;
        if (guiScreen instanceof GuiChest) {
            Container container = ((GuiChest)guiScreen).inventorySlots;
            if (container instanceof ContainerChest) {
                String string = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
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
        if (this.inSalvageGui && AutoSalvage.shouldSalvage(drawSlotEvent.slot.getStack())) {
            n2 = drawSlotEvent.slot.xDisplayPosition;
            n = drawSlotEvent.slot.yDisplayPosition;
            Gui.drawRect(n2, n, n2 + 16, n + 16, new Color(0, 255, 255, 120).getRGB());
        }
        if (this.inSalvageGui && drawSlotEvent.slot.slotNumber == this.currentSlot) {
            n2 = drawSlotEvent.slot.xDisplayPosition;
            n = drawSlotEvent.slot.yDisplayPosition;
            Gui.drawRect(n2, n, n2 + 16, n + 16, new Color(0, 105, 255, 120).getRGB());
        }
    }

    public static boolean shouldSalvage(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        NBTTagCompound nBTTagCompound = itemStack.getSubCompound("ExtraAttributes", false);
        if (nBTTagCompound == null) {
            return false;
        }
        if (!nBTTagCompound.hasKey("baseStatBoostPercentage") || nBTTagCompound.hasKey("dungeon_item_level")) {
            return false;
        }
        return !ItemUtils.getSkyBlockID(itemStack).equals("ICE_SPRAY_WAND");
    }
}

