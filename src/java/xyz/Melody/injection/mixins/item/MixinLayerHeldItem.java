package xyz.Melody.injection.mixins.item;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({LayerHeldItem.class})
public class MixinLayerHeldItem {
   @Shadow
   @Final
   private RendererLivingEntity livingEntityRenderer;
}
