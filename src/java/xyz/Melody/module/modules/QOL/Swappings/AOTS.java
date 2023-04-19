/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Swappings;

import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventKey;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AOTS
extends Module {
    private boolean shouldSwitch;
    private TimerUtil timer = new TimerUtil();

    public AOTS() {
        super("AOTS", new String[]{"aots"}, ModuleType.Swapping);
        this.setModInfo("Auto Swap Axe Of the Shredded.");
    }

    @EventHandler
    private void onTick(EventTick e) {
        if (this.shouldSwitch && this.timer.hasReached(200.0)) {
            ItemUtils.useSBItem("AXE_OF_THE_SHREDDED");
            this.timer.reset();
        }
        this.shouldSwitch = false;
    }

    @EventHandler
    private void onKey(EventKey event) {
        if (event.getKey() == this.getKey()) {
            this.shouldSwitch = true;
            this.setEnabled(true);
        }
    }
}

