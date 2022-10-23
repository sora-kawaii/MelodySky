package xyz.Melody.injection.mixins.entity;

import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.Melody.Client;
import xyz.Melody.module.balance.HitBox;

@Mixin({Entity.class})
public abstract class MixinEntity {
   @Shadow
   public double posX;
   @Shadow
   public double posY;
   @Shadow
   public double posZ;
   @Shadow
   public float height;
   @Shadow
   public float rotationPitch;
   @Shadow
   public float rotationYaw;
   @Shadow
   public Entity ridingEntity;
   @Shadow
   public double motionX;
   @Shadow
   public double motionY;
   @Shadow
   public double motionZ;
   @Shadow
   public boolean onGround;
   @Shadow
   public boolean noClip;
   @Shadow
   public World worldObj;
   @Shadow
   protected boolean isInWeb;
   @Shadow
   public float stepHeight;
   @Shadow
   public boolean isCollidedHorizontally;
   @Shadow
   public boolean isCollidedVertically;
   @Shadow
   public boolean isCollided;
   @Shadow
   public float distanceWalkedModified;
   @Shadow
   public float distanceWalkedOnStepModified;
   @Shadow
   protected Random rand;
   @Shadow
   public int fireResistance;
   @Shadow
   private int nextStepDistance;
   @Shadow
   private int fire;
   @Shadow
   public int ticksExisted;
   @Shadow
   public boolean inPortal;

   @Shadow
   public abstract boolean hitByEntity(Entity var1);

   @Shadow
   public abstract UUID getUniqueID();

   @Shadow
   public abstract AxisAlignedBB getEntityBoundingBox();

   @Shadow
   public abstract boolean isInWater();

   @Shadow
   public abstract boolean isSprinting();

   @Shadow
   public abstract void setFire(int var1);

   @Shadow
   protected abstract void dealFireDamage(int var1);

   @Shadow
   public abstract boolean isWet();

   @Shadow
   public abstract void addEntityCrashInfo(CrashReportCategory var1);

   @Shadow
   protected abstract void doBlockCollisions();

   @Shadow
   protected abstract void playStepSound(BlockPos var1, Block var2);

   @Shadow
   public abstract void setEntityBoundingBox(AxisAlignedBB var1);

   @Shadow
   public abstract boolean isSneaking();

   public int getNextStepDistance() {
      return this.nextStepDistance;
   }

   public void setNextStepDistance(int nextStepDistance) {
      this.nextStepDistance = nextStepDistance;
   }

   public int getFire() {
      return this.fire;
   }

   @Shadow
   public void moveEntity(double x, double y, double z) {
   }

   @Inject(
      method = "getCollisionBorderSize",
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getCollisionBorderSize(CallbackInfoReturnable callbackInfoReturnable) {
      HitBox hitBox = (HitBox)Client.instance.getModuleManager().getModuleByClass(HitBox.class);
      if (hitBox.isEnabled()) {
         callbackInfoReturnable.setReturnValue(((Double)hitBox.size.getValue()).floatValue());
      }

   }
}
