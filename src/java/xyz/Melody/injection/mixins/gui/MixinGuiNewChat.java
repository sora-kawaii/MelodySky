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
    private Minecraft field_146247_f;
    @Shadow
    private int field_146250_j;
    @Shadow
    private boolean field_146251_k;
    @Shadow
    private final List<ChatLine> field_146253_i = Lists.newArrayList();

    @Shadow
    public abstract int func_146232_i();

    @Shadow
    public abstract boolean func_146241_e();

    @Shadow
    public abstract float func_146244_h();

    @Shadow
    public abstract int func_146228_f();

    @Inject(method="drawChat", at={@At(value="HEAD")}, cancellable=true)
    public void drawChat(int n, CallbackInfo callbackInfo) {
        if (this.field_146247_f.field_71474_y.field_74343_n != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int n2 = this.func_146232_i();
            boolean bl = false;
            int n3 = this.field_146253_i.size();
            float f = this.field_146247_f.field_71474_y.field_74357_r * 0.9f + 0.1f;
            if (n3 > 0) {
                if (this.func_146241_e()) {
                    bl = true;
                }
                float f2 = this.func_146244_h();
                int n4 = MathHelper.func_76123_f((float)((float)this.func_146228_f() / f2));
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b((float)2.0f, (float)20.0f, (float)0.0f);
                GlStateManager.func_179152_a((float)f2, (float)f2, (float)1.0f);
                for (int i = 0; i + this.field_146250_j < this.field_146253_i.size() && i < n2; ++i) {
                    int n5;
                    ChatLine chatLine = this.field_146253_i.get(i + this.field_146250_j);
                    if (chatLine == null || (n5 = n - chatLine.func_74540_b()) >= 200 && !bl) continue;
                    double d = (double)n5 / 200.0;
                    d = 1.0 - d;
                    d *= 10.0;
                    d = MathHelper.func_151237_a((double)d, (double)0.0, (double)1.0);
                    d *= d;
                    int n6 = (int)(255.0 * d);
                    if (bl) {
                        n6 = 255;
                    }
                    if ((n6 = (int)((float)n6 * f)) <= 3) continue;
                    int n7 = 0;
                    int n8 = -i * 9;
                    if (UISettings.chatBackground) {
                        GuiNewChat.func_73734_a((int)n7, (int)(n8 - 9), (int)(n7 + n4 + 4), (int)n8, (int)(n6 / 2 << 24));
                    }
                    String string = chatLine.func_151461_a().func_150254_d();
                    GlStateManager.func_179147_l();
                    if (UISettings.chatTextShadow) {
                        this.field_146247_f.field_71466_p.func_175063_a(string, n7, n8 - 8, 0xFFFFFF + (n6 << 24));
                    } else {
                        this.field_146247_f.field_71466_p.func_78276_b(string, n7, n8 - 8, 0xFFFFFF + (n6 << 24));
                    }
                    GlStateManager.func_179118_c();
                    GlStateManager.func_179084_k();
                }
                if (bl) {
                    GlStateManager.func_179109_b((float)-3.0f, (float)0.0f, (float)0.0f);
                }
                GlStateManager.func_179121_F();
            }
        }
        callbackInfo.cancel();
    }
}

