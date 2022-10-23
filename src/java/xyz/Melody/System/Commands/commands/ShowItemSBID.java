package xyz.Melody.System.Commands.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.Item.ItemUtils;

public class ShowItemSBID extends Command {
   private Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

   public ShowItemSBID() {
      super("sbid", new String[]{"id"}, "", "FUCK YOU!");
   }

   public String execute(String[] args) {
      ItemStack holding = this.mc.thePlayer.getHeldItem() != null ? this.mc.thePlayer.getHeldItem() : null;
      if (holding != null) {
         NBTTagCompound compound = holding.getTagCompound();
         String id = ItemUtils.getSkyBlockID(holding);
         if (id.equals("NotSBItem")) {
            Helper.sendMessage("Error: " + id);
            return null;
         }

         NBTTagCompound enchants;
         String key;
         String id2;
         Set keys;
         Iterator var11;
         if (id.equals("ENCHANTED_BOOK")) {
            enchants = compound.getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
            keys = enchants.getKeySet();
            var11 = keys.iterator();

            while(var11.hasNext()) {
               key = (String)var11.next();
               id2 = "ENCHANTMENT_" + key.toUpperCase() + "_" + enchants.getInteger(key);
               Helper.sendMessage("Attribute Enchant Info: " + id2);
            }

            Helper.sendMessage("Skyblock ID: " + id);
            return null;
         }

         if (id.equals("PET")) {
            if (ItemUtils.getPetInfo(holding) != null) {
               enchants = compound.getCompoundTag("ExtraAttributes");
               JsonObject petInfo = (JsonObject)this.gson.fromJson(enchants.getString("petInfo"), JsonObject.class);
               String petID = "idk";
               if (petInfo.has("type") && petInfo.has("tier")) {
                  petID = "PET_" + petInfo.get("type").getAsString() + "_" + petInfo.get("tier").getAsString();
               }

               Helper.sendMessage("Attribute Pet Info: " + petID);
               Helper.sendMessage("Skyblock ID: " + id);
               return null;
            }
         } else {
            if (id.equals("RUNE")) {
               enchants = compound.getCompoundTag("ExtraAttributes").getCompoundTag("runes");
               keys = enchants.getKeySet();
               var11 = keys.iterator();

               while(var11.hasNext()) {
                  key = (String)var11.next();
                  id2 = "RUNE_" + key.toUpperCase() + "_" + enchants.getInteger(key);
                  Helper.sendMessage("Attribute Rune Info: " + id2);
               }

               Helper.sendMessage("Skyblock ID: " + id);
               return null;
            }

            Helper.sendMessage(id);
         }
      } else {
         Helper.sendMessage("Please Hold an Item to View it's Skyblock ID.");
      }

      return null;
   }
}
