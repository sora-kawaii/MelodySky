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

    public static void pitch(float f) {
        Helper.mc.thePlayer.rotationPitch = f;
    }

    public static float yaw() {
        return Helper.mc.thePlayer.rotationYaw;
    }

    public static void yaw(float f) {
        Helper.mc.thePlayer.rotationYaw = f;
    }

    public static double isInFov(float f, float f2, double d, double d2, double d3) {
        Vec3 vec3 = new Vec3(f, f2, 0.0);
        float[] fArray = RotationUtil.getAngleBetweenVecs(new Vec3(RotationUtil.mc.thePlayer.posX, RotationUtil.mc.thePlayer.posY, RotationUtil.mc.thePlayer.posZ), new Vec3(d, d2, d3));
        double d4 = MathHelper.wrapAngleTo180_double(vec3.xCoord - (double)fArray[0]);
        return Math.abs(d4) * 2.0;
    }

    public static float[] getAngleBetweenVecs(Vec3 vec3, Vec3 vec32) {
        double d = vec32.xCoord - vec3.xCoord;
        double d2 = vec32.yCoord - vec3.yCoord;
        double d3 = vec32.zCoord - vec3.zCoord;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)(Math.atan2(d3, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d2, d4) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }

    public static float[] getRotationFromPosition(double d, double d2, double d3) {
        double d4 = d - Minecraft.getMinecraft().thePlayer.posX;
        double d5 = d2 - Minecraft.getMinecraft().thePlayer.posZ;
        double d6 = d3 - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5);
        float f = (float)(Math.atan2(d5, d4) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d6, d7) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }

    public static float[] faceTarget(Entity entity, float f, float f2, boolean bl) {
        double d;
        double d2 = entity.posX - Helper.mc.thePlayer.posX;
        double d3 = entity.posZ - Helper.mc.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            d = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
        } else {
            d = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
        }
        double d4 = MathHelper.sqrt_double(d2 * d2 + d3 * d3);
        float f3 = (float)(Math.atan2(d3, d2) * 180.0 / Math.PI) - 90.0f;
        float f4 = (float)(-Math.atan2(d - (entity instanceof EntityPlayer ? 0.25 : 0.0), d4) * 180.0 / Math.PI);
        float f5 = RotationUtil.changeRotation(Helper.mc.thePlayer.rotationPitch, f4, f2);
        float f6 = RotationUtil.changeRotation(Helper.mc.thePlayer.rotationYaw, f3, f);
        return new float[]{f6, f5};
    }

    public static float changeRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    public static float[] getRotationToEntity(Entity entity) {
        double d = Helper.mc.thePlayer.posX;
        double d2 = Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight();
        double d3 = Helper.mc.thePlayer.posZ;
        double d4 = entity.posX;
        double d5 = entity.posY + (double)(entity.height / 2.0f);
        double d6 = entity.posZ;
        double d7 = d - d4;
        double d8 = d2 - d5;
        double d9 = d3 - d6;
        double d10 = Math.sqrt(Math.pow(d7, 2.0) + Math.pow(d9, 2.0));
        double d11 = Math.toDegrees(Math.atan2(d9, d7)) + 90.0;
        double d12 = Math.toDegrees(Math.atan2(d10, d8));
        return new float[]{(float)d11, (float)(90.0 - d12)};
    }

    public static float[] getRotations(Entity entity) {
        double d;
        if (entity == null) {
            return null;
        }
        double d2 = entity.posX - Helper.mc.thePlayer.posX;
        double d3 = entity.posZ - Helper.mc.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            d = entityLivingBase.posY + ((double)entityLivingBase.getEyeHeight() - 0.4) - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
        } else {
            d = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
        }
        double d4 = MathHelper.sqrt_double(d2 * d2 + d3 * d3);
        float f = (float)(Math.atan2(d3, d2) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-Math.atan2(d, d4) * 180.0 / Math.PI);
        return new float[]{f, f2};
    }

    public static float getDistanceBetweenAngles(float f, float f2) {
        float f3 = Math.abs(f - f2) % 360.0f;
        if (f3 > 180.0f) {
            f3 = 0.0f;
        }
        return f3;
    }

    public static int wrapAngleToDirection(float f, int n) {
        int n2 = (int)((double)(f + (float)(360 / (2 * n))) + 0.5) % 360;
        if (n2 < 0) {
            n2 += 360;
        }
        return n2 / (360 / n);
    }

    public static Rotation getVecRotationByRotation(Vec3 vec3) {
        return new Rotation(RotationUtil.getVecRotation(vec3)[0], RotationUtil.getVecRotation(vec3)[1]);
    }

    public static float[] getVecRotation(Vec3 vec3) {
        return RotationUtil.getVecRotation(Helper.mc.thePlayer.getPositionVector().addVector(0.0, Helper.mc.thePlayer.getEyeHeight(), 0.0), vec3);
    }

    public static float[] getVecRotation(Vec3 vec3, Vec3 vec32) {
        Vec3 vec33 = vec32.subtract(vec3);
        double d = vec33.normalize().lengthVector();
        float f = (float)Math.toDegrees(Math.atan2(vec33.zCoord, vec33.xCoord)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(vec33.yCoord, d)));
        return new float[]{f, f2};
    }

    public static float[] getPredictedRotations(EntityLivingBase entityLivingBase) {
        double d = entityLivingBase.posX + (entityLivingBase.posX - entityLivingBase.lastTickPosX);
        double d2 = entityLivingBase.posZ + (entityLivingBase.posZ - entityLivingBase.lastTickPosZ);
        double d3 = entityLivingBase.posY + (double)(entityLivingBase.getEyeHeight() / 2.0f);
        return RotationUtil.getRotationFromPosition(d, d2, d3);
    }

    public static float[] getRotations(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double)blockPos.getX() + 0.5 - RotationUtil.mc.thePlayer.posX + (double)enumFacing.getFrontOffsetX() / 2.0;
        double d2 = (double)blockPos.getZ() + 0.5 - RotationUtil.mc.thePlayer.posZ + (double)enumFacing.getFrontOffsetZ() / 2.0;
        double d3 = RotationUtil.mc.thePlayer.posY + (double)RotationUtil.mc.thePlayer.getEyeHeight() - ((double)blockPos.getY() + 0.5);
        double d4 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f = (float)(Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(Math.atan2(d3, d4) * 180.0 / Math.PI);
        if (f < 0.0f) {
            f += 360.0f;
        }
        return new float[]{f, f2};
    }
}

