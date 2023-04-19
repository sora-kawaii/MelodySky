/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI;

import java.awt.Color;
import xyz.Melody.GUI.ClickNew.Opacity;
import xyz.Melody.Utils.render.RenderUtil;

public final class Circle {
    public Opacity op = new Opacity(0);
    public float hmx = 0.0f;
    public float hmy = 0.0f;
    public float width;

    public Circle(int mouseX, int mouseY, int width) {
        this.hmx = mouseX;
        this.hmy = mouseY;
        this.width = width;
    }

    public void draw() {
        if (this.op.getOpacity() < this.width + 10.0f) {
            this.op.interp(this.width + 10.0f, 2.5f);
        } else {
            this.op.interp(0.0f, 0.0f);
        }
        if (this.op.getOpacity() > 0.0f) {
            int a = (int)(255.0f - this.op.getOpacity() * 2.5f / this.width * 100.0f);
            RenderUtil.drawFilledCircle(this.hmx, this.hmy, this.op.getOpacity(), new Color(255, 255, 255, a > 0 ? a : 0));
        }
    }
}

