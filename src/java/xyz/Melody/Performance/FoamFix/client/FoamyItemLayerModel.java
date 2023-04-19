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

    public FoamyItemLayerModel(ItemLayerModel parent) {
        this.parent = parent;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures) {
        return new FoamyItemLayerModel((ItemLayerModel)this.parent.retexture(textures));
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
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return FoamyItemLayerModel.bake(this.parent, state, format, bakedTextureGetter);
    }

    public static IFlexibleBakedModel bake(ItemLayerModel parent, IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        TextureAtlasSprite sprite;
        int i;
        List textures;
        ImmutableList.Builder builder = ImmutableList.builder();
        Optional<TRSRTransformation> transform = state.apply(Optional.absent());
        try {
            textures = TEXTURES_GET.invoke(parent);
        }
        catch (Throwable t) {
            return ItemLayerModel.instance.bake(state, format, bakedTextureGetter);
        }
        ImmutableList.Builder textureAtlas = new ImmutableList.Builder();
        if (BUILD_QUAD != null) {
            for (i = 0; i < textures.size(); ++i) {
                sprite = bakedTextureGetter.apply((ResourceLocation)textures.get(i));
                textureAtlas.add(sprite);
                try {
                    builder.add(BUILD_QUAD.invokeExact(format, transform, EnumFacing.SOUTH, i, 0.0f, 0.0f, 0.53125f, sprite.getMinU(), sprite.getMaxV(), 1.0f, 0.0f, 0.53125f, sprite.getMaxU(), sprite.getMaxV(), 1.0f, 1.0f, 0.53125f, sprite.getMaxU(), sprite.getMinV(), 0.0f, 1.0f, 0.53125f, sprite.getMinU(), sprite.getMinV()));
                    continue;
                }
                catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        } else {
            for (i = 0; i < textures.size(); ++i) {
                sprite = bakedTextureGetter.apply((ResourceLocation)textures.get(i));
                for (BakedQuad quad : ItemLayerModel.instance.getQuadsForSprite(i, sprite, format, transform)) {
                    if (quad.getFace() != EnumFacing.SOUTH) continue;
                    builder.add(quad);
                }
            }
        }
        TextureAtlasSprite particle = bakedTextureGetter.apply(textures.isEmpty() ? MISSINGNO : (ResourceLocation)textures.get(0));
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> map = IPerspectiveAwareModel.MapWrapper.getTransforms(state);
        return new DynamicItemModel((ImmutableList<BakedQuad>)builder.build(), particle, map, (List<TextureAtlasSprite>)((Object)textureAtlas.build()), format, transform).otherModel;
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    static {
        MethodHandle handle = null;
        try {
            handle = MethodHandles.lookup().unreflect(ReflectionHelper.findMethod(ItemLayerModel.class, null, new String[]{"buildQuad"}, VertexFormat.class, Optional.class, EnumFacing.class, Integer.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        BUILD_QUAD = handle;
        handle = null;
        try {
            handle = MethodHandles.lookup().unreflectGetter(ReflectionHelper.findField(ItemLayerModel.class, "textures"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        TEXTURES_GET = handle;
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

        public DynamicItemModel(ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, List<TextureAtlasSprite> textures, VertexFormat format, Optional<TRSRTransformation> transform) {
            this.fastQuads = quads;
            this.particle = particle;
            this.transforms = transforms;
            this.textures = textures;
            this.format = format;
            this.transform = transform;
            this.otherModel = new Dynamic3DItemModel(this);
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing facing) {
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
        public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
            Pair<? extends IFlexibleBakedModel, Matrix4f> pair = IPerspectiveAwareModel.MapWrapper.handlePerspective((IFlexibleBakedModel)this, this.transforms, type);
            if (type != ItemCameraTransforms.TransformType.GUI) {
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

        public Dynamic3DItemModel(DynamicItemModel parent) {
            this.parent = parent;
        }

        @Override
        public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
            Pair<? extends IFlexibleBakedModel, Matrix4f> pair = IPerspectiveAwareModel.MapWrapper.handlePerspective((IFlexibleBakedModel)this, this.parent.transforms, type);
            if (type == ItemCameraTransforms.TransformType.GUI && pair.getRight() == null) {
                return Pair.of(this.parent, null);
            }
            return pair;
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing facing) {
            return Collections.EMPTY_LIST;
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            if (this.quadsSoft == null || this.quadsSoft.get() == null) {
                ImmutableList.Builder builder = new ImmutableList.Builder();
                for (int i = 0; i < this.parent.textures.size(); ++i) {
                    TextureAtlasSprite sprite = (TextureAtlasSprite)this.parent.textures.get(i);
                    builder.addAll(ItemLayerModel.instance.getQuadsForSprite(i, sprite, this.parent.format, this.parent.transform));
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

