/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.MainWorld;

import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class MelodyPlayer
extends Module {
    private long lastInteractTime;
    public boolean click;
    private String harpTag;
    private Numbers<Double> delay = new Numbers<Double>("Delay", 100.0, 0.0, 300.0, 10.0);
    private Mode<Enum> clickMode = new Mode("ClickMode", (Enum[])cm.values(), (Enum)cm.Middle);

    public MelodyPlayer() {
        super("AutoMelody", new String[]{"am"}, ModuleType.QOL);
        this.addValues(this.clickMode, this.delay);
        this.setModInfo("Auto Play Melody.");
    }

    @EventHandler
    private void onTick(EventTick event) {
        if (this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChest && Client.inSkyblock && this.getGuiName(this.mc.currentScreen).startsWith("Harp -")) {
            ContainerChest chest = (ContainerChest)this.mc.thePlayer.openContainer;
            IInventory chestInv = chest.getLowerChestInventory();
            List invSlots = chest.inventorySlots;
            if (!this.click) {
                StringBuilder currentTag = new StringBuilder();
                for (int i = 1; i <= 34; ++i) {
                    if (chestInv.getStackInSlot(i) == null) continue;
                    currentTag.append(chestInv.getStackInSlot(i).getItem());
                }
                if (!currentTag.toString().equals(this.harpTag)) {
                    this.harpTag = currentTag.toString();
                    this.lastInteractTime = 0L;
                    this.click = true;
                }
            }
            if (this.click) {
                if (this.lastInteractTime == 0L) {
                    this.lastInteractTime = System.currentTimeMillis();
                    return;
                }
                if ((double)(System.currentTimeMillis() - this.lastInteractTime) >= (Double)this.delay.getValue()) {
                    int woolPos = -1;
                    Slot woolSlot = null;
                    for (int i = 28; i <= 34; ++i) {
                        if (chestInv.getStackInSlot(i) == null || chestInv.getStackInSlot(i).getItem() != Item.getItemFromBlock(Blocks.wool)) continue;
                        woolPos = i + 9;
                        woolSlot = (Slot)invSlots.get(i + 9);
                        break;
                    }
                    if (woolPos == -1) {
                        this.lastInteractTime = 0L;
                        this.click = false;
                        return;
                    }
                    switch (((Enum)this.clickMode.getValue()).toString().toLowerCase()) {
                        case "middle": {
                            this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, woolPos, 2, 3, this.mc.thePlayer);
                            break;
                        }
                        case "left": {
                            this.windowClick(this.mc.thePlayer.openContainer.windowId, woolSlot, 0, 0);
                            break;
                        }
                        case "right": {
                            this.windowClick(this.mc.thePlayer.openContainer.windowId, woolSlot, 1, 0);
                        }
                    }
                    this.lastInteractTime = 0L;
                    this.click = false;
                }
            }
        }
    }

    private void windowClick(int windowId, Slot slot, int mouseButtonClicked, int mode) {
        short tid = this.mc.thePlayer.openContainer.getNextTransactionID(this.mc.thePlayer.inventory);
        ItemStack itemstack = slot.getStack();
        this.mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(windowId, slot.slotNumber, mouseButtonClicked, mode, itemstack, tid));
    }

    public String getGuiName(GuiScreen gui) {
        if (gui instanceof GuiChest) {
            return ((ContainerChest)((GuiChest)gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
    }

    public String getInventoryName() {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return "null";
        }
        return this.mc.thePlayer.openContainer.inventorySlots.get((int)0).inventory.getName();
    }

    static enum cm {
        Middle,
        Left,
        Right;

    }
}

