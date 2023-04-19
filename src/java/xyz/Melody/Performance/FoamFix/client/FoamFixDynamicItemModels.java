/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.client;

import com.google.common.base.Function;
import java.lang.invoke.MethodHandle;
import java.util.Set;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import xyz.Melody.Performance.FoamFix.client.FoamyItemLayerModel;
import xyz.Melody.Performance.FoamFix.util.MethodHandleHelper;

public final class FoamFixDynamicItemModels {
    public static void register() {
        MethodHandle LOADERS = MethodHandleHelper.findFieldGetter(ModelLoaderRegistry.class, "loaders");
        try {
            Set loaders = LOADERS.invoke();
            loaders.remove(ItemLayerModel.Loader.instance);
            loaders.add(Loader.INSTANCE);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return FoamyItemLayerModel.bake((ItemLayerModel)((Object)this), state, format, bakedTextureGetter);
    }

    public static enum Loader implements ICustomModelLoader
    {
        INSTANCE;

        private static final IModel model;

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {
            ItemLayerModel.Loader.instance.onResourceManagerReload(resourceManager);
        }

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return ItemLayerModel.Loader.instance.accepts(modelLocation);
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) {
            return model;
        }

        static {
            model = new FoamyItemLayerModel(ItemLayerModel.instance);
        }
    }
}

