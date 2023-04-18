/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import java.awt.Color;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.container.DrawSlotEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.modules.QOL.Dungeons.AutoSalvage;

public final class AutoSell
extends Module {
    private Option<Boolean> DJ = new Option<Boolean>("DungeonJunks", true);
    private Option<Boolean> salvable = new Option<Boolean>("SalvageAble", false);
    private Option<Boolean> boooom = new Option<Boolean>("SuperBoooom", true);
    private Option<Boolean> rs = new Option<Boolean>("ReviveStones", true);
    private Option<Boolean> runes = new Option<Boolean>("Runes", true);
    private boolean inTradeMenu = false;
    private int tickCount = 0;
    private static final String[] dungeonJunk = new String[]{"Training Weight", "Healing VIII Splash Potion", "Healing 8 Slash Potion", "Premium Flesh", "Mimic Fragment", "Enchanted Rotten Flesh", "Enchanted Bone", "Defuse Kit", "Enchanted Ice", "Optic Lense", "Tripwire Hook", "Button", "Carpet", "Lever", "Journal Entry", "Sign"};

    public AutoSell() {
        super("AutoSell", new String[]{"as"}, ModuleType.Dungeons);
        this.addValues(this.DJ, this.salvable, this.boooom, this.runes, this.rs);
        this.setModInfo("Auto Sell Useless Items.");
    }

    @EventHandler
    public void onTick(EventTick eventTick) {
        List list;
        if (this.tickCount % 2 == 0 && Client.inSkyblock && this.inTradeMenu && this.mc.field_71462_r instanceof GuiChest && ((Slot)(list = ((GuiChest)this.mc.field_71462_r).field_147002_h.field_75151_b).get(49)).func_75211_c() != null && ((Slot)list.get(49)).func_75211_c().func_77973_b() != Item.func_150898_a((Block)Blocks.field_180401_cv)) {
            for (Slot slot : this.mc.field_71439_g.field_71069_bz.field_75151_b) {
                if (!this.shouldSell(slot.func_75211_c())) continue;
                this.windowClick(this.mc.field_71439_g.field_71070_bA.field_75152_c, slot, 2, 3);
                break;
            }
        }
        ++this.tickCount;
    }

    @EventHandler
    public void onBackgroundRender(EventTick eventTick) {
        Container container;
        GuiScreen guiScreen = this.mc.field_71462_r;
        if (guiScreen instanceof GuiChest && (container = ((GuiChest)guiScreen).field_147002_h) instanceof ContainerChest) {
            String string = ((ContainerChest)container).func_85151_d().func_145748_c_().func_150260_c();
            this.inTradeMenu = string.equals("Trades");
        }
    }

    @EventHandler
    public void onDrawSlot(DrawSlotEvent drawSlotEvent) {
        if (this.inTradeMenu && this.shouldSell(drawSlotEvent.slot.func_75211_c())) {
            int n = drawSlotEvent.slot.field_75223_e;
            int n2 = drawSlotEvent.slot.field_75221_f;
            Gui.func_73734_a((int)n, (int)n2, (int)(n + 16), (int)(n2 + 16), (int)new Color(128, 0, 128, 120).getRGB());
        }
    }

    private boolean shouldSell(ItemStack itemStack) {
        if (itemStack != null) {
            if (((Boolean)this.salvable.getValue()).booleanValue() && AutoSalvage.shouldSalvage(itemStack)) {
                return true;
            }
            if (((Boolean)this.boooom.getValue()).booleanValue() && ItemUtils.getSkyBlockID(itemStack).equals("SUPERBOOM_TNT")) {
                return true;
            }
            if (((Boolean)this.rs.getValue()).booleanValue() && ItemUtils.getSkyBlockID(itemStack).equals("REVIVE_STONE")) {
                return true;
            }
            if (((Boolean)this.runes.getValue()).booleanValue() && itemStack.func_82833_r().contains("Rune")) {
                return true;
            }
            if (((Boolean)this.DJ.getValue()).booleanValue()) {
                for (String string : dungeonJunk) {
                    if (!itemStack.func_82833_r().contains(string)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private void windowClick(int n, Slot slot, int n2, int n3) {
        short s = this.mc.field_71439_g.field_71070_bA.func_75136_a(this.mc.field_71439_g.field_71071_by);
        ItemStack itemStack = slot.func_75211_c();
        this.mc.func_147114_u().func_147297_a(new C0EPacketClickWindow(n, 45 + slot.field_75222_d, n2, n3, itemStack, s));
    }
}

