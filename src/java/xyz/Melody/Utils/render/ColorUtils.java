/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.render;

import java.awt.Color;
import xyz.Melody.Utils.Colors;

public final class ColorUtils {
    public static int randomColor() {
        return 0xFF000000 | (int)(Math.random() * 1.6777215E7);
    }

    public static int transparency(int n, double d) {
        Color color = new Color(n);
        float f = 0.003921569f * (float)color.getRed();
        float f2 = 0.003921569f * (float)color.getGreen();
        float f3 = 0.003921569f * (float)color.getBlue();
        return new Color(f, f2, f3, (float)d).getRGB();
    }

    public static int transparency(Color color, double d) {
        return new Color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)d).getRGB();
    }

    public static Color rainbow(long l2, float f) {
        float f2 = (float)(System.nanoTime() + l2) / 1.0E10f % 1.0f;
        long l3 = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(f2, 1.0f, 1.0f)), 16);
        Color color = new Color((int)l3);
        return new Color((float)color.getRed() / 255.0f * f, (float)color.getGreen() / 255.0f * f, (float)color.getBlue() / 255.0f * f, (float)color.getAlpha() / 255.0f);
    }

    public static float[] getRGBA(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        return new float[]{f2, f3, f4, f};
    }

    public static int intFromHex(String string) {
        try {
            if (string.equalsIgnoreCase("rainbow")) {
                return ColorUtils.rainbow(0L, 1.0f).getRGB();
            }
            return Integer.parseInt(string, 16);
        }
        catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    public static String hexFromInt(int n) {
        return ColorUtils.hexFromInt(new Color(n));
    }

    public static String hexFromInt(Color color) {
        return Integer.toHexString(color.getRGB()).substring(2);
    }

    public static Color blend(Color color, Color color2, double d) {
        float f = (float)d;
        float f2 = 1.0f - f;
        float[] fArray = new float[3];
        float[] fArray2 = new float[3];
        color.getColorComponents(fArray);
        color2.getColorComponents(fArray2);
        Color color3 = new Color(fArray[0] * f + fArray2[0] * f2, fArray[1] * f + fArray2[1] * f2, fArray[2] * f + fArray2[2] * f2);
        return color3;
    }

    public static Color blend(Color color, Color color2) {
        return ColorUtils.blend(color, color2, 0.5);
    }

    public static Color darker(Color color, double d) {
        int n = (int)Math.round((double)color.getRed() * (1.0 - d));
        int n2 = (int)Math.round((double)color.getGreen() * (1.0 - d));
        int n3 = (int)Math.round((double)color.getBlue() * (1.0 - d));
        if (n < 0) {
            n = 0;
        } else if (n > 255) {
            n = 255;
        }
        if (n2 < 0) {
            n2 = 0;
        } else if (n2 > 255) {
            n2 = 255;
        }
        if (n3 < 0) {
            n3 = 0;
        } else if (n3 > 255) {
            n3 = 255;
        }
        int n4 = color.getAlpha();
        return new Color(n, n2, n3, n4);
    }

    public static Color lighter(Color color, double d) {
        int n = (int)Math.round((double)color.getRed() * (1.0 + d));
        int n2 = (int)Math.round((double)color.getGreen() * (1.0 + d));
        int n3 = (int)Math.round((double)color.getBlue() * (1.0 + d));
        if (n < 0) {
            n = 0;
        } else if (n > 255) {
            n = 255;
        }
        if (n2 < 0) {
            n2 = 0;
        } else if (n2 > 255) {
            n2 = 255;
        }
        if (n3 < 0) {
            n3 = 0;
        } else if (n3 > 255) {
            n3 = 255;
        }
        int n4 = color.getAlpha();
        return new Color(n, n2, n3, n4);
    }

    public static String getHexName(Color color) {
        int n = color.getRed();
        int n2 = color.getGreen();
        int n3 = color.getBlue();
        String string = Integer.toString(n, 16);
        String string2 = Integer.toString(n2, 16);
        String string3 = Integer.toString(n3, 16);
        return ((StringBuilder)((Object)((StringBuilder)((Object)(string.length() == 2 ? string : "0" + string))).toString())).toString() + (string2.length() == 2 ? string2 : "0" + string2) + (string3.length() == 2 ? string3 : "0" + string3);
    }

    public static double colorDistance(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d4 - d;
        double d8 = d5 - d2;
        double d9 = d6 - d3;
        return Math.sqrt(d7 * d7 + d8 * d8 + d9 * d9);
    }

    public static double colorDistance(double[] dArray, double[] dArray2) {
        return ColorUtils.colorDistance(dArray[0], dArray[1], dArray[2], dArray2[0], dArray2[1], dArray2[2]);
    }

    public static double colorDistance(Color color, Color color2) {
        float[] fArray = new float[3];
        float[] fArray2 = new float[3];
        color.getColorComponents(fArray);
        color2.getColorComponents(fArray2);
        return ColorUtils.colorDistance(fArray[0], fArray[1], fArray[2], fArray2[0], fArray2[1], fArray2[2]);
    }

    public static boolean isDark(double d, double d2, double d3) {
        double d4 = ColorUtils.colorDistance(d, d2, d3, 1.0, 1.0, 1.0);
        double d5 = ColorUtils.colorDistance(d, d2, d3, 0.0, 0.0, 0.0);
        return d5 < d4;
    }

    public static boolean isDark(Color color) {
        float f = (float)color.getRed() / 255.0f;
        float f2 = (float)color.getGreen() / 255.0f;
        float f3 = (float)color.getBlue() / 255.0f;
        return ColorUtils.isDark(f, f2, f3);
    }

    public static Color getHealthColor(float f, float f2) {
        float[] fArray = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colorArray = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
        float f3 = f / f2;
        return ColorUtils.blendColors(fArray, colorArray, f3).brighter();
    }

    public static Color blendColors(float[] fArray, Color[] colorArray, float f) {
        if (fArray.length == colorArray.length) {
            int[] nArray = ColorUtils.getFractionIndices(fArray, f);
            float[] fArray2 = new float[]{fArray[nArray[0]], fArray[nArray[1]]};
            Color[] colorArray2 = new Color[]{colorArray[nArray[0]], colorArray[nArray[1]]};
            float f2 = fArray2[1] - fArray2[0];
            float f3 = f - fArray2[0];
            float f4 = f3 / f2;
            Color color = ColorUtils.blend(colorArray2[0], colorArray2[1], 1.0f - f4);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }

    public static int[] getFractionIndices(float[] fArray, float f) {
        int n;
        int[] nArray = new int[2];
        for (n = 0; n < fArray.length && fArray[n] <= f; ++n) {
        }
        if (n >= fArray.length) {
            n = fArray.length - 1;
        }
        nArray[0] = n - 1;
        nArray[1] = n;
        return nArray;
    }

    public static Color addAlpha(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    public static Color getArmorColor(float f, float f2) {
        float[] fArray = new float[]{0.0f, 0.33f, 0.66f, 1.0f};
        Color[] colorArray = new Color[]{new Color(Colors.DARKGREY.c), new Color(Colors.GRAY.c), new Color(Colors.DARKBLUE.c), new Color(Colors.BLUE.c)};
        float f3 = f / f2;
        return ColorUtils.blendColors(fArray, colorArray, f3).brighter();
    }

    public static Color colorFromInt(int n) {
        Color color = new Color(n);
        Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
        return color2;
    }
}

