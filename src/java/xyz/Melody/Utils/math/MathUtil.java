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

    public static double toDecimalLength(double d, int n) {
        return Double.parseDouble(String.format("%." + n + "f", d));
    }

    public static double round(double d, int n) {
        n = (int)MathHelper.func_151237_a((double)n, (double)0.0, (double)2.147483647E9);
        return Double.parseDouble(String.format("%." + n + "f", d));
    }

    public static float distanceToEntity(Entity entity, Entity entity2) {
        BlockPos blockPos = entity.func_180425_c();
        BlockPos blockPos2 = entity2.func_180425_c();
        return MathUtil.distanceToXYZ(blockPos.func_177958_n(), (float)blockPos.func_177956_o() + entity.func_70047_e(), blockPos.func_177952_p(), blockPos2.func_177958_n(), (float)blockPos2.func_177956_o() + entity2.func_70047_e(), blockPos2.func_177952_p());
    }

    public static float distanceToPos(BlockPos blockPos, BlockPos blockPos2) {
        return MathUtil.distanceToXYZ(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), blockPos2.func_177958_n(), blockPos2.func_177956_o(), blockPos2.func_177952_p());
    }

    public static float distanceToXYZ(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = Math.abs(d4 - d);
        double d8 = Math.abs(d6 - d3);
        double d9 = Math.abs(d5 - d2);
        double d10 = Math.sqrt(MathUtil.sq(d7) + MathUtil.sq(d8));
        double d11 = Math.sqrt(MathUtil.sq(d9) + MathUtil.sq(d10));
        return (float)Math.abs(d11);
    }

    public static boolean parsable(String string, byte by) {
        try {
            switch (by) {
                case 0: {
                    Short.parseShort(string);
                    break;
                }
                case 1: {
                    Byte.parseByte(string);
                    break;
                }
                case 2: {
                    Integer.parseInt(string);
                    break;
                }
                case 3: {
                    Float.parseFloat(string);
                    break;
                }
                case 4: {
                    Double.parseDouble(string);
                    break;
                }
                case 5: {
                    Long.parseLong(string);
                }
            }
        }
        catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            return false;
        }
        return true;
    }

    public static double sq(double d) {
        return d * d;
    }

    public static double randomDouble(double d, double d2) {
        return ThreadLocalRandom.current().nextDouble(d, d2);
    }

    public static double getBaseMovementSpeed() {
        double d = 0.2873;
        if (Helper.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            int n = Helper.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c();
            d *= 1.0 + 0.2 * (double)(n + 1);
        }
        return d;
    }

    public static double getHighestOffset(double d) {
        for (double d2 = 0.0; d2 < d; d2 += 0.01) {
            int[] nArray = new int[5];
            nArray[0] = -2;
            nArray[1] = -1;
            nArray[3] = 1;
            nArray[4] = 2;
            int[] nArray2 = nArray;
            int n = nArray.length;
            for (int i = 0; i < n; ++i) {
                int n2 = nArray2[i];
                if (Helper.mc.field_71441_e.func_72945_a(Helper.mc.field_71439_g, Helper.mc.field_71439_g.func_174813_aQ().func_72317_d(Helper.mc.field_71439_g.field_70159_w * (double)n2, d2, Helper.mc.field_71439_g.field_70179_y * (double)n2)).size() <= 0) continue;
                return d2 - 0.01;
            }
        }
        return d;
    }

    public static float calculateGaussianValue(float f, float f2) {
        double d = 3.141592653;
        double d2 = 1.0 / Math.sqrt(2.0 * d * (double)(f2 * f2));
        return (float)(d2 * Math.exp((double)(-(f * f)) / (2.0 * (double)(f2 * f2))));
    }

    public static class NumberType {
        public static final byte SHORT = 0;
        public static final byte BYTE = 1;
        public static final byte INT = 2;
        public static final byte FLOAT = 3;
        public static final byte DOUBLE = 4;
        public static final byte LONG = 5;

        public static byte getByType(Class<?> clazz) {
            if (clazz == Short.class) {
                return 0;
            }
            if (clazz == Byte.class) {
                return 1;
            }
            if (clazz == Integer.class) {
                return 2;
            }
            if (clazz == Float.class) {
                return 3;
            }
            if (clazz == Double.class) {
                return 4;
            }
            if (clazz == Long.class) {
                return 5;
            }
            return -1;
        }
    }
}

