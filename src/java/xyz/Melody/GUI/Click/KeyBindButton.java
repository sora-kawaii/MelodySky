/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Click;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import xyz.Melody.GUI.Click.ClickUi;
import xyz.Melody.GUI.Click.ValueButton;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.module.Module;

public final class KeyBindButton
extends ValueButton {
    public Module cheat;
    public double opacity = 0.0;
    public boolean bind;
    CFontRenderer font = FontLoaders.NMSL18;

    public KeyBindButton(Module module, int n, int n2) {
        super(null, n, n2);
        this.custom = true;
        this.bind = false;
        this.cheat = module;
    }

    @Override
    public void render(int n, int n2) {
        Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
        Gui.func_73734_a((int)(this.x - 10), (int)(this.y - 4), (int)(this.x + 80), (int)(this.y + 11), (int)new Color(220, 220, 220).getRGB());
        this.mfont.drawString("Bind", this.x - 5, this.y + 2, new Color(108, 108, 108).getRGB());
        this.mfont.drawString(((StringBuilder)((Object)(this.bind ? "" : ""))).toString() + Keyboard.getKeyName(this.cheat.getKey()), this.x + 76 - this.mfont.getStringWidth(Keyboard.getKeyName(this.cheat.getKey())), this.y + 2, new Color(108, 108, 108).getRGB());
    }

    @Override
    public void key(char c, int n) {
        if (this.bind) {
            this.cheat.setKey(n);
            ClickUi.binding = false;
            this.bind = false;
        }
        super.key(c, n);
    }

    @Override
    public void click(int n, int n2, int n3) {
        if (n > this.x - 7 && n < this.x + 85 && n2 > this.y - 6 && n2 < this.y + this.font.getStringHeight(this.cheat.getName()) + 5 && n3 == 0) {
            boolean bl;
            this.bind = bl = !this.bind;
            ClickUi.binding = bl;
        }
        if (n > this.x - 7 && n < this.x + 85 && n2 > this.y - 6 && n2 < this.y + this.font.getStringHeight(this.cheat.getName()) + 5 && n3 == 1) {
            this.cheat.setKey(0);
        }
        super.click(n, n2, n3);
    }
}

