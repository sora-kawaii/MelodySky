package xyz.Melody.System.Managers.Auctions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import xyz.Melody.Client;
import xyz.Melody.Utils.Base64;
import xyz.Melody.module.modules.others.FetchLBinData;

public class AhBzManager {
   public static volatile Map auctions = new HashMap();
   private static Map semi_auctions = new HashMap();
   private static Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
   public static Timer timer = new Timer();
   public static int totalAuctions = 0;

   public static void registerTimer() {
      FetchLBinData flb = (FetchLBinData)Client.instance.getModuleManager().getModuleByClass(FetchLBinData.class);
      int time = ((Double)flb.delay.getValue()).intValue() * 1000 * 60;
      if (time < 30000) {
         time = 30000;
      }

      timer.schedule(new TimerTask() {
         public void run() {
            AhBzManager.loadAuctions();
         }
      }, 1L, (long)time);
   }

   public static void loadAuctions() {
      try {
         int i = 0;
         loadBazaar();

         while(loadPage(i++)) {
         }

         auctions = semi_auctions;
         semi_auctions = new HashMap();
      } catch (IOException var1) {
         var1.printStackTrace();
      }

   }

   public static void loadBazaar() throws IOException {
      Client.instance.logger.info("[Melody] [Auction/Bazaar Manager] Fetching Bazaar Data.");
      URL url = new URL("https://api.hypixel.net/skyblock/bazaar");
      InputStreamReader reader = new InputStreamReader(url.openStream());
      JsonObject object = (JsonObject)(new JsonParser()).parse(reader);
      boolean success = object.get("success").getAsBoolean();
      if (success) {
         JsonObject element = object.getAsJsonObject("products");
         Iterator var5 = element.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry product = (Map.Entry)var5.next();
            String id = (String)product.getKey();
            AuctionData auctionData = (AuctionData)semi_auctions.get(id);
            boolean notexisted = auctionData == null;
            if (notexisted) {
               auctionData = new AuctionData(id);
            }

            auctionData.sellPrice = (long)((JsonElement)product.getValue()).getAsJsonObject().getAsJsonObject("quick_status").get("sellPrice").getAsInt();
            auctionData.buyPrice = (long)((JsonElement)product.getValue()).getAsJsonObject().getAsJsonObject("quick_status").get("buyPrice").getAsInt();
            if (notexisted) {
               semi_auctions.put(id, auctionData);
            }
         }

      }
   }

   public static boolean loadPage(int page) throws IOException {
      Client.instance.logger.info("[Melody] [Auction/Bazaar Manager] Fetching Page " + page + " of Auctions.");
      URL url = new URL("https://api.hypixel.net/skyblock/auctions?page=" + page);
      InputStreamReader reader = new InputStreamReader(url.openStream());
      JsonObject object = (JsonObject)(new JsonParser()).parse(reader);
      boolean success = object.get("success").getAsBoolean();
      if (!success) {
         return false;
      } else {
         int maxPage = object.get("totalPages").getAsInt() - 1;
         int totalAuctions = object.get("totalAuctions").getAsInt();
         Client.instance.logger.info("[Melody] [Auction/Bazaar Manager] Fetched page " + page + "/" + maxPage + " of auctions! (" + totalAuctions + " total)");
         JsonArray array = object.get("auctions").getAsJsonArray();
         Iterator var8 = array.iterator();

         while(true) {
            JsonObject element;
            String id;
            while(true) {
               NBTTagCompound attributes;
               NBTTagCompound runes;
               Set keys;
               String name;
               int tier;
               while(true) {
                  JsonElement isBin;
                  do {
                     do {
                        if (!var8.hasNext()) {
                           return page < maxPage;
                        }

                        JsonElement element2 = (JsonElement)var8.next();
                        element = element2.getAsJsonObject();
                        isBin = element.get("bin");
                     } while(isBin == null);
                  } while(!isBin.getAsBoolean());

                  byte[] itemData = Base64.decode(element.get("item_bytes").getAsString().replace("\\u003d", "="));
                  NBTTagCompound nbtTagCompound = CompressedStreamTools.readCompressed(new ByteArrayInputStream(itemData));
                  NBTTagCompound acutalItem = (NBTTagCompound)nbtTagCompound.getTagList("i", 10).get(0);
                  attributes = acutalItem.getCompoundTag("tag").getCompoundTag("ExtraAttributes");
                  id = attributes.getString("id");
                  if (!id.equals("ENCHANTED_BOOK")) {
                     break;
                  }

                  runes = attributes.getCompoundTag("enchantments");
                  keys = runes.getKeySet();
                  if (keys.size() == 1) {
                     name = (String)keys.iterator().next();
                     tier = runes.getInteger(name);
                     id = "ENCHANTMENT_" + name + "_" + tier;
                     break;
                  }
               }

               if (id.equals("PET")) {
                  JsonObject petInfo = (JsonObject)gson.fromJson(attributes.getString("petInfo"), JsonObject.class);
                  if (petInfo.has("type") && petInfo.has("tier")) {
                     id = "PET_" + petInfo.get("type").getAsString() + "_" + petInfo.get("tier").getAsString();
                  }
               }

               if (id.equals("POTION") && attributes.hasKey("potion") && attributes.hasKey("potion_level")) {
                  String enhanced = attributes.hasKey("enhanced") ? "ENHANCED" : "NOTENHANCED";
                  String extended = attributes.hasKey("extended") ? "EXTENDED" : "UNEXTENDED";
                  name = attributes.hasKey("splash") ? "SPLASH" : "DRINKABLE";
                  id = "POTION_" + attributes.getString("potion").toUpperCase() + "_" + attributes.getInteger("potion_level") + "_" + enhanced + "_" + extended + "_" + name;
               }

               if (!id.equals("RUNE")) {
                  break;
               }

               runes = attributes.getCompoundTag("runes");
               keys = runes.getKeySet();
               if (keys.size() == 1) {
                  name = (String)keys.iterator().next();
                  tier = runes.getInteger(name);
                  id = "RUNE_" + name + "_" + tier;
                  break;
               }
            }

            AuctionData auctionData = (AuctionData)semi_auctions.get(id);
            boolean notexisted = auctionData == null;
            if (notexisted) {
               auctionData = new AuctionData(id);
            }

            auctionData.prices.add(element.get("starting_bid").getAsLong());
            if (notexisted) {
               semi_auctions.put(id, auctionData);
            }
         }
      }
   }

   public static class AuctionData {
      private String id;
      private SortedSet prices;
      private long sellPrice = -1L;
      private long buyPrice = -1L;

      public AuctionData(String id) {
         this.id = id;
         this.prices = new TreeSet();
      }

      public long getBuyPrice() {
         return this.buyPrice;
      }

      public long getSellPrice() {
         return this.sellPrice;
      }

      public String getId() {
         return this.id;
      }

      public SortedSet getPrices() {
         return this.prices;
      }
   }
}
