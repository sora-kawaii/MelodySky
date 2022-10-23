package xyz.Melody.Utils.math;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import xyz.Melody.Utils.Helper;

public class RotationUtil {
   private static Minecraft mc = Minecraft.getMinecraft();

   public static float pitch() {
      return Helper.mc.thePlayer.rotationPitch;
   }

   public static void pitch(float pitch) {
      Helper.mc.thePlayer.rotationPitch = pitch;
   }

   public static float yaw() {
      return Helper.mc.thePlayer.rotationYaw;
   }

   public static void yaw(float yaw) {
      Helper.mc.thePlayer.rotationYaw = yaw;
   }

   public static double isInFov(float yaw, float pitch, double x, double y, double z) {
      Vec3 var7 = new Vec3((double)yaw, (double)pitch, 0.0);
      float[] var8 = getAngleBetweenVecs(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(x, y, z));
      double var9 = MathHelper.wrapAngleTo180_double(var7.xCoord - (double)var8[0]);
      return Math.abs(var9) * 2.0;
   }

   public static float[] getAngleBetweenVecs(Vec3 var0, Vec3 var1) {
      double var2 = var1.xCoord - var0.xCoord;
      double var3 = var1.yCoord - var0.yCoord;
      double var4 = var1.zCoord - var0.zCoord;
      double var5 = Math.sqrt(var2 * var2 + var4 * var4);
      float var6 = (float)(Math.atan2(var4, var2) * 180.0 / Math.PI) - 90.0F;
      float var7 = (float)(-(Math.atan2(var3, var5) * 180.0 / Math.PI));
      return new float[]{var6, var7};
   }

   public static float[] getRotationFromPosition(double x, double z, double y) {
      double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
      double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
      double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
      double dist = (double)MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
      float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0F;
      float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
      return new float[]{yaw, pitch};
   }

   public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
      double var4 = target.posX - Helper.mc.thePlayer.posX;
      double var8 = target.posZ - Helper.mc.thePlayer.posZ;
      double var6;
      if (target instanceof EntityLivingBase) {
         EntityLivingBase var10 = (EntityLivingBase)target;
         var6 = var10.posY + (double)var10.getEyeHeight() - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
      } else {
         var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
      }

      new Random();
      double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
      float var12 = (float)(Math.atan2(var8, var4) * 180.0 / Math.PI) - 90.0F;
      float var13 = (float)(-Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0.0), var14) * 180.0 / Math.PI);
      float pitch = changeRotation(Helper.mc.thePlayer.rotationPitch, var13, p_706253);
      float yaw = changeRotation(Helper.mc.thePlayer.rotationYaw, var12, p_706252);
      return new float[]{yaw, pitch};
   }

   public static float changeRotation(float p_706631, float p_706632, float p_706633) {
      float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
      if (var4 > p_706633) {
         var4 = p_706633;
      }

      if (var4 < -p_706633) {
         var4 = -p_706633;
      }

      return p_706631 + var4;
   }

   public static double[] getRotationToEntity(Entity entity) {
      double pX = Helper.mc.thePlayer.posX;
      double pY = Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight();
      double pZ = Helper.mc.thePlayer.posZ;
      double eX = entity.posX;
      double eY = entity.posY + (double)(entity.height / 2.0F);
      double eZ = entity.posZ;
      double dX = pX - eX;
      double dY = pY - eY;
      double dZ = pZ - eZ;
      double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
      double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
      double pitch = Math.toDegrees(Math.atan2(dH, dY));
      return new double[]{yaw, 90.0 - pitch};
   }

   public static float[] getRotations(Entity entity) {
      if (entity == null) {
         return null;
      } else {
         double diffX = entity.posX - Helper.mc.thePlayer.posX;
         double diffZ = entity.posZ - Helper.mc.thePlayer.posZ;
         double diffY;
         if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + ((double)elb.getEyeHeight() - 0.4) - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
         } else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
         }

         double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
         float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0F;
         float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / Math.PI);
         return new float[]{yaw, pitch};
      }
   }

   public static float getDistanceBetweenAngles(float angle1, float angle2) {
      float angle3 = Math.abs(angle1 - angle2) % 360.0F;
      if (angle3 > 180.0F) {
         angle3 = 0.0F;
      }

      return angle3;
   }

   public static int wrapAngleToDirection(float yaw, int zones) {
      int angle = (int)((double)(yaw + (float)(360 / (2 * zones))) + 0.5) % 360;
      if (angle < 0) {
         angle += 360;
      }

      return angle / (360 / zones);
   }

   public static Rotation getVecRotationByRotation(Vec3 pos) {
      return new Rotation(getVecRotation(pos)[0], getVecRotation(pos)[1]);
   }

   public static float[] getVecRotation(Vec3 position) {
      return getVecRotation(Helper.mc.thePlayer.getPositionVector().addVector(0.0, (double)Helper.mc.thePlayer.getEyeHeight(), 0.0), position);
   }

   public static float[] getVecRotation(Vec3 origin, Vec3 position) {
      Vec3 difference = position.subtract(origin);
      double distance = difference.normalize().lengthVector();
      float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
      return new float[]{yaw, pitch};
   }
}
