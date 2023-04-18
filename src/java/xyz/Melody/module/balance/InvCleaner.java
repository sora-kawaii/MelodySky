/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.game.InventoryUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AutoArmor;

public final class InvCleaner
extends Module {
    private Numbers<Double> BlockCap = new Numbers<Double>("MaxBlocks", 128.0, 0.0, 256.0, 8.0);
    private Numbers<Double> Delay = new Numbers<Double>("Delay", 100.0, 50.0, 1000.0, 100.0);
    private Option<Boolean> UHC = new Option<Boolean>("UHC", false);
    private Numbers<Double> weaponSlot = new Numbers<Double>("WeaponSlot", 36.0, 36.0, 44.0, 1.0);
    private Numbers<Double> pickaxeSlot = new Numbers<Double>("PickaxeSlot", 37.0, 36.0, 44.0, 1.0);
    private Numbers<Double> axeSlot = new Numbers<Double>("AxeSlot", 38.0, 36.0, 44.0, 1.0);
    private Numbers<Double> shovelSlot = new Numbers<Double>("ShovelSlot", 39.0, 36.0, 44.0, 1.0);
    private TimerUtil timer = new TimerUtil();

    public InvCleaner() {
        super("InvManager", new String[]{"InvCleaner"}, ModuleType.Balance);
        this.addValues(this.BlockCap, this.Delay, this.weaponSlot, this.pickaxeSlot, this.axeSlot, this.shovelSlot, this.UHC);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventHandler
    public void onEvent(EventTick eventTick) {
        ItemStack itemStack;
        int n;
        if (this.mc.field_71439_g.field_71070_bA instanceof ContainerChest && this.mc.field_71462_r instanceof GuiContainer) {
            return;
        }
        int n2 = ((Double)this.weaponSlot.getValue()).intValue();
        int n3 = ((Double)this.weaponSlot.getValue()).intValue();
        int n4 = ((Double)this.weaponSlot.getValue()).intValue();
        int n5 = ((Double)this.weaponSlot.getValue()).intValue();
        long l2 = ((Double)this.Delay.getValue()).longValue();
        AutoArmor autoArmor = (AutoArmor)Client.instance.getModuleManager().getModuleByClass(AutoArmor.class);
        long l3 = ((Double)autoArmor.delay.getValue()).longValue();
        if (this.timer.hasReached(l3) && this.mc.field_71462_r instanceof GuiInventory) {
            this.getBestArmor();
        }
        for (n = 1; n < 5; ++n) {
            if (!(this.mc.field_71439_g.field_71069_bz.func_75139_a(4 + n).func_75216_d() ? !autoArmor.isBestArmor(itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(4 + n).func_75211_c(), n) : this.invContainsType(n - 1))) continue;
            return;
        }
        if (!(this.mc.field_71462_r instanceof GuiInventory)) {
            return;
        }
        if (this.mc.field_71462_r == null || this.mc.field_71462_r instanceof GuiInventory || this.mc.field_71462_r instanceof GuiChat) {
            if (this.timer.hasReached(l2) && n2 >= 36) {
                if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(n2).func_75216_d()) {
                    this.getBestWeapon(n2);
                } else if (!this.isBestWeapon(this.mc.field_71439_g.field_71069_bz.func_75139_a(n2).func_75211_c())) {
                    this.getBestWeapon(n2);
                }
            }
            if (this.timer.hasReached(l2) && n3 >= 36) {
                this.getBestPickaxe(n3);
            }
            if (this.timer.hasReached(l2) && n4 >= 36) {
                this.getBestShovel(n4);
            }
            if (this.timer.hasReached(l2) && n5 >= 36) {
                this.getBestAxe(n5);
            }
            if (this.timer.hasReached(l2)) {
                for (n = 9; n < 45; ++n) {
                    if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(n).func_75216_d() || !this.shouldDrop(itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(n).func_75211_c(), n)) continue;
                    this.drop(n);
                    this.timer.reset();
                    if (l2 > 0L) break;
                }
            }
        }
    }

    public void shiftClick(int n) {
        this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71069_bz.field_75152_c, n, 0, 1, this.mc.field_71439_g);
    }

    public void swap(int n, int n2) {
        this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71069_bz.field_75152_c, n, n2, 2, this.mc.field_71439_g);
    }

    public void drop(int n) {
        this.mc.field_71442_b.func_78753_a(this.mc.field_71439_g.field_71069_bz.field_75152_c, n, 1, 4, this.mc.field_71439_g);
    }

    public boolean isBestWeapon(ItemStack itemStack) {
        float f = this.getDamage(itemStack);
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack2;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !(this.getDamage(itemStack2 = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()) > f) || !(itemStack2.func_77973_b() instanceof ItemSword)) continue;
            return false;
        }
        return true;
    }

    public void getBestWeapon(int n) {
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !this.isBestWeapon(itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()) || !(this.getDamage(itemStack) > 0.0f) || !(itemStack.func_77973_b() instanceof ItemSword)) continue;
            this.swap(i, n - 36);
            this.timer.reset();
            break;
        }
    }

    private float getDamage(ItemStack itemStack) {
        Item item;
        float f = 0.0f;
        Item item2 = itemStack.func_77973_b();
        if (item2 instanceof ItemTool) {
            item = (ItemTool)item2;
            f += (float)item.func_77612_l();
        }
        if (item2 instanceof ItemSword) {
            item = (ItemSword)item2;
            f += item.func_150931_i();
        }
        return f += (float)EnchantmentHelper.func_77506_a((int)Enchantment.field_180314_l.field_77352_x, (ItemStack)itemStack) * 1.25f + (float)EnchantmentHelper.func_77506_a((int)Enchantment.field_77334_n.field_77352_x, (ItemStack)itemStack) * 0.01f;
    }

    public boolean shouldDrop(ItemStack itemStack, int n) {
        if (itemStack.func_82833_r().contains("\u951f\u65a4\u62f7\u951f\ufffd")) {
            return false;
        }
        if (itemStack.func_82833_r().contains("\u951f\u63ed\u7877\u62f7")) {
            return false;
        }
        if (itemStack.func_82833_r().toLowerCase().contains("(right click)")) {
            return false;
        }
        if (((Boolean)this.UHC.getValue()).booleanValue()) {
            if (itemStack.func_82833_r().toLowerCase().contains("\u5934")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("apple")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("head")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("gold")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("crafting table")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("stick")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("and") && itemStack.func_82833_r().toLowerCase().contains("ril")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("axe of perun")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("barbarian")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("bloodlust")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("dragonchest")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("dragon sword")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("dragon armor")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("excalibur")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("exodus")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("fusion armor")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("hermes boots")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("hide of leviathan")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("scythe")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("seven-league boots")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("shoes of vidar")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("apprentice")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("master")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("vorpal")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("enchanted")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("spiked")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("tarnhelm")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("philosopher")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("anvil")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("panacea")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("fusion")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("excalibur")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u5b66\u5f92")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u5e08\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u65a9\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u9b54")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u53eb\u7877\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u7aed\u7678\u62f7\u6218\u9774")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u82f9\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u8857\ue1c6\u62f7\u951f\ufffd")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u7089")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("backpack")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u6854\u618b\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u6c50")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u9636\u9769\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\ufffd")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u7ef4\u951f\u65a4\u62f7\u6218\u9774")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u8857\ue1c6\u62f7\u951f\ufffd")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u9774")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("hermes")) {
                return false;
            }
            if (itemStack.func_82833_r().toLowerCase().contains("barbarian")) {
                return false;
            }
        }
        int n2 = ((Double)this.weaponSlot.getValue()).intValue();
        int n3 = ((Double)this.weaponSlot.getValue()).intValue();
        int n4 = ((Double)this.weaponSlot.getValue()).intValue();
        int n5 = ((Double)this.weaponSlot.getValue()).intValue();
        if (n == n2 && this.isBestWeapon(this.mc.field_71439_g.field_71069_bz.func_75139_a(n2).func_75211_c()) || n == n3 && this.isBestPickaxe(this.mc.field_71439_g.field_71069_bz.func_75139_a(n3).func_75211_c()) && n3 >= 0 || n == n5 && this.isBestAxe(this.mc.field_71439_g.field_71069_bz.func_75139_a(n5).func_75211_c()) && n5 >= 0 || n == n4 && this.isBestShovel(this.mc.field_71439_g.field_71069_bz.func_75139_a(n4).func_75211_c()) && n4 >= 0) {
            return false;
        }
        AutoArmor autoArmor = (AutoArmor)Client.instance.getModuleManager().getModuleByClass(AutoArmor.class);
        if (itemStack.func_77973_b() instanceof ItemArmor) {
            for (int i = 1; i < 5; ++i) {
                ItemStack itemStack2;
                if (this.mc.field_71439_g.field_71069_bz.func_75139_a(4 + i).func_75216_d() && autoArmor.isBestArmor(itemStack2 = this.mc.field_71439_g.field_71069_bz.func_75139_a(4 + i).func_75211_c(), i) || !autoArmor.isBestArmor(itemStack, i)) continue;
                return false;
            }
        }
        return ((Double)this.BlockCap.getValue()).intValue() != 0 && itemStack.func_77973_b() instanceof ItemBlock && (this.getBlockCount() > ((Double)this.BlockCap.getValue()).intValue() || InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)itemStack.func_77973_b()).func_179223_d())) || itemStack.func_77973_b() instanceof ItemPotion && this.isBadPotion(itemStack) || itemStack.func_77973_b() instanceof ItemHoe || itemStack.func_77973_b() instanceof ItemTool || itemStack.func_77973_b() instanceof ItemSword || itemStack.func_77973_b() instanceof ItemArmor || itemStack.func_77973_b() instanceof ItemBow || itemStack.func_77973_b().func_77658_a().contains("tnt") || itemStack.func_77973_b().func_77658_a().contains("stick") || itemStack.func_77973_b().func_77658_a().contains("egg") || itemStack.func_77973_b().func_77658_a().contains("string") || itemStack.func_77973_b().func_77658_a().contains("cake") || itemStack.func_77973_b().func_77658_a().contains("mushroom") || itemStack.func_77973_b().func_77658_a().contains("flint") || itemStack.func_77973_b().func_77658_a().contains("compass") || itemStack.func_77973_b().func_77658_a().contains("dyePowder") || itemStack.func_77973_b().func_77658_a().contains("feather") || itemStack.func_77973_b().func_77658_a().contains("bucket") || itemStack.func_77973_b().func_77658_a().contains("chest") && !itemStack.func_82833_r().toLowerCase().contains("collect") || itemStack.func_77973_b().func_77658_a().contains("snow") || itemStack.func_77973_b().func_77658_a().contains("fish") || itemStack.func_77973_b().func_77658_a().contains("enchant") || itemStack.func_77973_b().func_77658_a().contains("exp") || itemStack.func_77973_b().func_77658_a().contains("shears") || itemStack.func_77973_b().func_77658_a().contains("anvil") || itemStack.func_77973_b().func_77658_a().contains("torch") || itemStack.func_77973_b().func_77658_a().contains("seeds") || itemStack.func_77973_b().func_77658_a().contains("leather") || itemStack.func_77973_b().func_77658_a().contains("reeds") || itemStack.func_77973_b().func_77658_a().contains("skull") || itemStack.func_77973_b().func_77658_a().contains("record") || itemStack.func_77973_b().func_77658_a().contains("snowball") || itemStack.func_77973_b() instanceof ItemGlassBottle || itemStack.func_77973_b().func_77658_a().contains("piston");
    }

    private int getBlockCount() {
        int n = 0;
        for (int i = 0; i < 45; ++i) {
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d()) continue;
            ItemStack itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            Item item = itemStack.func_77973_b();
            if (!(itemStack.func_77973_b() instanceof ItemBlock) || InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)item).func_179223_d())) continue;
            n += itemStack.field_77994_a;
        }
        return n;
    }

    private void getBestPickaxe(int n) {
        int n2 = ((Double)this.weaponSlot.getValue()).intValue();
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !this.isBestPickaxe(itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()) || n2 == i || this.isBestWeapon(itemStack)) continue;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(n2).func_75216_d()) {
                this.swap(i, n2 - 36);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
            if (this.isBestPickaxe(this.mc.field_71439_g.field_71069_bz.func_75139_a(n2).func_75211_c())) continue;
            this.swap(i, n2 - 36);
            this.timer.reset();
            if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
            return;
        }
    }

    private void getBestShovel(int n) {
        int n2 = ((Double)this.weaponSlot.getValue()).intValue();
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !this.isBestShovel(itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()) || n2 == i || this.isBestWeapon(itemStack)) continue;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(n2).func_75216_d()) {
                this.swap(i, n2 - 36);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
            if (this.isBestShovel(this.mc.field_71439_g.field_71069_bz.func_75139_a(n2).func_75211_c())) continue;
            this.swap(i, n2 - 36);
            this.timer.reset();
            if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
            return;
        }
    }

    private void getBestAxe(int n) {
        int n2 = ((Double)this.weaponSlot.getValue()).intValue();
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !this.isBestAxe(itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()) || n2 == i || this.isBestWeapon(itemStack)) continue;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(n2).func_75216_d()) {
                this.swap(i, n2 - 36);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
            if (this.isBestAxe(this.mc.field_71439_g.field_71069_bz.func_75139_a(n2).func_75211_c())) continue;
            this.swap(i, n2 - 36);
            this.timer.reset();
            if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
            return;
        }
    }

    private boolean isBestPickaxe(ItemStack itemStack) {
        Item item = itemStack.func_77973_b();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        float f = this.getToolEffect(itemStack);
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack2;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !(this.getToolEffect(itemStack2 = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()) > f) || !(itemStack2.func_77973_b() instanceof ItemPickaxe)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestShovel(ItemStack itemStack) {
        Item item = itemStack.func_77973_b();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        float f = this.getToolEffect(itemStack);
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack2;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !(this.getToolEffect(itemStack2 = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()) > f) || !(itemStack2.func_77973_b() instanceof ItemSpade)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestAxe(ItemStack itemStack) {
        Item item = itemStack.func_77973_b();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        float f = this.getToolEffect(itemStack);
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack2;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !(this.getToolEffect(itemStack2 = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()) > f) || !(itemStack2.func_77973_b() instanceof ItemAxe) || this.isBestWeapon(itemStack)) continue;
            return false;
        }
        return true;
    }

    private float getToolEffect(ItemStack itemStack) {
        Item item = itemStack.func_77973_b();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        String string = item.func_77658_a();
        ItemTool itemTool = (ItemTool)item;
        float f = 1.0f;
        if (item instanceof ItemPickaxe) {
            f = itemTool.func_150893_a(itemStack, Blocks.field_150348_b);
            if (string.toLowerCase().contains("gold")) {
                f -= 5.0f;
            }
        } else if (item instanceof ItemSpade) {
            f = itemTool.func_150893_a(itemStack, Blocks.field_150346_d);
            if (string.toLowerCase().contains("gold")) {
                f -= 5.0f;
            }
        } else {
            if (!(item instanceof ItemAxe)) {
                return 1.0f;
            }
            f = itemTool.func_150893_a(itemStack, Blocks.field_150364_r);
            if (string.toLowerCase().contains("gold")) {
                f -= 5.0f;
            }
        }
        f += (float)((double)EnchantmentHelper.func_77506_a((int)Enchantment.field_77349_p.field_77352_x, (ItemStack)itemStack) * 0.0075);
        return f += (float)((double)EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)itemStack) / 100.0);
    }

    private boolean isBadPotion(ItemStack itemStack) {
        if (itemStack != null && itemStack.func_77973_b() instanceof ItemPotion) {
            ItemPotion itemPotion = (ItemPotion)itemStack.func_77973_b();
            if (itemPotion.func_77832_l(itemStack) == null) {
                return true;
            }
            for (Object e : itemPotion.func_77832_l(itemStack)) {
                PotionEffect potionEffect = (PotionEffect)e;
                if (potionEffect.func_76456_a() != Potion.field_76436_u.func_76396_c() && potionEffect.func_76456_a() != Potion.field_76433_i.func_76396_c() && potionEffect.func_76456_a() != Potion.field_76421_d.func_76396_c() && potionEffect.func_76456_a() != Potion.field_76437_t.func_76396_c()) continue;
                return true;
            }
        }
        return false;
    }

    boolean invContainsType(int n) {
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack;
            Item item;
            if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !((item = (itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()).func_77973_b()) instanceof ItemArmor)) continue;
            ItemArmor itemArmor = (ItemArmor)item;
            if (n != itemArmor.field_77881_a) continue;
            return true;
        }
        return false;
    }

    public void getBestArmor() {
        AutoArmor autoArmor = (AutoArmor)Client.instance.getModuleManager().getModuleByClass(AutoArmor.class);
        for (int i = 1; i < 5; ++i) {
            if (this.mc.field_71439_g.field_71069_bz.func_75139_a(4 + i).func_75216_d()) {
                ItemStack itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(4 + i).func_75211_c();
                if (autoArmor.isBestArmor(itemStack, i)) continue;
                this.drop(4 + i);
            }
            for (int j = 9; j < 45; ++j) {
                ItemStack itemStack;
                if (!this.mc.field_71439_g.field_71069_bz.func_75139_a(j).func_75216_d() || !autoArmor.isBestArmor(itemStack = this.mc.field_71439_g.field_71069_bz.func_75139_a(j).func_75211_c(), i) || !(AutoArmor.getProtection(itemStack) > 0.0f)) continue;
                this.shiftClick(j);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
        }
    }
}

