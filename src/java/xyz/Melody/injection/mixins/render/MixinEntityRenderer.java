/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.render;

import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.module.balance.Reach;
import xyz.Melody.module.modules.others.MotionBlur;
import xyz.Melody.module.modules.render.Cam;

@SideOnly(value=Side.CLIENT)
@Mixin(value={EntityRenderer.class})
public abstract class MixinEntityRenderer {
    @Shadow
    private Entity pointedEntity;
    @Shadow
    private ShaderGroup theShaderGroup;
    @Shadow
    private boolean useShader;
    @Shadow
    private Minecraft mc;
    @Shadow
    private float thirdPersonDistanceTemp;
    @Shadow
    private float thirdPersonDistance;
    @Shadow
    private boolean cloudFog;

    @Inject(method="updateCameraAndRender", at={@At(value="RETURN")})
    private void postUpdateCameraAndRender(float partialTicks, long nanoTime, CallbackInfo ci) {
        NotificationPublisher.publish(new ScaledResolution(this.mc));
    }

    @Inject(method="updateCameraAndRender", at={@At(value="INVOKE", target="Lnet/minecraft/client/shader/Framebuffer;bindFramebuffer(Z)V", shift=At.Shift.BEFORE)})
    public void updateCameraAndRender(float partialTicks, long nanoTime, CallbackInfo ci) {
        ArrayList<ShaderGroup> shaders = new ArrayList<ShaderGroup>();
        if (this.theShaderGroup != null && this.useShader) {
            shaders.add(this.theShaderGroup);
        }
        MotionBlur dick = (MotionBlur)Client.instance.getModuleManager().getModuleByClass(MotionBlur.class);
        ShaderGroup motionBlur = dick.getShader();
        if (dick.isEnabled()) {
            if (motionBlur != null) {
                shaders.add(motionBlur);
            }
            for (ShaderGroup shader : shaders) {
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                shader.loadShaderGroup(partialTicks);
                GlStateManager.popMatrix();
            }
        }
    }

    @Inject(method="updateShaderGroupSize", at={@At(value="RETURN")})
    public void updateShaderGroupSize(int width, int height, CallbackInfo ci) {
        if (this.mc.theWorld != null) {
            ShaderGroup motionBlur;
            MotionBlur dick = (MotionBlur)Client.instance.getModuleManager().getModuleByClass(MotionBlur.class);
            if (OpenGlHelper.shadersSupported && (motionBlur = dick.getShader()) != null) {
                motionBlur.createBindFramebuffers(width, height);
            }
        }
    }

    @Inject(method="renderWorldPass", at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift=At.Shift.BEFORE)})
    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        EventRender3D e = new EventRender3D(partialTicks);
        EventBus.getInstance().call(e);
    }

    @Inject(method="hurtCameraEffect", at={@At(value="HEAD")}, cancellable=true)
    private void injectHurtCameraEffect(CallbackInfo callbackInfo) {
        Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
        if (cam.isEnabled() && ((Boolean)cam.bht.getValue()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method="orientCamera", at={@At(value="INVOKE", target="Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D")}, cancellable=true)
    private void cameraClip(float partialTicks, CallbackInfo callbackInfo) {
        Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
        if (cam.isEnabled() && ((Boolean)cam.noClip.getValue()).booleanValue()) {
            callbackInfo.cancel();
            Entity entity = this.mc.getRenderViewEntity();
            float f = entity.getEyeHeight();
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
                f = (float)((double)f + 1.0);
                GlStateManager.translate(0.0f, 0.3f, 0.0f);
                if (!this.mc.gameSettings.debugCamEnable) {
                    BlockPos blockpos = new BlockPos(entity);
                    IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                    ForgeHooksClient.orientBedCamera(this.mc.theWorld, blockpos, iblockstate, entity);
                    GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f, 0.0f, -1.0f, 0.0f);
                    GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0f, 0.0f, 0.0f);
                }
            } else if (this.mc.gameSettings.thirdPersonView > 0) {
                double d3 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks;
                if (this.mc.gameSettings.debugCamEnable) {
                    GlStateManager.translate(0.0f, 0.0f, (float)(-d3));
                } else {
                    float f1 = entity.rotationYaw;
                    float f2 = entity.rotationPitch;
                    if (this.mc.gameSettings.thirdPersonView == 2) {
                        f2 += 180.0f;
                    }
                    if (this.mc.gameSettings.thirdPersonView == 2) {
                        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                    }
                    GlStateManager.rotate(entity.rotationPitch - f2, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(entity.rotationYaw - f1, 0.0f, 1.0f, 0.0f);
                    GlStateManager.translate(0.0f, 0.0f, (float)(-d3));
                    GlStateManager.rotate(f1 - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(f2 - entity.rotationPitch, 1.0f, 0.0f, 0.0f);
                }
            } else {
                GlStateManager.translate(0.0f, 0.0f, -0.1f);
            }
            if (!this.mc.gameSettings.debugCamEnable) {
                float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f;
                float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                float roll = 0.0f;
                if (entity instanceof EntityAnimal) {
                    EntityAnimal entityanimal = (EntityAnimal)entity;
                    yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0f;
                }
                Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
                EntityViewRenderEvent.CameraSetup event = new EntityViewRenderEvent.CameraSetup((EntityRenderer)((Object)this), entity, block, partialTicks, yaw, pitch, roll);
                MinecraftForge.EVENT_BUS.post(event);
                GlStateManager.rotate(event.roll, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(event.pitch, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(event.yaw, 0.0f, 1.0f, 0.0f);
            }
            GlStateManager.translate(0.0f, -f, 0.0f);
            double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
            double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
            double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
            this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
        }
    }

    @Inject(method="getMouseOver", at={@At(value="HEAD")}, cancellable=true)
    public void getMouseOver(float mouseOver, CallbackInfo ci) {
        Entity entity = this.mc.getRenderViewEntity();
        if (entity != null && this.mc.theWorld != null) {
            MovingObjectPosition movingObjectPosition;
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;
            Reach reach = (Reach)Client.instance.getModuleManager().getModuleByClass(Reach.class);
            double d0 = reach.isEnabled() ? (Double)reach.size.getValue() : (double)this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = entity.rayTrace(reach.isEnabled() ? (Double)reach.size.getValue() : d0, mouseOver);
            double d1 = d0;
            Vec3 vec3 = entity.getPositionEyes(mouseOver);
            boolean flag = false;
            if (this.mc.playerController.extendedReach()) {
                d0 = 6.0;
                d1 = 6.0;
            } else if (d0 > 3.0) {
                flag = true;
            }
            if (this.mc.objectMouseOver != null) {
                d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
            }
            if (reach.isEnabled() && (movingObjectPosition = entity.rayTrace(d1 = ((Double)reach.size.getValue()).doubleValue(), mouseOver)) != null) {
                d1 = movingObjectPosition.hitVec.distanceTo(vec3);
            }
            Vec3 vec31 = entity.getLook(mouseOver);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            this.pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0f;
            List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, p_apply_1_ -> p_apply_1_.canBeCollidedWith()));
            double d2 = d1;
            for (int j = 0; j < list.size(); ++j) {
                double d3;
                Entity entity1 = list.get(j);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (!(d2 >= 0.0)) continue;
                    this.pointedEntity = entity1;
                    vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                    d2 = 0.0;
                    continue;
                }
                if (movingobjectposition == null || !((d3 = vec3.distanceTo(movingobjectposition.hitVec)) < d2) && d2 != 0.0) continue;
                if (entity1 == entity.ridingEntity && !entity.canRiderInteract()) {
                    if (d2 != 0.0) continue;
                    this.pointedEntity = entity1;
                    vec33 = movingobjectposition.hitVec;
                    continue;
                }
                this.pointedEntity = entity1;
                vec33 = movingobjectposition.hitVec;
                d2 = d3;
            }
            if (this.pointedEntity != null && flag) {
                double d = vec3.distanceTo(vec33);
                double d3 = reach.isEnabled() ? (Double)reach.size.getValue() : 3.0;
                if (d > d3) {
                    this.pointedEntity = null;
                    this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
                }
            }
            if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);
                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }
            this.mc.mcProfiler.endSection();
        }
        ci.cancel();
    }
}

