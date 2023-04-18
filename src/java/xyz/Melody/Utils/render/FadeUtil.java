/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.render;

import java.awt.Color;
import java.util.function.Supplier;

public enum FadeUtil {
    GREEN(() -> new Color(0, 255, 138)),
    WHITE(() -> Color.WHITE),
    PURPLE(() -> new Color(198, 139, 255)),
    DARK_PURPLE(() -> new Color(133, 46, 215)),
    BLUE(() -> new Color(116, 202, 255)),
    RED(() -> new Color(255, 50, 50));

    private final Supplier<Color> colorSupplier;

    private FadeUtil(Supplier<Color> supplier) {
        this.colorSupplier = supplier;
    }

    public static Color fade(Color color) {
        return FadeUtil.fade(color, 2, 100);
    }

    public static Color fade(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)n / (float)n2 * 2.0f) % 2.0f - 1.0f);
        f = 0.4f + 0.5f * f;
        fArray[2] = f % 2.0f;
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    public Color getColor() {
        return this.colorSupplier.get();
    }
}

