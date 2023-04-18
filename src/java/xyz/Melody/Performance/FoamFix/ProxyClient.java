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

        public List<BakedQuad> func_177551_a(EnumFacing enumFacing) {
            return Collections.emptyList();
        }

        public List<BakedQuad> func_177550_a() {
            return Collections.emptyList();
        }

        public boolean func_177555_b() {
            return false;
        }

        public boolean func_177556_c() {
            return false;
        }

        public boolean func_177553_d() {
            return false;
        }

        public TextureAtlasSprite func_177554_e() {
            return Minecraft.func_71410_x().func_147117_R().getTextureExtry(TextureMap.field_174945_f.toString());
        }

        public ItemCameraTransforms func_177552_f() {
            return ItemCameraTransforms.field_178357_a;
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

