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
    public void onTick(EventTick event) {
        if (this.tickCount % 5 == 0 && this.inSalvageGui && this.salvaging && this.mc.currentScreen instanceof GuiChest) {
            List<Slot> chestInventory = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots;
            if (chestInventory != null && chestInventory.get(31).getStack() != null && chestInventory.get(22).getStack() != null) {
                if (chestInventory.get(22) != null && chestInventory.get(22).getStack() != null & chestInventory.get(31).getStack().getItem() == Item.getItemFromBlock(Blocks.stained_hardened_clay)) {
                    this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 31, 0, 0, this.mc.thePlayer);
                    Helper.sendMessage("");
                }
                if (chestInventory.get(22) != null && chestInventory.get(22).getStack() != null && chestInventory.get(22) != null && chestInventory.get(22).getStack() != null && chestInventory.get(31).getStack().getItem() == Item.getItemFromBlock(Blocks.beacon)) {
                    this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 31, 0, 0, this.mc.thePlayer);
                }
            }
            if (chestInventory.get(22).getStack() == null) {
                ArrayList<Slot> itemsToSalvage = new ArrayList<Slot>(this.mc.thePlayer.inventoryContainer.inventorySlots);
                itemsToSalvage.removeIf(slot -> !AutoSalvage.shouldSalvage(slot.getStack()));
                if (itemsToSalvage.isEmpty()) {
                    this.salvaging = false;
                } else {
                    this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, 45 + itemsToSalvage.get((int)0).slotNumber, 0, 1, this.mc.thePlayer);
                    this.currentSlot = 45 + itemsToSalvage.get((int)0).slotNumber;
                }
            }
        }
        ++this.tickCount;
    }

    @EventHandler
    public void onBackgroundRender(EventTick event) {
        GuiScreen gui = this.mc.currentScreen;
        if (gui instanceof GuiChest) {
            Container container = ((GuiChest)gui).inventorySlots;
            if (container instanceof ContainerChest) {
                String chestName = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
                this.inSalvageGui = chestName.equals("Salvage Item");
                this.salvaging = this.inSalvageGui;
            }
        } else {
            this.inSalvageGui = false;
        }
    }

    @EventHandler
    public void onDrawSlot(DrawSlotEvent event) {
        int y;
        int x;
        if (this.inSalvageGui && AutoSalvage.shouldSalvage(event.slot.getStack())) {
            x = event.slot.xDisplayPosition;
            y = event.slot.yDisplayPosition;
            Gui.drawRect(x, y, x + 16, y + 16, new Color(0, 255, 255, 120).getRGB());
        }
        if (this.inSalvageGui && event.slot.slotNumber == this.currentSlot) {
            x = event.slot.xDisplayPosition;
            y = event.slot.yDisplayPosition;
            Gui.drawRect(x, y, x + 16, y + 16, new Color(0, 105, 255, 120).getRGB());
        }
    }

    public static boolean shouldSalvage(ItemStack item) {
        if (item == null) {
            return false;
        }
        NBTTagCompound attributes = item.getSubCompound("ExtraAttributes", false);
        if (attributes == null) {
            return false;
        }
        if (!attributes.hasKey("baseStatBoostPercentage") || attributes.hasKey("dungeon_item_level")) {
            return false;
        }
        return !ItemUtils.getSkyBlockID(item).equals("ICE_SPRAY_WAND");
    }
}

