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
    public void injectData(Map<String, Object> data) {
        FoamFixShared.coremodEnabled = true;
        FoamFixShared.config.init(new File(new File("config"), "foamfix.cfg"), true);
        if (FoamFixShared.config.geBlacklistLibraryTransformers) {
            LaunchClassLoader classLoader = (LaunchClassLoader)this.getClass().getClassLoader();
            classLoader.addTransformerExclusion("com.ibm.icu.");
            classLoader.addTransformerExclusion("com.sun.");
            classLoader.addTransformerExclusion("gnu.trove.");
            classLoader.addTransformerExclusion("io.netty.");
            classLoader.addTransformerExclusion("it.unimi.dsi.fastutil.");
            classLoader.addTransformerExclusion("joptsimple.");
            classLoader.addTransformerExclusion("org.apache.");
            classLoader.addTransformerExclusion("oshi.");
            classLoader.addTransformerExclusion("scala.");
        }
        FoamFixTransformer.init();
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}

