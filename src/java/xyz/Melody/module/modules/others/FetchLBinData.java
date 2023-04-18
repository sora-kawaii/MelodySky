/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.System.Managers.Auctions.AhBzManager;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class FetchLBinData
extends Module {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Option<Boolean> sbonly = new Option<Boolean>("Skyblock Only", true);
    public Option<Boolean> hbin = new Option<Boolean>("HBin", false);
    public Option<Boolean> fmt = new Option<Boolean>("Format", false);
    public Numbers<Double> delay = new Numbers<Double>("Delay(Min)", 1.0, 0.5, 10.0, 0.5);
    public static String colorPrefix = "6";
    private TimerUtil timer = new TimerUtil();
    private static final NavigableMap<Long, String> suffixes = new TreeMap<Long, String>();

    public FetchLBinData() {
        super("FetchLBinData", new String[]{"lbin"}, ModuleType.Others);
        this.addValues(this.delay, this.sbonly, this.fmt, this.hbin);
        this.setModInfo("Show Auction or Bazaar Data as ToolTip.");
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent itemTooltipEvent) {
        ItemStack itemStack = itemTooltipEvent.itemStack;
        if (Client.inSkyblock || !((Boolean)this.sbonly.getValue()).booleanValue()) {
            NBTTagCompound nBTTagCompound = itemStack.func_77978_p();
            String string = ItemUtils.getSkyBlockID(itemStack);
            if (string.equals("NotSBItem")) {
                return;
            }
            if (string.equals("ENCHANTED_BOOK")) {
                NBTTagCompound nBTTagCompound2 = nBTTagCompound.func_74775_l("ExtraAttributes").func_74775_l("enchantments");
                Set set = nBTTagCompound2.func_150296_c();
                int n = 0;
                for (String string2 : set) {
                    String string3 = "ENCHANTMENT_" + string2.toUpperCase() + "_" + nBTTagCompound2.func_74762_e(string2);
                    AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(string3);
                    if (auctionData == null || ++n >= 10) continue;
                    if (auctionData.getPrices().size() == 0) {
                        itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Bazaar Sell\u00a77: " + (auctionData.getSellPrice() == -1L ? "\u00a78N/A" : "\u00a7e" + this.format(auctionData.getSellPrice())));
                        itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Bazaar Buy\u00a77: " + (auctionData.getBuyPrice() == -1L ? "\u00a78N/A" : "\u00a7e" + this.format(auctionData.getBuyPrice())));
                        continue;
                    }
                    if (auctionData.getSellPrice() == -1L) {
                        itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Lowest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().first()) : "\u00a78N/A"));
                        if (!((Boolean)this.hbin.getValue()).booleanValue()) continue;
                        itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Highest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().last()) : "\u00a78N/A"));
                        continue;
                    }
                    itemTooltipEvent.toolTip.add("\u00a78Failed to Fetch Action/Bazaar Data.");
                }
                if (n >= 10) {
                    itemTooltipEvent.toolTip.add("\u00a77" + (n - 10) + " more enchants... ");
                }
            } else if (string.equals("PET")) {
                if (ItemUtils.getPetInfo(itemStack) != null) {
                    NBTTagCompound nBTTagCompound3 = nBTTagCompound.func_74775_l("ExtraAttributes");
                    JsonObject jsonObject = this.gson.fromJson(nBTTagCompound3.func_74779_i("petInfo"), JsonObject.class);
                    String string4 = "idk";
                    if (jsonObject != null) {
                        AhBzManager.AuctionData auctionData;
                        if (jsonObject.has("type") && jsonObject.has("tier")) {
                            string4 = "PET_" + jsonObject.get("type").getAsString() + "_" + jsonObject.get("tier").getAsString();
                        }
                        if (string4 != "idk" && (auctionData = AhBzManager.auctions.get(string4)) != null && auctionData.getSellPrice() == -1L) {
                            itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Lowest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().first()) : "\u00a78N/A"));
                            if (((Boolean)this.hbin.getValue()).booleanValue()) {
                                itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Highest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().last()) : "\u00a78N/A"));
                            }
                        }
                    }
                }
            } else if (string.equals("RUNE")) {
                NBTTagCompound nBTTagCompound4 = nBTTagCompound.func_74775_l("ExtraAttributes").func_74775_l("runes");
                Set set = nBTTagCompound4.func_150296_c();
                for (String string5 : set) {
                    String string6 = "RUNE_" + string5.toUpperCase() + "_" + nBTTagCompound4.func_74762_e(string5);
                    AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(string6);
                    if (auctionData == null || auctionData.getSellPrice() != -1L) continue;
                    itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Lowest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().first()) : "\u00a78N/A"));
                    if (!((Boolean)this.hbin.getValue()).booleanValue()) continue;
                    itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Highest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().last()) : "\u00a78N/A"));
                }
            } else if (string.equals("POTION")) {
                NBTTagCompound nBTTagCompound5 = nBTTagCompound.func_74775_l("ExtraAttributes");
                if (nBTTagCompound5.func_74764_b("potion") && nBTTagCompound5.func_74764_b("potion_level")) {
                    String string7 = nBTTagCompound5.func_74764_b("enhanced") ? "ENHANCED" : "NOTENHANCED";
                    String string8 = nBTTagCompound5.func_74764_b("extended") ? "EXTENDED" : "UNEXTENDED";
                    String string9 = nBTTagCompound5.func_74764_b("splash") ? "SPLASH" : "DRINKABLE";
                    String string10 = "POTION_" + nBTTagCompound5.func_74779_i("potion").toUpperCase() + "_" + nBTTagCompound5.func_74762_e("potion_level") + "_" + string7 + "_" + string8 + "_" + string9;
                    AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(string10);
                    if (auctionData != null && auctionData.getSellPrice() == -1L) {
                        itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Lowest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().first()) : "\u00a78N/A"));
                        if (((Boolean)this.hbin.getValue()).booleanValue()) {
                            itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Highest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().last()) : "\u00a78N/A"));
                        }
                    }
                }
            } else {
                AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(string);
                itemTooltipEvent.toolTip.add("\u00a7f");
                if (auctionData == null) {
                    itemTooltipEvent.toolTip.add("\u00a78Failed to Fetch Action/Bazaar Data.");
                } else if (auctionData.getSellPrice() == -1L) {
                    itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Lowest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().first()) : "\u00a78N/A"));
                    if (((Boolean)this.hbin.getValue()).booleanValue()) {
                        itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Highest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().last()) : "\u00a78N/A"));
                    }
                } else if (auctionData.getPrices().size() == 0) {
                    itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Bazaar Sell\u00a77: " + (auctionData.getSellPrice() == -1L ? "\u00a78N/A" : "\u00a7e" + this.format(auctionData.getSellPrice())));
                    itemTooltipEvent.toolTip.add("\u00a7" + colorPrefix + "Bazaar Buy\u00a77: " + (auctionData.getBuyPrice() == -1L ? "\u00a78N/A" : "\u00a7e" + this.format(auctionData.getBuyPrice())));
                } else {
                    itemTooltipEvent.toolTip.add("\u00a78Failed to Fetch Action/Bazaar Data.");
                }
            }
        }
    }

    public String format(long l2) {
        if (!((Boolean)this.fmt.getValue()).booleanValue()) {
            String string = l2 + "";
            StringBuffer stringBuffer = new StringBuffer(string);
            for (int i = string.length() - 3; i >= 0; i -= 3) {
                stringBuffer.insert(i, ",");
            }
            String string2 = stringBuffer.toString();
            if (string2.startsWith(",")) {
                string2 = string2.replaceFirst(",", "");
            }
            return string2;
        }
        if (l2 == Long.MIN_VALUE) {
            return this.format(-9223372036854775807L);
        }
        if (l2 < 0L) {
            return "-" + this.format(-l2);
        }
        if (l2 < 1000L) {
            return Long.toString(l2);
        }
        Map.Entry<Long, String> entry = suffixes.floorEntry(l2);
        Long l3 = entry.getKey();
        String string = entry.getValue();
        long l4 = l2 / (l3 / 10L);
        boolean bl = l4 < 100L && (double)l4 / 10.0 != (double)(l4 / 10L);
        return bl ? (double)l4 / 10.0 + string : l4 / 10L + string;
    }

    static {
        suffixes.put(1000L, "k");
        suffixes.put(1000000L, "m");
        suffixes.put(1000000000L, "b");
    }
}

