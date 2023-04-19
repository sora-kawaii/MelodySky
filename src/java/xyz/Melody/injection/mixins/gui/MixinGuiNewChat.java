/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.ClientLib.UISettings;

@Mixin(value={GuiNewChat.class})
public abstract class MixinGuiNewChat {
    @Shadow
    private Minecraft mc;
    @Shadow
    private int scrollPos;
    @Shadow
    private boolean isScrolled;
    @Shadow
    private final List<ChatLine> drawnChatLines = Lists.newArrayList();

    @Shadow
    public abstract int getLineCount();

    @Shadow
    public abstract boolean getChatOpen();

    @Shadow
    public abstract float getChatScale();

    @Shadow
    public abstract int getChatWidth();

    @Inject(method="drawChat", at={@At(value="HEAD")}, cancellable=true)
    public void drawChat(int p_146230_1_, CallbackInfo ci) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            boolean flag = false;
            int k = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (k > 0) {
                if (this.getChatOpen()) {
                    flag = true;
                }
                float f1 = this.getChatScale();
                int n = MathHelper.ceiling_float_int((float)this.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0f, 20.0f, 0.0f);
                GlStateManager.scale(f1, f1, 1.0f);
                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
                    int j1;
                    ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);
                    if (chatline == null || (j1 = p_146230_1_ - chatline.getUpdatedCounter()) >= 200 && !flag) continue;
                    double d0 = (double)j1 / 200.0;
                    d0 = 1.0 - d0;
                    d0 *= 10.0;
                    d0 = MathHelper.clamp_double(d0, 0.0, 1.0);
                    d0 *= d0;
                    int l1 = (int)(255.0 * d0);
                    if (flag) {
                        l1 = 255;
                    }
                    if ((l1 = (int)((float)l1 * f)) <= 3) continue;
                    int i2 = 0;
                    int j2 = -i1 * 9;
                    if (UISettings.chatBackground) {
                        GuiNewChat.drawRect(i2, j2 - 9, i2 + n + 4, j2, l1 / 2 << 24);
                    }
                    String s = chatline.getChatComponent().getFormattedText();
                    GlStateManager.enableBlend();
                    if (UISettings.chatTextShadow) {
                        this.mc.fontRendererObj.drawStringWithShadow(s, i2, j2 - 8, 0xFFFFFF + (l1 << 24));
                    } else {
                        this.mc.fontRendererObj.drawString(s, i2, j2 - 8, 0xFFFFFF + (l1 << 24));
                    }
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                if (flag) {
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                }
                GlStateManager.popMatrix();
            }
        }
        ci.cancel();
    }
}

