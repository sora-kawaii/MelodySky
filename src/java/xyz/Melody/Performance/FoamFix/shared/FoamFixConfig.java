/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.shared;

import java.io.File;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.common.versioning.VersionRange;

public final class FoamFixConfig {
    public boolean lwWeakenResourceCache;
    public boolean clDeduplicate;
    public boolean clCleanRedundantModelRegistry;
    public boolean geBlacklistLibraryTransformers;
    public boolean geBlockPosPatch;
    public boolean geDynamicRegistrySizeScaling;
    public boolean geSmallPropertyStorage;
    public boolean geReplaceSimpleName;
    public int clDeduplicateRecursionLevel;
    private Configuration config;
    public boolean geSmallLightingOptimize = false;
    public boolean clDynamicItemModels;

    private boolean getBoolean(String string, String string2, boolean bl, String string3) {
        return this.config.getBoolean(string, string2, bl, string3);
    }

    private boolean getBoolean(String string, String string2, boolean bl, String string3, String string4) {
        VersionRange versionRange = VersionParser.parseRange(string4);
        DefaultArtifactVersion defaultArtifactVersion = new DefaultArtifactVersion("Forge", versionRange);
        if (defaultArtifactVersion.containsVersion(new DefaultArtifactVersion("Forge", ForgeVersion.getVersion()))) {
            return this.getBoolean(string, string2, bl, string3);
        }
        return false;
    }

    public void init(File file, boolean bl) {
        if (this.config == null) {
            this.config = new Configuration(file);
            this.lwWeakenResourceCache = this.getBoolean("weakenResourceCache", "launchwrapper", true, "Weaken LaunchWrapper's byte[] resource cache to make it cleanuppable by the GC. Safe.");
            this.clDeduplicate = this.getBoolean("deduplicateModels", "client", true, "Enable deduplication of redundant objects in memory.");
            this.clDeduplicateRecursionLevel = this.config.getInt("deduplicateModelsMaxRecursion", "client", 6, 1, Integer.MAX_VALUE, "The maximum amount of levels of recursion for the deduplication process. Smaller values will deduplicate less data, but make the process run faster.");
            this.clCleanRedundantModelRegistry = this.getBoolean("clearDuplicateModelRegistry", "client", true, "Clears the baked models generated in the first pass *before* entering the second pass, instead of *after*. While this doesn't reduce memory usage in-game, it does reduce it noticeably during loading.");
            this.geDynamicRegistrySizeScaling = this.getBoolean("dynamicRegistrySizeScaling", "general", true, "Makes large FML registries scale their availability BitSets dynamically, saving ~48MB of RAM.", "(,13.19.1.2190)");
            if (bl) {
                this.geBlacklistLibraryTransformers = this.getBoolean("blacklistLibraryTransformers", "coremod", true, "Stops certain non-Minecraft-related libraries from being ASM transformed. You shouldn't be transforming those anyway.");
                this.geSmallPropertyStorage = this.getBoolean("smallPropertyStorage", "coremod", true, "Replaces the default BlockState/ExtendedBlockState implementations with a far more memory-efficient variant.");
                this.geBlockPosPatch = this.getBoolean("optimizedBlockPos", "coremod", true, "Optimizes BlockPos mutable/immutable getters to run on the same variables, letting them be inlined and thus theoretically increasing performance.");
                this.geReplaceSimpleName = this.getBoolean("replaceWorldSimpleName", "coremod", true, "Replaces Class.getSimpleName in World.updateEntities with getName. As Class.getName's output is cached, unlike getSimpleName, this should provide a small performance boost.");
                this.clDynamicItemModels = this.getBoolean("dynamicItemModels", "coremod", true, "Make 3D forms of items be rendered dynamically and cached when necessary.");
            }
            this.config.save();
        }
    }
}

