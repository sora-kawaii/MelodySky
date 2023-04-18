/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI;

import java.awt.Color;
import org.lwjgl.input.Mouse;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.Utils.render.RenderUtil;

public final class HUDWindow {
    public HUDApi api;
    public boolean drag;
    public int x;
    public int y;
    public int dragX;
    public int dragY;

    public HUDWindow(HUDApi hUDApi, int n, int n2) {
        this.api = hUDApi;
        this.x = n;
        this.y = n2;
    }

    public void render(int n, int n2) {
        if (this.api.isEnabled()) {
            RenderUtil.drawFilledCircle(this.api.x - 4, this.api.y - 4, 4.0f, new Color(0, 177, 35));
        } else {
            RenderUtil.drawFilledCircle(this.api.x - 4, this.api.y - 4, 4.0f, new Color(45, 49, 45));
        }
        if (this.drag) {
            if (!Mouse.isButtonDown(0)) {
                this.drag = false;
            }
            this.x = n - this.dragX;
            this.y = n2 - this.dragY;
            this.api.setXY(this.x, this.y);
        }
    }

    public void mouseScroll(int n, int n2, int n3) {
    }

    public void click(int n, int n2, int n3) {
        if (n > this.api.x - 8 && n < this.api.x && n2 > this.api.y - 8 && n2 < this.api.y) {
            if (n3 == 0) {
                this.drag = true;
                this.dragX = n - this.x;
                this.dragY = n2 - this.y;
            }
            if (n3 == 1) {
                this.api.setEnabled(!this.api.isEnabled());
            }
        }
    }
}

