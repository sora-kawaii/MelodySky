/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.coremod;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import java.util.List;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public final class FoamFixCoreContainer
extends DummyModContainer {
    private static final ModMetadata md = new ModMetadata();

    public FoamFixCoreContainer() {
        super(md);
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }

    @Override
    public List<String> getOwnedPackages() {
        return ImmutableList.of("xyz.Melody.FoamFix.coremod");
    }

    static {
        FoamFixCoreContainer.md.modId = "foamfixcore";
        FoamFixCoreContainer.md.name = "FoamFixCore";
        FoamFixCoreContainer.md.description = "I'm actually just an optional part of FoamFix, available exclusively as part of the Anarchy version!";
        FoamFixCoreContainer.md.authorList = ImmutableList.of("asie");
        FoamFixCoreContainer.md.version = "7.7.4";
    }
}

