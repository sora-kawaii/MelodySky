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
    public void onTick(EventTick eventTick) {
        List<Slot> list;
        if (this.tickCount % 2 == 0 && Client.inSkyblock && this.inTradeMenu && this.mc.currentScreen instanceof GuiChest && (list = ((GuiChest)this.mc.currentScreen).inventorySlots.inventorySlots).get(49).getStack() != null && list.get(49).getStack().getItem() != Item.getItemFromBlock(Blocks.barrier)) {
            for (Slot slot : this.mc.thePlayer.inventoryContainer.inventorySlots) {
                if (!this.shouldSell(slot.getStack())) continue;
                this.windowClick(this.mc.thePlayer.openContainer.windowId, slot, 2, 3);
                break;
            }
        }
        ++this.tickCount;
    }

    @EventHandler
    public void onBackgroundRender(EventTick eventTick) {
        Container container;
        GuiScreen guiScreen = this.mc.currentScreen;
        if (guiScreen instanceof GuiChest && (container = ((GuiChest)guiScreen).inventorySlots) instanceof ContainerChest) {
            String string = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
            this.inTradeMenu = string.equals("Trades");
        }
    }

    @EventHandler
    public void onDrawSlot(DrawSlotEvent drawSlotEvent) {
        if (this.inTradeMenu && this.shouldSell(drawSlotEvent.slot.getStack())) {
            int n = drawSlotEvent.slot.xDisplayPosition;
            int n2 = drawSlotEvent.slot.yDisplayPosition;
            Gui.drawRect(n, n2, n + 16, n2 + 16, new Color(128, 0, 128, 120).getRGB());
        }
    }

    private boolean shouldSell(ItemStack itemStack) {
        if (itemStack != null) {
            if (((Boolean)this.salvable.getValue()).booleanValue() && AutoSalvage.shouldSalvage(itemStack)) {
                return true;
            }
            if (((Boolean)this.boooom.getValue()).booleanValue() && ItemUtils.getSkyBlockID(itemStack).equals("SUPERBOOM_TNT")) {
                return true;
            }
            if (((Boolean)this.rs.getValue()).booleanValue() && ItemUtils.getSkyBlockID(itemStack).equals("REVIVE_STONE")) {
                return true;
            }
            if (((Boolean)this.runes.getValue()).booleanValue() && itemStack.getDisplayName().contains("Rune")) {
                return true;
            }
            if (((Boolean)this.DJ.getValue()).booleanValue()) {
                for (String string : dungeonJunk) {
                    if (!itemStack.getDisplayName().contains(string)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private void windowClick(int n, Slot slot, int n2, int n3) {
        short s = this.mc.thePlayer.openContainer.getNextTransactionID(this.mc.thePlayer.inventory);
        ItemStack itemStack = slot.getStack();
        this.mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(n, 45 + slot.slotNumber, n2, n3, itemStack, s));
    }
}

