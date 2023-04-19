/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.value.Option;
import xyz.Melody.System.Managers.Auctions.AhBzManager;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class DungeonChestProfit
extends Module {
    private Option<Boolean> format = new Option<Boolean>("Format", false);
    private Option<Boolean> showCost = new Option<Boolean>("ShowCost", false);
    private final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\ufffd\ufffd[0-9A-FK-OR]");
    private static final NavigableMap<Long, String> suffixes = new TreeMap<Long, String>();

    public DungeonChestProfit() {
        super("ChestProfit", new String[]{"as"}, ModuleType.Dungeons);
        this.addValues(this.format, this.showCost);
        this.setModInfo("Dungeon Chest Profit.");
    }

    @SubscribeEvent
    public void onGuiBGRender(GuiScreenEvent.BackgroundDrawnEvent rendered) {
        if (!(rendered.gui instanceof GuiChest)) {
            return;
        }
        GlStateManager.disableLighting();
        if (!Client.inDungeons) {
            return;
        }
        ContainerChest chest = (ContainerChest)((GuiChest)rendered.gui).inventorySlots;
        String chestName = chest.getLowerChestInventory().getName();
        String wood = "Wood Chest";
        String gold = "Gold Chest";
        String diamond = "Diamond Chest";
        String emerald = "Emerald Chest";
        String obsidian = "Obsidian Chest";
        String bedrock = "Bedrock Chest";
        if (!(chestName.equals(wood) || chestName.equals(gold) || chestName.equals(diamond) || chestName.equals(emerald) || chestName.equals(obsidian) || chestName.equals(bedrock))) {
            return;
        }
        IInventory actualChest = chest.getLowerChestInventory();
        int chestCost = 0;
        int itemPrice = 0;
        for (int i = 0; i < actualChest.getSizeInventory(); ++i) {
            ItemStack item = actualChest.getStackInSlot(i);
            if (item == null) continue;
            itemPrice = (int)((long)itemPrice + this.getPrice(item) * (long)item.stackSize);
        }
        ItemStack rewardChest = actualChest.getStackInSlot(31);
        if (rewardChest != null && rewardChest.getDisplayName().endsWith((Object)((Object)EnumChatFormatting.GREEN) + "Open Reward Chest")) {
            try {
                String line6 = this.cleanColor(ItemUtils.getLoreFromNBT(rewardChest.getTagCompound())[6]);
                StringBuilder cost = new StringBuilder();
                for (int i = 0; i < line6.length(); ++i) {
                    char c = line6.charAt(i);
                    if ("0123456789".indexOf(c) < 0) continue;
                    cost.append(c);
                }
                if (cost.length() > 0) {
                    chestCost = Integer.parseInt(cost.toString());
                }
            }
            catch (Exception line6) {
                // empty catch block
            }
        }
        int i = 222;
        int j = i - 108;
        int ySize = j + actualChest.getSizeInventory() / 9 * 18;
        int left = (rendered.gui.width - 10) / 2;
        int top = (rendered.gui.height - ySize - 18) / 2;
        GlStateManager.pushMatrix();
        GlStateManager.translate(left, top, 0.0f);
        FontRenderer fr = this.mc.fontRendererObj;
        String str = (itemPrice > chestCost ? "+" : "") + this.format(itemPrice - chestCost);
        fr.drawString("Profit: " + (Object)((Object)(itemPrice > chestCost ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)) + str, 5, 15, -1);
        if (((Boolean)this.showCost.getValue()).booleanValue()) {
            fr.drawString("Cost: " + (Object)((Object)EnumChatFormatting.RED) + chestCost, 5, -2, -1);
        }
        GlStateManager.popMatrix();
    }

    public long getPrice(ItemStack itemStack) {
        if (itemStack == null) {
            return 0L;
        }
        NBTTagCompound compound = itemStack.getTagCompound();
        if (compound == null) {
            return 0L;
        }
        if (!compound.hasKey("ExtraAttributes")) {
            return 0L;
        }
        String id = compound.getCompoundTag("ExtraAttributes").getString("id");
        if (id.equals("ENCHANTED_BOOK")) {
            NBTTagCompound enchants = compound.getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
            Set<String> keys = enchants.getKeySet();
            Iterator<String> iterator = keys.iterator();
            if (iterator.hasNext()) {
                String key = iterator.next();
                String id2 = "ENCHANTMENT_" + key.toUpperCase() + "_" + enchants.getInteger(key);
                AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(id2);
                if (auctionData == null) {
                    return 0L;
                }
                return auctionData.getSellPrice();
            }
        } else {
            AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(id);
            if (auctionData == null) {
                return 0L;
            }
            if (auctionData.getSellPrice() == -1L) {
                return auctionData.getPrices().size() != 0 ? auctionData.getPrices().first() : 0L;
            }
            if (auctionData.getPrices().size() == 0) {
                return auctionData.getSellPrice();
            }
            return 0L;
        }
        return 0L;
    }

    public String stripColor(String input) {
        return this.STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public String cleanColor(String in) {
        return in.replaceAll("(?i)\\u00A7.", "");
    }

    public String format(long value) {
        if (!((Boolean)this.format.getValue()).booleanValue()) {
            String number = value + "";
            StringBuffer numStr = new StringBuffer(number);
            for (int i = number.length() - 3; i >= 0; i -= 3) {
                numStr.insert(i, ",");
            }
            String finalStr = numStr.toString();
            if (finalStr.startsWith(",")) {
                finalStr = finalStr.replaceFirst(",", "");
            }
            if (finalStr.startsWith("-,")) {
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

