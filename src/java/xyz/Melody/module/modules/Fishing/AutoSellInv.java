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
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoSellInv
extends Module {
    private boolean shouldOpenBazaar = false;
    private boolean bazaarOpen = false;
    private boolean clickedSellInv = false;
    private boolean clickSell1 = false;
    private boolean clickedBack = false;
    private boolean clickedSellSack = false;
    private boolean clickSell2 = false;
    private Numbers<Double> tickValue = new Numbers<Double>("ClickTicks", 20.0, 10.0, 60.0, 5.0);
    private Option<Boolean> openFBag = new Option<Boolean>("Sack", true);
    private int ticks = 0;

    public AutoSellInv() {
        super("AutoSellInv", new String[]{"afb"}, ModuleType.Fishing);
        this.addValues(this.tickValue, this.openFBag);
        this.setColor(new Color(191, 191, 191).getRGB());
        this.setModInfo("Auto Sell Inventory(Sacks).");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.reset();
        super.onDisable();
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onChat(ClientChatReceivedEvent event) {
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (message.toLowerCase().equals("your inventory is full!")) {
            NotificationPublisher.queue("Inventory Full!", "Trying To Sell Inv and Sacks.", NotificationType.WARN, 3000);
            this.reset();
            this.shouldOpenBazaar = true;
        }
    }

    @EventHandler
    public void onDrawGuiBackground(EventTick event) {
        Container container;
        if (this.ticks < ((Double)this.tickValue.getValue()).intValue()) {
            ++this.ticks;
            return;
        }
        this.ticks = 0;
        if (this.shouldOpenBazaar) {
            this.openBazaar();
        }
        GuiScreen gui = this.mc.currentScreen;
        if (Client.inSkyblock && gui instanceof GuiChest && (container = ((GuiChest)gui).inventorySlots) instanceof ContainerChest) {
            String chestName = this.getGuiName(gui);
            if (chestName.startsWith("Bazaar") && !this.bazaarOpen) {
                this.bazaarOpen = true;
            }
            if (this.bazaarOpen && !this.clickedSellInv) {
                this.clickSlot(47, 0);
                this.clickedSellInv = true;
                return;
            }
            if (this.clickedSellInv && !this.clickSell1) {
                if (this.getGuiName(gui).startsWith("Are")) {
                    this.clickSlot(11, 0);
                }
                this.clickSell1 = true;
                return;
            }
            if (this.clickSell1 && !this.clickedBack) {
                if (this.getGuiName(gui).startsWith("Are")) {
                    this.clickSlot(22, 0);
                }
                this.clickedBack = true;
                return;
            }
            if (this.clickedBack && !this.clickedSellSack) {
                this.clickSlot(48, 0);
                this.clickedSellSack = true;
                return;
            }
            if (this.clickedSellSack && !this.clickSell2) {
                if (this.getGuiName(gui).startsWith("Are")) {
                    this.clickSlot(11, 0);
                }
                this.clickSell2 = true;
                return;
            }
            if (this.clickSell2) {
                this.mc.thePlayer.closeScreen();
                this.reset();
            }
        }
    }

    private void reset() {
        this.bazaarOpen = false;
        this.clickedSellInv = false;
        this.clickSell1 = false;
        this.clickedBack = false;
        this.clickedSellSack = false;
        this.clickSell2 = false;
    }

    public void openBazaar() {
        this.mc.thePlayer.sendChatMessage("/bazaar");
        this.bazaarOpen = true;
        this.shouldOpenBazaar = false;
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

