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
    public void onGuiBGRender(GuiScreenEvent.BackgroundDrawnEvent backgroundDrawnEvent) {
        char c;
        int n;
        Object object;
        if (!(backgroundDrawnEvent.gui instanceof GuiChest)) {
            return;
        }
        GlStateManager.func_179140_f();
        if (!Client.inDungeons) {
            return;
        }
        ContainerChest containerChest = (ContainerChest)((GuiChest)backgroundDrawnEvent.gui).field_147002_h;
        String string = containerChest.func_85151_d().func_70005_c_();
        String string2 = "Wood Chest";
        String string3 = "Gold Chest";
        String string4 = "Diamond Chest";
        String string5 = "Emerald Chest";
        String string6 = "Obsidian Chest";
        String string7 = "Bedrock Chest";
        if (!(string.equals(string2) || string.equals(string3) || string.equals(string4) || string.equals(string5) || string.equals(string6) || string.equals(string7))) {
            return;
        }
        IInventory iInventory = containerChest.func_85151_d();
        int n2 = 0;
        int n3 = 0;
        for (int i = 0; i < iInventory.func_70302_i_(); ++i) {
            object = iInventory.func_70301_a(i);
            if (object == null) continue;
            n3 = (int)((long)n3 + this.getPrice((ItemStack)object) * (long)((ItemStack)object).field_77994_a);
        }
        ItemStack itemStack = iInventory.func_70301_a(31);
        if (itemStack != null && itemStack.func_82833_r().endsWith((Object)((Object)EnumChatFormatting.GREEN) + "Open Reward Chest")) {
            try {
                object = this.cleanColor(ItemUtils.getLoreFromNBT(itemStack.func_77978_p())[6]);
                StringBuilder stringBuilder = new StringBuilder();
                for (n = 0; n < ((String)object).length(); ++n) {
                    c = ((String)object).charAt(n);
                    if ("0123456789".indexOf(c) < 0) continue;
                    stringBuilder.append(c);
                }
                if (stringBuilder.length() > 0) {
                    n2 = Integer.parseInt(stringBuilder.toString());
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        int n4 = 222;
        int n5 = n4 - 108;
        n = n5 + iInventory.func_70302_i_() / 9 * 18;
        c = (backgroundDrawnEvent.gui.field_146294_l - 10) / 2;
        int n6 = (backgroundDrawnEvent.gui.field_146295_m - n - 18) / 2;
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)c, (float)n6, (float)0.0f);
        FontRenderer fontRenderer = this.mc.field_71466_p;
        String string8 = (n3 > n2 ? "+" : "") + this.format(n3 - n2);
        fontRenderer.func_78276_b("Profit: " + (Object)((Object)(n3 > n2 ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)) + string8, 5, 15, -1);
        if (((Boolean)this.showCost.getValue()).booleanValue()) {
            fontRenderer.func_78276_b("Cost: " + (Object)((Object)EnumChatFormatting.RED) + n2, 5, -2, -1);
        }
        GlStateManager.func_179121_F();
    }

    public long getPrice(ItemStack itemStack) {
        if (itemStack == null) {
            return 0L;
        }
        NBTTagCompound nBTTagCompound = itemStack.func_77978_p();
        if (nBTTagCompound == null) {
            return 0L;
        }
        if (!nBTTagCompound.func_74764_b("ExtraAttributes")) {
            return 0L;
        }
        String string = nBTTagCompound.func_74775_l("ExtraAttributes").func_74779_i("id");
        if (string.equals("ENCHANTED_BOOK")) {
            NBTTagCompound nBTTagCompound2 = nBTTagCompound.func_74775_l("ExtraAttributes").func_74775_l("enchantments");
            Set set = nBTTagCompound2.func_150296_c();
            Iterator iterator = set.iterator();
            if (iterator.hasNext()) {
                String string2 = (String)iterator.next();
                String string3 = "ENCHANTMENT_" + string2.toUpperCase() + "_" + nBTTagCompound2.func_74762_e(string2);
                AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(string3);
                if (auctionData == null) {
                    return 0L;
                }
                return auctionData.getSellPrice();
            }
        } else {
            AhBzManager.AuctionData auctionData = AhBzManager.auctions.get(string);
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

    public String stripColor(String string) {
        return this.STRIP_COLOR_PATTERN.matcher(string).replaceAll("");
    }

    public String cleanColor(String string) {
        return string.replaceAll("(?i)\\u00A7.", "");
    }

    public String format(long l2) {
        if (!((Boolean)this.format.getValue()).booleanValue()) {
            String string = l2 + "";
            StringBuffer stringBuffer = new StringBuffer(string);
            for (int i = string.length() - 3; i >= 0; i -= 3) {
                stringBuffer.insert(i, ",");
            }
            String string2 = stringBuffer.toString();
            if (string2.startsWith(",")) {
                string2 = string2.replaceFirst(",", "");
            }
            if (string2.startsWith("-,")) {
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

