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
    private void onPacketRCV(EventPacketRecieve eventPacketRecieve) {
        NBTTagCompound nBTTagCompound;
        S2FPacketSetSlot s2FPacketSetSlot;
        if (eventPacketRecieve.getPacket() instanceof S2FPacketSetSlot && (s2FPacketSetSlot = (S2FPacketSetSlot)eventPacketRecieve.getPacket()).func_149174_e() != null && (nBTTagCompound = s2FPacketSetSlot.func_149174_e().getSubCompound("ExtraAttributes", false)) != null && nBTTagCompound.hasKey("id")) {
            if (nBTTagCompound.getString("id").contains("GEMSTONE_GAUNTLET")) {
                eventPacketRecieve.setCancelled(true);
            }
            if (nBTTagCompound.getString("id").contains("DRILL")) {
                eventPacketRecieve.setCancelled(true);
            }
            if (s2FPacketSetSlot.func_149174_e().getItem() == Items.prismarine_shard) {
                eventPacketRecieve.setCancelled(true);
            }
            if (s2FPacketSetSlot.func_149174_e().getItem() == Items.diamond_pickaxe) {
                eventPacketRecieve.setCancelled(true);
            }
        }
    }
}

