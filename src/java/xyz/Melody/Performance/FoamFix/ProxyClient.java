/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix;

import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import xyz.Melody.Performance.FoamFix.ProxyCommon;
import xyz.Melody.Performance.FoamFix.client.Deduplicator;
import xyz.Melody.Performance.FoamFix.client.FoamFixModelDeduplicate;
import xyz.Melody.Performance.FoamFix.client.FoamFixModelRegistryDuplicateWipe;
import xyz.Melody.Performance.FoamFix.shared.FoamFixShared;

public final class ProxyClient
extends ProxyCommon {
    public static Deduplicator deduplicator = new Deduplicator();
    public static final IBakedModel DUMMY_MODEL = new IBakedModel(){

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing facing) {
            return Collections.emptyList();
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            return Collections.emptyList();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(TextureMap.LOCATION_MISSING_TEXTURE.toString());
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }
    };

    @Override
    public void preInit() {
        super.preInit();
        if (!FoamFixShared.config.clDeduplicate) {
            deduplicator = null;
        }
    }

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(new FoamFixModelDeduplicate());
        if (FoamFixShared.config.clCleanRedundantModelRegistry) {
            MinecraftForge.EVENT_BUS.register(new FoamFixModelRegistryDuplicateWipe());
        }
    }

    @Override
    public void postInit() {
        super.postInit();
        if (deduplicator != null) {
            ProxyClient.deduplicator.successfuls = 0;
        }
    }
}

