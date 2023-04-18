/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoArmor
extends Module {
    public Numbers<Double> delay = new Numbers<Double>("Delay", 100.0, 50.0, 1000.0, 50.0);
    private TimerUtil timer = new TimerUtil();

    public AutoArmor() {
        super("AutoArmor", new String[]{"AutoArmor"}, ModuleType.Balance);
        super.addValues(this.delay);
    }

    @EventHandler
    public void onEvent(EventTick eventTick) {
        float f = ((Double)this.delay.getValue()).floatValue();
        if (!(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if ((this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory || this.mc.currentScreen instanceof GuiChat) && this.timer.hasReached(f)) {
            this.getBestArmor();
        }
    }

    public void getBestArmor() {
        for (int i = 1; i < 5; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(4 + i).getHasStack()) {
                ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(4 + i).getStack();
                if (this.isBestArmor(itemStack, i)) continue;
                this.drop(4 + i);
            }
            for (int j = 9; j < 45; ++j) {
                ItemStack itemStack;
                if (!this.mc.thePlayer.inventoryContainer.getSlot(j).getHasStack() || !this.isBestArmor(itemStack = this.mc.thePlayer.inventoryContainer.getSlot(j).getStack(), i) || !(AutoArmor.getProtection(itemStack) > 0.0f)) continue;
                this.shiftClick(j);
                this.timer.reset();
            }
        }
    }

    public boolean isBestArmor(ItemStack itemStack, int n) {
        float f = AutoArmor.getProtection(itemStack);
        String string = "";
        if (n == 1) {
            string = "helmet";
        } else if (n == 2) {
            string = "chestplate";
        } else if (n == 3) {
            string = "leggings";
        } else if (n == 4) {
            string = "boots";
        }
        if (!itemStack.getUnlocalizedName().contains(string)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            ItemStack itemStack2;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(AutoArmor.getProtection(itemStack2 = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > f) || !itemStack2.getUnlocalizedName().contains(string)) continue;
            return false;
        }
        return true;
    }

    public void shiftClick(int n) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, n, 0, 1, this.mc.thePlayer);
    }

    public void drop(int n) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, n, 1, 4, this.mc.thePlayer);
    }

    public static float getProtection(ItemStack itemStack) {
        float f = 0.0f;
        if (itemStack.getItem() instanceof ItemArmor) {
            ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
            f += (float)((double)itemArmor.damageReduceAmount + (double)((100 - itemArmor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack)) * 0.0075);
            f += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, itemStack) / 100.0);
            f += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, itemStack) / 100.0);
            f += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, itemStack) / 100.0);
            f += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 50.0);
            f += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, itemStack) / 100.0);
        }
        return f;
    }

    public static enum EMode {
        OpenInv;

    }
}

