/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.item;

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

@Mixin(value={ItemRenderer.class})
public abstract class MixinItemRenderer {
    @Shadow
    private float field_78454_c;
    @Shadow
    private float field_78451_d;
    @Shadow
    private ItemStack field_78453_b;

    @Inject(method="renderItemInFirstPerson", at={@At(value="HEAD")}, cancellable=true)
    public void renderItemInFirstPerson(float f, CallbackInfo callbackInfo) {
        boolean bl = Client.instance.getModuleManager().getModuleByClass(OldAnimations.class).isEnabled();
        if (bl && this.field_78453_b != null) {
            ItemRenderer itemRenderer = (ItemRenderer)((Object)this);
            float f2 = this.field_78451_d + (this.field_78454_c - this.field_78451_d) * f;
            if (AnimationHandler.getInstance().renderItemInFirstPerson(itemRenderer, this.field_78453_b, f2, f)) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method="renderFireInFirstPerson", at={@At(value="HEAD")}, cancellable=true)
    private void renderFireInFirstPerson(CallbackInfo callbackInfo) {
        Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
        if (cam.isEnabled() && ((Boolean)cam.noFire.getValue()).booleanValue()) {
            callbackInfo.cancel();
        }
    }
}

