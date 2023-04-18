/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.rendering.EventRenderEntityModel;
import xyz.Melody.injection.mixins.render.MixinRender;

@Mixin(value={RendererLivingEntity.class})
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase>
extends MixinRender<T> {
    @Shadow
    protected ModelBase mainModel;
    @Shadow
    protected boolean renderOutlines;
    @Shadow
    private static final Logger logger = LogManager.getLogger();

    @Shadow
    protected abstract float getSwingProgress(T var1, float var2);

    @Shadow
    protected abstract float interpolateRotation(float var1, float var2, float var3);

    @Shadow
    protected abstract void renderLivingAt(T var1, double var2, double var4, double var6);

    @Shadow
    protected abstract float handleRotationFloat(T var1, float var2);

    @Shadow
    protected abstract void rotateCorpse(T var1, float var2, float var3, float var4);

    @Shadow
    protected abstract void preRenderCallback(T var1, float var2);

    @Shadow
    protected abstract void renderModel(T var1, float var2, float var3, float var4, float var5, float var6, float var7);

    @Shadow
    protected abstract void renderLayers(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

    @Shadow
    protected abstract boolean setScoreTeamColor(T var1);

    @Shadow
    protected abstract void unsetScoreTeamColor();

    @Shadow
    protected abstract boolean setDoRenderBrightness(T var1, float var2);

    @Shadow
    protected abstract void unsetBrightness();

    @Inject(method="renderModel", at={@At(value="HEAD")}, cancellable=true)
    private void renderModel(EntityLivingBase entityLivingBase, float f, float f2, float f3, float f4, float f5, float f6, CallbackInfo callbackInfo) {
        EventRenderEntityModel eventRenderEntityModel = new EventRenderEntityModel(entityLivingBase, f, f2, f3, f4, f5, f6, this.mainModel);
        EventBus.getInstance().call(eventRenderEntityModel);
        if (eventRenderEntityModel.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Override
    @Overwrite
    public void doRender(T t, double d, double d2, double d3, float f, float f2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(t, f2);
        this.mainModel.isRiding = ((Entity)t).isRiding();
        if (t.shouldRiderSit()) {
            this.mainModel.isRiding = ((Entity)t).isRiding() && ((EntityLivingBase)t).ridingEntity != null && ((EntityLivingBase)t).ridingEntity.shouldRiderSit();
        }
        this.mainModel.isChild = ((EntityLivingBase)t).isChild();
        try {
            float f3;
            float f4 = this.interpolateRotation(((EntityLivingBase)t).prevRenderYawOffset, ((EntityLivingBase)t).renderYawOffset, f2);
            float f5 = this.interpolateRotation(((EntityLivingBase)t).prevRotationYawHead, ((EntityLivingBase)t).rotationYawHead, f2);
            float f6 = f5 - f4;
            if (this.mainModel.isRiding && ((EntityLivingBase)t).ridingEntity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase)((EntityLivingBase)t).ridingEntity;
                f4 = this.interpolateRotation(entityLivingBase.prevRenderYawOffset, entityLivingBase.renderYawOffset, f2);
                f6 = f5 - f4;
                f3 = MathHelper.wrapAngleTo180_float(f6);
                if (f3 < -85.0f) {
                    f3 = -85.0f;
                }
                if (f3 >= 85.0f) {
                    f3 = 85.0f;
                }
                f4 = f5 - f3;
                if (f3 * f3 > 2500.0f) {
                    f4 += f3 * 0.2f;
                }
            }
            float f7 = t == Minecraft.getMinecraft().thePlayer ? Client.instance.prevRotationPitchHead + (Client.instance.rotationPitchHead - Client.instance.prevRotationPitchHead) * f2 : ((EntityLivingBase)t).prevRotationPitch + (((EntityLivingBase)t).rotationPitch - ((EntityLivingBase)t).prevRotationPitch) * f2;
            this.renderLivingAt(t, d, d2, d3);
            f3 = this.handleRotationFloat(t, f2);
            this.rotateCorpse(t, f3, f4, f2);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0f, -1.0f, 1.0f);
            this.preRenderCallback(t, f2);
            GlStateManager.translate(0.0f, -1.5078125f, 0.0f);
            float f8 = ((EntityLivingBase)t).prevLimbSwingAmount + (((EntityLivingBase)t).limbSwingAmount - ((EntityLivingBase)t).prevLimbSwingAmount) * f2;
            float f9 = ((EntityLivingBase)t).limbSwing - ((EntityLivingBase)t).limbSwingAmount * (1.0f - f2);
            if (((EntityLivingBase)t).isChild()) {
                f9 *= 3.0f;
            }
            if (f8 > 1.0f) {
                f8 = 1.0f;
            }
            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations((EntityLivingBase)t, f9, f8, f2);
            this.mainModel.setRotationAngles(f9, f8, f3, f6, f7, 0.0625f, (Entity)t);
            this.renderModel(t, f9, f8, f3, f6, f7, 0.0625f);
            if (this.renderOutlines) {
                boolean bl = this.setScoreTeamColor(t);
                this.renderModel(t, f9, f8, f3, f6, f7, 0.0625f);
                if (bl) {
                    this.unsetScoreTeamColor();
                }
            } else {
                boolean bl = this.setDoRenderBrightness(t, f2);
                this.renderModel(t, f9, f8, f3, f6, f7, 0.0625f);
                if (bl) {
                    this.unsetBrightness();
                }
                GlStateManager.depthMask(true);
                if (!(t instanceof EntityPlayer) || !((EntityPlayer)t).isSpectator()) {
                    this.renderLayers(t, f9, f8, f2, f3, f6, f7, 0.0625f);
                }
            }
            GlStateManager.disableRescaleNormal();
        }
        catch (Exception exception) {
            logger.error("Couldn't render entity", (Throwable)exception);
        }
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        if (!this.renderOutlines) {
            super.doRender(t, d, d2, d3, f, f2);
        }
    }
}

