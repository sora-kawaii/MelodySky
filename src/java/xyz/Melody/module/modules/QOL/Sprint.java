/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class Sprint
extends Module {
    public Sprint() {
        super("Sprint", new String[]{"run"}, ModuleType.QOL);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.setModInfo("Toggle Sprint.");
    }

    @EventHandler
    private void onUpdate(EventPreUpdate eventPreUpdate) {
        if (this.mc.field_71439_g.func_71024_bL().func_75116_a() > 6 && this.mc.field_71439_g.field_70701_bs > 0.0f) {
            this.mc.field_71439_g.func_70031_b(true);
        }
    }
}

