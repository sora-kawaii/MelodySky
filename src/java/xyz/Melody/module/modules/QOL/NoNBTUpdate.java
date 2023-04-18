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
        if (eventPacketRecieve.getPacket() instanceof S2FPacketSetSlot && (s2FPacketSetSlot = (S2FPacketSetSlot)eventPacketRecieve.getPacket()).func_149174_e() != null && (nBTTagCompound = s2FPacketSetSlot.func_149174_e().func_179543_a("ExtraAttributes", false)) != null && nBTTagCompound.func_74764_b("id")) {
            if (nBTTagCompound.func_74779_i("id").contains("GEMSTONE_GAUNTLET")) {
                eventPacketRecieve.setCancelled(true);
            }
            if (nBTTagCompound.func_74779_i("id").contains("DRILL")) {
                eventPacketRecieve.setCancelled(true);
            }
            if (s2FPacketSetSlot.func_149174_e().func_77973_b() == Items.field_179562_cC) {
                eventPacketRecieve.setCancelled(true);
            }
            if (s2FPacketSetSlot.func_149174_e().func_77973_b() == Items.field_151046_w) {
                eventPacketRecieve.setCancelled(true);
            }
        }
    }
}

