package xyz.Melody.Utils.Item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import xyz.Melody.Client;
import xyz.Melody.Utils.Helper;

public class ItemUtils {
   public static Minecraft mc = Minecraft.getMinecraft();

   public static boolean useSBItem(String itm, boolean left) {
      if (useItem(itm.toUpperCase(), !left)) {
         return true;
      } else {
         Helper.sendMessage("Missing Item in Hotbar: " + itm);
         return false;
      }
   }

   public static boolean useSBItem(String itm) {
      if (useItem(itm.toUpperCase(), true)) {
         return true;
      } else {
         Helper.sendMessage("Missing Item in Hotbar: " + itm);
         return false;
      }
   }

   public static boolean hasItemInHotbar(String itemId) {
      for(int i = 0; i < 9; ++i) {
         ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
         if (itemId.equals(getSkyBlockID(item))) {
            return true;
         }
      }

      return false;
   }

   private static boolean useItem(String itemId, boolean rightClick) {
      for(int i = 0; i < 9; ++i) {
         ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
         if (itemId.equals(getSkyBlockID(item))) {
            int previousItem = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = i;
            if (rightClick) {
               mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, item);
            } else {
               Client.leftClick();
            }

            mc.thePlayer.inventory.currentItem = previousItem;
            return true;
         }
      }

      return false;
   }

   public static NBTTagCompound getExtraAttributes(ItemStack item) {
      return !item.hasTagCompound() ? null : item.getSubCompound("ExtraAttributes", false);
   }

   public static String getSkyBlockID(ItemStack item) {
      NBTTagCompound extraAttributes;
      return item != null && (extraAttributes = item.getSubCompound("ExtraAttributes", false)) != null && extraAttributes.hasKey("id") ? extraAttributes.getString("id") : "NotSBItem";
   }

   public static NBTTagCompound getPetInfo(ItemStack item) {
      if (getSkyBlockID(item) != "NotSBItem") {
         NBTTagCompound compound = item.getTagCompound();
         NBTTagCompound info = compound.getCompoundTag("ExtraAttributes").getCompoundTag("petInfo");
         return info;
      } else {
         return null;
      }
   }

   public static String[] getLoreFromNBT(NBTTagCompound tag) {
      String[] lore = new String[0];
      NBTTagCompound display = tag.getCompoundTag("display");
      if (display.hasKey("Lore", 9)) {
         NBTTagList list = display.getTagList("Lore", 8);
         lore = new String[list.tagCount()];

         for(int k = 0; k < list.tagCount(); ++k) {
            lore[k] = list.getStringTagAt(k);
         }
      }

      return lore;
   }
}
