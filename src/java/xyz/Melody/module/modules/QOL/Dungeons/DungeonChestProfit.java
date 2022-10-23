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
import xyz.Melody.Event.value.Value;
import xyz.Melody.System.Managers.Auctions.AhBzManager;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class DungeonChestProfit extends Module {
   private Option format = new Option("Format", false);
   private Option showCost = new Option("ShowCost", false);
   private final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)��[0-9A-FK-OR]");
   private static final NavigableMap suffixes = new TreeMap();

   public DungeonChestProfit() {
      super("ChestProfit", new String[]{"as"}, ModuleType.Dungeons);
      this.addValues(new Value[]{this.format, this.showCost});
      this.setModInfo("Dungeon Chest Profit.");
   }

   @SubscribeEvent
   public void onGuiBGRender(GuiScreenEvent.BackgroundDrawnEvent rendered) {
      if (rendered.gui instanceof GuiChest) {
         GlStateManager.disableLighting();
         if (Client.inDungeons) {
            ContainerChest chest = (ContainerChest)((GuiChest)rendered.gui).inventorySlots;
            String chestName = chest.getLowerChestInventory().getName();
            String wood = "Wood Chest";
            String gold = "Gold Chest";
            String diamond = "Diamond Chest";
            String emerald = "Emerald Chest";
            String obsidian = "Obsidian Chest";
            String bedrock = "Bedrock Chest";
            if (chestName.equals(wood) || chestName.equals(gold) || chestName.equals(diamond) || chestName.equals(emerald) || chestName.equals(obsidian) || chestName.equals(bedrock)) {
               IInventory actualChest = chest.getLowerChestInventory();
               int chestCost = 0;
               int itemPrice = 0;

               for(int i = 0; i < actualChest.getSizeInventory(); ++i) {
                  ItemStack item = actualChest.getStackInSlot(i);
                  if (item != null) {
                     itemPrice = (int)((long)itemPrice + this.getPrice(item) * (long)item.stackSize);
                  }
               }

               ItemStack rewardChest = actualChest.getStackInSlot(31);
               int i;
               int c;
               if (rewardChest != null && rewardChest.getDisplayName().endsWith(EnumChatFormatting.GREEN + "Open Reward Chest")) {
                  try {
                     String line6 = this.cleanColor(ItemUtils.getLoreFromNBT(rewardChest.getTagCompound())[6]);
                     StringBuilder cost = new StringBuilder();

                     for(i = 0; i < line6.length(); ++i) {
                        c = line6.charAt(i);
                        if ("0123456789".indexOf(c) >= 0) {
                           cost.append((char)c);
                        }
                     }

                     if (cost.length() > 0) {
                        chestCost = Integer.parseInt(cost.toString());
                     }
                  } catch (Exception var21) {
                  }
               }

               int i = 222;
               int j = i - 108;
               i = j + actualChest.getSizeInventory() / 9 * 18;
               c = (rendered.gui.width - 10) / 2;
               int top = (rendered.gui.height - i - 18) / 2;
               GlStateManager.pushMatrix();
               GlStateManager.translate((float)c, (float)top, 0.0F);
               FontRenderer fr = this.mc.fontRendererObj;
               String str = (itemPrice > chestCost ? "+" : "") + this.format((long)(itemPrice - chestCost));
               fr.drawString("Profit: " + (itemPrice > chestCost ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + str, 5, 15, -1);
               if ((Boolean)this.showCost.getValue()) {
                  fr.drawString("Cost: " + EnumChatFormatting.RED + chestCost, 5, -2, -1);
               }

               GlStateManager.popMatrix();
            }
         }
      }
   }

   public long getPrice(ItemStack itemStack) {
      if (itemStack == null) {
         return 0L;
      } else {
         NBTTagCompound compound = itemStack.getTagCompound();
         if (compound == null) {
            return 0L;
         } else if (!compound.hasKey("ExtraAttributes")) {
            return 0L;
         } else {
            String id = compound.getCompoundTag("ExtraAttributes").getString("id");
            if (id.equals("ENCHANTED_BOOK")) {
               NBTTagCompound enchants = compound.getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
               Set keys = enchants.getKeySet();
               Iterator var6 = keys.iterator();
               if (var6.hasNext()) {
                  String key = (String)var6.next();
                  String id2 = "ENCHANTMENT_" + key.toUpperCase() + "_" + enchants.getInteger(key);
                  AhBzManager.AuctionData auctionData = (AhBzManager.AuctionData)AhBzManager.auctions.get(id2);
                  return auctionData == null ? 0L : auctionData.getSellPrice();
               } else {
                  return 0L;
               }
            } else {
               AhBzManager.AuctionData auctionData = (AhBzManager.AuctionData)AhBzManager.auctions.get(id);
               if (auctionData == null) {
                  return 0L;
               } else if (auctionData.getSellPrice() == -1L) {
                  return auctionData.getPrices().size() != 0 ? (Long)auctionData.getPrices().first() : 0L;
               } else {
                  return auctionData.getPrices().size() == 0 ? auctionData.getSellPrice() : 0L;
               }
            }
         }
      }
   }

   public String stripColor(String input) {
      return this.STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
   }

   public String cleanColor(String in) {
      return in.replaceAll("(?i)\\u00A7.", "");
   }

   public String format(long value) {
      String finalStr;
      if (!(Boolean)this.format.getValue()) {
         String number = value + "";
         StringBuffer numStr = new StringBuffer(number);

         for(int i = number.length() - 3; i >= 0; i -= 3) {
            numStr.insert(i, ",");
         }

         finalStr = numStr.toString();
         if (finalStr.startsWith(",")) {
            finalStr = finalStr.replaceFirst(",", "");
         }

         if (finalStr.startsWith("-,")) {
            finalStr = finalStr.replaceFirst(",", "");
         }

         return finalStr;
      } else if (value == Long.MIN_VALUE) {
         return this.format(-9223372036854775807L);
      } else if (value < 0L) {
         return "-" + this.format(-value);
      } else if (value < 1000L) {
         return Long.toString(value);
      } else {
         Map.Entry e = suffixes.floorEntry(value);
         Long divideBy = (Long)e.getKey();
         finalStr = (String)e.getValue();
         long truncated = value / (divideBy / 10L);
         boolean hasDecimal = truncated < 100L && (double)truncated / 10.0 != (double)(truncated / 10L);
         return hasDecimal ? (double)truncated / 10.0 + finalStr : truncated / 10L + finalStr;
      }
   }

   static {
      suffixes.put(1000L, "k");
      suffixes.put(1000000L, "m");
      suffixes.put(1000000000L, "b");
   }
}
