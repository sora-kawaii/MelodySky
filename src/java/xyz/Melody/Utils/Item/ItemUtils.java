/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.Item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import xyz.Melody.Client;
import xyz.Melody.Utils.Helper;

public final class ItemUtils {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean useSBItem(String string, boolean bl) {
        if (ItemUtils.useItem(string.toUpperCase(), !bl)) {
            return true;
        }
        Helper.sendMessage("Missing Item in Hotbar: " + string);
        return false;
    }

    public static boolean useSBItem(String string) {
        if (ItemUtils.useItem(string.toUpperCase(), true)) {
            return true;
        }
        Helper.sendMessage("Missing Item in Hotbar: " + string);
        return false;
    }

    public static boolean hasItemInHotbar(String string) {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = ItemUtils.mc.thePlayer.inventory.getStackInSlot(i);
            if (!string.equals(ItemUtils.getSkyBlockID(itemStack))) continue;
            return true;
        }
        return false;
    }

    private static boolean useItem(String string, boolean bl) {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = ItemUtils.mc.thePlayer.inventory.getStackInSlot(i);
            if (!string.equals(ItemUtils.getSkyBlockID(itemStack))) continue;
            int n = ItemUtils.mc.thePlayer.inventory.currentItem;
            ItemUtils.mc.thePlayer.inventory.currentItem = i;
            if (bl) {
                ItemUtils.mc.playerController.sendUseItem(ItemUtils.mc.thePlayer, ItemUtils.mc.theWorld, itemStack);
            } else {
                Client.leftClick();
            }
            ItemUtils.mc.thePlayer.inventory.currentItem = n;
            return true;
        }
        return false;
    }

    public static NBTTagCompound getExtraAttributes(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            return null;
        }
        return itemStack.getSubCompound("ExtraAttributes", false);
    }

    public static String getSkyBlockID(ItemStack itemStack) {
        NBTTagCompound nBTTagCompound;
        if (itemStack != null && (nBTTagCompound = itemStack.getSubCompound("ExtraAttributes", false)) != null && nBTTagCompound.hasKey("id")) {
            return nBTTagCompound.getString("id");
        }
        return "NotSBItem";
    }

    public static NBTTagCompound getPetInfo(ItemStack itemStack) {
        if (ItemUtils.getSkyBlockID(itemStack) != "NotSBItem") {
            NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
            NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("ExtraAttributes").getCompoundTag("petInfo");
            return nBTTagCompound2;
        }
        return null;
    }

    public static String[] getLoreFromNBT(NBTTagCompound nBTTagCompound) {
        String[] stringArray = new String[]{};
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("display");
        if (nBTTagCompound2.hasKey("Lore", 9)) {
            NBTTagList nBTTagList = nBTTagCompound2.getTagList("Lore", 8);
            stringArray = new String[nBTTagList.tagCount()];
            for (int i = 0; i < nBTTagList.tagCount(); ++i) {
                stringArray[i] = nBTTagList.getStringTagAt(i);
            }
        }
        return stringArray;
    }
}

