package xyz.Melody.injection.mixins.item;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.System.Animations.AnimationHandler;
import xyz.Melody.module.modules.others.OldAnimations;
import xyz.Melody.module.modules.render.Cam;

@Mixin({ItemRenderer.class})
public abstract class MixinItemRenderer {
   @Shadow
   private float equippedProgress;
   @Shadow
   private float prevEquippedProgress;
   @Shadow
   private ItemStack itemToRender;
   @Shadow
   private int equippedItemSlot;

   @Shadow
   public abstract void transformFirstPersonItem(float var1, float var2);

   @Shadow
   public abstract void renderItemMap(AbstractClientPlayer var1, float var2, float var3, float var4);

   @Inject(
      method = "renderItemInFirstPerson",
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderItemInFirstPerson(float partialTicks, CallbackInfo ci) {
      boolean anim = Client.instance.getModuleManager().getModuleByClass(OldAnimations.class).isEnabled();
      if (anim && this.itemToRender != null) {
         ItemRenderer $this = (ItemRenderer)this;
         float equipProgress = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks;
         if (AnimationHandler.getInstance().renderItemInFirstPerson($this, this.itemToRender, equipProgress, partialTicks)) {
            ci.cancel();
         }
      }

   }

   @Inject(
      method = "renderFireInFirstPerson",
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderFireInFirstPerson(CallbackInfo ci) {
      Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
      if (cam.isEnabled() && (Boolean)cam.noFire.getValue()) {
         ci.cancel();
      }

   }
}
