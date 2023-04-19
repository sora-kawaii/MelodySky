/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.math;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.math.Rotation;

public final class RotationUtil {
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
        Vec3 var7 = new Vec3(yaw, pitch, 0.0);
        float[] var8 = RotationUtil.getAngleBetweenVecs(new Vec3(RotationUtil.mc.thePlayer.posX, RotationUtil.mc.thePlayer.posY, RotationUtil.mc.thePlayer.posZ), new Vec3(x, y, z));
        double var9 = MathHelper.wrapAngleTo180_double(var7.xCoord - (double)var8[0]);
        return Math.abs(var9) * 2.0;
    }

    public static float[] getAngleBetweenVecs(Vec3 var0, Vec3 var1) {
        double var2 = var1.xCoord - var0.xCoord;
        double var3 = var1.yCoord - var0.yCoord;
        double var4 = var1.zCoord - var0.zCoord;
        double var5 = Math.sqrt(var2 * var2 + var4 * var4);
        float var6 = (float)(Math.atan2(var4, var2) * 180.0 / Math.PI) - 90.0f;
        float var7 = (float)(-(Math.atan2(var3, var5) * 180.0 / Math.PI));
        return new float[]{var6, var7};
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
        double var6;
        double var4 = target.posX - Helper.mc.thePlayer.posX;
        double var8 = target.posZ - Helper.mc.thePlayer.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var10 = (EntityLivingBase)target;
            var6 = var10.posY + (double)var10.getEyeHeight() - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
        } else {
            var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
        }
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0 / Math.PI) - 90.0f;
        float var13 = (float)(-Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0.0), var14) * 180.0 / Math.PI);
        float pitch = RotationUtil.changeRotation(Helper.mc.thePlayer.rotationPitch, var13, p_706253);
        float yaw = RotationUtil.changeRotation(Helper.mc.thePlayer.rotationYaw, var12, p_706252);
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

    public static float[] getRotationToEntity(Entity entity) {
        double pX = Helper.mc.thePlayer.posX;
        double pY = Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight();
        double pZ = Helper.mc.thePlayer.posZ;
        double eX = entity.posX;
        double eY = entity.posY + (double)(entity.height / 2.0f);
        double eZ = entity.posZ;
        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        double pitch = Math.toDegrees(Math.atan2(dH, dY));
        return new float[]{(float)yaw, (float)(90.0 - pitch)};
    }

    public static float[] getRotations(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - Helper.mc.thePlayer.posX;
        double diffZ = entity.posZ - Helper.mc.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + ((double)elb.getEyeHeight() - 0.4) - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
        } else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
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
        return new Rotation(RotationUtil.getVecRotation(pos)[0], RotationUtil.getVecRotation(pos)[1]);
    }

    public static float[] getVecRotation(Vec3 position) {
        return RotationUtil.getVecRotation(Helper.mc.thePlayer.getPositionVector().addVector(0.0, Helper.mc.thePlayer.getEyeHeight(), 0.0), position);
    }

    public static float[] getVecRotation(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        double distance = difference.normalize().lengthVector();
        float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[]{yaw, pitch};
    }

    public static float[] getPredictedRotations(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtil.getRotationFromPosition(x, z, y);
    }

    public static float[] getRotations(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - RotationUtil.mc.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - RotationUtil.mc.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double d1 = RotationUtil.mc.thePlayer.posY + (double)RotationUtil.mc.thePlayer.getEyeHeight() - ((double)block.getY() + 0.5);
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }
}

