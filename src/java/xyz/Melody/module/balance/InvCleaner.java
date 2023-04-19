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
    public void onEvent(EventTick event) {
        ItemStack is;
        if (this.mc.thePlayer.openContainer instanceof ContainerChest && this.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        int weaponSlot = ((Double)this.weaponSlot.getValue()).intValue();
        int pickaxeSlot = ((Double)this.weaponSlot.getValue()).intValue();
        int shovelSlot = ((Double)this.weaponSlot.getValue()).intValue();
        int axeSlot = ((Double)this.weaponSlot.getValue()).intValue();
        long delay = ((Double)this.Delay.getValue()).longValue();
        AutoArmor aar = (AutoArmor)Client.instance.getModuleManager().getModuleByClass(AutoArmor.class);
        long Adelay = ((Double)aar.delay.getValue()).longValue();
        if (this.timer.hasReached(Adelay) && this.mc.currentScreen instanceof GuiInventory) {
            this.getBestArmor();
        }
        for (int type = 1; type < 5; ++type) {
            if (!(this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack() ? !aar.isBestArmor(is = this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack(), type) : this.invContainsType(type - 1))) continue;
            return;
        }
        if (!(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory || this.mc.currentScreen instanceof GuiChat) {
            if (this.timer.hasReached(delay) && weaponSlot >= 36) {
                if (!this.mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
                    this.getBestWeapon(weaponSlot);
                } else if (!this.isBestWeapon(this.mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) {
                    this.getBestWeapon(weaponSlot);
                }
            }
            if (this.timer.hasReached(delay) && pickaxeSlot >= 36) {
                this.getBestPickaxe(pickaxeSlot);
            }
            if (this.timer.hasReached(delay) && shovelSlot >= 36) {
                this.getBestShovel(shovelSlot);
            }
            if (this.timer.hasReached(delay) && axeSlot >= 36) {
                this.getBestAxe(axeSlot);
            }
            if (this.timer.hasReached(delay)) {
                for (int j = 9; j < 45; ++j) {
                    if (!this.mc.thePlayer.inventoryContainer.getSlot(j).getHasStack() || !this.shouldDrop(is = this.mc.thePlayer.inventoryContainer.getSlot(j).getStack(), j)) continue;
                    this.drop(j);
                    this.timer.reset();
                    if (delay > 0L) break;
                }
            }
        }
    }

    public void shiftClick(int slot) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, this.mc.thePlayer);
    }

    public void swap(int slot1, int hotbarSlot) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, this.mc.thePlayer);
    }

    public void drop(int slot) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, this.mc.thePlayer);
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = this.getDamage(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getDamage(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > damage) || !(is.getItem() instanceof ItemSword)) continue;
            return false;
        }
        return true;
    }

    public void getBestWeapon(int slot) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestWeapon(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || !(this.getDamage(is) > 0.0f) || !(is.getItem() instanceof ItemSword)) continue;
            this.swap(i, slot - 36);
            this.timer.reset();
            break;
        }
    }

    private float getDamage(ItemStack stack) {
        float damage = 0.0f;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool)item;
            damage += (float)tool.getMaxDamage();
        }
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword)item;
            damage += sword.getDamageVsEntity();
        }
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }

    public boolean shouldDrop(ItemStack stack, int slot) {
        if (stack.getDisplayName().contains("\u951f\u65a4\u62f7\u951f\ufffd")) {
            return false;
        }
        if (stack.getDisplayName().contains("\u951f\u63ed\u7877\u62f7")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
            return false;
        }
        if (((Boolean)this.UHC.getValue()).booleanValue()) {
            if (stack.getDisplayName().toLowerCase().contains("\u5934")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("apple")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("head")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("gold")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("crafting table")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("stick")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("and") && stack.getDisplayName().toLowerCase().contains("ril")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("axe of perun")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("barbarian")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("bloodlust")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("dragonchest")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("dragon sword")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("dragon armor")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("excalibur")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("exodus")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("fusion armor")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("hermes boots")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("hide of leviathan")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("scythe")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("seven-league boots")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("shoes of vidar")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("apprentice")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("master")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("vorpal")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("enchanted")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("spiked")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("tarnhelm")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("philosopher")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("anvil")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("panacea")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("fusion")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("excalibur")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u5b66\u5f92")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u5e08\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u65a9\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u9b54")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u53eb\u7877\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u7aed\u7678\u62f7\u6218\u9774")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u82f9\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u8857\ue1c6\u62f7\u951f\ufffd")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u7089")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("backpack")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u6854\u618b\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u6c50")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u9636\u9769\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\ufffd")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u7ef4\u951f\u65a4\u62f7\u6218\u9774")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u8857\ue1c6\u62f7\u951f\ufffd")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u951f\u65a4\u62f7")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u4e4b\u9774")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("hermes")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("barbarian")) {
                return false;
            }
        }
        int weaponSlot = ((Double)this.weaponSlot.getValue()).intValue();
        int pickaxeSlot = ((Double)this.weaponSlot.getValue()).intValue();
        int shovelSlot = ((Double)this.weaponSlot.getValue()).intValue();
        int axeSlot = ((Double)this.weaponSlot.getValue()).intValue();
        if (slot == weaponSlot && this.isBestWeapon(this.mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack()) || slot == pickaxeSlot && this.isBestPickaxe(this.mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack()) && pickaxeSlot >= 0 || slot == axeSlot && this.isBestAxe(this.mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack()) && axeSlot >= 0 || slot == shovelSlot && this.isBestShovel(this.mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack()) && shovelSlot >= 0) {
            return false;
        }
        AutoArmor aar = (AutoArmor)Client.instance.getModuleManager().getModuleByClass(AutoArmor.class);
        if (stack.getItem() instanceof ItemArmor) {
            for (int type = 1; type < 5; ++type) {
                ItemStack is;
                if (this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack() && aar.isBestArmor(is = this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack(), type) || !aar.isBestArmor(stack, type)) continue;
                return false;
            }
        }
        return ((Double)this.BlockCap.getValue()).intValue() != 0 && stack.getItem() instanceof ItemBlock && (this.getBlockCount() > ((Double)this.BlockCap.getValue()).intValue() || InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)stack.getItem()).getBlock())) || stack.getItem() instanceof ItemPotion && this.isBadPotion(stack) || stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("tnt") || stack.getItem().getUnlocalizedName().contains("stick") || stack.getItem().getUnlocalizedName().contains("egg") || stack.getItem().getUnlocalizedName().contains("string") || stack.getItem().getUnlocalizedName().contains("cake") || stack.getItem().getUnlocalizedName().contains("mushroom") || stack.getItem().getUnlocalizedName().contains("flint") || stack.getItem().getUnlocalizedName().contains("compass") || stack.getItem().getUnlocalizedName().contains("dyePowder") || stack.getItem().getUnlocalizedName().contains("feather") || stack.getItem().getUnlocalizedName().contains("bucket") || stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect") || stack.getItem().getUnlocalizedName().contains("snow") || stack.getItem().getUnlocalizedName().contains("fish") || stack.getItem().getUnlocalizedName().contains("enchant") || stack.getItem().getUnlocalizedName().contains("exp") || stack.getItem().getUnlocalizedName().contains("shears") || stack.getItem().getUnlocalizedName().contains("anvil") || stack.getItem().getUnlocalizedName().contains("torch") || stack.getItem().getUnlocalizedName().contains("seeds") || stack.getItem().getUnlocalizedName().contains("leather") || stack.getItem().getUnlocalizedName().contains("reeds") || stack.getItem().getUnlocalizedName().contains("skull") || stack.getItem().getUnlocalizedName().contains("record") || stack.getItem().getUnlocalizedName().contains("snowball") || stack.getItem() instanceof ItemGlassBottle || stack.getItem().getUnlocalizedName().contains("piston");
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    private void getBestPickaxe(int slot) {
        int pickaxeSlot = ((Double)this.weaponSlot.getValue()).intValue();
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestPickaxe(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || pickaxeSlot == i || this.isBestWeapon(is)) continue;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
                this.swap(i, pickaxeSlot - 36);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
            if (this.isBestPickaxe(this.mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())) continue;
            this.swap(i, pickaxeSlot - 36);
            this.timer.reset();
            if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
            return;
        }
    }

    private void getBestShovel(int slot) {
        int shovelSlot = ((Double)this.weaponSlot.getValue()).intValue();
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestShovel(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || shovelSlot == i || this.isBestWeapon(is)) continue;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
                this.swap(i, shovelSlot - 36);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
            if (this.isBestShovel(this.mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())) continue;
            this.swap(i, shovelSlot - 36);
            this.timer.reset();
            if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
            return;
        }
    }

    private void getBestAxe(int slot) {
        int axeSlot = ((Double)this.weaponSlot.getValue()).intValue();
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestAxe(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || axeSlot == i || this.isBestWeapon(is)) continue;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
                this.swap(i, axeSlot - 36);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
            if (this.isBestAxe(this.mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())) continue;
            this.swap(i, axeSlot - 36);
            this.timer.reset();
            if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
            return;
        }
    }

    private boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > value) || !(is.getItem() instanceof ItemPickaxe)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > value) || !(is.getItem() instanceof ItemSpade)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > value) || !(is.getItem() instanceof ItemAxe) || this.isBestWeapon(stack)) continue;
            return false;
        }
        return true;
    }

    private float getToolEffect(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool)item;
        float value = 1.0f;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else {
            if (!(item instanceof ItemAxe)) {
                return 1.0f;
            }
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        value += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
        return value += (float)((double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (potion.getEffects(stack) == null) {
                return true;
            }
            for (PotionEffect o : potion.getEffects(stack)) {
                PotionEffect effect = o;
                if (effect.getPotionID() != Potion.poison.getId() && effect.getPotionID() != Potion.harm.getId() && effect.getPotionID() != Potion.moveSlowdown.getId() && effect.getPotionID() != Potion.weakness.getId()) continue;
                return true;
            }
        }
        return false;
    }

    boolean invContainsType(int type) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            Item item;
            if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = (is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemArmor)) continue;
            ItemArmor armor = (ItemArmor)item;
            if (type != armor.armorType) continue;
            return true;
        }
        return false;
    }

    public void getBestArmor() {
        AutoArmor aar = (AutoArmor)Client.instance.getModuleManager().getModuleByClass(AutoArmor.class);
        for (int type = 1; type < 5; ++type) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (aar.isBestArmor(is, type)) continue;
                this.drop(4 + type);
            }
            for (int i = 9; i < 45; ++i) {
                ItemStack is2;
                if (!this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !aar.isBestArmor(is2 = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack(), type) || !(AutoArmor.getProtection(is2) > 0.0f)) continue;
                this.shiftClick(i);
                this.timer.reset();
                if (((Double)this.Delay.getValue()).longValue() <= 0L) continue;
                return;
            }
        }
    }
}

