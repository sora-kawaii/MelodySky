package xyz.Melody.injection.mixins.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.module.modules.render.NoArmorRender;

@Mixin({LayerArmorBase.class})
public abstract class MixinLayerArmorBase implements LayerRenderer {
   @Shadow
   public abstract void renderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9);

   @Inject(
      method = "doRenderLayer",
      at = {@At("HEAD")},
      cancellable = true
   )
   public void doRenderLayer(EntityLivingBase entity, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
      NoArmorRender nar = (NoArmorRender)Client.instance.getModuleManager().getModuleByClass(NoArmorRender.class);
      if (nar.isEnabled() && entity instanceof EntityPlayer && (Boolean)nar.armor.getValue()) {
         if ((Boolean)nar.selfOnly.getValue()) {
            if (entity == Minecraft.getMinecraft().thePlayer) {
               ci.cancel();
            }
         } else {
            ci.cancel();
         }
      }

   }

   @Overwrite
   public ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot) {
      NoArmorRender nar = (NoArmorRender)Client.instance.getModuleManager().getModuleByClass(NoArmorRender.class);
      if (nar.isEnabled() && (Boolean)nar.armor.getValue()) {
         if ((Boolean)nar.selfOnly.getValue()) {
            return entitylivingbaseIn == Minecraft.getMinecraft().thePlayer ? null : entitylivingbaseIn.getCurrentArmor(armorSlot - 1);
         } else {
            return null;
         }
      } else {
         return entitylivingbaseIn.getCurrentArmor(armorSlot - 1);
      }
   }
}
