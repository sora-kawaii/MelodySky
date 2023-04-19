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
    public void onEvent(EventTick event) {
        float delay = ((Double)this.delay.getValue()).floatValue();
        if (!(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if ((this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory || this.mc.currentScreen instanceof GuiChat) && this.timer.hasReached(delay)) {
            this.getBestArmor();
        }
    }

    public void getBestArmor() {
        for (int type = 1; type < 5; ++type) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (this.isBestArmor(is, type)) continue;
                this.drop(4 + type);
            }
            for (int i = 9; i < 45; ++i) {
                ItemStack is2;
                if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestArmor(is2 = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack(), type) || !(AutoArmor.getProtection(is2) > 0.0f)) continue;
                this.shiftClick(i);
                this.timer.reset();
            }
        }
    }

    public boolean isBestArmor(ItemStack stack, int type) {
        float prot = AutoArmor.getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        } else if (type == 2) {
            strType = "chestplate";
        } else if (type == 3) {
            strType = "leggings";
        } else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(AutoArmor.getProtection(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > prot) || !is.getUnlocalizedName().contains(strType)) continue;
            return false;
        }
        return true;
    }

    public void shiftClick(int slot) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, this.mc.thePlayer);
    }

    public void drop(int slot) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, this.mc.thePlayer);
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot += (float)((double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075);
            prot += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            prot += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            prot += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            prot += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            prot += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack) / 100.0);
        }
        return prot;
    }

    public static enum EMode {
        OpenInv;

    }
}

