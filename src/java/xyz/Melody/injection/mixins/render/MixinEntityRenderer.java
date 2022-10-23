package xyz.Melody.injection.mixins.render;

import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.module.balance.Reach;
import xyz.Melody.module.modules.render.Cam;

@SideOnly(Side.CLIENT)
@Mixin({EntityRenderer.class})
public abstract class MixinEntityRenderer {
   @Shadow
   private Entity pointedEntity;
   @Shadow
   private Minecraft mc;
   @Shadow
   private float thirdPersonDistanceTemp;
   @Shadow
   private float thirdPersonDistance;
   @Shadow
   private boolean cloudFog;

   @Shadow
   public abstract void loadShader(ResourceLocation var1);

   @Inject(
      method = "renderWorldPass",
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z",
   shift = Shift.BEFORE
)}
   )
   private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
      EventRender3D e = new EventRender3D(partialTicks);
      EventBus.getInstance().call(e);
   }

   @Inject(
      method = "hurtCameraEffect",
      at = {@At("HEAD")},
      cancellable = true
   )
   private void injectHurtCameraEffect(CallbackInfo callbackInfo) {
      Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
      if (cam.isEnabled() && (Boolean)cam.bht.getValue()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = "orientCamera",
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/Vec3;distanceTo(Lnet/minecraft/util/Vec3;)D"
)},
      cancellable = true
   )
   private void cameraClip(float partialTicks, CallbackInfo callbackInfo) {
      Cam cam = (Cam)Client.instance.getModuleManager().getModuleByClass(Cam.class);
      if (cam.isEnabled() && (Boolean)cam.noClip.getValue()) {
         callbackInfo.cancel();
         Entity entity = this.mc.getRenderViewEntity();
         float f = entity.getEyeHeight();
         double d3;
         float f1;
         if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
            f = (float)((double)f + 1.0);
            GlStateManager.translate(0.0F, 0.3F, 0.0F);
            if (!this.mc.gameSettings.debugCamEnable) {
               BlockPos blockpos = new BlockPos(entity);
               IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
               ForgeHooksClient.orientBedCamera(this.mc.theWorld, blockpos, iblockstate, entity);
               GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
               GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
            }
         } else if (this.mc.gameSettings.thirdPersonView > 0) {
            d3 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks);
            if (this.mc.gameSettings.debugCamEnable) {
               GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
            } else {
               f1 = entity.rotationYaw;
               float f2 = entity.rotationPitch;
               if (this.mc.gameSettings.thirdPersonView == 2) {
                  f2 += 180.0F;
               }

               if (this.mc.gameSettings.thirdPersonView == 2) {
                  GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
               }

               GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
               GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
               GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
               GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
               GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
         } else {
            GlStateManager.translate(0.0F, 0.0F, -0.1F);
         }

         if (!this.mc.gameSettings.debugCamEnable) {
            float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F;
            float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            f1 = 0.0F;
            if (entity instanceof EntityAnimal) {
               EntityAnimal entityanimal = (EntityAnimal)entity;
               yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F;
            }

            Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
            EntityViewRenderEvent.CameraSetup event = new EntityViewRenderEvent.CameraSetup((EntityRenderer)this, entity, block, (double)partialTicks, yaw, pitch, f1);
            MinecraftForge.EVENT_BUS.post(event);
            GlStateManager.rotate(event.roll, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(event.pitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(event.yaw, 0.0F, 1.0F, 0.0F);
         }

         GlStateManager.translate(0.0F, -f, 0.0F);
         d3 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
         double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
         double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
         this.cloudFog = this.mc.renderGlobal.hasCloudFog(d3, d1, d2, partialTicks);
      }

   }

   @Overwrite
   public void getMouseOver(float p_getMouseOver_1_) {
      Entity entity = this.mc.getRenderViewEntity();
      if (entity != null && this.mc.theWorld != null) {
         this.mc.mcProfiler.startSection("pick");
         this.mc.pointedEntity = null;
         Reach reach = (Reach)Client.instance.getModuleManager().getModuleByClass(Reach.class);
         double d0 = reach.isEnabled() ? (Double)reach.size.getValue() : (double)this.mc.playerController.getBlockReachDistance();
         this.mc.objectMouseOver = entity.rayTrace(reach.isEnabled() ? (Double)reach.size.getValue() : d0, p_getMouseOver_1_);
         double d1 = d0;
         Vec3 vec3 = entity.getPositionEyes(p_getMouseOver_1_);
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

         if (reach.isEnabled()) {
            d1 = (Double)reach.size.getValue();
            MovingObjectPosition movingObjectPosition = entity.rayTrace(d1, p_getMouseOver_1_);
            if (movingObjectPosition != null) {
               d1 = movingObjectPosition.hitVec.distanceTo(vec3);
            }
         }

         Vec3 vec31 = entity.getLook(p_getMouseOver_1_);
         Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
         this.pointedEntity = null;
         Vec3 vec33 = null;
         float f = 1.0F;
         List list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, (p_apply_1_) -> {
            return p_apply_1_.canBeCollidedWith();
         }));
         double d2 = d1;

         for(int j = 0; j < list.size(); ++j) {
            Entity entity1 = (Entity)list.get(j);
            float f1 = entity1.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
            if (axisalignedbb.isVecInside(vec3)) {
               if (d2 >= 0.0) {
                  this.pointedEntity = entity1;
                  vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                  d2 = 0.0;
               }
            } else if (movingobjectposition != null) {
               double d3 = vec3.distanceTo(movingobjectposition.hitVec);
               if (d3 < d2 || d2 == 0.0) {
                  if (entity1 == entity.ridingEntity && !entity.canRiderInteract()) {
                     if (d2 == 0.0) {
                        this.pointedEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                     }
                  } else {
                     this.pointedEntity = entity1;
                     vec33 = movingobjectposition.hitVec;
                     d2 = d3;
                  }
               }
            }
         }

         if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > (reach.isEnabled() ? (Double)reach.size.getValue() : 3.0)) {
            this.pointedEntity = null;
            this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectType.MISS, vec33, (EnumFacing)null, new BlockPos(vec33));
         }

         if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
            this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);
            if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
               this.mc.pointedEntity = this.pointedEntity;
            }
         }

         this.mc.mcProfiler.endSection();
      }

   }
}
