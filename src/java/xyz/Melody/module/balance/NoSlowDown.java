/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class NoSlowDown
extends Module {
    public NoSlowDown() {
        super("NoSlowDown", new String[]{"noslow", "nsd", "noslowdown"}, ModuleType.Balance);
        this.setModInfo("No Slow Down When Using Item.");
    }

    @EventHandler
    private void onUpdate(EventPreUpdate eventPreUpdate) {
        if (Client.inSkyblock) {
            return;
        }
        if (this.mc.field_71439_g.func_71052_bv() > 0 && this.mc.field_71439_g.field_70122_E) {
            this.mc.field_71439_g.field_71174_a.func_147297_a(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.field_71439_g.field_71071_by.func_70448_g(), 0.0f, 0.0f, 0.0f));
        }
    }
}

