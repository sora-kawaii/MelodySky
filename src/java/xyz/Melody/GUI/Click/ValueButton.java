/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Click;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;

public class ValueButton {
    public Value value;
    public String name;
    public boolean custom = false;
    public boolean change;
    public int x;
    public int y;
    public double opacity = 0.0;
    CFontRenderer mfont = FontLoaders.NMSL18;

    public ValueButton(Value value, int n, int n2) {
        this.value = value;
        this.x = n;
        this.y = n2;
        this.name = "";
        if (this.value instanceof Option) {
            this.change = (Boolean)((Option)this.value).getValue();
        } else if (this.value instanceof Mode) {
            this.name = "" + ((Mode)this.value).getValue();
        } else if (value instanceof Numbers) {
            Numbers numbers = (Numbers)value;
            this.name = ((StringBuilder)((Object)this.name)).toString() + ((Number)numbers.getValue()).doubleValue();
        }
        this.opacity = 0.0;
    }

    public void render(int n, int n2) {
        if (!this.custom) {
            double d;
            Numbers numbers;
            this.opacity = n > this.x - 7 && n < this.x + 85 && n2 > this.y - 6 && n2 < this.y + FontLoaders.NMSL18.getStringHeight(this.value.getName()) + 5 ? (this.opacity + 10.0 < 200.0 ? (this.opacity += 10.0) : 200.0) : (this.opacity - 6.0 > 0.0 ? (this.opacity -= 6.0) : 0.0);
            if (this.value instanceof Option) {
                this.change = (Boolean)((Option)this.value).getValue();
            } else if (this.value instanceof Mode) {
                this.name = "" + ((Mode)this.value).getValue();
            } else if (this.value instanceof Numbers) {
                numbers = (Numbers)this.value;
                this.name = "" + ((Number)numbers.getValue()).doubleValue();
                if (n > this.x - 7 && n < this.x + 85 && n2 > this.y - 6 && n2 < this.y + this.mfont.getStringHeight(this.value.getName()) + 5 && Mouse.isButtonDown(0)) {
                    d = ((Number)numbers.getMinimum()).doubleValue();
                    double d2 = ((Number)numbers.getMaximum()).doubleValue();
                    double d3 = ((Number)numbers.getIncrement()).doubleValue();
                    double d4 = (double)n - ((double)this.x + 1.0);
                    double d5 = d4 / 68.0;
                    d5 = Math.min(Math.max(0.0, d5), 1.0);
                    double d6 = (d2 - d) * d5;
                    double d7 = d + d6;
                    d7 = (double)Math.round(d7 * (1.0 / d3)) / (1.0 / d3);
                    numbers.setValue(d7);
                }
            }
            Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
            RenderUtil.drawFastRoundedRect(this.x - 10, this.y - 4, this.x + 80, this.y + 11, 1.0f, new Color(220, 220, 220).getRGB());
            this.mfont.drawString(this.value.getName(), this.x - 5, this.y + 1, new Color(108, 108, 108).getRGB());
            this.mfont.drawString(this.name, this.x + 76 - this.mfont.getStringWidth(this.name), this.y + 2, new Color(108, 108, 108).getRGB());
            if (this.value instanceof Numbers) {
                numbers = (Numbers)this.value;
                d = 68.0f * (((Number)numbers.getValue()).floatValue() - ((Number)numbers.getMinimum()).floatValue()) / (((Number)numbers.getMaximum()).floatValue() - ((Number)numbers.getMinimum()).floatValue());
                RenderUtil.drawFastRoundedRect(this.x, this.y + this.mfont.getStringHeight(this.value.getName()) + 3, (float)((double)this.x + d + 1.0), this.y + this.mfont.getStringHeight(this.value.getName()) + 4, 1.0f, new Color(68, 68, 68).getRGB());
            }
            if (this.change) {
                Gui.func_73734_a((int)(this.x + 70), (int)this.y, (int)(this.x + 77), (int)(this.y + 7), (int)new Color(108, 108, 108).getRGB());
            }
        }
    }

    public void key(char c, int n) {
    }

    public void click(int n, int n2, int n3) {
        if (!this.custom && n > this.x - 7 && n < this.x + 85 && n2 > this.y - 6 && n2 < this.y + FontLoaders.NMSL18.getStringHeight(this.value.getName()) + 5) {
            if (this.value instanceof Option) {
                Option option;
                option.setValue((Boolean)(option = (Option)this.value).getValue() == false);
                return;
            }
            if (this.value instanceof Mode) {
                Mode mode = (Mode)this.value;
                Enum enum_ = (Enum)mode.getValue();
                int n4 = enum_.ordinal() + 1 >= mode.getModes().length ? 0 : enum_.ordinal() + 1;
                this.value.setValue(mode.getModes()[n4]);
            }
        }
    }
}

