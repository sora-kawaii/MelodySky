/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Swappings;

import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class WitherImpact
extends Module {
    private Mode<Enum> mode = new Mode("Sword", (Enum[])swords.values(), (Enum)swords.Hyperion);

    public WitherImpact() {
        super("WitherImpact", new String[]{"wi"}, ModuleType.Swapping);
        this.addValues(this.mode);
        this.setModInfo("Auto Swap Wither Impact Swords.");
    }

    @EventHandler
    private void onTick(EventTick e) {
        switch (((Enum)this.mode.getValue()).toString().toUpperCase()) {
            case "HYPERION": {
                ItemUtils.useSBItem("HYPERION");
                break;
            }
            case "SCYLLA": {
                ItemUtils.useSBItem("SCYLLA");
                break;
            }
            case "ASTRAEA": {
                ItemUtils.useSBItem("ASTRAEA");
                break;
            }
            case "VALKYRIE": {
                ItemUtils.useSBItem("VALKYRIE");
                break;
            }
            case "NECRONBLADE": {
                ItemUtils.useSBItem("NECRON_BLADE");
            }
        }
        this.setEnabled(false);
    }

    static enum swords {
        Hyperion,
        Scylla,
        Astraea,
        Valkyrie,
        NecronBlade;

    }
}

