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
        if (this.mc.thePlayer.openContainer instanceof ContainerChest && this.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        int n2 = ((Double)this.weaponSlot.getValue()).intValue();
        int n3 = ((Double)this.weaponSlot.getValue()).intValue();
        int n4 = ((Double)this.weaponSlot.getValue()).intValue();
        int n5 = ((Double)this.weaponSlot.getValue()).intValue();
        long l2 = ((Double)this.Delay.getValue()).longValue();
        AutoArmor autoArmor = (AutoArmor)Client.instance.getModuleManager().getModuleByClass(AutoArmor.class);
        long l3 = ((Double)autoArmor.delay.getValue()).longValue();
        if (this.timer.hasReached(l3) && this.mc.currentScreen instanceof GuiInventory) {
            this.getBestArmor();
        }
        for (n = 1; n < 5; ++n) {
            if (!(this.mc.thePlayer.inventoryContainer.getSlot(4 + n).getHasStack() ? !autoArmor.isBestArmor(itemStack = this.mc.thePlayer.inventoryContainer.getSlot(4 + n).getStack(), n) : this.invContainsType(n - 1))) continue;
            return;
        }
        if (!(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory || this.mc.currentScreen instanceof GuiChat) {
            if (this.timer.hasReached(l2) && n2 >= 36) {
                if (!this.mc.thePlayer.inventoryContainer.getSlot(n2).getHasStack()) {
                    this.getBestWeapon(n2);
                } else if (!this.isBestWeapon(this.mc.thePlayer.inventoryContainer.getSlot(n2).getStack())) {
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
                    if (!this.mc.thePlayer.inventoryContainer.getSlot(n).getHasStack() || !this.shouldDrop(itemStack = this.mc.thePlayer.inventoryContainer.getSlot(n).getStack(), n)) continue;
                    this.drop(n);
                    this.timer.reset();
                    if (l2 > 0L) break;
                }
            }
        }
    }

    public void shiftClick(int n) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, n, 0, 1, this.mc.thePlayer);
    }

    public void swap(int n, int n2) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, n, n2, 2, this.mc.thePlayer);
    }

    public void drop(int n) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, n, 1, 4, this.mc.thePlayer);
    }

    public boolean isBestWeapon(ItemStack itemStack) {
        float f = this.getDamage(itemStack);
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack2;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getDamage(itemStack2 = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > f) || !(itemStack2.getItem() instanceof ItemSword)) continue;
            return false;
        }
        return true;
    }

    public void getBestWeapon(int n) {
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestWeapon(itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || !(this.getDamage(itemStack) > 0.0f) || !(itemStack.getItem() instanceof ItemSword)) continue;
            this.swap(i, n - 36);
            this.timer.reset();
            break;
        }
    }

    private float getDamage(ItemStack itemStack) {
        Item item;
        float f = 0.0f;
        Item item2 = itemStack.getItem();
        if (item2 instanceof ItemTool) {
            item = (ItemTool)item2;
            f += (float)item.getMaxDamage();
        }
        if (item2 instanceof ItemSword) {
            item = (ItemSword)item2;
            f += ((ItemSword)item).getDamageVsEntity();
        }
        return f += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
    }

    public boolean shouldDrop(ItemStack itemStack, int n) {
        if (itemStack.getDisplayName().contains("\u951f\u65a4\u62f7\u951f\ufffd")) {
            return false;
        }
        if (itemStack.getDisplayName().contains("\u951f\u63ed\u7877\u62f7")) {
            return false;
        }
        if (itemStack.getDisplayName().toLowerCase().contains("(right click)")) {
            return false;
        }
        if (((Boolean)this.UHC.getValue()).booleanValue()) {
            if (itemStack.getDisplayName().toLowerCase().contains("\u5934")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("apple")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("head")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("gold")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("crafting table")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("stick")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("and") && itemStack.getDisplayName().toLowerCase().contains("ril")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("axe of perun")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("barbarian")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("bloodlust")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("dragonchest")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("dragon sword")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("dragon armor")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("excalibur")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("exodus")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("fusion armor")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("hermes boots")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("hide of leviathan")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("scythe")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("seven-league boots")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("shoes of vidar")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("apprentice")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("master")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("vorpal")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("enchanted")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("spiked")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("tarnhelm")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("philosopher")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("anvil")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("panacea")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("fusion")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("excalibur")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u5b66\u5f92")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u5e08\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u65a9\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u9b54")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u53eb\u7877\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u7aed\u7678\u62f7\u6218\u9774")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u82f9\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u8857\ue1c6\u62f7\u951f\ufffd")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u7089")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("backpack")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u6854\u618b\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u6c50")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u9636\u9769\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\ufffd")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u7ef4\u951f\u65a4\u62f7\u6218\u9774")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u8857\ue1c6\u62f7\u951f\ufffd")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u9774")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("hermes")) {
                return false;
            }
            if (itemStack.getDisplayName().toLowerCase().contains("barbarian")) {
                return false;
            }
        }
        int n2 = ((Double)this.weaponSlot.getValue()).intValue();
        int n3 = ((Double)this.weaponSlot.getValue()).intValue();
        int n4 = ((Double)this.weaponSlot.getValue()).intValue();
        int n5 = ((Double)this.weaponSlot.getValue()).intValue();
        if (n == n2 && this.isBestWeapon(this.mc.thePlayer.inventoryContainer.getSlot(n2).getStack()) || n == n3 && this.isBestPickaxe(this.mc.thePlayer.inventoryContainer.getSlot(n3).getStack()) && n3 >= 0 || n == n5 && this.isBestAxe(this.mc.thePlayer.inventoryContainer.getSlot(n5).getStack()) && n5 >= 0 || n == n4 && this.isBestShovel(this.mc.thePlayer.inventoryContainer.getSlot(n4).getStack()) && n4 >= 0) {
            return false;
        }
        AutoArmor autoArmor = (AutoArmor)Client.instance.getModuleManager().getModuleByClass(AutoArmor.class);
        if (itemStack.getItem() instanceof ItemArmor) {
            for (int i = 1; i < 5; ++i) {
                ItemStack itemStack2;
                if (this.mc.thePlayer.inventoryContainer.getSlot(4 + i).getHasStack() && autoArmor.isBestArmor(itemStack2 = this.mc.thePlayer.inventoryContainer.getSlot(4 + i).getStack(), i) || !autoArmor.isBestArmor(itemStack, i)) continue;
                return false;
            }
        }
        return ((Double)this.BlockCap.getValue()).intValue() != 0 && itemStack.getItem() instanceof ItemBlock && (this.getBlockCount() > ((Double)this.BlockCap.getValue()).intValue() || InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)itemStack.getItem()).getBlock())) || itemStack.getItem() instanceof ItemPotion && this.isBadPotion(itemStack) || itemStack.getItem() instanceof ItemHoe || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemBow || itemStack.getItem().getUnlocalizedName().contains("tnt") || itemStack.getItem().getUnlocalizedName().contains("stick") || itemStack.getItem().getUnlocalizedName().contains("egg") || itemStack.getItem().getUnlocalizedName().contains("string") || itemStack.getItem().getUnlocalizedName().contains("cake") || itemStack.getItem().getUnlocalizedName().contains("mushroom") || itemStack.getItem().getUnlocalizedName().contains("flint") || itemStack.getItem().getUnlocalizedName().contains("compass") || itemStack.getItem().getUnlocalizedName().contains("dyePowder") || itemStack.getItem().getUnlocalizedName().contains("feather") || itemStack.getItem().getUnlocalizedName().contains("bucket") || itemStack.getItem().getUnlocalizedName().contains("chest") && !itemStack.getDisplayName().toLowerCase().contains("collect") || itemStack.getItem().getUnlocalizedName().contains("snow") || itemStack.getItem().getUnlocalizedName().contains("fish") || itemStack.getItem().getUnlocalizedName().contains("enchant") || itemStack.getItem().getUnlocalizedName().contains("exp") || itemStack.getItem().getUnlocalizedName().contains("shears") || itemStack.getItem().getUnlocalizedName().contains("anvil") || itemStack.getItem().getUnlocalizedName().contains("torch") || itemStack.getItem().getUnlocalizedName().contains("seeds") || itemStack.getItem().getUnlocalizedName().contains("leather") || itemStack.getItem().getUnlocalizedName().contains("reeds") || itemStack.getItem().getUnlocalizedName().contains("skull") || itemStack.getItem().getUnlocalizedName().contains("record") || itemStack.getItem().getUnlocalizedName().contains("snowball") || itemStack.getItem() instanceof ItemGlassBottle || itemStack.getItem().getUnlocalizedName().contains("piston");
    }

    private int getBlockCount() {
        int n = 0;
        for (int i = 0; i < 45; ++i) {
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = itemStack.getItem();
            if (!(itemStack.getItem() instanceof ItemBlock) || InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)item).getBlock())) continue;
            n += itemStack.stackSize;
        }
        return n;
    }

    private void getBestPickaxe(int n) {
        int n2 = ((Double)this.weaponSlot.getValue()).intValue();
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestPickaxe(itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || n2 == i || this.isBestWeapon(itemStack)) continue;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(n2).getHasStack()) {
                this.swap(i, n2 - 36);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
            if (this.isBestPickaxe(this.mc.thePlayer.inventoryContainer.getSlot(n2).getStack())) continue;
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
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestShovel(itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || n2 == i || this.isBestWeapon(itemStack)) continue;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(n2).getHasStack()) {
                this.swap(i, n2 - 36);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
            if (this.isBestShovel(this.mc.thePlayer.inventoryContainer.getSlot(n2).getStack())) continue;
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
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestAxe(itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || n2 == i || this.isBestWeapon(itemStack)) continue;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(n2).getHasStack()) {
                this.swap(i, n2 - 36);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
            if (this.isBestAxe(this.mc.thePlayer.inventoryContainer.getSlot(n2).getStack())) continue;
            this.swap(i, n2 - 36);
            this.timer.reset();
            if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
            return;
        }
    }

    private boolean isBestPickaxe(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        float f = this.getToolEffect(itemStack);
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack2;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(itemStack2 = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > f) || !(itemStack2.getItem() instanceof ItemPickaxe)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestShovel(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        float f = this.getToolEffect(itemStack);
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack2;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(itemStack2 = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > f) || !(itemStack2.getItem() instanceof ItemSpade)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestAxe(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        float f = this.getToolEffect(itemStack);
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack2;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(itemStack2 = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > f) || !(itemStack2.getItem() instanceof ItemAxe) || this.isBestWeapon(itemStack)) continue;
            return false;
        }
        return true;
    }

    private float getToolEffect(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        String string = item.getUnlocalizedName();
        ItemTool itemTool = (ItemTool)item;
        float f = 1.0f;
        if (item instanceof ItemPickaxe) {
            f = itemTool.getStrVsBlock(itemStack, Blocks.stone);
            if (string.toLowerCase().contains("gold")) {
                f -= 5.0f;
            }
        } else if (item instanceof ItemSpade) {
            f = itemTool.getStrVsBlock(itemStack, Blocks.dirt);
            if (string.toLowerCase().contains("gold")) {
                f -= 5.0f;
            }
        } else {
            if (!(item instanceof ItemAxe)) {
                return 1.0f;
            }
            f = itemTool.getStrVsBlock(itemStack, Blocks.log);
            if (string.toLowerCase().contains("gold")) {
                f -= 5.0f;
            }
        }
        f += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack) * 0.0075);
        return f += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 100.0);
    }

    private boolean isBadPotion(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof ItemPotion) {
            ItemPotion itemPotion = (ItemPotion)itemStack.getItem();
            if (itemPotion.getEffects(itemStack) == null) {
                return true;
            }
            for (PotionEffect potionEffect : itemPotion.getEffects(itemStack)) {
                PotionEffect potionEffect2 = potionEffect;
                if (potionEffect2.getPotionID() != Potion.poison.getId() && potionEffect2.getPotionID() != Potion.harm.getId() && potionEffect2.getPotionID() != Potion.moveSlowdown.getId() && potionEffect2.getPotionID() != Potion.weakness.getId()) continue;
                return true;
            }
        }
        return false;
    }

    boolean invContainsType(int n) {
        for (int i = 9; i < 45; ++i) {
            ItemStack itemStack;
            Item item;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = (itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemArmor)) continue;
            ItemArmor itemArmor = (ItemArmor)item;
            if (n != itemArmor.armorType) continue;
            return true;
        }
        return false;
    }

    public void getBestArmor() {
        AutoArmor autoArmor = (AutoArmor)Client.instance.getModuleManager().getModuleByClass(AutoArmor.class);
        for (int i = 1; i < 5; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(4 + i).getHasStack()) {
                ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(4 + i).getStack();
                if (autoArmor.isBestArmor(itemStack, i)) continue;
                this.drop(4 + i);
            }
            for (int j = 9; j < 45; ++j) {
                ItemStack itemStack;
                if (!this.mc.thePlayer.inventoryContainer.getSlot(j).getHasStack() || !autoArmor.isBestArmor(itemStack = this.mc.thePlayer.inventoryContainer.getSlot(j).getStack(), i) || !(AutoArmor.getProtection(itemStack) > 0.0f)) continue;
                this.shiftClick(j);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
        }
    }
}

