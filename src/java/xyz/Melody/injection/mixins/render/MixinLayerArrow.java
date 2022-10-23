package xyz.Melody.injection.mixins.render;

import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.module.modules.render.NoArmorRender;

@Mixin({LayerArrow.class})
public abstract class MixinLayerArrow implements LayerRenderer {
   @Inject(
      method = "doRenderLayer",
      at = {@At("HEAD")},
      cancellable = true
   )
   public void doRenderLayer(EntityLivingBase entity, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
      NoArmorRender nar = (NoArmorRender)Client.instance.getModuleManager().getModuleByClass(NoArmorRender.class);
      if (nar.isEnabled() && entity instanceof EntityPlayer && (Boolean)nar.arrows.getValue()) {
         ci.cancel();
      }

   }
}
