/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Managers.Auctions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import xyz.Melody.Client;
import xyz.Melody.Utils.Base64;
import xyz.Melody.module.modules.others.FetchLBinData;

public final class AhBzManager {
    private static Map<String, AuctionData> semi_auctions;
    public static Thread auctionTimerThread;
    public static volatile Map<String, AuctionData> auctions;
    public static int totalAuctions;
    private static Gson gson;

    public static void registerTimer() {
        auctionTimerThread = new Thread(() -> {
            FetchLBinData fetchLBinData = (FetchLBinData)Client.instance.getModuleManager().getModuleByClass(FetchLBinData.class);
            int n = ((Double)fetchLBinData.delay.getValue()).intValue() * 1000 * 60;
            AhBzManager.loadAuctions();
            while (true) {
                try {
                    while (!fetchLBinData.isEnabled()) {
                        Thread.sleep(10000L);
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    continue;
                }
                Thread.sleep(n);
                AhBzManager.loadAuctions();
                continue;
                break;
            }
        });
        auctionTimerThread.setName("Melody -> Auction Thread");
        auctionTimerThread.start();
    }

    static {
        auctions = new HashMap<String, AuctionData>();
        semi_auctions = new HashMap<String, AuctionData>();
        gson = new GsonBuilder().setPrettyPrinting().create();
        totalAuctions = 0;
    }

    public static boolean loadPage(int n) throws IOException {
        URL uRL = new URL("https://api.hypixel.net/skyblock/auctions?page=" + n);
        InputStreamReader inputStreamReader = new InputStreamReader(uRL.openStream());
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(inputStreamReader);
        boolean bl = jsonObject.get("success").getAsBoolean();
        if (!bl) {
            return false;
        }
        int n2 = jsonObject.get("totalPages").getAsInt() - 1;
        JsonArray jsonArray = jsonObject.get("auctions").getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            boolean bl2;
            int n3;
            String string;
            Object object;
            Object object2;
            JsonObject jsonObject2 = jsonElement.getAsJsonObject();
            JsonElement jsonElement2 = jsonObject2.get("bin");
            if (jsonElement2 == null || !jsonElement2.getAsBoolean()) continue;
            byte[] byArray = Base64.decode(jsonObject2.get("item_bytes").getAsString().replace("\\u003d", "="));
            NBTTagCompound nBTTagCompound = CompressedStreamTools.func_74796_a((InputStream)new ByteArrayInputStream(byArray));
            NBTTagCompound nBTTagCompound2 = (NBTTagCompound)nBTTagCompound.func_150295_c("i", 10).func_179238_g(0);
            NBTTagCompound nBTTagCompound3 = nBTTagCompound2.func_74775_l("tag").func_74775_l("ExtraAttributes");
            String string2 = nBTTagCompound3.func_74779_i("id");
            if (string2.equals("ENCHANTED_BOOK")) {
                object2 = nBTTagCompound3.func_74775_l("enchantments");
                object = object2.func_150296_c();
                if (object.size() != 1) continue;
                string = (String)object.iterator().next();
                n3 = object2.func_74762_e(string);
                string2 = "ENCHANTMENT_" + string + "_" + n3;
            }
            if (string2.equals("PET") && ((JsonObject)(object2 = gson.fromJson(nBTTagCompound3.func_74779_i("petInfo"), JsonObject.class))).has("type") && ((JsonObject)object2).has("tier")) {
                string2 = "PET_" + ((JsonObject)object2).get("type").getAsString() + "_" + ((JsonObject)object2).get("tier").getAsString();
            }
            if (string2.equals("POTION") && nBTTagCompound3.func_74764_b("potion") && nBTTagCompound3.func_74764_b("potion_level")) {
                object2 = nBTTagCompound3.func_74764_b("enhanced") ? "ENHANCED" : "NOTENHANCED";
                object = nBTTagCompound3.func_74764_b("extended") ? "EXTENDED" : "UNEXTENDED";
                string = nBTTagCompound3.func_74764_b("splash") ? "SPLASH" : "DRINKABLE";
                string2 = "POTION_" + nBTTagCompound3.func_74779_i("potion").toUpperCase() + "_" + nBTTagCompound3.func_74762_e("potion_level") + "_" + (String)object2 + "_" + (String)object + "_" + string;
            }
            if (string2.equals("RUNE")) {
                object2 = nBTTagCompound3.func_74775_l("runes");
                object = object2.func_150296_c();
                if (object.size() != 1) continue;
                string = (String)object.iterator().next();
                n3 = object2.func_74762_e(string);
                string2 = "RUNE_" + string + "_" + n3;
            }
            boolean bl3 = bl2 = (object2 = semi_auctions.get(string2)) == null;
            if (bl2) {
                object2 = new AuctionData(string2);
            }
            ((AuctionData)object2).prices.add(jsonObject2.get("starting_bid").getAsLong());
            if (!bl2) continue;
            semi_auctions.put(string2, (AuctionData)object2);
        }
        return n < n2;
    }

    public static void loadAuctions() {
        Client.instance.logger.info("[Melody] [Auction/Bazaar Manager] Loading Auctions.");
        int n = 0;
        AhBzManager.loadBazaar();
        while (AhBzManager.loadPage(n++)) {
        }
        try {
            auctions = semi_auctions;
            semi_auctions = new HashMap<String, AuctionData>();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void loadBazaar() throws IOException {
        Client.instance.logger.info("[Melody] [Auction/Bazaar Manager] Fetching Bazaar Data.");
        URL uRL = new URL("https://api.hypixel.net/skyblock/bazaar");
        InputStreamReader inputStreamReader = new InputStreamReader(uRL.openStream());
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(inputStreamReader);
        boolean bl = jsonObject.get("success").getAsBoolean();
        if (!bl) {
            return;
        }
        JsonObject jsonObject2 = jsonObject.getAsJsonObject("products");
        for (Map.Entry<String, JsonElement> entry : jsonObject2.entrySet()) {
            boolean bl2;
            String string = entry.getKey();
            AuctionData auctionData = semi_auctions.get(string);
            boolean bl3 = bl2 = auctionData == null;
            if (bl2) {
                auctionData = new AuctionData(string);
            }
            auctionData.sellPrice = entry.getValue().getAsJsonObject().getAsJsonObject("quick_status").get("sellPrice").getAsInt();
            auctionData.buyPrice = entry.getValue().getAsJsonObject().getAsJsonObject("quick_status").get("buyPrice").getAsInt();
            if (!bl2) continue;
            semi_auctions.put(string, auctionData);
        }
    }

    public static class AuctionData {
        private long sellPrice;
        private long buyPrice;
        private String id;
        private SortedSet<Long> prices;

        public String getId() {
            return this.id;
        }

        public SortedSet<Long> getPrices() {
            return this.prices;
        }

        public long getSellPrice() {
            return this.sellPrice;
        }

        public AuctionData(String string) {
            this.sellPrice = -1L;
            this.buyPrice = -1L;
            this.id = string;
            this.prices = new TreeSet<Long>();
        }

        public long getBuyPrice() {
            return this.buyPrice;
        }
    }
}

