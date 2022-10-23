package xyz.Melody.injection.mixins.client;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.rendering.EventDrawText;

@Mixin({FontRenderer.class})
public class MixinFontRenderer {
   @ModifyVariable(
      method = "renderString",
      at = @At("HEAD"),
      ordinal = 0
   )
   private String renderString(String string) {
      if (string == null) {
         return string;
      } else {
         EventDrawText textEvent = new EventDrawText(string);
         EventBus.getInstance().call(textEvent);
         return textEvent.getText();
      }
   }
}
