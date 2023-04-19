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

    public ValueButton(Value value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.name = "";
        if (this.value instanceof Option) {
            this.change = (Boolean)((Option)this.value).getValue();
        } else if (this.value instanceof Mode) {
            this.name = "" + ((Mode)this.value).getValue();
        } else if (value instanceof Numbers) {
            Numbers v = (Numbers)value;
            this.name = ((StringBuilder)((Object)this.name)).toString() + ((Number)v.getValue()).doubleValue();
        }
        this.opacity = 0.0;
    }

    public void render(int mouseX, int mouseY) {
        if (!this.custom) {
            Numbers v;
            this.opacity = mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.NMSL18.getStringHeight(this.value.getName()) + 5 ? (this.opacity + 10.0 < 200.0 ? (this.opacity += 10.0) : 200.0) : (this.opacity - 6.0 > 0.0 ? (this.opacity -= 6.0) : 0.0);
            if (this.value instanceof Option) {
                this.change = (Boolean)((Option)this.value).getValue();
            } else if (this.value instanceof Mode) {
                this.name = "" + ((Mode)this.value).getValue();
            } else if (this.value instanceof Numbers) {
                v = (Numbers)this.value;
                this.name = "" + ((Number)v.getValue()).doubleValue();
                if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + this.mfont.getStringHeight(this.value.getName()) + 5 && Mouse.isButtonDown(0)) {
                    double min = ((Number)v.getMinimum()).doubleValue();
                    double max = ((Number)v.getMaximum()).doubleValue();
                    double inc = ((Number)v.getIncrement()).doubleValue();
                    double valAbs = (double)mouseX - ((double)this.x + 1.0);
                    double perc = valAbs / 68.0;
                    perc = Math.min(Math.max(0.0, perc), 1.0);
                    double valRel = (max - min) * perc;
                    double val = min + valRel;
                    val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                    v.setValue(val);
                }
            }
            Gui.drawRect(0, 0, 0, 0, 0);
            RenderUtil.drawFastRoundedRect(this.x - 10, this.y - 4, this.x + 80, this.y + 11, 1.0f, new Color(220, 220, 220).getRGB());
            this.mfont.drawString(this.value.getName(), this.x - 5, this.y + 1, new Color(108, 108, 108).getRGB());
            this.mfont.drawString(this.name, this.x + 76 - this.mfont.getStringWidth(this.name), this.y + 2, new Color(108, 108, 108).getRGB());
            if (this.value instanceof Numbers) {
                v = (Numbers)this.value;
                double render = 68.0f * (((Number)v.getValue()).floatValue() - ((Number)v.getMinimum()).floatValue()) / (((Number)v.getMaximum()).floatValue() - ((Number)v.getMinimum()).floatValue());
                RenderUtil.drawFastRoundedRect(this.x, this.y + this.mfont.getStringHeight(this.value.getName()) + 3, (float)((double)this.x + render + 1.0), this.y + this.mfont.getStringHeight(this.value.getName()) + 4, 1.0f, new Color(68, 68, 68).getRGB());
            }
            if (this.change) {
                Gui.drawRect(this.x + 70, this.y, this.x + 77, this.y + 7, new Color(108, 108, 108).getRGB());
            }
        }
    }

    public void key(char typedChar, int keyCode) {
    }

    public void click(int mouseX, int mouseY, int button) {
        if (!this.custom && mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.NMSL18.getStringHeight(this.value.getName()) + 5) {
            if (this.value instanceof Option) {
                Option v;
                v.setValue((Boolean)(v = (Option)this.value).getValue() == false);
                return;
            }
            if (this.value instanceof Mode) {
                Mode m = (Mode)this.value;
                Enum current = (Enum)m.getValue();
                int next = current.ordinal() + 1 >= m.getModes().length ? 0 : current.ordinal() + 1;
                this.value.setValue(m.getModes()[next]);
            }
        }
    }
}

