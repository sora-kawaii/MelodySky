package xyz.Melody.Utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils {
   public static NBTTagCompound getExtraAttributes(ItemStack item) {
      return item != null && item.hasTagCompound() ? item.getSubCompound("ExtraAttributes", false) : null;
   }
}
