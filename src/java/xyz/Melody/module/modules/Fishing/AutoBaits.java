/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.Fishing;

import java.awt.Color;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoBaits
extends Module {
    private boolean bazaarOpen = false;
    private boolean fishingCatOpen = false;
    private boolean baitsOpen = false;
    private boolean fishBaitsOpen = false;
    private boolean buyingFishBaits = false;
    private boolean boughtBaits = false;
    private boolean petOpen = false;
    private boolean sbmenuOpen = false;
    private boolean baitStorageOpen = false;
    private Numbers<Double> tickValue = new Numbers<Double>("ClickTicks", 20.0, 10.0, 60.0, 5.0);
    private Option<Boolean> openFBag = new Option<Boolean>("OpenBaitsBag", true);
    private int ticks = 0;

    public AutoBaits() {
        super("AutoFishBaits", new String[]{"afb"}, ModuleType.Fishing);
        this.addValues(this.tickValue, this.openFBag);
        this.setColor(new Color(191, 191, 191).getRGB());
        this.setModInfo("Auto Buy Fish Baits(Fill Inv).");
    }

    @Override
    public void onEnable() {
        this.openBazaar();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.reset();
        super.onDisable();
    }

    @EventHandler
    public void onDrawGuiBackground(EventTick event) {
        Container container;
        if (this.ticks < ((Double)this.tickValue.getValue()).intValue()) {
            ++this.ticks;
            return;
        }
        this.ticks = 0;
        GuiScreen gui = this.mc.currentScreen;
        if (Client.inSkyblock && gui instanceof GuiChest && (container = ((GuiChest)gui).inventorySlots) instanceof ContainerChest) {
            String chestName = this.getGuiName(gui);
            if (chestName.startsWith("Bazaar") && !this.bazaarOpen) {
                this.bazaarOpen = true;
            }
            if (this.bazaarOpen && !this.fishingCatOpen) {
                this.clickSlot(27, 0);
                this.fishingCatOpen = true;
                return;
            }
            if (this.fishingCatOpen && !this.baitsOpen) {
                this.clickSlot(30, 0);
                this.baitsOpen = true;
                return;
            }
            if (this.baitsOpen && !this.fishBaitsOpen) {
                this.clickSlot(12, 0);
                this.fishBaitsOpen = true;
                return;
            }
            if (this.fishBaitsOpen && !this.buyingFishBaits) {
                this.clickSlot(10, 0);
                this.buyingFishBaits = true;
                return;
            }
            if (this.buyingFishBaits && !this.boughtBaits) {
                this.clickSlot(14, 0);
                this.boughtBaits = true;
                return;
            }
            if (this.boughtBaits && !this.petOpen) {
                this.mc.thePlayer.closeScreen();
                this.petOpen = true;
                if (!((Boolean)this.openFBag.getValue()).booleanValue()) {
                    this.setEnabled(false);
                }
            }
            if (((Boolean)this.openFBag.getValue()).booleanValue()) {
                if (this.sbmenuOpen && !this.baitStorageOpen) {
                    this.clickSlot(48, 0);
                    this.baitStorageOpen = true;
                    return;
                }
                if (this.baitStorageOpen) {
                    this.clickSlot(43, 0);
                    this.setEnabled(false);
                    return;
                }
            }
        }
        if (Client.inSkyblock && this.petOpen && !this.sbmenuOpen) {
            this.mc.thePlayer.sendChatMessage("/pet");
            this.sbmenuOpen = true;
            return;
        }
    }

    private void reset() {
        this.petOpen = false;
        this.baitsOpen = false;
        this.sbmenuOpen = false;
        this.bazaarOpen = false;
        this.boughtBaits = false;
        this.fishBaitsOpen = false;
        this.fishingCatOpen = false;
        this.buyingFishBaits = false;
        this.baitStorageOpen = false;
    }

    public void openBazaar() {
        this.mc.thePlayer.sendChatMessage("/bazaar");
        this.bazaarOpen = true;
    }

    public String getGuiName(GuiScreen gui) {
        if (gui instanceof GuiChest) {
            return ((ContainerChest)((GuiChest)gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
    }

    private void clickSlot(int slot, int incrementWindowId) {
        this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId + incrementWindowId, slot, 2, 3, this.mc.thePlayer);
    }
}

