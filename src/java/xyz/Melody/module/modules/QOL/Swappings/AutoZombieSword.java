/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Swappings;

import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoZombieSword
extends Module {
    private Mode<Enum> type = new Mode("Sword", (Enum[])swordType.values(), (Enum)swordType.FloridZombie);

    public AutoZombieSword() {
        super("AutoHeal", new String[]{"aheal"}, ModuleType.Swapping);
        this.addValues(this.type);
        this.setModInfo("Auto Swap Zombie Swords and Use 5 Times.");
    }

    @EventHandler
    private void onTick(EventTick e) {
        block10: for (int i = 0; i < 5; ++i) {
            switch (((Enum)this.type.getValue()).toString().toUpperCase()) {
                case "ZOMBIE": {
                    ItemUtils.useSBItem("ZOMBIE_SWORD");
                    continue block10;
                }
                case "ORNATEZOMBIE": {
                    ItemUtils.useSBItem("ORNATE_ZOMBIE_SWORD");
                    continue block10;
                }
                case "FLORIDZOMBIE": {
                    ItemUtils.useSBItem("FLORID_ZOMBIE_SWORD");
                }
            }
        }
        Helper.sendMessage("AutoZombieSword Disabled.");
        this.setEnabled(false);
    }

    static enum swordType {
        FloridZombie,
        OrnateZombie,
        Zombie;

    }
}

