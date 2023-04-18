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
    public static final List<Block> BLOCK_BLACKLIST = Arrays.asList(Blocks.field_150381_bn, Blocks.field_150486_ae, Blocks.field_150477_bB, Blocks.field_150447_bR, Blocks.field_150467_bQ, Blocks.field_150354_m, Blocks.field_150321_G, Blocks.field_150478_aa, Blocks.field_150462_ai, Blocks.field_150460_al, Blocks.field_150392_bi, Blocks.field_150367_z, Blocks.field_150456_au, Blocks.field_150452_aw, Blocks.field_150323_B, Blocks.field_150409_cd, Blocks.field_150335_W, Blocks.field_180393_cK, Blocks.field_180394_cL);
    public static Minecraft mc = Minecraft.func_71410_x();

    public void dropSlot(int n) {
        int n2 = new GuiInventory((EntityPlayer)InventoryUtils.mc.field_71439_g).field_147002_h.field_75152_c;
        InventoryUtils.mc.field_71442_b.func_78753_a(n2, n, 1, 4, InventoryUtils.mc.field_71439_g);
    }

    public static void updateInventory() {
        for (int i = 0; i < 44; ++i) {
            try {
                int n = i < 9 ? 36 : 0;
                Minecraft.func_71410_x().field_71439_g.field_71174_a.func_147297_a(new C10PacketCreativeInventoryAction(i + n, Minecraft.func_71410_x().field_71439_g.field_71071_by.field_70462_a[i]));
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static ItemStack getStackInSlot(int n) {
        return InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n);
    }

    public static boolean isBestArmorOfTypeInInv(ItemStack itemStack) {
        try {
            ItemArmor itemArmor;
            ItemStack itemStack2;
            int n;
            if (itemStack == null) {
                return false;
            }
            if (itemStack.func_77973_b() == null) {
                return false;
            }
            if (itemStack.func_77973_b() != null && !(itemStack.func_77973_b() instanceof ItemArmor)) {
                return false;
            }
            ItemArmor itemArmor2 = (ItemArmor)itemStack.func_77973_b();
            int n2 = InventoryUtils.getArmorProt(itemStack);
            for (n = 0; n < 4; ++n) {
                itemStack2 = InventoryUtils.mc.field_71439_g.field_71071_by.field_70460_b[n];
                if (itemStack2 == null) continue;
                itemArmor = (ItemArmor)itemStack2.func_77973_b();
                if (itemArmor.field_77881_a != itemArmor2.field_77881_a || InventoryUtils.getArmorProt(itemStack2) < n2) continue;
                return false;
            }
            for (n = 0; n < InventoryUtils.mc.field_71439_g.field_71071_by.func_70302_i_() - 4; ++n) {
                itemStack2 = InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n);
                if (itemStack2 == null || !(itemStack2.func_77973_b() instanceof ItemArmor)) continue;
                itemArmor = (ItemArmor)itemStack2.func_77973_b();
                if (itemArmor.field_77881_a != itemArmor2.field_77881_a || itemArmor == itemArmor2 || InventoryUtils.getArmorProt(itemStack2) < n2) continue;
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
            ItemStack itemStack = Minecraft.func_71410_x().field_71439_g.field_71071_by.func_70301_a(i);
            if (itemStack == null || itemStack.func_77973_b() != item) continue;
            return true;
        }
        return false;
    }

    public static boolean hotbarHas(Item item, int n) {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemStack = Minecraft.func_71410_x().field_71439_g.field_71071_by.func_70301_a(i);
            if (itemStack == null || itemStack.func_77973_b() != item || InventoryUtils.getSlotID(itemStack.func_77973_b()) != n) continue;
            return true;
        }
        return false;
    }

    public static int getSlotID(Item item) {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemStack = Minecraft.func_71410_x().field_71439_g.field_71071_by.func_70301_a(i);
            if (itemStack == null || itemStack.func_77973_b() != item) continue;
            return i;
        }
        return -1;
    }

    public static ItemStack getItemBySlotID(int n) {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemStack = Minecraft.func_71410_x().field_71439_g.field_71071_by.func_70301_a(i);
            if (itemStack == null || InventoryUtils.getSlotID(itemStack.func_77973_b()) != n) continue;
            return itemStack;
        }
        return null;
    }

    public static int getArmorProt(ItemStack itemStack) {
        int n = -1;
        if (itemStack != null && itemStack.func_77973_b() != null && itemStack.func_77973_b() instanceof ItemArmor) {
            n = ((ItemArmor)itemStack.func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(itemStack)) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{itemStack}, (DamageSource)DamageSource.field_76377_j);
        }
        return n;
    }

    public static int getBestSwordSlotID(ItemStack itemStack, double d) {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemStack2 = Minecraft.func_71410_x().field_71439_g.field_71071_by.func_70301_a(i);
            if (itemStack2 == null || itemStack2 != itemStack || InventoryUtils.getSwordDamage(itemStack2) != InventoryUtils.getSwordDamage(itemStack)) continue;
            return i;
        }
        return -1;
    }

    private static double getSwordDamage(ItemStack itemStack) {
        double d = 0.0;
        Optional optional = itemStack.func_111283_C().values().stream().findFirst();
        if (optional.isPresent()) {
            d = ((AttributeModifier)optional.get()).func_111164_d();
        }
        return d += (double)EnchantmentHelper.func_152377_a((ItemStack)itemStack, (EnumCreatureAttribute)EnumCreatureAttribute.UNDEFINED);
    }

    public boolean isBestChest(int n) {
        if (InventoryUtils.getStackInSlot(n) != null && InventoryUtils.getStackInSlot(n).func_77973_b() != null && InventoryUtils.getStackInSlot(n).func_77973_b() instanceof ItemArmor) {
            int n2;
            ItemArmor itemArmor;
            int n3 = ((ItemArmor)InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n).func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n))) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n)}, (DamageSource)DamageSource.field_76377_j);
            if (InventoryUtils.mc.field_71439_g.field_71071_by.field_70460_b[2] != null) {
                ItemStack itemStack = InventoryUtils.mc.field_71439_g.field_71071_by.field_70460_b[2];
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).func_77973_b();
                n2 = ((ItemArmor)itemStack.func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(itemStack)) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{itemStack}, (DamageSource)DamageSource.field_76377_j);
                if (n2 > n3 || n2 == n3) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.field_71439_g.field_71071_by.func_70302_i_(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemArmor)) continue;
                n2 = ((ItemArmor)InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i))) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i)}, (DamageSource)DamageSource.field_76377_j);
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).func_77973_b();
                ItemArmor itemArmor2 = (ItemArmor)InventoryUtils.getStackInSlot(i).func_77973_b();
                if (itemArmor.field_77881_a != 1 || itemArmor2.field_77881_a != 1 || n2 <= n3) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestHelmet(int n) {
        if (InventoryUtils.getStackInSlot(n) != null && InventoryUtils.getStackInSlot(n).func_77973_b() != null && InventoryUtils.getStackInSlot(n).func_77973_b() instanceof ItemArmor) {
            int n2;
            ItemArmor itemArmor;
            int n3 = ((ItemArmor)InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n).func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n))) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n)}, (DamageSource)DamageSource.field_76377_j);
            if (InventoryUtils.mc.field_71439_g.field_71071_by.field_70460_b[3] != null) {
                ItemStack itemStack = InventoryUtils.mc.field_71439_g.field_71071_by.field_70460_b[3];
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).func_77973_b();
                n2 = ((ItemArmor)itemStack.func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(itemStack)) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{itemStack}, (DamageSource)DamageSource.field_76377_j);
                if (n2 > n3 || n2 == n3) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.field_71439_g.field_71071_by.func_70302_i_(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemArmor)) continue;
                n2 = ((ItemArmor)InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i))) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i)}, (DamageSource)DamageSource.field_76377_j);
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).func_77973_b();
                ItemArmor itemArmor2 = (ItemArmor)InventoryUtils.getStackInSlot(i).func_77973_b();
                if (itemArmor.field_77881_a != 0 || itemArmor2.field_77881_a != 0 || n2 <= n3) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestLeggings(int n) {
        if (InventoryUtils.getStackInSlot(n) != null && InventoryUtils.getStackInSlot(n).func_77973_b() != null && InventoryUtils.getStackInSlot(n).func_77973_b() instanceof ItemArmor) {
            int n2;
            ItemArmor itemArmor;
            int n3 = ((ItemArmor)InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n).func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n))) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n)}, (DamageSource)DamageSource.field_76377_j);
            if (InventoryUtils.mc.field_71439_g.field_71071_by.field_70460_b[1] != null) {
                ItemStack itemStack = InventoryUtils.mc.field_71439_g.field_71071_by.field_70460_b[1];
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).func_77973_b();
                n2 = ((ItemArmor)itemStack.func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(itemStack)) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{itemStack}, (DamageSource)DamageSource.field_76377_j);
                if (n2 > n3 || n2 == n3) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.field_71439_g.field_71071_by.func_70302_i_(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemArmor)) continue;
                n2 = ((ItemArmor)InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i))) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i)}, (DamageSource)DamageSource.field_76377_j);
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).func_77973_b();
                ItemArmor itemArmor2 = (ItemArmor)InventoryUtils.getStackInSlot(i).func_77973_b();
                if (itemArmor.field_77881_a != 2 || itemArmor2.field_77881_a != 2 || n2 <= n3) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestBoots(int n) {
        if (InventoryUtils.getStackInSlot(n) != null && InventoryUtils.getStackInSlot(n).func_77973_b() != null && InventoryUtils.getStackInSlot(n).func_77973_b() instanceof ItemArmor) {
            int n2;
            ItemArmor itemArmor;
            int n3 = ((ItemArmor)InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n).func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n))) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n)}, (DamageSource)DamageSource.field_76377_j);
            if (InventoryUtils.mc.field_71439_g.field_71071_by.field_70460_b[0] != null) {
                ItemStack itemStack = InventoryUtils.mc.field_71439_g.field_71071_by.field_70460_b[0];
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).func_77973_b();
                n2 = ((ItemArmor)itemStack.func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(itemStack)) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{itemStack}, (DamageSource)DamageSource.field_76377_j);
                if (n2 > n3 || n2 == n3) {
                    return false;
                }
            }
            for (int i = 0; i < InventoryUtils.mc.field_71439_g.field_71071_by.func_70302_i_(); ++i) {
                if (InventoryUtils.getStackInSlot(i) == null || !(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemArmor)) continue;
                n2 = ((ItemArmor)InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i))) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i)}, (DamageSource)DamageSource.field_76377_j);
                itemArmor = (ItemArmor)InventoryUtils.getStackInSlot(n).func_77973_b();
                ItemArmor itemArmor2 = (ItemArmor)InventoryUtils.getStackInSlot(i).func_77973_b();
                if (itemArmor.field_77881_a != 3 || itemArmor2.field_77881_a != 3 || n2 <= n3) continue;
                return false;
            }
        }
        return true;
    }

    public boolean isBestSword(int n) {
        return this.getBestWeapon() == n;
    }

    public static int getItemType(ItemStack itemStack) {
        if (itemStack.func_77973_b() instanceof ItemArmor) {
            ItemArmor itemArmor = (ItemArmor)itemStack.func_77973_b();
            return itemArmor.field_77881_a;
        }
        return -1;
    }

    public static float getItemDamage(ItemStack itemStack) {
        Iterator iterator;
        Multimap multimap = itemStack.func_111283_C();
        if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
            Map.Entry entry = iterator.next();
            AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
            double d = attributeModifier.func_111169_c() != 1 && attributeModifier.func_111169_c() != 2 ? attributeModifier.func_111164_d() : attributeModifier.func_111164_d() * 100.0;
            return attributeModifier.func_111164_d() > 1.0 ? 1.0f + (float)d : 1.0f;
        }
        return 1.0f;
    }

    public boolean hasItemMoreTimes(int n) {
        ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();
        arrayList.clear();
        for (int i = 0; i < InventoryUtils.mc.field_71439_g.field_71071_by.func_70302_i_(); ++i) {
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
        int n = InventoryUtils.mc.field_71439_g.field_71071_by.field_70461_c;
        int n2 = -1;
        float f = 1.0f;
        for (int n3 = 0; n3 < 9; n3 = (int)((byte)(n3 + 1))) {
            float f2;
            ItemStack itemStack = InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n3);
            if (itemStack == null) continue;
            float f3 = InventoryUtils.getItemDamage(itemStack);
            f3 += EnchantmentHelper.func_152377_a((ItemStack)itemStack, (EnumCreatureAttribute)EnumCreatureAttribute.UNDEFINED);
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
        int n = InventoryUtils.mc.field_71439_g.field_71071_by.field_70461_c;
        int n2 = -1;
        float f = 1.0f;
        for (int n3 = 0; n3 < InventoryUtils.mc.field_71439_g.field_71071_by.func_70302_i_(); n3 = (int)((byte)(n3 + 1))) {
            float f2;
            ItemStack itemStack;
            if (InventoryUtils.getStackInSlot(n3) == null || (itemStack = InventoryUtils.getStackInSlot(n3)) == null || itemStack.func_77973_b() == null || !(itemStack.func_77973_b() instanceof ItemSword)) continue;
            float f3 = InventoryUtils.getItemDamage(itemStack);
            f3 += EnchantmentHelper.func_152377_a((ItemStack)itemStack, (EnumCreatureAttribute)EnumCreatureAttribute.UNDEFINED);
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
        if (InventoryUtils.getStackInSlot(n) != null && InventoryUtils.getStackInSlot(n).func_77973_b() != null && InventoryUtils.getStackInSlot(n).func_77973_b() instanceof ItemArmor) {
            n2 = ((ItemArmor)InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n).func_77973_b()).func_82812_d().func_78044_b(InventoryUtils.getItemType(InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n))) + EnchantmentHelper.func_77508_a((ItemStack[])new ItemStack[]{InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(n)}, (DamageSource)DamageSource.field_76377_j);
        }
        return n2;
    }

    public static int getFirstItem(Item item) {
        for (int i = 0; i < InventoryUtils.mc.field_71439_g.field_71071_by.func_70302_i_(); ++i) {
            if (InventoryUtils.getStackInSlot(i) == null || InventoryUtils.getStackInSlot(i).func_77973_b() == null || InventoryUtils.getStackInSlot(i).func_77973_b() != item) continue;
            return i;
        }
        return -1;
    }

    public static boolean isBestSword(ItemStack itemStack, int n) {
        if (itemStack != null && itemStack.func_77973_b() instanceof ItemSword) {
            for (int i = 0; i < InventoryUtils.mc.field_71439_g.field_71071_by.func_70302_i_(); ++i) {
                ItemStack itemStack2 = InventoryUtils.mc.field_71439_g.field_71071_by.func_70301_a(i);
                if (itemStack2 == null || !(itemStack2.func_77973_b() instanceof ItemSword) || !(InventoryUtils.getItemDamage(itemStack2) >= InventoryUtils.getItemDamage(itemStack)) || n == i) continue;
                return false;
            }
        }
        return true;
    }

    public static int getAmountInHotbar(String string) {
        for (int i = 0; i < 8; ++i) {
            ItemStack itemStack = InventoryUtils.mc.field_71439_g.field_71071_by.field_70462_a[i];
            if (itemStack == null || !StringUtils.func_76338_a((String)itemStack.func_82833_r()).equals(string)) continue;
            return itemStack.field_77994_a;
        }
        return 0;
    }
}

