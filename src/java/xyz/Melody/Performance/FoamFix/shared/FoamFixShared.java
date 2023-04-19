/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.shared;

import xyz.Melody.Performance.FoamFix.shared.FoamFixConfig;

public final class FoamFixShared {
    public static final FoamFixConfig config = new FoamFixConfig();
    public static boolean coremodEnabled = false;
    public static int ramSaved = 0;

    public static boolean hasOptifine() {
        try {
            return Class.forName("optifine.OptiFineTweaker") != null;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }
}

