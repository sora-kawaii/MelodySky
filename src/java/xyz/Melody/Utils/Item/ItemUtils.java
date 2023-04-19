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

    public static boolean useSBItem(String itm, boolean left) {
        if (ItemUtils.useItem(itm.toUpperCase(), !left)) {
            return true;
        }
        Helper.sendMessage("Missing Item in Hotbar: " + itm);
        return false;
    }

    public static boolean useSBItem(String itm) {
        if (ItemUtils.useItem(itm.toUpperCase(), true)) {
            return true;
        }
        Helper.sendMessage("Missing Item in Hotbar: " + itm);
        return false;
    }

    public static boolean hasItemInHotbar(String itemId) {
        for (int i = 0; i < 9; ++i) {
            ItemStack item = ItemUtils.mc.thePlayer.inventory.getStackInSlot(i);
            if (!itemId.equals(ItemUtils.getSkyBlockID(item))) continue;
            return true;
        }
        return false;
    }

    private static boolean useItem(String itemId, boolean rightClick) {
        for (int i = 0; i < 9; ++i) {
            ItemStack item = ItemUtils.mc.thePlayer.inventory.getStackInSlot(i);
            if (!itemId.equals(ItemUtils.getSkyBlockID(item))) continue;
            int previousItem = ItemUtils.mc.thePlayer.inventory.currentItem;
            ItemUtils.mc.thePlayer.inventory.currentItem = i;
            if (rightClick) {
                ItemUtils.mc.playerController.sendUseItem(ItemUtils.mc.thePlayer, ItemUtils.mc.theWorld, item);
            } else {
                Client.leftClick();
            }
            ItemUtils.mc.thePlayer.inventory.currentItem = previousItem;
            return true;
        }
        return false;
    }

    public static NBTTagCompound getExtraAttributes(ItemStack item) {
        if (!item.hasTagCompound()) {
            return null;
        }
        return item.getSubCompound("ExtraAttributes", false);
    }

    public static String getSkyBlockID(ItemStack item) {
        NBTTagCompound extraAttributes;
        if (item != null && (extraAttributes = item.getSubCompound("ExtraAttributes", false)) != null && extraAttributes.hasKey("id")) {
            return extraAttributes.getString("id");
        }
        return "NotSBItem";
    }

    public static NBTTagCompound getPetInfo(ItemStack item) {
        if (ItemUtils.getSkyBlockID(item) != "NotSBItem") {
            NBTTagCompound compound = item.getTagCompound();
            NBTTagCompound info = compound.getCompoundTag("ExtraAttributes").getCompoundTag("petInfo");
            return info;
        }
        return null;
    }

    public static String[] getLoreFromNBT(NBTTagCompound tag) {
        String[] lore = new String[]{};
        NBTTagCompound display = tag.getCompoundTag("display");
        if (display.hasKey("Lore", 9)) {
            NBTTagList list = display.getTagList("Lore", 8);
            lore = new String[list.tagCount()];
            for (int k = 0; k < list.tagCount(); ++k) {
                lore[k] = list.getStringTagAt(k);
            }
        }
        return lore;
    }
}

