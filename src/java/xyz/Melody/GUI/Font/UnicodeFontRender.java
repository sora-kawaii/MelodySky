/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Font;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public final class UnicodeFontRender
extends FontRenderer {
    private final UnicodeFont font;
    public HashMap<String, Integer> widthMap;
    public HashMap<String, Integer> heightMap;

    public UnicodeFontRender(Font font, boolean bl) {
        super(Minecraft.func_71410_x().field_71474_y, new ResourceLocation("textures/font/ascii.png"), Minecraft.func_71410_x().func_110434_K(), false);
        int n = -1;
        int n2 = -1;
        this.widthMap = new HashMap();
        this.heightMap = new HashMap();
        this.font = new UnicodeFont(font);
        this.font.addAsciiGlyphs();
        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        if (n2 > -1 && n > -1) {
            this.font.addGlyphs(n, n2);
        }
        if (bl) {
            this.font.addGlyphs(0, 65535);
        }
        try {
            this.font.loadGlyphs();
        }
        catch (SlickException slickException) {
            throw new RuntimeException(slickException);
        }
        this.field_78288_b = this.font.getHeight("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789") / 2;
    }

    public int drawStringWithColor(String string, float f, float f2, int n, int n2) {
        string = "\u00a7r" + string;
        float f3 = -1.0f;
        for (String string2 : string.split("\u00a7")) {
            if (string2.length() < 1) continue;
            switch (string2.charAt(0)) {
                case '0': {
                    n = new Color(0, 0, 0, n2).getRGB();
                    break;
                }
                case '1': {
                    n = new Color(0, 0, 170, n2).getRGB();
                    break;
                }
                case '2': {
                    n = new Color(0, 170, 0, n2).getRGB();
                    break;
                }
                case '3': {
                    n = new Color(0, 170, 170, n2).getRGB();
                    break;
                }
                case '4': {
                    n = new Color(170, 0, 0, n2).getRGB();
                    break;
                }
                case '5': {
                    n = new Color(170, 0, 170, n2).getRGB();
                    break;
                }
                case '6': {
                    n = new Color(255, 170, 0, n2).getRGB();
                    break;
                }
                case '7': {
                    n = new Color(170, 170, 170, n2).getRGB();
                    break;
                }
                case '8': {
                    n = new Color(85, 85, 85, n2).getRGB();
                    break;
                }
                case '9': {
                    n = new Color(85, 85, 255, n2).getRGB();
                    break;
                }
                case 'a': {
                    n = new Color(85, 255, 85, n2).getRGB();
                    break;
                }
                case 'b': {
                    n = new Color(85, 255, 255, n2).getRGB();
                    break;
                }
                case 'c': {
                    n = new Color(255, 85, 85, n2).getRGB();
                    break;
                }
                case 'd': {
                    n = new Color(255, 85, 255, n2).getRGB();
                    break;
                }
                case 'e': {
                    n = new Color(255, 255, 85, n2).getRGB();
                    break;
                }
                case 'f': {
                    n = new Color(255, 255, 255, n2).getRGB();
                    break;
                }
                case 'r': {
                    n = new Color(255, 255, 255, n2).getRGB();
                }
            }
            Color color = new Color(n);
            string2 = string2.substring(1, string2.length());
            this.drawString(string2, f + f3, f2, this.getColor(color.getRed(), color.getGreen(), color.getBlue(), n2));
            f3 += (float)(this.func_78256_a(string2) + 1);
        }
        return (int)f3;
    }

    public int getColor(int n, int n2, int n3, int n4) {
        int n5 = 0;
        int n6 = n5 | n4 << 24;
        n6 |= n << 16;
        n6 |= n2 << 8;
        return n6 |= n3;
    }

    public int drawString(String string, float f, float f2, int n) {
        if (string == null) {
            return 0;
        }
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        boolean bl = GL11.glIsEnabled(3042);
        boolean bl2 = GL11.glIsEnabled(2896);
        boolean bl3 = GL11.glIsEnabled(3553);
        GL11.glEnable(3042);
        if (bl2) {
            GL11.glDisable(2896);
        }
        if (bl3) {
            GL11.glDisable(3553);
        }
        this.font.drawString(f *= 2.0f, f2 *= 2.0f, string, new org.newdawn.slick.Color(n));
        if (bl3) {
            GL11.glEnable(3553);
        }
        if (bl2) {
            GL11.glEnable(2896);
        }
        if (!bl) {
            GL11.glDisable(3042);
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glPopMatrix();
        GlStateManager.func_179144_i((int)((int)f));
        return (int)f;
    }

    public int func_175063_a(String string, float f, float f2, int n) {
        this.drawString(string, f + 1.0f, f2 + 1.0f, -16777216);
        return this.drawString(string, f, f2, n);
    }

    public int func_78263_a(char c) {
        return this.func_78256_a(Character.toString(c));
    }

    public int func_78256_a(String string) {
        return this.font.getWidth(string) / 2;
    }

    public int getStringHeight(String string) {
        return this.font.getHeight(string) / 2;
    }

    public void drawCenteredString(String string, float f, float f2, int n) {
        this.drawString(string, f - (float)(this.func_78256_a(string) / 2), f2, n);
    }

    public int getHeight() {
        return this.getStringHeight("WECabc");
    }
}

