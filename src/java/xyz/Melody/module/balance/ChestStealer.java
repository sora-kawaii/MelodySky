/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import java.awt.Color;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class ChestStealer
extends Module {
    private Numbers<Double> delay = new Numbers<Double>("Delay", 70.0, 0.0, 1000.0, 10.0);
    private Option<Boolean> menucheck = new Option<Boolean>("MenuCheck", true);
    private TimerUtil timer = new TimerUtil();

    public ChestStealer() {
        super("ChestStealer", new String[]{"cheststeal", "chests", "stealer"}, ModuleType.Balance);
        this.addValues(this.delay, this.menucheck);
        this.setColor(new Color(218, 97, 127).getRGB());
    }

    @EventHandler
    private void onUpdate(EventTick event) {
        this.setSuffix(this.delay.getValue());
        if (this.mc.thePlayer != null && this.mc.thePlayer.openContainer != null && this.mc.thePlayer.openContainer instanceof ContainerChest) {
            int i = 0;
            ContainerChest container = (ContainerChest)this.mc.thePlayer.openContainer;
            if (StatCollector.translateToLocal("container.chest").equalsIgnoreCase(container.getLowerChestInventory().getDisplayName().getUnformattedText()) || StatCollector.translateToLocal("container.chestDouble").equalsIgnoreCase(container.getLowerChestInventory().getDisplayName().getUnformattedText()) && ((Boolean)this.menucheck.getValue()).booleanValue()) {
                for (i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                    if (container.getLowerChestInventory().getStackInSlot(i) == null || !this.timer.hasReached((Double)this.delay.getValue())) continue;
                    this.mc.playerController.windowClick(container.windowId, i, 0, 1, this.mc.thePlayer);
                    this.timer.reset();
                }
                if (this.isEmpty()) {
                    this.mc.thePlayer.closeScreen();
                }
            } else {
                return;
            }
        }
    }

    private boolean isEmpty() {
        if (this.mc.thePlayer.openContainer != null && this.mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest)this.mc.thePlayer.openContainer;
            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                ItemStack itemStack = container.getLowerChestInventory().getStackInSlot(i);
                if (itemStack == null || itemStack.getItem() == null) continue;
                return false;
            }
        }
        return true;
    }
}

