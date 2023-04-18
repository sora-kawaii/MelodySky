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
        if (!(this.mc.field_71462_r instanceof GuiInventory)) {
            return;
        }
        if ((this.mc.field_71462_r == null || this.mc.field_71462_r instanceof GuiInventory || this.mc.field_71462_r instanceof GuiChat) && this.timer.hasReached(f)) {
            this.getBestArmor();
        }
    }

    public void getBestArmor() {
        for (int i = 1; i < 5; ++i) {
            if (this.mc.field_71439_g.field_71069_bz.func_75139_a(4 + i).func_75216_d()) {
                ItemStack itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(4 + i).func_75211_c();
                if (this.isBestArmor(itemStack, i)) continue;
                this.drop(4 + i);
            }
            for (int j = 9; j < 45; ++j) {
                ItemStack itemStack;
                if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(j).func_75216_d() || !this.isBestArmor(itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(j).func_75211_c(), i) || !(AutoArmor.getProtection(itemStack) > 0.0f)) continue;
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
        if (!itemStack.func_77977_a().contains(string)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            ItemStack itemStack2;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !(AutoArmor.getProtection(itemStack2 = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()) > f) || !itemStack2.func_77977_a().contains(string)) continue;
            return false;
        }
        return true;
    }

    public void shiftClick(int n) {
        this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71069_bz.field_75152_c, n, 0, 1, this.mc.field_71439_g);
    }

    public void drop(int n) {
        this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71069_bz.field_75152_c, n, 1, 4, this.mc.field_71439_g);
    }

    public static float getProtection(ItemStack itemStack) {
        float f = 0.0f;
        if (itemStack.func_77973_b() instanceof ItemArmor) {
            ItemArmor itemArmor = (ItemArmor)itemStack.func_77973_b();
            f += (float)((double)itemArmor.field_77879_b + (double)((100 - itemArmor.field_77879_b) * EnchantmentHelper.func_77506_a((int)Enchantment.field_180310_c.field_77352_x, (ItemStack)itemStack)) * 0.0075);
            f += (float)((double)EnchantmentHelper.func_77506_a((int)Enchantment.field_77327_f.field_77352_x, (ItemStack)itemStack) / 100.0);
            f += (float)((double)EnchantmentHelper.func_77506_a((int)Enchantment.field_77329_d.field_77352_x, (ItemStack)itemStack) / 100.0);
            f += (float)((double)EnchantmentHelper.func_77506_a((int)Enchantment.field_92091_k.field_77352_x, (ItemStack)itemStack) / 100.0);
            f += (float)((double)EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)itemStack) / 50.0);
            f += (float)((double)EnchantmentHelper.func_77506_a((int)Enchantment.field_180309_e.field_77352_x, (ItemStack)itemStack) / 100.0);
        }
        return f;
    }

    public static enum EMode {
        OpenInv;

    }
}

