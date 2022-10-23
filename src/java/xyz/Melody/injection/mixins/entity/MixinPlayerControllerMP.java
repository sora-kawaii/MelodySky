package xyz.Melody.injection.mixins.entity;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.misc.EventClickSlot;
import xyz.Melody.Event.events.world.EventAttackEntity;

@Mixin({PlayerControllerMP.class})
public class MixinPlayerControllerMP {
   @Shadow
   protected int blockHitDelay;

   @Inject(
      method = "updateController",
      at = {@At("HEAD")}
   )
   public void updateController(CallbackInfo ci) {
   }

   @Inject(
      method = "attackEntity",
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;syncCurrentPlayItem()V"
)}
   )
   private void attackEntity(EntityPlayer entityPlayer, Entity targetEntity, CallbackInfo callbackInfo) {
      if (targetEntity != null) {
         EventBus.getInstance().call(new EventAttackEntity(targetEntity));
      }
   }

   @Inject(
      method = "windowClick",
      at = {@At("HEAD")}
   )
   private void windowClick(int windowid, int slot, int button, int mode, EntityPlayer entityPlayer, CallbackInfoReturnable ci) {
      EventBus.getInstance().call(new EventClickSlot(windowid, slot, button, mode, entityPlayer));
   }
}
