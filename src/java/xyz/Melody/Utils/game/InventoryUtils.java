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

    public void dropSlot(int n) {
        int n2 = new GuiInventory((EntityPlayer)InventoryUtils.mc.thePlayer).inventorySlots.windowId;
        InventoryUtils.mc.playerController.windowClick(n2, n, 1, 4, InventoryUtils.mc.thePlayer);
    }

    public static void updateInventory() {
        for (int i = 0; i < 44; ++i) {
            try {
                int n = i < 9 ? 36 : 0;
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(i + n, Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i]));
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static ItemStack getStackInSlot(int n) {
        return InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n);
    }

    public static boolean isBestArmorOfTypeInInv(ItemStack itemStack) {
        try {
            ItemArmor itemArmor;
            ItemStack itemStack2;
            int n;
            if (itemStack == null) {
                return false;
            }
            if (itemStack.getItem() == null) {
                return false;
            }
            if (itemStack.getItem() != null && !(itemStack.getItem() instanceof ItemArmor)) {
                return false;
            }
            ItemArmor itemArmor2 = (ItemArmor)itemStack.getItem();
            int n2 = InventoryUtils.getArmorProt(itemStack);
            for (n = 0; n < 4; ++n) {
                itemStack2 = InventoryUtils.mc.thePlayer.inventory.armorInventory[n];
                if (itemStack2 == null) continue;
                itemArmor = (ItemArmor)itemStack2.getItem();
                if (itemArmor.armorType != itemArmor2.armorType || InventoryUtils.getArmorProt(itemStack2) < n2) continue;
                return false;
            }
            for (n = 0; n < InventoryUtils.mc.thePlayer.inventory.getSizeInventory() - 4; ++n) {
                itemStack2 = InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n);
                if (itemStack2 == null || !(itemStack2.getItem() instanceof ItemArmor)) continue;
                itemArmor = (ItemArmor)itemStack2.getItem();
                if (itemArmor.armorType != itemArmor2.armorType || itemArmor == itemArmor2 || InventoryUtils.getArmorProt(itemStack2) < n2) continue;
                return false;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return true;
    }

    public static boolean hotbarHas(Item item) {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() != item) continue;
            return true;
        }
        return false;
    }

    public static boolean hotbarHas(Item item, int n) {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() != item || InventoryUtils.getSlotID(itemStack.getItem()) != n) continue;
            return true;
        }
        return false;
    }

    public static int getSlotID(Item item) {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() != item) continue;
            return i;
        }
        return -1;
    }

    public static ItemStack getItemBySlotID(int n) {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || InventoryUtils.getSlotID(itemStack.getItem()) != n) continue;
            return itemStack;
        }
        return null;
    }

    public static int getArmorProt(ItemStack itemStack) {
        int n = -1;
        if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemArmor) {
            n = ((ItemArmor)itemStack.getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(itemStack)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
        }
        return n;
    }

    public static int getBestSwordSlotID(ItemStack itemStack, double d) {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemStack2 = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
            if (itemStack2 == null || itemStack2 != itemStack || InventoryUtils.getSwordDamage(itemStack2) != InventoryUtils.getSwordDamage(itemStack)) continue;
            return i;
        }
        return -1;
    }

    private static double getSwordDamage(ItemStack itemStack) {
        double d = 0.0;
        Optional<AttributeModifier> optional = itemStack.getAttributeModifiers().values().stream().findFirst();
        if (optional.isPresent()) {
            d = optional.get().getAmount();
        }
        return d += (double)EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
    }

    public boolean isBestChest(int n) {
        if (InventoryUtils.getStackInSlot(n) != null && InventoryUtils.getStackInSlot(n).getItem() != null && InventoryUtils.getStackInSlot(n).getItem() instanceof ItemArmor) {
            int n2;
            ItemArmor itemArmor;
            int n3 = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n)}, DamageSource.generic);
            if (InventoryUtils.mc.thePlayer.inventory.armorInventory[2] != null) {
                ItemStack itemStack = InventoryUtils.mc.thePlayer.inventory.armorInventory[2];
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).getItem();
                n2 = ((ItemArmor)itemStack.getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(itemStack)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
                if (n2 > n3 || n2 == n3) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) continue;
                n2 = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i)}, DamageSource.generic);
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).getItem();
                ItemArmor itemArmor2 = (ItemArmor)InventoryUtils.getStackInSlot(i).getItem();
                if (itemArmor.armorType != 1 || itemArmor2.armorType != 1 || n2 <= n3) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestHelmet(int n) {
        if (InventoryUtils.getStackInSlot(n) != null && InventoryUtils.getStackInSlot(n).getItem() != null && InventoryUtils.getStackInSlot(n).getItem() instanceof ItemArmor) {
            int n2;
            ItemArmor itemArmor;
            int n3 = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n)}, DamageSource.generic);
            if (InventoryUtils.mc.thePlayer.inventory.armorInventory[3] != null) {
                ItemStack itemStack = InventoryUtils.mc.thePlayer.inventory.armorInventory[3];
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).getItem();
                n2 = ((ItemArmor)itemStack.getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(itemStack)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
                if (n2 > n3 || n2 == n3) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) continue;
                n2 = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i)}, DamageSource.generic);
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).getItem();
                ItemArmor itemArmor2 = (ItemArmor)InventoryUtils.getStackInSlot(i).getItem();
                if (itemArmor.armorType != 0 || itemArmor2.armorType != 0 || n2 <= n3) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestLeggings(int n) {
        if (InventoryUtils.getStackInSlot(n) != null && InventoryUtils.getStackInSlot(n).getItem() != null && InventoryUtils.getStackInSlot(n).getItem() instanceof ItemArmor) {
            int n2;
            ItemArmor itemArmor;
            int n3 = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n)}, DamageSource.generic);
            if (InventoryUtils.mc.thePlayer.inventory.armorInventory[1] != null) {
                ItemStack itemStack = InventoryUtils.mc.thePlayer.inventory.armorInventory[1];
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).getItem();
                n2 = ((ItemArmor)itemStack.getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(itemStack)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
                if (n2 > n3 || n2 == n3) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) continue;
                n2 = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i)}, DamageSource.generic);
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).getItem();
                ItemArmor itemArmor2 = (ItemArmor)InventoryUtils.getStackInSlot(i).getItem();
                if (itemArmor.armorType != 2 || itemArmor2.armorType != 2 || n2 <= n3) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestBoots(int n) {
        if (InventoryUtils.getStackInSlot(n) != null && InventoryUtils.getStackInSlot(n).getItem() != null && InventoryUtils.getStackInSlot(n).getItem() instanceof ItemArmor) {
            int n2;
            ItemArmor itemArmor;
            int n3 = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n)}, DamageSource.generic);
            if (InventoryUtils.mc.thePlayer.inventory.armorInventory[0] != null) {
                ItemStack itemStack = InventoryUtils.mc.thePlayer.inventory.armorInventory[0];
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).getItem();
                n2 = ((ItemArmor)itemStack.getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(itemStack)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
                if (n2 > n3 || n2 == n3) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) continue;
                n2 = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i)}, DamageSource.generic);
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).getItem();
                ItemArmor itemArmor2 = (ItemArmor)InventoryUtils.getStackInSlot(i).getItem();
                if (itemArmor.armorType != 3 || itemArmor2.armorType != 3 || n2 <= n3) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestSword(int n) {
        return this.getBestWeapon() == n;
    }

    public static int getItemType(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemArmor) {
            ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
            return itemArmor.armorType;
        }
        return -1;
    }

    public static float getItemDamage(ItemStack itemStack) {
        Iterator<Map.Entry<String, AttributeModifier>> iterator;
        Multimap<String, AttributeModifier> multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
            Map.Entry<String, AttributeModifier> entry = iterator.next();
            AttributeModifier attributeModifier = entry.getValue();
            double d = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;
            return attributeModifier.getAmount() > 1.0 ? 1.0f + (float)d : 1.0f;
        }
        return 1.0f;
    }

    public boolean hasItemMoreTimes(int n) {
        ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();
        arrayList.clear();
        for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
            if (!arrayList.contains(InventoryUtils.getStackInSlot(i))) {
                arrayList.add(InventoryUtils.getStackInSlot(i));
                continue;
            }
            if (InventoryUtils.getStackInSlot(i) != InventoryUtils.getStackInSlot(n)) continue;
            return true;
        }
        return false;
    }

    public int getBestWeaponInHotbar() {
        int n = InventoryUtils.mc.thePlayer.inventory.currentItem;
        int n2 = -1;
        float f = 1.0f;
        for (int n3 = 0; n3 < 9; n3 = (int)((byte)(n3 + 1))) {
            float f2;
            ItemStack itemStack = InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n3);
            if (itemStack == null) continue;
            float f3 = InventoryUtils.getItemDamage(itemStack);
            f3 += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
            if (!(f2 > f)) continue;
            f = f3;
            n2 = n3;
        }
        if (n2 != -1) {
            return n2;
        }
        return n;
    }

    public int getBestWeapon() {
        int n = InventoryUtils.mc.thePlayer.inventory.currentItem;
        int n2 = -1;
        float f = 1.0f;
        for (int n3 = 0; n3 < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); n3 = (int)((byte)(n3 + 1))) {
            float f2;
            ItemStack itemStack;
            if (InventoryUtils.getStackInSlot(n3) == null || (itemStack = InventoryUtils.getStackInSlot(n3)) == null || itemStack.getItem() == null || !(itemStack.getItem() instanceof ItemSword)) continue;
            float f3 = InventoryUtils.getItemDamage(itemStack);
            f3 += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
            if (!(f2 > f)) continue;
            f = f3;
            n2 = n3;
        }
        if (n2 != -1) {
            return n2;
        }
        return n;
    }

    public int getArmorProt(int n) {
        int n2 = -1;
        if (InventoryUtils.getStackInSlot(n) != null && InventoryUtils.getStackInSlot(n).getItem() != null && InventoryUtils.getStackInSlot(n).getItem() instanceof ItemArmor) {
            n2 = ((ItemArmor)InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n).getItem()).getArmorMaterial().getDamageReductionAmount(InventoryUtils.getItemType(InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n))) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{InventoryUtils.mc.thePlayer.inventory.getStackInSlot(n)}, DamageSource.generic);
        }
        return n2;
    }

    public static int getFirstItem(Item item) {
        for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
            if (InventoryUtils.getStackInSlot(i) == null || InventoryUtils.getStackInSlot(i).getItem() == null || InventoryUtils.getStackInSlot(i).getItem() != item) continue;
            return i;
        }
        return -1;
    }

    public static boolean isBestSword(ItemStack itemStack, int n) {
        if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
            for (int i = 0; i < InventoryUtils.mc.thePlayer.inventory.getSizeInventory(); ++i) {
                ItemStack itemStack2 = InventoryUtils.mc.thePlayer.inventory.getStackInSlot(i);
                if (itemStack2 == null || !(itemStack2.getItem() instanceof ItemSword) || !(InventoryUtils.getItemDamage(itemStack2) >= InventoryUtils.getItemDamage(itemStack)) || n == i) continue;
                return false;
            }
        }
        return true;
    }

    public static int getAmountInHotbar(String string) {
        for (int i = 0; i < 8; ++i) {
            ItemStack itemStack = InventoryUtils.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || !StringUtils.stripControlCodes(itemStack.getDisplayName()).equals(string)) continue;
            return itemStack.stackSize;
        }
        return 0;
    }
}

