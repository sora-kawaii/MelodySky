/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import net.minecraft.entity.Entity;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class InvisEntity
extends Module {
    public InvisEntity() {
        super("InvisEntity", new String[]{"ie"}, ModuleType.QOL);
        this.setColor(new Color(244, 255, 149).getRGB());
        this.setModInfo("Remove The Entity What You are Looking.");
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        if (this.mc.field_71476_x == null) {
            return;
        }
        if (this.mc.field_71476_x.field_72308_g == null) {
            return;
        }
        if (Client.instance.alt.func_151468_f()) {
            Entity entity = this.mc.field_71476_x.field_72308_g;
            this.mc.field_71441_e.func_72900_e(entity);
        }
        this.setEnabled(false);
    }
}

