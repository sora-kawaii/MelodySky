/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import xyz.Melody.Event.value.Mode;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class ClientCommands
extends Module {
    public Mode<Enum> mode = new Mode("Mode", (Enum[])commandMode.values(), (Enum)commandMode.dot);

    public ClientCommands() {
        super("ClientCommands", new String[]{"cc", "ccm", "command"}, ModuleType.Others);
        this.addValues(this.mode);
        this.setModInfo("dot: .xxx | bar: -xxx | wavy_line: ~xxx");
        this.setEnabled(true);
    }

    static enum commandMode {
        dot,
        bar,
        wavy_line;

    }
}

