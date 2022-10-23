package xyz.Melody.injection.mixins.gui;

import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.rendering.EventRender2D;

@SideOnly(Side.CLIENT)
@Mixin({GuiSpectator.class})
public class MixinGuiSpectator {
   @Inject(
      method = "renderTooltip",
      at = {@At("RETURN")}
   )
   private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo ci) {
      EventBus.getInstance().call(new EventRender2D(partialTicks));
   }
}
