/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.coremod;

import java.io.File;
import java.util.Map;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import xyz.Melody.Performance.FoamFix.coremod.FoamFixTransformer;
import xyz.Melody.Performance.FoamFix.shared.FoamFixShared;

@IFMLLoadingPlugin.Name(value="Do not report to Forge! Remove FoamFixAPI (or replace with FoamFixAPI-Lawful) and try again.")
@IFMLLoadingPlugin.SortingIndex(value=1001)
@IFMLLoadingPlugin.TransformerExclusions(value={"xyz.Melody.FoamFix"})
public final class FoamFixCore
implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"xyz.Melody.FoamFix.coremod.FoamFixTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return "xyz.Melody.FoamFix.coremod.FoamFixCoreContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {
        FoamFixShared.coremodEnabled = true;
        FoamFixShared.config.init(new File(new File("config"), "foamfix.cfg"), true);
        if (FoamFixShared.config.geBlacklistLibraryTransformers) {
            LaunchClassLoader launchClassLoader = (LaunchClassLoader)this.getClass().getClassLoader();
            launchClassLoader.addTransformerExclusion("com.ibm.icu.");
            launchClassLoader.addTransformerExclusion("com.sun.");
            launchClassLoader.addTransformerExclusion("gnu.trove.");
            launchClassLoader.addTransformerExclusion("io.netty.");
            launchClassLoader.addTransformerExclusion("it.unimi.dsi.fastutil.");
            launchClassLoader.addTransformerExclusion("joptsimple.");
            launchClassLoader.addTransformerExclusion("org.apache.");
            launchClassLoader.addTransformerExclusion("oshi.");
            launchClassLoader.addTransformerExclusion("scala.");
        }
        FoamFixTransformer.init();
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}

