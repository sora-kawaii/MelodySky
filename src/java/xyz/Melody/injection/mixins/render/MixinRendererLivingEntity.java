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
    private void renderModel(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo callbackInfo) {
        EventRenderEntityModel event = new EventRenderEntityModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, this.mainModel);
        EventBus.getInstance().call(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Override
    @Overwrite
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
        this.mainModel.isRiding = ((Entity)entity).isRiding();
        if (entity.shouldRiderSit()) {
            this.mainModel.isRiding = ((Entity)entity).isRiding() && ((EntityLivingBase)entity).ridingEntity != null && ((EntityLivingBase)entity).ridingEntity.shouldRiderSit();
        }
        this.mainModel.isChild = ((EntityLivingBase)entity).isChild();
        try {
            float f = this.interpolateRotation(((EntityLivingBase)entity).prevRenderYawOffset, ((EntityLivingBase)entity).renderYawOffset, partialTicks);
            float f1 = this.interpolateRotation(((EntityLivingBase)entity).prevRotationYawHead, ((EntityLivingBase)entity).rotationYawHead, partialTicks);
            float f2 = f1 - f;
            if (this.mainModel.isRiding && ((EntityLivingBase)entity).ridingEntity instanceof EntityLivingBase) {
                EntityLivingBase entitylivingbase = (EntityLivingBase)((EntityLivingBase)entity).ridingEntity;
                f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                f2 = f1 - f;
                float f3 = MathHelper.wrapAngleTo180_float(f2);
                if (f3 < -85.0f) {
                    f3 = -85.0f;
                }
                if (f3 >= 85.0f) {
                    f3 = 85.0f;
                }
                f = f1 - f3;
                if (f3 * f3 > 2500.0f) {
                    f += f3 * 0.2f;
                }
            }
            float f8 = entity == Minecraft.getMinecraft().thePlayer ? Client.instance.prevRotationPitchHead + (Client.instance.rotationPitchHead - Client.instance.prevRotationPitchHead) * partialTicks : ((EntityLivingBase)entity).prevRotationPitch + (((EntityLivingBase)entity).rotationPitch - ((EntityLivingBase)entity).prevRotationPitch) * partialTicks;
            this.renderLivingAt(entity, x, y, z);
            float f7 = this.handleRotationFloat(entity, partialTicks);
            this.rotateCorpse(entity, f7, f, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0f, -1.0f, 1.0f);
            this.preRenderCallback(entity, partialTicks);
            GlStateManager.translate(0.0f, -1.5078125f, 0.0f);
            float f5 = ((EntityLivingBase)entity).prevLimbSwingAmount + (((EntityLivingBase)entity).limbSwingAmount - ((EntityLivingBase)entity).prevLimbSwingAmount) * partialTicks;
            float f6 = ((EntityLivingBase)entity).limbSwing - ((EntityLivingBase)entity).limbSwingAmount * (1.0f - partialTicks);
            if (((EntityLivingBase)entity).isChild()) {
                f6 *= 3.0f;
            }
            if (f5 > 1.0f) {
                f5 = 1.0f;
            }
            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations((EntityLivingBase)entity, f6, f5, partialTicks);
            this.mainModel.setRotationAngles(f6, f5, f7, f2, f8, 0.0625f, (Entity)entity);
            this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625f);
            if (this.renderOutlines) {
                boolean flag1 = this.setScoreTeamColor(entity);
                this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625f);
                if (flag1) {
                    this.unsetScoreTeamColor();
                }
            } else {
                boolean flag = this.setDoRenderBrightness(entity, partialTicks);
                this.renderModel(entity, f6, f5, f7, f2, f8, 0.0625f);
                if (flag) {
                    this.unsetBrightness();
                }
                GlStateManager.depthMask(true);
                if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                    this.renderLayers(entity, f6, f5, partialTicks, f7, f2, f8, 0.0625f);
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
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    @Overwrite
    public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
        this.doRender((T)((EntityLivingBase)entity), d, d2, d3, f, f2);
    }
}

