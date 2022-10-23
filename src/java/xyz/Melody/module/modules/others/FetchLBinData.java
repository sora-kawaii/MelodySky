package xyz.Melody.module.modules.others;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.Iterator;
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
import xyz.Melody.Event.value.Value;
import xyz.Melody.System.Managers.Auctions.AhBzManager;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class FetchLBinData extends Module {
   private Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
   public Option sbonly = new Option("Skyblock Only", true);
   public Option hbin = new Option("HBin", false);
   public Option fmt = new Option("Format", false);
   public Numbers delay = new Numbers("Delay(Min)", 1.0, 0.5, 10.0, 0.5);
   public static String colorPrefix = "6";
   private TimerUtil timer = new TimerUtil();
   private static final NavigableMap suffixes = new TreeMap();

   public FetchLBinData() {
      super("FetchLBinData", new String[]{"lbin"}, ModuleType.Others);
      this.addValues(new Value[]{this.delay, this.sbonly, this.fmt, this.hbin});
      this.setModInfo("Show Auction or Bazaar Data as ToolTip.");
   }

   @SubscribeEvent
   public void onItemTooltip(ItemTooltipEvent e) {
      ItemStack hoveredItem = e.itemStack;
      if (Client.inSkyblock || !(Boolean)this.sbonly.getValue()) {
         NBTTagCompound compound = hoveredItem.getTagCompound();
         String id = ItemUtils.getSkyBlockID(hoveredItem);
         if (id.equals("NotSBItem")) {
            return;
         }

         NBTTagCompound attributes;
         Set keys;
         String pot_id;
         if (id.equals("ENCHANTED_BOOK")) {
            attributes = compound.getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
            keys = attributes.getKeySet();
            int iterations = 0;
            Iterator var8 = keys.iterator();

            while(var8.hasNext()) {
               pot_id = (String)var8.next();
               ++iterations;
               String id2 = "ENCHANTMENT_" + pot_id.toUpperCase() + "_" + attributes.getInteger(pot_id);
               AhBzManager.AuctionData auctionData = (AhBzManager.AuctionData)AhBzManager.auctions.get(id2);
               if (auctionData != null && iterations < 10) {
                  if (auctionData.getPrices().size() == 0) {
                     e.toolTip.add("§" + colorPrefix + "Bazaar Sell§7: " + (auctionData.getSellPrice() == -1L ? "§8N/A" : "§e" + this.format(auctionData.getSellPrice())));
                     e.toolTip.add("§" + colorPrefix + "Bazaar Buy§7: " + (auctionData.getBuyPrice() == -1L ? "§8N/A" : "§e" + this.format(auctionData.getBuyPrice())));
                  } else if (auctionData.getSellPrice() == -1L) {
                     e.toolTip.add("§" + colorPrefix + "Lowest Bin§7: " + (auctionData.getPrices().size() != 0 ? "§e" + this.format((Long)auctionData.getPrices().first()) : "§8N/A"));
                     if ((Boolean)this.hbin.getValue()) {
                        e.toolTip.add("§" + colorPrefix + "Highest Bin§7: " + (auctionData.getPrices().size() != 0 ? "§e" + this.format((Long)auctionData.getPrices().last()) : "§8N/A"));
                     }
                  } else {
                     e.toolTip.add("§8Failed to Fetch Action/Bazaar Data.");
                  }
               }
            }

            if (iterations >= 10) {
               e.toolTip.add("§7" + (iterations - 10) + " more enchants... ");
            }
         } else {
            String petID;
            if (id.equals("PET")) {
               if (ItemUtils.getPetInfo(hoveredItem) != null) {
                  attributes = compound.getCompoundTag("ExtraAttributes");
                  JsonObject petInfo = (JsonObject)this.gson.fromJson(attributes.getString("petInfo"), JsonObject.class);
                  petID = "idk";
                  if (petInfo.has("type") && petInfo.has("tier")) {
                     petID = "PET_" + petInfo.get("type").getAsString() + "_" + petInfo.get("tier").getAsString();
                  }

                  if (petID != "idk") {
                     AhBzManager.AuctionData auctionData = (AhBzManager.AuctionData)AhBzManager.auctions.get(petID);
                     if (auctionData != null && auctionData.getSellPrice() == -1L) {
                        e.toolTip.add("§" + colorPrefix + "Lowest Bin§7: " + (auctionData.getPrices().size() != 0 ? "§e" + this.format((Long)auctionData.getPrices().first()) : "§8N/A"));
                        if ((Boolean)this.hbin.getValue()) {
                           e.toolTip.add("§" + colorPrefix + "Highest Bin§7: " + (auctionData.getPrices().size() != 0 ? "§e" + this.format((Long)auctionData.getPrices().last()) : "§8N/A"));
                        }
                     }
                  }
               }
            } else {
               String splash;
               AhBzManager.AuctionData auctionData;
               if (id.equals("RUNE")) {
                  attributes = compound.getCompoundTag("ExtraAttributes").getCompoundTag("runes");
                  keys = attributes.getKeySet();
                  Iterator var17 = keys.iterator();

                  while(var17.hasNext()) {
                     splash = (String)var17.next();
                     pot_id = "RUNE_" + splash.toUpperCase() + "_" + attributes.getInteger(splash);
                     auctionData = (AhBzManager.AuctionData)AhBzManager.auctions.get(pot_id);
                     if (auctionData != null && auctionData.getSellPrice() == -1L) {
                        e.toolTip.add("§" + colorPrefix + "Lowest Bin§7: " + (auctionData.getPrices().size() != 0 ? "§e" + this.format((Long)auctionData.getPrices().first()) : "§8N/A"));
                        if ((Boolean)this.hbin.getValue()) {
                           e.toolTip.add("§" + colorPrefix + "Highest Bin§7: " + (auctionData.getPrices().size() != 0 ? "§e" + this.format((Long)auctionData.getPrices().last()) : "§8N/A"));
                        }
                     }
                  }
               } else if (id.equals("POTION")) {
                  attributes = compound.getCompoundTag("ExtraAttributes");
                  if (attributes.hasKey("potion") && attributes.hasKey("potion_level")) {
                     String enhanced = attributes.hasKey("enhanced") ? "ENHANCED" : "NOTENHANCED";
                     petID = attributes.hasKey("extended") ? "EXTENDED" : "UNEXTENDED";
                     splash = attributes.hasKey("splash") ? "SPLASH" : "DRINKABLE";
                     pot_id = "POTION_" + attributes.getString("potion").toUpperCase() + "_" + attributes.getInteger("potion_level") + "_" + enhanced + "_" + petID + "_" + splash;
                     auctionData = (AhBzManager.AuctionData)AhBzManager.auctions.get(pot_id);
                     if (auctionData != null && auctionData.getSellPrice() == -1L) {
                        e.toolTip.add("§" + colorPrefix + "Lowest Bin§7: " + (auctionData.getPrices().size() != 0 ? "§e" + this.format((Long)auctionData.getPrices().first()) : "§8N/A"));
                        if ((Boolean)this.hbin.getValue()) {
                           e.toolTip.add("§" + colorPrefix + "Highest Bin§7: " + (auctionData.getPrices().size() != 0 ? "§e" + this.format((Long)auctionData.getPrices().last()) : "§8N/A"));
                        }
                     }
                  }
               } else {
                  AhBzManager.AuctionData auctionData = (AhBzManager.AuctionData)AhBzManager.auctions.get(id);
                  e.toolTip.add("§f");
                  if (auctionData == null) {
                     e.toolTip.add("§8Failed to Fetch Action/Bazaar Data.");
                  } else if (auctionData.getSellPrice() == -1L) {
                     e.toolTip.add("§" + colorPrefix + "Lowest Bin§7: " + (auctionData.getPrices().size() != 0 ? "§e" + this.format((Long)auctionData.getPrices().first()) : "§8N/A"));
                     if ((Boolean)this.hbin.getValue()) {
                        e.toolTip.add("§" + colorPrefix + "Highest Bin§7: " + (auctionData.getPrices().size() != 0 ? "§e" + this.format((Long)auctionData.getPrices().last()) : "§8N/A"));
                     }
                  } else if (auctionData.getPrices().size() == 0) {
                     e.toolTip.add("§" + colorPrefix + "Bazaar Sell§7: " + (auctionData.getSellPrice() == -1L ? "§8N/A" : "§e" + this.format(auctionData.getSellPrice())));
                     e.toolTip.add("§" + colorPrefix + "Bazaar Buy§7: " + (auctionData.getBuyPrice() == -1L ? "§8N/A" : "§e" + this.format(auctionData.getBuyPrice())));
                  } else {
                     e.toolTip.add("§8Failed to Fetch Action/Bazaar Data.");
                  }
               }
            }
         }
      }

   }

   public String format(long value) {
      String suffix;
      if (!(Boolean)this.fmt.getValue()) {
         String number = value + "";
         StringBuffer numStr = new StringBuffer(number);

         for(int i = number.length() - 3; i >= 0; i -= 3) {
            numStr.insert(i, ",");
         }

         suffix = numStr.toString();
         if (suffix.startsWith(",")) {
            suffix = suffix.replaceFirst(",", "");
         }

         return suffix;
      } else if (value == Long.MIN_VALUE) {
         return this.format(-9223372036854775807L);
      } else if (value < 0L) {
         return "-" + this.format(-value);
      } else if (value < 1000L) {
         return Long.toString(value);
      } else {
         Map.Entry e = suffixes.floorEntry(value);
         Long divideBy = (Long)e.getKey();
         suffix = (String)e.getValue();
         long truncated = value / (divideBy / 10L);
         boolean hasDecimal = truncated < 100L && (double)truncated / 10.0 != (double)(truncated / 10L);
         return hasDecimal ? (double)truncated / 10.0 + suffix : truncated / 10L + suffix;
      }
   }

   static {
      suffixes.put(1000L, "k");
      suffixes.put(1000000L, "m");
      suffixes.put(1000000000L, "b");
   }
}
