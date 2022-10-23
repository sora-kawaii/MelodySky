package xyz.Melody.injection.mixins.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.container.DrawSlotEvent;

@Mixin({GuiContainer.class})
public abstract class MixinGuiContainer extends MixinGuiScreen {
   @Shadow
   public Container inventorySlots;

   @Inject(
      method = "drawSlot",
      at = {@At("HEAD")},
      cancellable = true
   )
   private void beforeDrawSlot(Slot slot, CallbackInfo callbackInfo) {
      DrawSlotEvent event = new DrawSlotEvent(this.inventorySlots, slot);
      EventBus.getInstance().call(event);
      if (event.isCancelled()) {
         callbackInfo.cancel();
      }

   }
}
