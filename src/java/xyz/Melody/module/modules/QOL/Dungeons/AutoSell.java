/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import java.awt.Color;
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
import net.minecraft.network.play.client.C0EPacketClickWindow;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.container.DrawSlotEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.modules.QOL.Dungeons.AutoSalvage;

public final class AutoSell
extends Module {
    private Option<Boolean> DJ = new Option<Boolean>("DungeonJunks", true);
    private Option<Boolean> salvable = new Option<Boolean>("SalvageAble", false);
    private Option<Boolean> boooom = new Option<Boolean>("SuperBoooom", true);
    private Option<Boolean> rs = new Option<Boolean>("ReviveStones", true);
    private Option<Boolean> runes = new Option<Boolean>("Runes", true);
    private boolean inTradeMenu = false;
    private int tickCount = 0;
    private static final String[] dungeonJunk = new String[]{"Training Weight", "Healing VIII Splash Potion", "Healing 8 Slash Potion", "Premium Flesh", "Mimic Fragment", "Enchanted Rotten Flesh", "Enchanted Bone", "Defuse Kit", "Enchanted Ice", "Optic Lense", "Tripwire Hook", "Button", "Carpet", "Lever", "Journal Entry", "Sign"};

    public AutoSell() {
        super("AutoSell", new String[]{"as"}, ModuleType.Dungeons);
        this.addValues(this.DJ, this.salvable, this.boooom, this.runes, this.rs);
        this.setModInfo("Auto Sell Useless Items.");
    }

    @EventHandler
    public void onTick(EventTick event) {
        List<Slot> chestInventory;
        if (this.tickCount % 2 == 0 && Client.inSkyblock && this.inTradeMenu && this.mc.currentScreen instanceof GuiChest && (chestInventory = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots).get(49).getStack() != null && chestInventory.get(49).getStack().getItem() != Item.getItemFromBlock(Blocks.barrier)) {
            for (Slot slot : this.mc.thePlayer.inventoryContainer.inventorySlots) {
                if (!this.shouldSell(slot.getStack())) continue;
                this.windowClick(this.mc.thePlayer.openContainer.windowId, slot, 2, 3);
                break;
            }
        }
        ++this.tickCount;
    }

    @EventHandler
    public void onBackgroundRender(EventTick event) {
        Container container;
        GuiScreen gui = this.mc.currentScreen;
        if (gui instanceof GuiChest && (container = ((GuiChest)gui).inventorySlots) instanceof ContainerChest) {
            String chestName = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
            this.inTradeMenu = chestName.equals("Trades");
        }
    }

    @EventHandler
    public void onDrawSlot(DrawSlotEvent event) {
        if (this.inTradeMenu && this.shouldSell(event.slot.getStack())) {
            int x = event.slot.xDisplayPosition;
            int y = event.slot.yDisplayPosition;
            Gui.drawRect(x, y, x + 16, y + 16, new Color(128, 0, 128, 120).getRGB());
        }
    }

    private boolean shouldSell(ItemStack item) {
        if (item != null) {
            if (((Boolean)this.salvable.getValue()).booleanValue() && AutoSalvage.shouldSalvage(item)) {
                return true;
            }
            if (((Boolean)this.boooom.getValue()).booleanValue() && ItemUtils.getSkyBlockID(item).equals("SUPERBOOM_TNT")) {
                return true;
            }
            if (((Boolean)this.rs.getValue()).booleanValue() && ItemUtils.getSkyBlockID(item).equals("REVIVE_STONE")) {
                return true;
            }
            if (((Boolean)this.runes.getValue()).booleanValue() && item.getDisplayName().contains("Rune")) {
                return true;
            }
            if (((Boolean)this.DJ.getValue()).booleanValue()) {
                for (String name : dungeonJunk) {
                    if (!item.getDisplayName().contains(name)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private void windowClick(int windowId, Slot slot, int mouseButtonClicked, int mode) {
        short tid = this.mc.thePlayer.openContainer.getNextTransactionID(this.mc.thePlayer.inventory);
        ItemStack itemstack = slot.getStack();
        this.mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(windowId, 45 + slot.slotNumber, mouseButtonClicked, mode, itemstack, tid));
    }
}

