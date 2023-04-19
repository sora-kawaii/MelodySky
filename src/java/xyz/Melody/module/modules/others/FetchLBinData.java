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
    public void onItemTooltip(ItemTooltipEvent e) {
        ItemStack hoveredItem = e.itemStack;
        if (Client.inSkyblock || !((Boolean)this.sbonly.getValue()).booleanValue()) {
            NBTTagCompound compound = hoveredItem.getTagCompound();
            String id = ItemUtils.getSkyBlockID(hoveredItem);
            if (id.equals("NotSBItem")) {
                return;
            }
            if (id.equals("ENCHANTED_BOOK")) {
                NBTTagCompound enchants = compound.getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
                Set<String> keys = enchants.getKeySet();
                int iterations = 0;
                for (String key : keys) {
                    String id2 = "ENCHANTMENT_" + key.toUpperCase() + "_" + enchants.getInteger(key);
                    AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(id2);
                    if (auctionData == null || ++iterations >= 10) continue;
                    if (auctionData.getPrices().size() == 0) {
                        e.toolTip.add("\u00a7" + colorPrefix + "Bazaar Sell\u00a77: " + (auctionData.getSellPrice() == -1L ? "\u00a78N/A" : "\u00a7e" + this.format(auctionData.getSellPrice())));
                        e.toolTip.add("\u00a7" + colorPrefix + "Bazaar Buy\u00a77: " + (auctionData.getBuyPrice() == -1L ? "\u00a78N/A" : "\u00a7e" + this.format(auctionData.getBuyPrice())));
                        continue;
                    }
                    if (auctionData.getSellPrice() == -1L) {
                        e.toolTip.add("\u00a7" + colorPrefix + "Lowest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().first()) : "\u00a78N/A"));
                        if (!((Boolean)this.hbin.getValue()).booleanValue()) continue;
                        e.toolTip.add("\u00a7" + colorPrefix + "Highest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().last()) : "\u00a78N/A"));
                        continue;
                    }
                    e.toolTip.add("\u00a78Failed to Fetch Action/Bazaar Data.");
                }
                if (iterations >= 10) {
                    e.toolTip.add("\u00a77" + (iterations - 10) + " more enchants... ");
                }
            } else if (id.equals("PET")) {
                if (ItemUtils.getPetInfo(hoveredItem) != null) {
                    NBTTagCompound info = compound.getCompoundTag("ExtraAttributes");
                    JsonObject petInfo = this.gson.fromJson(info.getString("petInfo"), JsonObject.class);
                    String petID = "idk";
                    if (petInfo != null) {
                        AhBzManager.AuctionData auctionData;
                        if (petInfo.has("type") && petInfo.has("tier")) {
                            petID = "PET_" + petInfo.get("type").getAsString() + "_" + petInfo.get("tier").getAsString();
                        }
                        if (petID != "idk" && (auctionData = AhBzManager.auctions.get(petID)) != null && auctionData.getSellPrice() == -1L) {
                            e.toolTip.add("\u00a7" + colorPrefix + "Lowest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().first()) : "\u00a78N/A"));
                            if (((Boolean)this.hbin.getValue()).booleanValue()) {
                                e.toolTip.add("\u00a7" + colorPrefix + "Highest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().last()) : "\u00a78N/A"));
                            }
                        }
                    }
                }
            } else if (id.equals("RUNE")) {
                NBTTagCompound runes = compound.getCompoundTag("ExtraAttributes").getCompoundTag("runes");
                Set<String> keys = runes.getKeySet();
                for (String key : keys) {
                    String id2 = "RUNE_" + key.toUpperCase() + "_" + runes.getInteger(key);
                    AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(id2);
                    if (auctionData == null || auctionData.getSellPrice() != -1L) continue;
                    e.toolTip.add("\u00a7" + colorPrefix + "Lowest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().first()) : "\u00a78N/A"));
                    if (!((Boolean)this.hbin.getValue()).booleanValue()) continue;
                    e.toolTip.add("\u00a7" + colorPrefix + "Highest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().last()) : "\u00a78N/A"));
                }
            } else if (id.equals("POTION")) {
                NBTTagCompound attributes = compound.getCompoundTag("ExtraAttributes");
                if (attributes.hasKey("potion") && attributes.hasKey("potion_level")) {
                    String enhanced = attributes.hasKey("enhanced") ? "ENHANCED" : "NOTENHANCED";
                    String extended = attributes.hasKey("extended") ? "EXTENDED" : "UNEXTENDED";
                    String splash = attributes.hasKey("splash") ? "SPLASH" : "DRINKABLE";
                    String pot_id = "POTION_" + attributes.getString("potion").toUpperCase() + "_" + attributes.getInteger("potion_level") + "_" + enhanced + "_" + extended + "_" + splash;
                    AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(pot_id);
                    if (auctionData != null && auctionData.getSellPrice() == -1L) {
                        e.toolTip.add("\u00a7" + colorPrefix + "Lowest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().first()) : "\u00a78N/A"));
                        if (((Boolean)this.hbin.getValue()).booleanValue()) {
                            e.toolTip.add("\u00a7" + colorPrefix + "Highest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().last()) : "\u00a78N/A"));
                        }
                    }
                }
            } else {
                AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(id);
                e.toolTip.add("\u00a7f");
                if (auctionData == null) {
                    e.toolTip.add("\u00a78Failed to Fetch Action/Bazaar Data.");
                } else if (auctionData.getSellPrice() == -1L) {
                    e.toolTip.add("\u00a7" + colorPrefix + "Lowest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().first()) : "\u00a78N/A"));
                    if (((Boolean)this.hbin.getValue()).booleanValue()) {
                        e.toolTip.add("\u00a7" + colorPrefix + "Highest Bin\u00a77: " + (auctionData.getPrices().size() != 0 ? "\u00a7e" + this.format(auctionData.getPrices().last()) : "\u00a78N/A"));
                    }
                } else if (auctionData.getPrices().size() == 0) {
                    e.toolTip.add("\u00a7" + colorPrefix + "Bazaar Sell\u00a77: " + (auctionData.getSellPrice() == -1L ? "\u00a78N/A" : "\u00a7e" + this.format(auctionData.getSellPrice())));
                    e.toolTip.add("\u00a7" + colorPrefix + "Bazaar Buy\u00a77: " + (auctionData.getBuyPrice() == -1L ? "\u00a78N/A" : "\u00a7e" + this.format(auctionData.getBuyPrice())));
                } else {
                    e.toolTip.add("\u00a78Failed to Fetch Action/Bazaar Data.");
                }
            }
        }
    }

    public String format(long value) {
        if (!((Boolean)this.fmt.getValue()).booleanValue()) {
            String number = value + "";
            StringBuffer numStr = new StringBuffer(number);
            for (int i = number.length() - 3; i >= 0; i -= 3) {
                numStr.insert(i, ",");
            }
            String finalStr = numStr.toString();
            if (finalStr.startsWith(",")) {
                finalStr = finalStr.replaceFirst(",", "");
            }
            return finalStr;
        }
        if (value == Long.MIN_VALUE) {
            return this.format(-9223372036854775807L);
        }
        if (value < 0L) {
            return "-" + this.format(-value);
        }
        if (value < 1000L) {
            return Long.toString(value);
        }
        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();
        long truncated = value / (divideBy / 10L);
        boolean hasDecimal = truncated < 100L && (double)truncated / 10.0 != (double)(truncated / 10L);
        return hasDecimal ? (double)truncated / 10.0 + suffix : truncated / 10L + suffix;
    }

    static {
        suffixes.put(1000L, "k");
        suffixes.put(1000000L, "m");
        suffixes.put(1000000000L, "b");
    }
}

