/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.Item.ItemUtils;

public final class ShowItemSBID
extends Command {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ShowItemSBID() {
        super("sbid", new String[]{"id"}, "", "FUCK YOU!");
    }

    @Override
    public String execute(String[] stringArray) {
        ItemStack itemStack;
        ItemStack itemStack2 = itemStack = this.mc.field_71439_g.func_70694_bm() != null ? this.mc.field_71439_g.func_70694_bm() : null;
        if (itemStack != null) {
            NBTTagCompound nBTTagCompound = itemStack.func_77978_p();
            String string = ItemUtils.getSkyBlockID(itemStack);
            if (string.equals("NotSBItem")) {
                Helper.sendMessage("Error: " + string);
                return null;
            }
            if (string.equals("ENCHANTED_BOOK")) {
                NBTTagCompound nBTTagCompound2 = nBTTagCompound.func_74775_l("ExtraAttributes").func_74775_l("enchantments");
                Set set = nBTTagCompound2.func_150296_c();
                for (String string2 : set) {
                    String string3 = "ENCHANTMENT_" + string2.toUpperCase() + "_" + nBTTagCompound2.func_74762_e(string2);
                    Helper.sendMessage("Attribute Enchant Info: " + string3);
                }
                Helper.sendMessage("Skyblock ID: " + string);
                return null;
            }
            if (string.equals("PET")) {
                if (ItemUtils.getPetInfo(itemStack) != null) {
                    NBTTagCompound nBTTagCompound3 = nBTTagCompound.func_74775_l("ExtraAttributes");
                    JsonObject jsonObject = this.gson.fromJson(nBTTagCompound3.func_74779_i("petInfo"), JsonObject.class);
                    String string4 = "idk";
                    if (jsonObject.has("type") && jsonObject.has("tier")) {
                        string4 = "PET_" + jsonObject.get("type").getAsString() + "_" + jsonObject.get("tier").getAsString();
                    }
                    Helper.sendMessage("Attribute Pet Info: " + string4);
                    Helper.sendMessage("Skyblock ID: " + string);
                    return null;
                }
            } else {
                if (string.equals("RUNE")) {
                    NBTTagCompound nBTTagCompound4 = nBTTagCompound.func_74775_l("ExtraAttributes").func_74775_l("runes");
                    Set set = nBTTagCompound4.func_150296_c();
                    for (String string5 : set) {
                        String string6 = "RUNE_" + string5.toUpperCase() + "_" + nBTTagCompound4.func_74762_e(string5);
                        Helper.sendMessage("Attribute Rune Info: " + string6);
                    }
                    Helper.sendMessage("Skyblock ID: " + string);
                    return null;
                }
                Helper.sendMessage(string);
            }
        } else {
            Helper.sendMessage("Please Hold an Item to View it's Skyblock ID.");
        }
        return null;
    }
}

