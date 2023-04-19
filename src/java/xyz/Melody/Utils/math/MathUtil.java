/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.math;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import xyz.Melody.Utils.Helper;

public final class MathUtil {
    public static Random random = new Random();

    public static double toDecimalLength(double in, int places) {
        return Double.parseDouble(String.format("%." + places + "f", in));
    }

    public static double round(double in, int places) {
        places = (int)MathHelper.clamp_double(places, 0.0, 2.147483647E9);
        return Double.parseDouble(String.format("%." + places + "f", in));
    }

    public static float distanceToEntity(Entity e0, Entity e1) {
        BlockPos e0Pos = e0.getPosition();
        BlockPos e1Pos = e1.getPosition();
        return MathUtil.distanceToXYZ(e0Pos.getX(), (float)e0Pos.getY() + e0.getEyeHeight(), e0Pos.getZ(), e1Pos.getX(), (float)e1Pos.getY() + e1.getEyeHeight(), e1Pos.getZ());
    }

    public static float distanceToPos(BlockPos p0, BlockPos p1) {
        return MathUtil.distanceToXYZ(p0.getX(), p0.getY(), p0.getZ(), p1.getX(), p1.getY(), p1.getZ());
    }

    public static float distanceToXYZ(double x, double y, double z, double x1, double y1, double z1) {
        double xDist = Math.abs(x1 - x);
        double zDist = Math.abs(z1 - z);
        double yDist = Math.abs(y1 - y);
        double xzDist = Math.sqrt(MathUtil.sq(xDist) + MathUtil.sq(zDist));
        double distance = Math.sqrt(MathUtil.sq(yDist) + MathUtil.sq(xzDist));
        return (float)Math.abs(distance);
    }

    public static boolean parsable(String s, byte type) {
        try {
            switch (type) {
                case 0: {
                    Short.parseShort(s);
                    break;
                }
                case 1: {
                    Byte.parseByte(s);
                    break;
                }
                case 2: {
                    Integer.parseInt(s);
                    break;
                }
                case 3: {
                    Float.parseFloat(s);
                    break;
                }
                case 4: {
                    Double.parseDouble(s);
                    break;
                }
                case 5: {
                    Long.parseLong(s);
                }
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static double sq(double in) {
        return in * in;
    }

    public static double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double getBaseMovementSpeed() {
        double baseSpeed = 0.2873;
        if (Helper.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Helper.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static double getHighestOffset(double max) {
        for (double i = 0.0; i < max; i += 0.01) {
            int[] arrn = new int[5];
            arrn[0] = -2;
            arrn[1] = -1;
            arrn[3] = 1;
            arrn[4] = 2;
            int[] arrn2 = arrn;
            int n = arrn.length;
            for (int n2 = 0; n2 < n; ++n2) {
                int offset = arrn2[n2];
                if (Helper.mc.theWorld.getCollidingBoundingBoxes(Helper.mc.thePlayer, Helper.mc.thePlayer.getEntityBoundingBox().offset(Helper.mc.thePlayer.motionX * (double)offset, i, Helper.mc.thePlayer.motionZ * (double)offset)).size() <= 0) continue;
                return i - 0.01;
            }
        }
        return max;
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (double)(sigma * sigma));
        return (float)(output * Math.exp((double)(-(x * x)) / (2.0 * (double)(sigma * sigma))));
    }

    public static class NumberType {
        public static final byte SHORT = 0;
        public static final byte BYTE = 1;
        public static final byte INT = 2;
        public static final byte FLOAT = 3;
        public static final byte DOUBLE = 4;
        public static final byte LONG = 5;

        public static byte getByType(Class<?> cls) {
            if (cls == Short.class) {
                return 0;
            }
            if (cls == Byte.class) {
                return 1;
            }
            if (cls == Integer.class) {
                return 2;
            }
            if (cls == Float.class) {
                return 3;
            }
            if (cls == Double.class) {
                return 4;
            }
            if (cls == Long.class) {
                return 5;
            }
            return -1;
        }
    }
}

