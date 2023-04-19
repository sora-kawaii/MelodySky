/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.game;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StringUtils;

public final class InventoryUtils {
    public static final List<Block> BLOCK_BLACKLIST = Arrays.asList(Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest, Blocks.anvil, Blocks.sand, Blocks.web, Blocks.torch, Blocks.crafting_table, Blocks.furnace, Blocks.waterlily, Blocks.dispenser, Blocks.stone_pressure_plate, Blocks.wooden_pressure_plate, Blocks.noteblock, Blocks.dropper, Blocks.tnt, Blocks.standing_banner, Blocks.wall_banner);
    public static Minecraft mc = Minecraft.getMinecraft();

    public void dropSlot(int slot) {
        int windowId = new GuiInventory((EntityPlayer)InventoryUtils.mc.thePlayer).inventorySlots.windowId;
        InventoryUtils.mc.playerController.windowClick(windowId, slot, 1, 4, InventoryUtils.mc.thePlayer);
    }

    public static void updateInventory() {
        for (int index = 0; index < 44; ++index) {
            try {
                int offset = index < 9 ? 36 : 0;
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(index + offset, Minecraft.getMinecraft().thePlayer.inventory.mainInventory[index]));
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static ItemStack getStackInSlot(int slot) {
        return InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot);
    }

    public static boolean isBestArmorOfTypeInInv(ItemStack is) {
        try {
            ItemArmor otherArmor;
            ItemStack stack;
            int i;
            if (is == null) {
                return false;
            }
            if (is.getItem() == null) {
                return false;
            }
            if (is.getItem() != null && !(is.getItem() instanceof ItemArmor)) {
                return false;
            }
            ItemArmor ia = (ItemArmor)is.getItem();
            int prot = InventoryUtils.getArmorProt(is);
            for (i = 0; i < 4; ++i) {
                stack = InventoryUtils.mc.thePlayer.inventory.armorInventory[i];
                if (stack == null) continue;
                otherArmor = (ItemArmor)stack.getItem();
                if (otherArmor.armorType != ia.armorType || InventoryUtils.getArmorProt(stack) < prot) continue;
                return false;
            }
            for (i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory() - 4; ++i) {
                stack = InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i);
                if (stack == null || !(stack.getItem() instanceof ItemArmor)) continue;
                otherArmor = (ItemArmor)stack.getItem();
                if (otherArmor.armorType != ia.armorType || otherArmor == ia || InventoryUtils.getArmorProt(stack) < prot) continue;
                return false;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return true;
    }

    public static boolean hotbarHas(Item item) {
        for (int index = 0; index <= 36; ++index) {
            ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
            if (stack == null || stack.getItem() != item) continue;
            return true;
        }
        return false;
    }

    public static boolean hotbarHas(Item item, int slotID) {
        for (int index = 0; index <= 36; ++index) {
            ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
            if (stack == null || stack.getItem() != item || InventoryUtils.getSlotID(stack.getItem()) != slotID) continue;
            return true;
        }
        return false;
    }

    public static int getSlotID(Item item) {
        for (int index = 0; index <= 36; ++index) {
            ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
            if (stack == null || stack.getItem() != item) continue;
            return index;
        }
        return -1;
    }

    public static ItemStack getItemBySlotID(int slotID) {
        for (int index = 0; index <= 36; ++index) {
            ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
            if (stack == null || InventoryUtils.getSlotID(stack.getItem()) != slotID) continue;
            return stack;
        }
        return null;
    }

    public static int getArmorProt(ItemStack i) {
        int armorprot = -1;
        if (i != null && i.getItem() != null && i.getItem() instanceof ItemArmor) {
            armorprot = ((ItemArmor)i.getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(i)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{i}, DamageSource.generic);
        }
        return armorprot;
    }

    public static int getBestSwordSlotID(ItemStack item, double damage) {
        for (int index = 0; index <= 36; ++index) {
            ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
            if (stack == null || stack != item || InventoryUtils.getSwordDamage(stack) != InventoryUtils.getSwordDamage(item)) continue;
            return index;
        }
        return -1;
    }

    private static double getSwordDamage(ItemStack itemStack) {
        double damage = 0.0;
        Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
        if (attributeModifier.isPresent()) {
            damage = attributeModifier.get().getAmount();
        }
        return damage += (double)EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
    }

    public boolean isBestChest(int slot) {
        if (InventoryUtils.getStackInSlot(slot) != null && InventoryUtils.getStackInSlot(slot).getItem() != null && InventoryUtils.getStackInSlot(slot).getItem() instanceof ItemArmor) {
            int otherProtection;
            ItemArmor ia1;
            int slotProtection = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot)}, DamageSource.generic);
            if (InventoryUtils.mc.thePlayer.inventory.armorInventory[2] != null) {
                ItemStack is = InventoryUtils.mc.thePlayer.inventory.armorInventory[2];
                ia1 = (ItemArmor)InventoryUtils.getStackInSlot(slot).getItem();
                otherProtection = ((ItemArmor)is.getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(is)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{is}, DamageSource.generic);
                if (otherProtection > slotProtection || otherProtection == slotProtection) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) continue;
                otherProtection = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i)}, DamageSource.generic);
                ia1 = (ItemArmor)InventoryUtils.getStackInSlot(slot).getItem();
                ItemArmor ia2 = (ItemArmor)InventoryUtils.getStackInSlot(i).getItem();
                if (ia1.armorType != 1 || ia2.armorType != 1 || otherProtection <= slotProtection) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestHelmet(int slot) {
        if (InventoryUtils.getStackInSlot(slot) != null && InventoryUtils.getStackInSlot(slot).getItem() != null && InventoryUtils.getStackInSlot(slot).getItem() instanceof ItemArmor) {
            int otherProtection;
            ItemArmor ia1;
            int slotProtection = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot)}, DamageSource.generic);
            if (InventoryUtils.mc.thePlayer.inventory.armorInventory[3] != null) {
                ItemStack is = InventoryUtils.mc.thePlayer.inventory.armorInventory[3];
                ia1 = (ItemArmor)InventoryUtils.getStackInSlot(slot).getItem();
                otherProtection = ((ItemArmor)is.getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(is)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{is}, DamageSource.generic);
                if (otherProtection > slotProtection || otherProtection == slotProtection) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) continue;
                otherProtection = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i)}, DamageSource.generic);
                ia1 = (ItemArmor)InventoryUtils.getStackInSlot(slot).getItem();
                ItemArmor ia2 = (ItemArmor)InventoryUtils.getStackInSlot(i).getItem();
                if (ia1.armorType != 0 || ia2.armorType != 0 || otherProtection <= slotProtection) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestLeggings(int slot) {
        if (InventoryUtils.getStackInSlot(slot) != null && InventoryUtils.getStackInSlot(slot).getItem() != null && InventoryUtils.getStackInSlot(slot).getItem() instanceof ItemArmor) {
            int otherProtection;
            ItemArmor ia1;
            int slotProtection = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot)}, DamageSource.generic);
            if (InventoryUtils.mc.thePlayer.inventory.armorInventory[1] != null) {
                ItemStack is = InventoryUtils.mc.thePlayer.inventory.armorInventory[1];
                ia1 = (ItemArmor)InventoryUtils.getStackInSlot(slot).getItem();
                otherProtection = ((ItemArmor)is.getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(is)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{is}, DamageSource.generic);
                if (otherProtection > slotProtection || otherProtection == slotProtection) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) continue;
                otherProtection = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i)}, DamageSource.generic);
                ia1 = (ItemArmor)InventoryUtils.getStackInSlot(slot).getItem();
                ItemArmor ia2 = (ItemArmor)InventoryUtils.getStackInSlot(i).getItem();
                if (ia1.armorType != 2 || ia2.armorType != 2 || otherProtection <= slotProtection) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestBoots(int slot) {
        if (InventoryUtils.getStackInSlot(slot) != null && InventoryUtils.getStackInSlot(slot).getItem() != null && InventoryUtils.getStackInSlot(slot).getItem() instanceof ItemArmor) {
            int otherProtection;
            ItemArmor ia1;
            int slotProtection = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot)}, DamageSource.generic);
            if (InventoryUtils.mc.thePlayer.inventory.armorInventory[0] != null) {
                ItemStack is = InventoryUtils.mc.thePlayer.inventory.armorInventory[0];
                ia1 = (ItemArmor)InventoryUtils.getStackInSlot(slot).getItem();
                otherProtection = ((ItemArmor)is.getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(is)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{is}, DamageSource.generic);
                if (otherProtection > slotProtection || otherProtection == slotProtection) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) continue;
                otherProtection = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i)}, DamageSource.generic);
                ia1 = (ItemArmor)InventoryUtils.getStackInSlot(slot).getItem();
                ItemArmor ia2 = (ItemArmor)InventoryUtils.getStackInSlot(i).getItem();
                if (ia1.armorType != 3 || ia2.armorType != 3 || otherProtection <= slotProtection) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestSword(int slotIn) {
        return this.getBestWeapon() == slotIn;
    }

    public static int getItemType(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)itemStack.getItem();
            return armor.armorType;
        }
        return -1;
    }

    public static float getItemDamage(ItemStack itemStack) {
        Iterator<Map.Entry<String, AttributeModifier>> iterator;
        Multimap<String, AttributeModifier> multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
            Map.Entry<String, AttributeModifier> entry = iterator.next();
            AttributeModifier attributeModifier = entry.getValue();
            double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;
            return attributeModifier.getAmount() > 1.0 ? 1.0f + (float)damage : 1.0f;
        }
        return 1.0f;
    }

    public boolean hasItemMoreTimes(int slotIn) {
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        stacks.clear();
        for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
            if (!stacks.contains(InventoryUtils.getStackInSlot(i))) {
                stacks.add(InventoryUtils.getStackInSlot(i));
                continue;
            }
            if (InventoryUtils.getStackInSlot(i) != InventoryUtils.getStackInSlot(slotIn)) continue;
            return true;
        }
        return false;
    }

    public int getBestWeaponInHotbar() {
        int originalSlot = InventoryUtils.mc.thePlayer.inventory.currentItem;
        int weaponSlot = -1;
        float weaponDamage = 1.0f;
        for (int slot = 0; slot < 9; slot = (int)((byte)(slot + 1))) {
            float f;
            ItemStack itemStack = InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot);
            if (itemStack == null) continue;
            float damage = InventoryUtils.getItemDamage(itemStack);
            damage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
            if (!(f > weaponDamage)) continue;
            weaponDamage = damage;
            weaponSlot = slot;
        }
        if (weaponSlot != -1) {
            return weaponSlot;
        }
        return originalSlot;
    }

    public int getBestWeapon() {
        int originalSlot = InventoryUtils.mc.thePlayer.inventory.currentItem;
        int weaponSlot = -1;
        float weaponDamage = 1.0f;
        for (int slot = 0; slot < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); slot = (int)((byte)(slot + 1))) {
            float f;
            ItemStack itemStack;
            if (InventoryUtils.getStackInSlot(slot) == null || (itemStack = InventoryUtils.getStackInSlot(slot)) == null || itemStack.getItem() == null || !(itemStack.getItem() instanceof ItemSword)) continue;
            float damage = InventoryUtils.getItemDamage(itemStack);
            damage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
            if (!(f > weaponDamage)) continue;
            weaponDamage = damage;
            weaponSlot = slot;
        }
        if (weaponSlot != -1) {
            return weaponSlot;
        }
        return originalSlot;
    }

    public int getArmorProt(int i) {
        int armorprot = -1;
        if (InventoryUtils.getStackInSlot(i) != null && InventoryUtils.getStackInSlot(i).getItem() != null && InventoryUtils.getStackInSlot(i).getItem() instanceof ItemArmor) {
            armorprot = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i)}, DamageSource.generic);
        }
        return armorprot;
    }

    public static int getFirstItem(Item i1) {
        for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
            if (InventoryUtils.getStackInSlot(i) == null || InventoryUtils.getStackInSlot(i).getItem() == null || InventoryUtils.getStackInSlot(i).getItem() != i1) continue;
            return i;
        }
        return -1;
    }

    public static boolean isBestSword(ItemStack itemSword, int slot) {
        if (itemSword != null && itemSword.getItem() instanceof ItemSword) {
            for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
                ItemStack iStack = InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i);
                if (iStack == null || !(iStack.getItem() instanceof ItemSword) || !(InventoryUtils.getItemDamage(iStack) >= InventoryUtils.getItemDamage(itemSword)) || slot == i) continue;
                return false;
            }
        }
        return true;
    }

    public static int getAmountInHotbar(String item) {
        for (int i = 0; i < 8; ++i) {
            ItemStack is = InventoryUtils.mc.thePlayer.inventory.mainInventory[i];
            if (is == null || !StringUtils.stripControlCodes(is.getDisplayName()).equals(item)) continue;
            return is.stackSize;
        }
        return 0;
    }
}

