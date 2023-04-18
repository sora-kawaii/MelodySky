/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Click;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.Click.KeyBindButton;
import xyz.Melody.GUI.Click.ValueButton;
import xyz.Melody.GUI.Click.Window;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;

public class Button {
    public Module cheat;
    CFontRenderer font = FontLoaders.NMSL18;
    public Window parent;
    public int x;
    public int y;
    public int index;
    public int remander;
    public double opacity = 0.0;
    public ArrayList<ValueButton> buttons = new ArrayList();
    public boolean expand;

    public Button(Module module, int n, int n2) {
        this.cheat = module;
        this.x = n;
        this.y = n2;
        int n3 = n2 + 14;
        for (Value<?> value : module.getValues()) {
            this.buttons.add(new ValueButton(value, n + 5, n3));
            n3 += 15;
        }
        this.buttons.add(new KeyBindButton(module, n + 5, n3));
    }

    public void render(int n, int n2) {
        if (this.index != 0) {
            Button button = this.parent.buttons.get(this.index - 1);
            this.y = button.y + 15 + (button.expand ? 15 * button.buttons.size() : 0);
        }
        for (int i = 0; i < this.buttons.size(); ++i) {
            this.buttons.get((int)i).y = this.y + 14 + 15 * i;
            this.buttons.get((int)i).x = this.x + 5;
        }
        Gui.drawRect(this.x - 5, this.y - 5, this.x + 85, (int)((double)(this.y + this.font.getStringHeight(this.cheat.getName())) + 3.8), new Color(233, 233, 233).getRGB());
        if (this.cheat.isEnabled()) {
            RenderUtil.drawFastRoundedRect(this.x - 5, this.y - 5, this.x + 85, (float)((double)(this.y + this.font.getStringHeight(this.cheat.getName())) + 3.8), 1.0f, new Color(234, 234, 234).getRGB());
            this.font.drawString(this.cheat.getName(), this.x, this.y, new Color(47, 154, 241).getRGB());
        } else {
            this.font.drawString(this.cheat.getName(), this.x, this.y, new Color(108, 108, 108).getRGB());
        }
        if (this.expand || this.buttons.size() > 1) {
            // empty if block
        }
        if (this.expand) {
            this.buttons.forEach(valueButton -> valueButton.render(n, n2));
        }
    }

    public void key(char c, int n) {
        this.buttons.forEach(valueButton -> valueButton.key(c, n));
    }

    public void click(int n, int n2, int n3) {
        if (n > this.x - 7 && n < this.x + 85 && n2 > this.y - 6 && n2 < this.y + this.font.getStringHeight(this.cheat.getName()) + 4) {
            if (n3 == 0) {
                this.cheat.setEnabled(!this.cheat.isEnabled());
            }
            if (n3 == 1 && !this.buttons.isEmpty()) {
                boolean bl = this.expand = !this.expand;
            }
        }
        if (this.expand) {
            this.buttons.forEach(valueButton -> valueButton.click(n, n2, n3));
        }
    }

    public void setParent(Window window) {
        this.parent = window;
        for (int i = 0; i < this.parent.buttons.size(); ++i) {
            if (this.parent.buttons.get(i) != this) continue;
            this.index = i;
            this.remander = this.parent.buttons.size() - i;
            break;
        }
    }
}

