/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Performance.FoamFix.client;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.vecmath.Matrix4f;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

public final class FoamyItemLayerModel
implements IRetexturableModel<ItemLayerModel> {
    private static final ResourceLocation MISSINGNO = new ResourceLocation("missingno");
    private static final MethodHandle BUILD_QUAD;
    private static final MethodHandle TEXTURES_GET;
    private final ItemLayerModel parent;

    public FoamyItemLayerModel(ItemLayerModel itemLayerModel) {
        this.parent = itemLayerModel;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> immutableMap) {
        return new FoamyItemLayerModel((ItemLayerModel)this.parent.retexture(immutableMap));
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return this.parent.getDependencies();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return this.parent.getTextures();
    }

    @Override
    public IFlexibleBakedModel bake(IModelState iModelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> function) {
        return FoamyItemLayerModel.bake(this.parent, iModelState, vertexFormat, function);
    }

    public static IFlexibleBakedModel bake(ItemLayerModel itemLayerModel, IModelState iModelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> function) {
        Object object;
        int n;
        List list;
        ImmutableList.Builder builder = ImmutableList.builder();
        Optional<TRSRTransformation> optional = iModelState.apply(Optional.absent());
        try {
            list = TEXTURES_GET.invoke(itemLayerModel);
        }
        catch (Throwable throwable) {
            return ItemLayerModel.instance.bake(iModelState, vertexFormat, function);
        }
        ImmutableList.Builder builder2 = new ImmutableList.Builder();
        if (BUILD_QUAD != null) {
            for (n = 0; n < list.size(); ++n) {
                object = function.apply((ResourceLocation)list.get(n));
                builder2.add(object);
                try {
                    builder.add(BUILD_QUAD.invokeExact(vertexFormat, optional, EnumFacing.SOUTH, n, 0.0f, 0.0f, 0.53125f, ((TextureAtlasSprite)object).getMinU(), ((TextureAtlasSprite)object).getMaxV(), 1.0f, 0.0f, 0.53125f, ((TextureAtlasSprite)object).getMaxU(), ((TextureAtlasSprite)object).getMaxV(), 1.0f, 1.0f, 0.53125f, ((TextureAtlasSprite)object).getMaxU(), ((TextureAtlasSprite)object).getMinV(), 0.0f, 1.0f, 0.53125f, ((TextureAtlasSprite)object).getMinU(), ((TextureAtlasSprite)object).getMinV()));
                    continue;
                }
                catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        } else {
            for (n = 0; n < list.size(); ++n) {
                object = function.apply((ResourceLocation)list.get(n));
                for (BakedQuad bakedQuad : ItemLayerModel.instance.getQuadsForSprite(n, (TextureAtlasSprite)object, vertexFormat, optional)) {
                    if (bakedQuad.getFace() != EnumFacing.SOUTH) continue;
                    builder.add(bakedQuad);
                }
            }
        }
        TextureAtlasSprite textureAtlasSprite = function.apply(list.isEmpty() ? MISSINGNO : (ResourceLocation)list.get(0));
        object = IPerspectiveAwareModel.MapWrapper.getTransforms(iModelState);
        return new DynamicItemModel((ImmutableList<BakedQuad>)builder.build(), textureAtlasSprite, (ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation>)object, (List<TextureAtlasSprite>)((Object)builder2.build()), vertexFormat, optional).otherModel;
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    static {
        MethodHandle methodHandle = null;
        try {
            methodHandle = MethodHandles.lookup().unreflect(ReflectionHelper.findMethod(ItemLayerModel.class, null, new String[]{"buildQuad"}, VertexFormat.class, Optional.class, EnumFacing.class, Integer.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        BUILD_QUAD = methodHandle;
        methodHandle = null;
        try {
            methodHandle = MethodHandles.lookup().unreflectGetter(ReflectionHelper.findField(ItemLayerModel.class, "textures"));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        TEXTURES_GET = methodHandle;
    }

    public static class DynamicItemModel
    implements IPerspectiveAwareModel,
    IFlexibleBakedModel {
        private final List<TextureAtlasSprite> textures;
        private final TextureAtlasSprite particle;
        private final ImmutableList<BakedQuad> fastQuads;
        private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
        private final VertexFormat format;
        private final Optional<TRSRTransformation> transform;
        private final IFlexibleBakedModel otherModel;

        public DynamicItemModel(ImmutableList<BakedQuad> immutableList, TextureAtlasSprite textureAtlasSprite, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> immutableMap, List<TextureAtlasSprite> list, VertexFormat vertexFormat, Optional<TRSRTransformation> optional) {
            this.fastQuads = immutableList;
            this.particle = textureAtlasSprite;
            this.transforms = immutableMap;
            this.textures = list;
            this.format = vertexFormat;
            this.transform = optional;
            this.otherModel = new Dynamic3DItemModel(this);
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing enumFacing) {
            return Collections.emptyList();
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            return this.fastQuads;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return true;
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
            return this.particle;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }

        @Override
        public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType transformType) {
            Pair<? extends IFlexibleBakedModel, Matrix4f> pair = IPerspectiveAwareModel.MapWrapper.handlePerspective((IFlexibleBakedModel)this, this.transforms, transformType);
            if (transformType != ItemCameraTransforms.TransformType.GUI) {
                return Pair.of(this.otherModel, pair.getRight());
            }
            return pair;
        }

        @Override
        public VertexFormat getFormat() {
            return this.format;
        }
    }

    public static class Dynamic3DItemModel
    implements IPerspectiveAwareModel,
    IFlexibleBakedModel {
        private final DynamicItemModel parent;
        private SoftReference<List<BakedQuad>> quadsSoft = null;

        public Dynamic3DItemModel(DynamicItemModel dynamicItemModel) {
            this.parent = dynamicItemModel;
        }

        @Override
        public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType transformType) {
            Pair<? extends IFlexibleBakedModel, Matrix4f> pair = IPerspectiveAwareModel.MapWrapper.handlePerspective((IFlexibleBakedModel)this, this.parent.transforms, transformType);
            if (transformType == ItemCameraTransforms.TransformType.GUI && pair.getRight() == null) {
                return Pair.of(this.parent, null);
            }
            return pair;
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing enumFacing) {
            return Collections.EMPTY_LIST;
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            if (this.quadsSoft == null || this.quadsSoft.get() == null) {
                ImmutableList.Builder builder = new ImmutableList.Builder();
                for (int i = 0; i < this.parent.textures.size(); ++i) {
                    TextureAtlasSprite textureAtlasSprite = (TextureAtlasSprite)this.parent.textures.get(i);
                    builder.addAll(ItemLayerModel.instance.getQuadsForSprite(i, textureAtlasSprite, this.parent.format, this.parent.transform));
                }
                this.quadsSoft = new SoftReference<ImmutableCollection>(builder.build());
            }
            return this.quadsSoft.get();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return true;
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
            return this.parent.particle;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }

        @Override
        public VertexFormat getFormat() {
            return this.parent.getFormat();
        }
    }
}

