/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import xyz.Melody.Event.value.Numbers;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class Reach
extends Module {
    public Numbers<Double> size = new Numbers<Double>("Size", 3.5, 0.0, 6.0, 0.1);

    public Reach() {
        super("Reach", new String[]{"hitBox"}, ModuleType.Balance);
        this.addValues(this.size);
    }
}

