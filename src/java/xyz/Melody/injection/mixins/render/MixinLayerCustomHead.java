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
    public void doRenderLayer(EntityLivingBase entityLivingBase, float f, float f2, float f3, float f4, float f5, float f6, float f7, CallbackInfo callbackInfo) {
        NoArmorRender noArmorRender = (NoArmorRender)Client.instance.getModuleManager().getModuleByClass(NoArmorRender.class);
        MobTracker mobTracker = (MobTracker)Client.instance.getModuleManager().getModuleByClass(MobTracker.class);
        if (mobTracker.isEnabled() && mobTracker.checked != null && !mobTracker.checked.isEmpty() && mobTracker.checked.containsKey(entityLivingBase)) {
            callbackInfo.cancel();
        }
        if (noArmorRender.isEnabled() && entityLivingBase instanceof EntityPlayer && ((Boolean)noArmorRender.chead.getValue()).booleanValue()) {
            if (((Boolean)noArmorRender.selfOnly.getValue()).booleanValue()) {
                if (entityLivingBase == Minecraft.getMinecraft().thePlayer) {
                    callbackInfo.cancel();
                }
            } else {
                callbackInfo.cancel();
            }
        }
    }
}

