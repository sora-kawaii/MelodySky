/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.module.modules.QOL.MobTracker;
import xyz.Melody.module.modules.render.NoArmorRender;

@Mixin(value={LayerArmorBase.class})
public abstract class MixinLayerArmorBase<T extends ModelBase>
implements LayerRenderer<EntityLivingBase> {
    @Shadow
    public abstract void func_177182_a(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9);

    @Inject(method="doRenderLayer", at={@At(value="HEAD")}, cancellable=true)
    public void doRenderLayer(EntityLivingBase entityLivingBase, float f, float f2, float f3, float f4, float f5, float f6, float f7, CallbackInfo callbackInfo) {
        NoArmorRender noArmorRender = (NoArmorRender)Client.instance.getModuleManager().getModuleByClass(NoArmorRender.class);
        MobTracker mobTracker = (MobTracker)Client.instance.getModuleManager().getModuleByClass(MobTracker.class);
        if (mobTracker.isEnabled() && mobTracker.checked != null && !mobTracker.checked.isEmpty() && mobTracker.checked.containsKey(entityLivingBase)) {
            callbackInfo.cancel();
        }
        if (noArmorRender.isEnabled() && entityLivingBase instanceof EntityPlayer && ((Boolean)noArmorRender.armor.getValue()).booleanValue()) {
            if (((Boolean)noArmorRender.selfOnly.getValue()).booleanValue()) {
                if (entityLivingBase == Minecraft.func_71410_x().field_71439_g) {
                    callbackInfo.cancel();
                }
            } else {
                callbackInfo.cancel();
            }
        }
    }

    @Overwrite
    public ItemStack func_177176_a(EntityLivingBase entityLivingBase, int n) {
        NoArmorRender noArmorRender = (NoArmorRender)Client.instance.getModuleManager().getModuleByClass(NoArmorRender.class);
        MobTracker mobTracker = (MobTracker)Client.instance.getModuleManager().getModuleByClass(MobTracker.class);
        if (mobTracker.isEnabled() && mobTracker.checked != null && !mobTracker.checked.isEmpty() && mobTracker.checked.containsKey(entityLivingBase)) {
            return null;
        }
        if (noArmorRender.isEnabled() && ((Boolean)noArmorRender.armor.getValue()).booleanValue()) {
            if (((Boolean)noArmorRender.selfOnly.getValue()).booleanValue()) {
                if (entityLivingBase == Minecraft.func_71410_x().field_71439_g) {
                    return null;
                }
                return entityLivingBase.func_82169_q(n - 1);
            }
            return null;
        }
        return entityLivingBase.func_82169_q(n - 1);
    }
}

