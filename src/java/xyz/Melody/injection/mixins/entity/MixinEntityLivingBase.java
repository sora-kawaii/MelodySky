package xyz.Melody.injection.mixins.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.Melody.Client;
import xyz.Melody.module.modules.render.Cam;

@Mixin({EntityLivingBase.class})
public abstract class MixinEntityLivingBase extends MixinEntity {
   @Shadow
   private EntityLivingBase entityLivingToAttack;
   @Shadow
   private int revengeTimer;

   @Shadow
   protected abstract void updateFallState(double var1, boolean var3, Block var4, BlockPos var5);

   @Shadow
   public abstract IAttributeInstance getEntityAttribute(IAttribute var1);

   @Shadow
   public void onLivingUpdate() {
   }

   @Inject(
      method = "isPotionActive(Lnet/minecraft/potion/Potion;)Z",
      at = {@At("HEAD")},
      cancellable = true
   )
   private void isPotionActive(Potion p_isPotionActive_1_, CallbackInfoReturnable ciReturnbale) {
      Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
      if ((p_isPotionActive_1_ == Potion.confusion || p_isPotionActive_1_ == Potion.blindness) && cam.isEnabled() && (Boolean)cam.noBlindness.getValue()) {
         ciReturnbale.setReturnValue(false);
      }

   }
}
