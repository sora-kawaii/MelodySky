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
    private void onTick(EventTick eventTick) {
        if (this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChest && Client.inSkyblock && this.getGuiName(this.mc.currentScreen).startsWith("Harp -")) {
            ContainerChest containerChest = (ContainerChest)this.mc.thePlayer.openContainer;
            IInventory iInventory = containerChest.getLowerChestInventory();
            List list = containerChest.inventorySlots;
            if (!this.click) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i <= 34; ++i) {
                    if (iInventory.getStackInSlot(i) == null) continue;
                    stringBuilder.append(iInventory.getStackInSlot(i).getItem());
                }
                if (!stringBuilder.toString().equals(this.harpTag)) {
                    this.harpTag = stringBuilder.toString();
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
                    int n = -1;
                    Slot slot = null;
                    for (int i = 28; i <= 34; ++i) {
                        if (iInventory.getStackInSlot(i) == null || iInventory.getStackInSlot(i).getItem() != Item.getItemFromBlock(Blocks.wool)) continue;
                        n = i + 9;
                        slot = (Slot)list.get(i + 9);
                        break;
                    }
                    if (n == -1) {
                        this.lastInteractTime = 0L;
                        this.click = false;
                        return;
                    }
                    switch (((Enum)this.clickMode.getValue()).toString().toLowerCase()) {
                        case "middle": {
                            this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, n, 2, 3, this.mc.thePlayer);
                            break;
                        }
                        case "left": {
                            this.windowClick(this.mc.thePlayer.openContainer.windowId, slot, 0, 0);
                            break;
                        }
                        case "right": {
                            this.windowClick(this.mc.thePlayer.openContainer.windowId, slot, 1, 0);
                        }
                    }
                    this.lastInteractTime = 0L;
                    this.click = false;
                }
            }
        }
    }

    private void windowClick(int n, Slot slot, int n2, int n3) {
        short s = this.mc.thePlayer.openContainer.getNextTransactionID(this.mc.thePlayer.inventory);
        ItemStack itemStack = slot.getStack();
        this.mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(n, slot.slotNumber, n2, n3, itemStack, s));
    }

    public String getGuiName(GuiScreen guiScreen) {
        if (guiScreen instanceof GuiChest) {
            return ((ContainerChest)((GuiChest)guiScreen).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
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

