package xyz.Melody.injection.mixins.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import xyz.Melody.Client;
import xyz.Melody.module.modules.others.OldAnimations;

@Mixin({ModelBiped.class})
public class ModelBipedMixin extends ModelBase {
   @ModifyConstant(
      method = "setRotationAngles",
      constant = @Constant(
   floatValue = -0.5235988F
)
   )
   private float cancelRotation(float original) {
      OldAnimations anim = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
      if (anim.isEnabled()) {
         return (Boolean)anim.oldBlockhitting.getValue() ? 0.0F : original;
      } else {
         return original;
      }
   }
}
