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
    private void postUpdateCameraAndRender(float f, long l2, CallbackInfo callbackInfo) {
        NotificationPublisher.publish(new ScaledResolution(this.mc));
    }

    @Inject(method="updateCameraAndRender", at={@At(value="INVOKE", target="Lnet/minecraft/client/shader/Framebuffer;bindFramebuffer(Z)V", shift=At.Shift.BEFORE)})
    public void updateCameraAndRender(float f, long l2, CallbackInfo callbackInfo) {
        ArrayList<ShaderGroup> arrayList = new ArrayList<ShaderGroup>();
        if (this.theShaderGroup != null && this.useShader) {
            arrayList.add(this.theShaderGroup);
        }
        MotionBlur motionBlur = (MotionBlur)Client.instance.getModuleManager().getModuleByClass(MotionBlur.class);
        ShaderGroup shaderGroup = motionBlur.getShader();
        if (motionBlur.isEnabled()) {
            if (shaderGroup != null) {
                arrayList.add(shaderGroup);
            }
            for (ShaderGroup shaderGroup2 : arrayList) {
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                shaderGroup2.loadShaderGroup(f);
                GlStateManager.popMatrix();
            }
        }
    }

    @Inject(method="updateShaderGroupSize", at={@At(value="RETURN")})
    public void updateShaderGroupSize(int n, int n2, CallbackInfo callbackInfo) {
        if (this.mc.theWorld != null) {
            ShaderGroup shaderGroup;
            MotionBlur motionBlur = (MotionBlur)Client.instance.getModuleManager().getModuleByClass(MotionBlur.class);
            if (OpenGlHelper.shadersSupported && (shaderGroup = motionBlur.getShader()) != null) {
                shaderGroup.createBindFramebuffers(n, n2);
            }
        }
    }

    @Inject(method="renderWorldPass", at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift=At.Shift.BEFORE)})
    private void renderWorldPass(int n, float f, long l2, CallbackInfo callbackInfo) {
        EventRender3D eventRender3D = new EventRender3D(f);
        EventBus.getInstance().call(eventRender3D);
    }

    @Inject(method="hurtCameraEffect", at={@At(value="HEAD")}, cancellable=true)
    private void injectHurtCameraEffect(CallbackInfo callbackInfo) {
        Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
        if (cam.isEnabled() && ((Boolean)cam.bht.getValue()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method="orientCamera", at={@At(value="INVOKE", target="Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D")}, cancellable=true)
    private void cameraClip(float f, CallbackInfo callbackInfo) {
        Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
        if (cam.isEnabled() && ((Boolean)cam.noClip.getValue()).booleanValue()) {
            float f2;
            callbackInfo.cancel();
            Entity entity = this.mc.getRenderViewEntity();
            float f3 = entity.getEyeHeight();
            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
                f3 = (float)((double)f3 + 1.0);
                GlStateManager.translate(0.0f, 0.3f, 0.0f);
                if (!this.mc.gameSettings.debugCamEnable) {
                    BlockPos blockPos = new BlockPos(entity);
                    IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos);
                    ForgeHooksClient.orientBedCamera(this.mc.theWorld, blockPos, iBlockState, entity);
                    GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f + 180.0f, 0.0f, -1.0f, 0.0f);
                    GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f, -1.0f, 0.0f, 0.0f);
                }
            } else if (this.mc.gameSettings.thirdPersonView > 0) {
                double d = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * f;
                if (this.mc.gameSettings.debugCamEnable) {
                    GlStateManager.translate(0.0f, 0.0f, (float)(-d));
                } else {
                    f2 = entity.rotationYaw;
                    float f4 = entity.rotationPitch;
                    if (this.mc.gameSettings.thirdPersonView == 2) {
                        f4 += 180.0f;
                    }
                    if (this.mc.gameSettings.thirdPersonView == 2) {
                        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                    }
                    GlStateManager.rotate(entity.rotationPitch - f4, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(entity.rotationYaw - f2, 0.0f, 1.0f, 0.0f);
                    GlStateManager.translate(0.0f, 0.0f, (float)(-d));
                    GlStateManager.rotate(f2 - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(f4 - entity.rotationPitch, 1.0f, 0.0f, 0.0f);
                }
            } else {
                GlStateManager.translate(0.0f, 0.0f, -0.1f);
            }
            if (!this.mc.gameSettings.debugCamEnable) {
                float f5 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f + 180.0f;
                float f6 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f;
                f2 = 0.0f;
                if (entity instanceof EntityAnimal) {
                    EntityAnimal entityAnimal = (EntityAnimal)entity;
                    f5 = entityAnimal.prevRotationYawHead + (entityAnimal.rotationYawHead - entityAnimal.prevRotationYawHead) * f + 180.0f;
                }
                Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, f);
                EntityViewRenderEvent.CameraSetup cameraSetup = new EntityViewRenderEvent.CameraSetup((EntityRenderer)((Object)this), entity, block, f, f5, f6, f2);
                MinecraftForge.EVENT_BUS.post(cameraSetup);
                GlStateManager.rotate(cameraSetup.roll, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(cameraSetup.pitch, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(cameraSetup.yaw, 0.0f, 1.0f, 0.0f);
            }
            GlStateManager.translate(0.0f, -f3, 0.0f);
            double d = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)f;
            double d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)f + (double)f3;
            double d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)f;
            this.cloudFog = this.mc.renderGlobal.hasCloudFog(d, d2, d3, f);
        }
    }

    @Inject(method="getMouseOver", at={@At(value="HEAD")}, cancellable=true)
    public void getMouseOver(float f, CallbackInfo callbackInfo) {
        Entity entity2 = this.mc.getRenderViewEntity();
        if (entity2 != null && this.mc.theWorld != null) {
            Object object;
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;
            Reach reach = (Reach)Client.instance.getModuleManager().getModuleByClass(Reach.class);
            double d = reach.isEnabled() ? (Double)reach.size.getValue() : (double)this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = entity2.rayTrace(reach.isEnabled() ? (Double)reach.size.getValue() : d, f);
            double d2 = d;
            Vec3 vec3 = entity2.getPositionEyes(f);
            boolean bl = false;
            if (this.mc.playerController.extendedReach()) {
                d = 6.0;
                d2 = 6.0;
            } else if (d > 3.0) {
                bl = true;
            }
            if (this.mc.objectMouseOver != null) {
                d2 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
            }
            if (reach.isEnabled() && (object = entity2.rayTrace(d2 = ((Double)reach.size.getValue()).doubleValue(), f)) != null) {
                d2 = ((MovingObjectPosition)object).hitVec.distanceTo(vec3);
            }
            object = entity2.getLook(f);
            Vec3 vec32 = vec3.addVector(((Vec3)object).xCoord * d, ((Vec3)object).yCoord * d, ((Vec3)object).zCoord * d);
            this.pointedEntity = null;
            Vec3 vec33 = null;
            float f2 = 1.0f;
            List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity2, entity2.getEntityBoundingBox().addCoord(((Vec3)object).xCoord * d, ((Vec3)object).yCoord * d, ((Vec3)object).zCoord * d).expand(f2, f2, f2), Predicates.and(EntitySelectors.NOT_SPECTATING, entity -> entity.canBeCollidedWith()));
            double d3 = d2;
            for (int i = 0; i < list.size(); ++i) {
                double d4;
                Entity entity3 = list.get(i);
                float f3 = entity3.getCollisionBorderSize();
                AxisAlignedBB axisAlignedBB = entity3.getEntityBoundingBox().expand(f3, f3, f3);
                MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(vec3, vec32);
                if (axisAlignedBB.isVecInside(vec3)) {
                    if (!(d3 >= 0.0)) continue;
                    this.pointedEntity = entity3;
                    vec33 = movingObjectPosition == null ? vec3 : movingObjectPosition.hitVec;
                    d3 = 0.0;
                    continue;
                }
                if (movingObjectPosition == null || !((d4 = vec3.distanceTo(movingObjectPosition.hitVec)) < d3) && d3 != 0.0) continue;
                if (entity3 == entity2.ridingEntity && !entity2.canRiderInteract()) {
                    if (d3 != 0.0) continue;
                    this.pointedEntity = entity3;
                    vec33 = movingObjectPosition.hitVec;
                    continue;
                }
                this.pointedEntity = entity3;
                vec33 = movingObjectPosition.hitVec;
                d3 = d4;
            }
            if (this.pointedEntity != null && bl) {
                double d5 = vec3.distanceTo(vec33);
                double d6 = reach.isEnabled() ? (Double)reach.size.getValue() : 3.0;
                if (d5 > d6) {
                    this.pointedEntity = null;
                    this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
                }
            }
            if (this.pointedEntity != null && (d3 < d2 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);
                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }
            this.mc.mcProfiler.endSection();
        }
        callbackInfo.cancel();
    }
}

