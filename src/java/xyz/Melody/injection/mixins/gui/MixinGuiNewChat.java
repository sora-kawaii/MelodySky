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
    public void drawChat(int n, CallbackInfo callbackInfo) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int n2 = this.getLineCount();
            boolean bl = false;
            int n3 = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (n3 > 0) {
                if (this.getChatOpen()) {
                    bl = true;
                }
                float f2 = this.getChatScale();
                int n4 = MathHelper.ceiling_float_int((float)this.getChatWidth() / f2);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0f, 20.0f, 0.0f);
                GlStateManager.scale(f2, f2, 1.0f);
                for (int i = 0; i + this.scrollPos < this.drawnChatLines.size() && i < n2; ++i) {
                    int n5;
                    ChatLine chatLine = this.drawnChatLines.get(i + this.scrollPos);
                    if (chatLine == null || (n5 = n - chatLine.getUpdatedCounter()) >= 200 && !bl) continue;
                    double d = (double)n5 / 200.0;
                    d = 1.0 - d;
                    d *= 10.0;
                    d = MathHelper.clamp_double(d, 0.0, 1.0);
                    d *= d;
                    int n6 = (int)(255.0 * d);
                    if (bl) {
                        n6 = 255;
                    }
                    if ((n6 = (int)((float)n6 * f)) <= 3) continue;
                    int n7 = 0;
                    int n8 = -i * 9;
                    if (UISettings.chatBackground) {
                        GuiNewChat.drawRect(n7, n8 - 9, n7 + n4 + 4, n8, n6 / 2 << 24);
                    }
                    String string = chatLine.getChatComponent().getFormattedText();
                    GlStateManager.enableBlend();
                    if (UISettings.chatTextShadow) {
                        this.mc.fontRendererObj.drawStringWithShadow(string, n7, n8 - 8, 0xFFFFFF + (n6 << 24));
                    } else {
                        this.mc.fontRendererObj.drawString(string, n7, n8 - 8, 0xFFFFFF + (n6 << 24));
                    }
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                if (bl) {
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                }
                GlStateManager.popMatrix();
            }
        }
        callbackInfo.cancel();
    }
}

