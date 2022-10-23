package xyz.Melody.injection.mixins.render;

import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.Melody.Client;
import xyz.Melody.System.Animations.AnimationHandler;
import xyz.Melody.module.modules.others.OldAnimations;

@Mixin(
   value = {RenderFish.class},
   priority = 1001
)
public class RenderFishMixin {
   @Redirect(
      method = "doRender",
      at = @At(
   value = "NEW",
   target = "net/minecraft/util/Vec3",
   ordinal = 0
)
   )
   private Vec3 oldFishingLine(double x, double y, double z) {
      OldAnimations anim = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
      if (anim.isEnabled()) {
         return !(Boolean)anim.oldRod.getValue() ? new Vec3(x, y, z) : AnimationHandler.getInstance().getOffset();
      } else {
         return new Vec3(x, y, z);
      }
   }
}
