/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Font;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;
import xyz.Melody.GUI.Font.CFont;

public class CFontRenderer
extends CFont {
    protected CFont.CharData[] boldChars = new CFont.CharData[256];
    protected CFont.CharData[] italicChars = new CFont.CharData[256];
    protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
    private final int[] colorCode = new int[32];
    private final String colorcodeIdentifiers = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public CFontRenderer(Font font, boolean bl) {
        super(font, bl);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public float drawStringWithShadow(String string, double d, double d2, int n) {
        float f = this.drawString(string, d + 0.3, d2 + 0.3, n, true);
        return Math.max(f, this.drawString(string, d, d2, n, false));
    }

    public float drawString(String string, float f, float f2, int n) {
        return this.drawString(string, f, f2, n, false);
    }

    public float drawCenteredString(String string, float f, float f2, int n) {
        return this.drawString(string, f - (float)(this.getStringWidth(string) / 2), f2, n);
    }

    public float drawCenteredStringWithShadow(String string, float f, float f2, int n) {
        return this.drawStringWithShadow(string, f - (float)(this.getStringWidth(string) / 2), f2, n);
    }

    public float drawCenteredStringWithShadow(String string, double d, double d2, int n) {
        return this.drawStringWithShadow(string, d - (double)(this.getStringWidth(string) / 2), d2, n);
    }

    public float drawString(String string, double d, double d2, int n, boolean bl) {
        GlStateManager.func_179117_G();
        d -= 1.0;
        if (string == null) {
            return 0.0f;
        }
        if (n == 0x20FFFFFF) {
            n = 0xFFFFFF;
        }
        if ((n & 0xFC000000) == 0) {
            n |= 0xFF000000;
        }
        if (bl) {
            n = (n & 0xFCFCFC) >> 2 | n & 0xFF000000;
        }
        CFont.CharData[] charDataArray = this.charData;
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = true;
        d *= 2.0;
        d2 = (d2 - 3.0) * 2.0;
        if (bl6) {
            GlStateManager.func_179117_G();
            GL11.glPushMatrix();
            GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b((int)770, (int)771);
            GlStateManager.func_179131_c((float)((float)(n >> 16 & 0xFF) / 255.0f), (float)((float)(n >> 8 & 0xFF) / 255.0f), (float)((float)(n & 0xFF) / 255.0f), (float)f);
            int n2 = string.length();
            GlStateManager.func_179098_w();
            GlStateManager.func_179144_i((int)this.tex.func_110552_b());
            GL11.glBindTexture(3553, this.tex.func_110552_b());
            for (int i = 0; i < n2; ++i) {
                char c = string.charAt(i);
                if (c == '\u00a7' && i < n2) {
                    int n3 = 21;
                    try {
                        n3 = "0123456789abcdefklmnor".indexOf(string.charAt(i + 1));
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    if (n3 < 16) {
                        bl2 = false;
                        bl3 = false;
                        bl5 = false;
                        bl4 = false;
                        GlStateManager.func_179144_i((int)this.tex.func_110552_b());
                        charDataArray = this.charData;
                        if (n3 < 0 || n3 > 15) {
                            n3 = 15;
                        }
                        if (bl) {
                            n3 += 16;
                        }
                        int n4 = this.colorCode[n3];
                        GlStateManager.func_179131_c((float)((float)(n4 >> 16 & 0xFF) / 255.0f), (float)((float)(n4 >> 8 & 0xFF) / 255.0f), (float)((float)(n4 & 0xFF) / 255.0f), (float)f);
                    } else if (n3 != 16) {
                        if (n3 == 17) {
                            bl2 = true;
                            if (bl3) {
                                GlStateManager.func_179144_i((int)this.texItalicBold.func_110552_b());
                                charDataArray = this.boldItalicChars;
                            } else {
                                GlStateManager.func_179144_i((int)this.texBold.func_110552_b());
                                charDataArray = this.boldChars;
                            }
                        } else if (n3 == 18) {
                            bl4 = true;
                        } else if (n3 == 19) {
                            bl5 = true;
                        } else if (n3 == 20) {
                            bl3 = true;
                            if (bl2) {
                                GlStateManager.func_179144_i((int)this.texItalicBold.func_110552_b());
                                charDataArray = this.boldItalicChars;
                            } else {
                                GlStateManager.func_179144_i((int)this.texItalic.func_110552_b());
                                charDataArray = this.italicChars;
                            }
                        } else if (n3 == 21) {
                            bl2 = false;
                            bl3 = false;
                            bl5 = false;
                            bl4 = false;
                            GlStateManager.func_179131_c((float)((float)(n >> 16 & 0xFF) / 255.0f), (float)((float)(n >> 8 & 0xFF) / 255.0f), (float)((float)(n & 0xFF) / 255.0f), (float)f);
                            GlStateManager.func_179144_i((int)this.tex.func_110552_b());
                            charDataArray = this.charData;
                        }
                    }
                    ++i;
                    continue;
                }
                if (c >= charDataArray.length || c < '\u0000') continue;
                GL11.glBegin(4);
                this.drawChar(charDataArray, c, (float)d, (float)d2);
                GL11.glEnd();
                if (bl4) {
                    this.drawLine(d, d2 + (double)(charDataArray[c].height / 2), d + (double)charDataArray[c].width - 8.0, d2 + (double)(charDataArray[c].height / 2), 1.0f);
                }
                if (bl5) {
                    this.drawLine(d, d2 + (double)charDataArray[c].height - 2.0, d + (double)charDataArray[c].width - 8.0, d2 + (double)charDataArray[c].height - 2.0, 1.0f);
                }
                d += (double)(charDataArray[c].width - 8 + this.charOffset);
            }
            GL11.glHint(3155, 4352);
            GL11.glPopMatrix();
        }
        GlStateManager.func_179117_G();
        return (float)d / 2.0f;
    }

    @Override
    public int getStringWidth(String string) {
        if (string == null) {
            return 0;
        }
        int n = 0;
        CFont.CharData[] charDataArray = this.charData;
        boolean bl = false;
        boolean bl2 = false;
        int n2 = string.length();
        for (int i = 0; i < n2; ++i) {
            char c = string.charAt(i);
            if (c == '\u00a7' && i < n2) {
                int n3 = "0123456789abcdefklmnor".indexOf(c);
                if (n3 < 16) {
                    bl = false;
                    bl2 = false;
                } else if (n3 == 17) {
                    bl = true;
                    charDataArray = bl2 ? this.boldItalicChars : this.boldChars;
                } else if (n3 == 20) {
                    bl2 = true;
                    charDataArray = bl ? this.boldItalicChars : this.italicChars;
                } else if (n3 == 21) {
                    bl = false;
                    bl2 = false;
                    charDataArray = this.charData;
                }
                ++i;
                continue;
            }
            if (c >= charDataArray.length || c < '\u0000') continue;
            n += charDataArray[c].width - 8 + this.charOffset;
        }
        return n / 2;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean bl) {
        super.setAntiAlias(bl);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean bl) {
        super.setFractionalMetrics(bl);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
    }

    private void drawLine(double d, double d2, double d3, double d4, float f) {
        GL11.glDisable(3553);
        GL11.glLineWidth(f);
        GL11.glBegin(1);
        GL11.glVertex2d(d, d2);
        GL11.glVertex2d(d3, d4);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public List<String> wrapWords(String string, double d) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if ((double)this.getStringWidth(string) > d) {
            String[] stringArray = string.split(" ");
            String string2 = "";
            int n = 65535;
            for (String string3 : stringArray) {
                for (int i = 0; i < string3.toCharArray().length; ++i) {
                    char c = string3.toCharArray()[i];
                    if (c != '\u00a7' || i >= string3.toCharArray().length - 1) continue;
                    n = string3.toCharArray()[i + 1];
                }
                StringBuilder stringBuilder = new StringBuilder();
                if ((double)this.getStringWidth(stringBuilder.append(((StringBuilder)((Object)string2)).toString()).append(string3).append(" ").toString()) < d) {
                    string2 = ((StringBuilder)((Object)string2)).toString() + (String)string3 + " ";
                    continue;
                }
                arrayList.add(string2);
                string2 = String.valueOf(167 + n) + (String)string3 + " ";
            }
            if (string2.length() > 0) {
                if ((double)this.getStringWidth(string2) < d) {
                    arrayList.add(String.valueOf(167 + n) + string2 + " ");
                    string2 = "";
                } else {
                    for (String string4 : this.formatString(string2, d)) {
                        arrayList.add(string4);
                    }
                }
            }
        } else {
            arrayList.add(string);
        }
        return arrayList;
    }

    public List<String> formatString(String string, double d) {
        ArrayList<String> arrayList = new ArrayList<String>();
        String string2 = "";
        int n = 65535;
        char[] cArray = string.toCharArray();
        for (int i = 0; i < cArray.length; ++i) {
            char c = cArray[i];
            if (c == '\u00a7' && i < cArray.length - 1) {
                n = cArray[i + 1];
            }
            StringBuilder stringBuilder = new StringBuilder();
            if ((double)this.getStringWidth(stringBuilder.append(((StringBuilder)((Object)string2)).toString()).append(c).toString()) < d) {
                string2 = ((StringBuilder)((Object)string2)).toString() + c;
                continue;
            }
            arrayList.add(string2);
            string2 = String.valueOf(167 + n) + String.valueOf(c);
        }
        if (string2.length() > 0) {
            arrayList.add(string2);
        }
        return arrayList;
    }

    private void setupMinecraftColorcodes() {
        for (int i = 0; i < 32; ++i) {
            int n = (i >> 3 & 1) * 85;
            int n2 = (i >> 2 & 1) * 170 + n;
            int n3 = (i >> 1 & 1) * 170 + n;
            int n4 = (i >> 0 & 1) * 170 + n;
            if (i == 6) {
                n2 += 85;
            }
            if (i >= 16) {
                n2 /= 4;
                n3 /= 4;
                n4 /= 4;
            }
            this.colorCode[i] = (n2 & 0xFF) << 16 | (n3 & 0xFF) << 8 | n4 & 0xFF;
        }
    }

    public void drawOutlinedString(String string, float f, float f2, int n, int n2) {
        this.drawString(string, f - 0.5f, f2, n);
        this.drawString(string, f + 0.5f, f2, n);
        this.drawString(string, f, f2 - 0.5f, n);
        this.drawString(string, f, f2 + 0.5f, n);
        this.drawString(string, f, f2, n2);
    }

    public void drawCenterOutlinedString(String string, float f, float f2, int n, int n2) {
        this.drawString(string, f - (float)(this.getStringWidth(string) / 2) - 0.5f, f2, n);
        this.drawString(string, f - (float)(this.getStringWidth(string) / 2) + 0.5f, f2, n);
        this.drawString(string, f - (float)(this.getStringWidth(string) / 2), f2 - 0.5f, n);
        this.drawString(string, f - (float)(this.getStringWidth(string) / 2), f2 + 0.5f, n);
        this.drawString(string, f - (float)(this.getStringWidth(string) / 2), f2, n2);
    }

    public int drawStringWithShadow(String string, float f, float f2, int n, int n2) {
        String[] stringArray;
        string = "\u00a7r" + string;
        float f3 = -1.0f;
        String[] stringArray2 = stringArray = string.split("\u00a7");
        for (String string2 : stringArray) {
            if (string2.length() < 1) continue;
            switch (string2.charAt(0)) {
                case '0': {
                    n = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    n = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    n = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    n = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    n = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    n = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    n = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    n = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    n = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    n = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    n = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    n = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    n = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    n = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    n = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    n = new Color(255, 255, 255).getRGB();
                }
            }
            Color color = new Color(n);
            string2 = string2.substring(1, string2.length());
            int n3 = (n & 0xFCFCFC) >> 2 | n & 0xFF000000;
            this.drawString(string2, f + f3 + 0.5f, f2 + 0.5f, this.getColor(0, 0, 0, 80));
            this.drawString(string2, f + f3, f2, this.getColor(color.getRed(), color.getGreen(), color.getBlue(), n2));
            f3 += (float)(this.getStringWidth(string2) + 1);
        }
        return (int)f3;
    }

    public int drawStringWithShadow(String string, float f, float f2, int n, int n2, int n3, int n4) {
        String[] stringArray;
        string = "\u00a7r" + string;
        float f3 = -1.0f;
        String[] stringArray2 = stringArray = string.split("\u00a7");
        for (String string2 : stringArray) {
            if (string2.length() < 1) continue;
            switch (string2.charAt(0)) {
                case '0': {
                    n = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    n = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    n = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    n = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    n = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    n = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    n = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    n = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    n = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    n = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    n = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    n = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    n = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    n = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    n = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    n = new Color(255, 255, 255).getRGB();
                }
            }
            Color color = new Color(n);
            string2 = string2.substring(1, string2.length());
            int n5 = (n & 0xFCFCFC) >> 2 | n & 0xFF000000;
            this.drawString(string2, f + f3 + (float)n3, f2 + (float)n3, n2);
            this.drawString(string2, f + f3, f2, this.getColor(color.getRed(), color.getGreen(), color.getBlue(), n4));
            f3 += (float)(this.getStringWidth(string2) + 1);
        }
        return (int)f3;
    }

    public int getColor(int n, int n2, int n3, int n4) {
        boolean bl = false;
        int n5 = 0 | n4 << 24;
        n5 |= n << 16;
        n5 |= n2 << 8;
        return n5 |= n3;
    }

    public String trimStringToWidthPassword(String string, int n, boolean bl) {
        string = string.replaceAll("\u00c3\u201a", "");
        return "";
    }

    public String trimStringToWidth(String string, int n, boolean bl) {
        string = string.replaceAll("\u00c3\u201a", "");
        return "";
    }

    public String trimStringToWidth(String string, int n) {
        string = string.replaceAll("\u00c3\u201a", "");
        return "";
    }

    public int drawPassword(String string, double d, float f, int n) {
        return (int)this.drawString(string.replaceAll(".", "."), d, f, n, false);
    }

    public double getPasswordWidth(String string) {
        string = string.replaceAll("\u00c3\u201a", "");
        return 0.0;
    }
}

