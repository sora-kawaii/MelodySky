/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.module.modules.QOL.MobTracker;
import xyz.Melody.module.modules.render.NoArmorRender;

@Mixin(value={LayerCustomHead.class})
public abstract class MixinLayerCustomHead
implements LayerRenderer<EntityLivingBase> {
    @Inject(method="doRenderLayer", at={@At(value="HEAD")}, cancellable=true)
    public void doRenderLayer(EntityLivingBase entity, float arg1, float arg2, float arg3, float arg4, float arg5, float arg6, float arg7, CallbackInfo ci) {
        NoArmorRender nar = (NoArmorRender)Client.instance.getModuleManager().getModuleByClass(NoArmorRender.class);
        MobTracker mt = (MobTracker)Client.instance.getModuleManager().getModuleByClass(MobTracker.class);
        if (mt.isEnabled() && mt.checked != null && !mt.checked.isEmpty() && mt.checked.containsKey(entity)) {
            ci.cancel();
        }
        if (nar.isEnabled() && entity instanceof EntityPlayer && ((Boolean)nar.chead.getValue()).booleanValue()) {
            if (((Boolean)nar.selfOnly.getValue()).booleanValue()) {
                if (entity == Minecraft.getMinecraft().thePlayer) {
                    ci.cancel();
                }
            } else {
                ci.cancel();
            }
        }
    }
}

