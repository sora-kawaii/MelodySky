/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import xyz.Melody.Event.value.Numbers;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class PlayerList
extends Module {
    public Numbers<Double> scanDelay = new Numbers<Double>("ScanDelay", 500.0, 100.0, 2000.0, 10.0);
    public Numbers<Double> range = new Numbers<Double>("Range", 30.0, 20.0, 100.0, 5.0);

    public PlayerList() {
        super("PlayerList", new String[]{"cc", "ccm", "command"}, ModuleType.Others);
        this.addValues(this.scanDelay, this.range);
    }
}

