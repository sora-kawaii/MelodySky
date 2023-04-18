/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import xyz.Melody.Event.value.Mode;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class SayMimicKilled
extends Module {
    public Mode<Enum> mode = new Mode("Mode", (Enum[])Chats.values(), (Enum)Chats.Party);

    public SayMimicKilled() {
        super("SayMimicKilled", new String[]{"as"}, ModuleType.Dungeons);
        this.addValues(this.mode);
        this.setModInfo("Say Mimic Killed When Mimic dead.");
    }

    public static enum Chats {
        Party,
        All;

    }
}

