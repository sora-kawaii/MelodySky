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

    public CFontRenderer(Font font, boolean antiAlias) {
        super(font, antiAlias);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public float drawStringWithShadow(String text, double x, double y, int color) {
        float shadowWidth = this.drawString(text, x + 0.3, y + 0.3, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawString(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    public float drawCenteredString(String text, float x, float y, int color) {
        return this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
        return this.drawStringWithShadow(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public float drawCenteredStringWithShadow(String text, double x, double y, int color) {
        return this.drawStringWithShadow(text, x - (double)(this.getStringWidth(text) / 2), y, color);
    }

    public float drawString(String text, double x, double y, int color, boolean shadow) {
        GlStateManager.resetColor();
        x -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        if (color == 0x20FFFFFF) {
            color = 0xFFFFFF;
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (shadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        if (render) {
            GlStateManager.resetColor();
            GL11.glPushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
            int size = text.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(this.tex.getGlTextureId());
            GL11.glBindTexture(3553, this.tex.getGlTextureId());
            for (int i = 0; i < size; ++i) {
                char character = text.charAt(i);
                if (character == '\u00a7' && i < size) {
                    int colorIndex = 21;
                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                        if (colorIndex < 0 || colorIndex > 15) {
                            colorIndex = 15;
                        }
                        if (shadow) {
                            colorIndex += 16;
                        }
                        int colorcode = this.colorCode[colorIndex];
                        GlStateManager.color((float)(colorcode >> 16 & 0xFF) / 255.0f, (float)(colorcode >> 8 & 0xFF) / 255.0f, (float)(colorcode & 0xFF) / 255.0f, alpha);
                    } else if (colorIndex != 16) {
                        if (colorIndex == 17) {
                            bold = true;
                            if (italic) {
                                GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                currentData = this.boldItalicChars;
                            } else {
                                GlStateManager.bindTexture(this.texBold.getGlTextureId());
                                currentData = this.boldChars;
                            }
                        } else if (colorIndex == 18) {
                            strikethrough = true;
                        } else if (colorIndex == 19) {
                            underline = true;
                        } else if (colorIndex == 20) {
                            italic = true;
                            if (bold) {
                                GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                currentData = this.boldItalicChars;
                            } else {
                                GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                                currentData = this.italicChars;
                            }
                        } else if (colorIndex == 21) {
                            bold = false;
                            italic = false;
                            underline = false;
                            strikethrough = false;
                            GlStateManager.color((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
                            GlStateManager.bindTexture(this.tex.getGlTextureId());
                            currentData = this.charData;
                        }
                    }
                    ++i;
                    continue;
                }
                if (character >= currentData.length || character < '\u0000') continue;
                GL11.glBegin(4);
                this.drawChar(currentData, character, (float)x, (float)y);
                GL11.glEnd();
                if (strikethrough) {
                    this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double)currentData[character].width - 8.0, y + (double)(currentData[character].height / 2), 1.0f);
                }
                if (underline) {
                    this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double)currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0, 1.0f);
                }
                x += (double)(currentData[character].width - 8 + this.charOffset);
            }
            GL11.glHint(3155, 4352);
            GL11.glPopMatrix();
        }
        GlStateManager.resetColor();
        return (float)x / 2.0f;
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        CFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7' && i < size) {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? this.boldItalicChars : this.boldChars;
                } else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? this.boldItalicChars : this.italicChars;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
                continue;
            }
            if (character >= currentData.length || character < '\u0000') continue;
            width += currentData[character].width - 8 + this.charOffset;
        }
        return width / 2;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public List<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if ((double)this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            int lastColorCode = 65535;
            for (String word : words) {
                for (int i = 0; i < word.toCharArray().length; ++i) {
                    char c = word.toCharArray()[i];
                    if (c != '\u00a7' || i >= word.toCharArray().length - 1) continue;
                    lastColorCode = word.toCharArray()[i + 1];
                }
                StringBuilder stringBuilder = new StringBuilder();
                if ((double)this.getStringWidth(stringBuilder.append(((StringBuilder)((Object)currentWord)).toString()).append(word).append(" ").toString()) < width) {
                    currentWord = ((StringBuilder)((Object)currentWord)).toString() + word + " ";
                    continue;
                }
                finalWords.add(currentWord);
                currentWord = String.valueOf(167 + lastColorCode) + word + " ";
            }
            if (currentWord.length() > 0) {
                if ((double)this.getStringWidth(currentWord) < width) {
                    finalWords.add(String.valueOf(167 + lastColorCode) + currentWord + " ");
                    currentWord = "";
                } else {
                    for (String s : this.formatString(currentWord, width)) {
                        finalWords.add(s);
                    }
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }

    public List<String> formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        int lastColorCode = 65535;
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            if (c == '\u00a7' && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            StringBuilder stringBuilder = new StringBuilder();
            if ((double)this.getStringWidth(stringBuilder.append(((StringBuilder)((Object)currentWord)).toString()).append(c).toString()) < width) {
                currentWord = ((StringBuilder)((Object)currentWord)).toString() + c;
                continue;
            }
            finalWords.add(currentWord);
            currentWord = String.valueOf(167 + lastColorCode) + String.valueOf(c);
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }
        return finalWords;
    }

    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index >> 0 & 1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }

    public void drawOutlinedString(String text, float x, float y, int borderColor, int color) {
        this.drawString(text, x - 0.5f, y, borderColor);
        this.drawString(text, x + 0.5f, y, borderColor);
        this.drawString(text, x, y - 0.5f, borderColor);
        this.drawString(text, x, y + 0.5f, borderColor);
        this.drawString(text, x, y, color);
    }

    public void drawCenterOutlinedString(String text, float x, float y, int borderColor, int color) {
        this.drawString(text, x - (float)(this.getStringWidth(text) / 2) - 0.5f, y, borderColor);
        this.drawString(text, x - (float)(this.getStringWidth(text) / 2) + 0.5f, y, borderColor);
        this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y - 0.5f, borderColor);
        this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y + 0.5f, borderColor);
        this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public int drawStringWithShadow(String text, float x, float y, int color, int alpha) {
        String[] split;
        text = "\u00a7r" + text;
        float len = -1.0f;
        String[] array = split = text.split("\u00a7");
        for (String str : split) {
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            int Shadowcolor = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
            this.drawString(str, x + len + 0.5f, y + 0.5f, this.getColor(0, 0, 0, 80));
            this.drawString(str, x + len, y, this.getColor(col.getRed(), col.getGreen(), col.getBlue(), alpha));
            len += (float)(this.getStringWidth(str) + 1);
        }
        return (int)len;
    }

    public int drawStringWithShadow(String text, float x, float y, int color, int colorshadow, int shift, int alpha) {
        String[] split;
        text = "\u00a7r" + text;
        float len = -1.0f;
        String[] array = split = text.split("\u00a7");
        for (String str : split) {
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            int Shadowcolor = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
            this.drawString(str, x + len + (float)shift, y + (float)shift, colorshadow);
            this.drawString(str, x + len, y, this.getColor(col.getRed(), col.getGreen(), col.getBlue(), alpha));
            len += (float)(this.getStringWidth(str) + 1);
        }
        return (int)len;
    }

    public int getColor(int red, int green, int blue, int alpha) {
        boolean color = false;
        int color2 = 0 | alpha << 24;
        color2 |= red << 16;
        color2 |= green << 8;
        return color2 |= blue;
    }

    public String trimStringToWidthPassword(String p_78262_1_, int p_78262_2_, boolean custom) {
        p_78262_1_ = p_78262_1_.replaceAll("\u00c3\u201a", "");
        return "";
    }

    public String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_) {
        p_78262_1_ = p_78262_1_.replaceAll("\u00c3\u201a", "");
        return "";
    }

    public String trimStringToWidth(String p_78262_1_, int p_78262_2_) {
        p_78262_1_ = p_78262_1_.replaceAll("\u00c3\u201a", "");
        return "";
    }

    public int drawPassword(String text, double x2, float y2, int color) {
        return (int)this.drawString(text.replaceAll(".", "."), x2, y2, color, false);
    }

    public double getPasswordWidth(String text) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0.0;
    }
}

