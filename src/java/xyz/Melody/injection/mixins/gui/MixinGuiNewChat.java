package xyz.Melody.injection.mixins.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.UISettings;

@Mixin({GuiNewChat.class})
public abstract class MixinGuiNewChat {
   @Shadow
   @Final
   private Minecraft mc;
   @Shadow
   private int scrollPos;
   @Shadow
   private boolean isScrolled;
   @Shadow
   private final List drawnChatLines = Lists.newArrayList();

   @Shadow
   public abstract int getLineCount();

   @Shadow
   public abstract boolean getChatOpen();

   @Shadow
   public abstract float getChatScale();

   @Shadow
   public abstract int getChatWidth();

   @Inject(
      method = "drawChat",
      at = {@At("HEAD")},
      cancellable = true
   )
   public void drawChat(int p_146230_1_, CallbackInfo ci) {
      if (this.mc.gameSettings.chatVisibility != EnumChatVisibility.HIDDEN) {
         int i = this.getLineCount();
         boolean flag = false;
         int j = 0;
         int k = this.drawnChatLines.size();
         float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
         if (k > 0) {
            if (this.getChatOpen()) {
               flag = true;
            }

            float f1 = this.getChatScale();
            int l = MathHelper.ceiling_float_int((float)this.getChatWidth() / f1);
            GlStateManager.pushMatrix();
            GlStateManager.translate(2.0F, 20.0F, 0.0F);
            GlStateManager.scale(f1, f1, 1.0F);

            int i1;
            int l2;
            int i2;
            for(i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
               ChatLine chatline = (ChatLine)this.drawnChatLines.get(i1 + this.scrollPos);
               if (chatline != null && ((l2 = p_146230_1_ - chatline.getUpdatedCounter()) < 200 || flag)) {
                  double d0 = (double)l2 / 200.0;
                  d0 = 1.0 - d0;
                  d0 *= 10.0;
                  d0 = MathHelper.clamp_double(d0, 0.0, 1.0);
                  d0 *= d0;
                  int l1 = (int)(255.0 * d0);
                  if (flag) {
                     l1 = 255;
                  }

                  l1 = (int)((float)l1 * f);
                  ++j;
                  if (l1 > 3) {
                     i2 = 0;
                     int j2 = -i1 * 9;
                     if (UISettings.chatBackground) {
                        GuiNewChat.drawRect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
                     }

                     String s = chatline.getChatComponent().getFormattedText();
                     GlStateManager.enableBlend();
                     if (UISettings.chatTextShadow) {
                        this.mc.fontRendererObj.drawStringWithShadow(s, (float)i2, (float)(j2 - 8), 16777215 + (l1 << 24));
                     } else {
                        this.mc.fontRendererObj.drawString(s, i2, j2 - 8, 16777215 + (l1 << 24));
                     }

                     GlStateManager.disableAlpha();
                     GlStateManager.disableBlend();
                  }
               }
            }

            if (flag) {
               i1 = this.mc.fontRendererObj.FONT_HEIGHT;
               GlStateManager.translate(-3.0F, 0.0F, 0.0F);
               l2 = k * i1 + k;
               int i3 = j * i1 + j;
               int j3 = this.scrollPos * i3 / k;
               int k1 = i3 * i3 / l2;
               if (l2 != i3) {
                  int k3 = j3 > 0 ? true : true;
                  i2 = this.isScrolled ? 13382451 : 3355562;
               }
            }

            GlStateManager.popMatrix();
         }
      }

      ci.cancel();
   }
}
