/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class NoNBTUpdate
extends Module {
    public NoNBTUpdate() {
        super("NoNBTUpdate", new String[]{"nbt"}, ModuleType.QOL);
        this.setColor(new Color(191, 191, 191).getRGB());
        this.setModInfo("Anti NBT Updates on Drill/Gauntlets.");
    }

    @EventHandler
    private void onPacketRCV(EventPacketRecieve e) {
        NBTTagCompound extraAttributes;
        S2FPacketSetSlot pack;
        if (e.getPacket() instanceof S2FPacketSetSlot && (pack = (S2FPacketSetSlot)e.getPacket()).func_149174_e() != null && (extraAttributes = pack.func_149174_e().getSubCompound("ExtraAttributes", false)) != null && extraAttributes.hasKey("id")) {
            if (extraAttributes.getString("id").contains("GEMSTONE_GAUNTLET")) {
                e.setCancelled(true);
            }
            if (extraAttributes.getString("id").contains("DRILL")) {
                e.setCancelled(true);
            }
            if (pack.func_149174_e().getItem() == Items.prismarine_shard) {
                e.setCancelled(true);
            }
            if (pack.func_149174_e().getItem() == Items.diamond_pickaxe) {
                e.setCancelled(true);
            }
        }
    }
}

