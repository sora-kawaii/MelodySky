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
        MethodHandle methodHandle = MethodHandleHelper.findFieldGetter(ModelLoaderRegistry.class, "loaders");
        try {
            Set set = methodHandle.invoke();
            set.remove(ItemLayerModel.Loader.instance);
            set.add(Loader.INSTANCE);
        }
        catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public IFlexibleBakedModel bake(IModelState iModelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> function) {
        return FoamyItemLayerModel.bake((ItemLayerModel)((Object)this), iModelState, vertexFormat, function);
    }

    public static enum Loader implements ICustomModelLoader
    {
        INSTANCE;

        private static final IModel model;

        public void func_110549_a(IResourceManager iResourceManager) {
            ItemLayerModel.Loader.instance.func_110549_a(iResourceManager);
        }

        @Override
        public boolean accepts(ResourceLocation resourceLocation) {
            return ItemLayerModel.Loader.instance.accepts(resourceLocation);
        }

        @Override
        public IModel loadModel(ResourceLocation resourceLocation) {
            return model;
        }

        static {
            model = new FoamyItemLayerModel(ItemLayerModel.instance);
        }
    }
}

