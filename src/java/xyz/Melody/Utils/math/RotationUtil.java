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
    private static Minecraft mc = Minecraft.func_71410_x();

    public static float pitch() {
        return Helper.mc.field_71439_g.field_70125_A;
    }

    public static void pitch(float f) {
        Helper.mc.field_71439_g.field_70125_A = f;
    }

    public static float yaw() {
        return Helper.mc.field_71439_g.field_70177_z;
    }

    public static void yaw(float f) {
        Helper.mc.field_71439_g.field_70177_z = f;
    }

    public static double isInFov(float f, float f2, double d, double d2, double d3) {
        Vec3 vec3 = new Vec3(f, f2, 0.0);
        float[] fArray = RotationUtil.getAngleBetweenVecs(new Vec3(RotationUtil.mc.field_71439_g.field_70165_t, RotationUtil.mc.field_71439_g.field_70163_u, RotationUtil.mc.field_71439_g.field_70161_v), new Vec3(d, d2, d3));
        double d4 = MathHelper.func_76138_g((double)(vec3.field_72450_a - (double)fArray[0]));
        return Math.abs(d4) * 2.0;
    }

    public static float[] getAngleBetweenVecs(Vec3 vec3, Vec3 vec32) {
        double d = vec32.field_72450_a - vec3.field_72450_a;
        double d2 = vec32.field_72448_b - vec3.field_72448_b;
        double d3 = vec32.field_72449_c - vec3.field_72449_c;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)(Math.atan2(d3, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d2, d4) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }

    public static float[] getRotationFromPosition(double d, double d2, double d3) {
        double d4 = d - Minecraft.func_71410_x().field_71439_g.field_70165_t;
        double d5 = d2 - Minecraft.func_71410_x().field_71439_g.field_70161_v;
        double d6 = d3 - Minecraft.func_71410_x().field_71439_g.field_70163_u - 1.2;
        double d7 = MathHelper.func_76133_a((double)(d4 * d4 + d5 * d5));
        float f = (float)(Math.atan2(d5, d4) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d6, d7) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }

    public static float[] faceTarget(Entity entity, float f, float f2, boolean bl) {
        double d;
        double d2 = entity.field_70165_t - Helper.mc.field_71439_g.field_70165_t;
        double d3 = entity.field_70161_v - Helper.mc.field_71439_g.field_70161_v;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            d = entityLivingBase.field_70163_u + (double)entityLivingBase.func_70047_e() - (Helper.mc.field_71439_g.field_70163_u + (double)Helper.mc.field_71439_g.func_70047_e());
        } else {
            d = (entity.func_174813_aQ().field_72338_b + entity.func_174813_aQ().field_72337_e) / 2.0 - (Helper.mc.field_71439_g.field_70163_u + (double)Helper.mc.field_71439_g.func_70047_e());
        }
        double d4 = MathHelper.func_76133_a((double)(d2 * d2 + d3 * d3));
        float f3 = (float)(Math.atan2(d3, d2) * 180.0 / Math.PI) - 90.0f;
        float f4 = (float)(-Math.atan2(d - (entity instanceof EntityPlayer ? 0.25 : 0.0), d4) * 180.0 / Math.PI);
        float f5 = RotationUtil.changeRotation(Helper.mc.field_71439_g.field_70125_A, f4, f2);
        float f6 = RotationUtil.changeRotation(Helper.mc.field_71439_g.field_70177_z, f3, f);
        return new float[]{f6, f5};
    }

    public static float changeRotation(float f, float f2, float f3) {
        float f4 = MathHelper.func_76142_g((float)(f2 - f));
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    public static float[] getRotationToEntity(Entity entity) {
        double d = Helper.mc.field_71439_g.field_70165_t;
        double d2 = Helper.mc.field_71439_g.field_70163_u + (double)Helper.mc.field_71439_g.func_70047_e();
        double d3 = Helper.mc.field_71439_g.field_70161_v;
        double d4 = entity.field_70165_t;
        double d5 = entity.field_70163_u + (double)(entity.field_70131_O / 2.0f);
        double d6 = entity.field_70161_v;
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
        double d2 = entity.field_70165_t - Helper.mc.field_71439_g.field_70165_t;
        double d3 = entity.field_70161_v - Helper.mc.field_71439_g.field_70161_v;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            d = entityLivingBase.field_70163_u + ((double)entityLivingBase.func_70047_e() - 0.4) - (Helper.mc.field_71439_g.field_70163_u + (double)Helper.mc.field_71439_g.func_70047_e());
        } else {
            d = (entity.func_174813_aQ().field_72338_b + entity.func_174813_aQ().field_72337_e) / 2.0 - (Helper.mc.field_71439_g.field_70163_u + (double)Helper.mc.field_71439_g.func_70047_e());
        }
        double d4 = MathHelper.func_76133_a((double)(d2 * d2 + d3 * d3));
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
        return RotationUtil.getVecRotation(Helper.mc.field_71439_g.func_174791_d().func_72441_c(0.0, Helper.mc.field_71439_g.func_70047_e(), 0.0), vec3);
    }

    public static float[] getVecRotation(Vec3 vec3, Vec3 vec32) {
        Vec3 vec33 = vec32.func_178788_d(vec3);
        double d = vec33.func_72432_b().func_72433_c();
        float f = (float)Math.toDegrees(Math.atan2(vec33.field_72449_c, vec33.field_72450_a)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(vec33.field_72448_b, d)));
        return new float[]{f, f2};
    }

    public static float[] getPredictedRotations(EntityLivingBase entityLivingBase) {
        double d = entityLivingBase.field_70165_t + (entityLivingBase.field_70165_t - entityLivingBase.field_70142_S);
        double d2 = entityLivingBase.field_70161_v + (entityLivingBase.field_70161_v - entityLivingBase.field_70136_U);
        double d3 = entityLivingBase.field_70163_u + (double)(entityLivingBase.func_70047_e() / 2.0f);
        return RotationUtil.getRotationFromPosition(d, d2, d3);
    }

    public static float[] getRotations(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double)blockPos.func_177958_n() + 0.5 - RotationUtil.mc.field_71439_g.field_70165_t + (double)enumFacing.func_82601_c() / 2.0;
        double d2 = (double)blockPos.func_177952_p() + 0.5 - RotationUtil.mc.field_71439_g.field_70161_v + (double)enumFacing.func_82599_e() / 2.0;
        double d3 = RotationUtil.mc.field_71439_g.field_70163_u + (double)RotationUtil.mc.field_71439_g.func_70047_e() - ((double)blockPos.func_177956_o() + 0.5);
        double d4 = MathHelper.func_76133_a((double)(d * d + d2 * d2));
        float f = (float)(Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(Math.atan2(d3, d4) * 180.0 / Math.PI);
        if (f < 0.0f) {
            f += 360.0f;
        }
        return new float[]{f, f2};
    }
}

