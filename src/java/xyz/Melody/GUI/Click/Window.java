/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Click;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import xyz.Melody.GUI.Click.Button;
import xyz.Melody.GUI.Click.ValueButton;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.System.Managers.Client.ModuleManager;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.modules.others.HUD;

public final class Window {
    CFontRenderer font;
    public ModuleType category;
    public ArrayList<Button> buttons;
    public boolean drag;
    public boolean extended;
    public int x;
    public int y;
    public int expand;
    public int dragX;
    public int dragY;
    public int max;
    public int scroll;
    public int scrollTo;
    public double angel;
    private TimerUtil timer = new TimerUtil();

    public Window(ModuleType moduleType, int n, int n2) {
        this.font = FontLoaders.NMSL18;
        this.buttons = new ArrayList();
        this.category = moduleType;
        this.x = n;
        this.y = n2;
        this.max = 120;
        int n3 = n2 + 22;
        for (Module object : ModuleManager.getModules()) {
            if (object.getType() != moduleType || object instanceof HUD) continue;
            this.buttons.add(new Button(object, n + 5, n3));
            n3 += 15;
        }
        for (Button button : this.buttons) {
            button.setParent(this);
        }
    }

    public void render(int n, int n2) {
        int n3 = 0;
        for (Button object : this.buttons) {
            if (object.expand) {
                for (ValueButton valueButton : object.buttons) {
                    n3 += 15;
                }
            }
            n3 += 15;
        }
        int n4 = 15 + n3;
        if (this.extended) {
            this.expand = this.expand + 5 < n4 ? (this.expand = this.expand + 5) : n4;
            this.angel = this.angel + 20.0 < 180.0 ? (this.angel = this.angel + 20.0) : 180.0;
        } else {
            this.expand = this.expand - 5 > 0 ? (this.expand = this.expand - 5) : 0;
            this.angel = this.angel - 20.0 > 0.0 ? (this.angel = this.angel - 20.0) : 0.0;
        }
        RenderUtil.drawFastRoundedRect(this.x - 2, this.y, this.x + 92, this.y + 17, 1.0f, new Color(255, 255, 255).getRGB());
        GlStateManager.resetColor();
        this.font.drawString(this.category.name(), this.x + 15, this.y + 6, new Color(108, 108, 108).getRGB());
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.x + 90 - 10, this.y + 5, 0.0f);
        GlStateManager.rotate((float)this.angel, 0.0f, 0.0f, -1.0f);
        GlStateManager.translate(-this.x + 90 - 10, -this.y + 5, 0.0f);
        GlStateManager.popMatrix();
        if (this.expand > 0) {
            this.buttons.forEach(button -> button.render(n, n2));
        }
        if (this.drag) {
            if (!Mouse.isButtonDown(0)) {
                this.drag = false;
            }
            this.x = n - this.dragX;
            this.y = n2 - this.dragY;
            this.buttons.get((int)0).y = this.y + 22 - this.scroll;
            for (Button button2 : this.buttons) {
                button2.x = this.x + 5;
            }
        }
    }

    public void key(char c, int n) {
        this.buttons.forEach(button -> button.key(c, n));
    }

    public void mouseScroll(int n, int n2, int n3) {
        if (n > this.x - 2 && n < this.x + 92 && n2 > this.y - 2 && n2 < this.y + 17 + this.expand) {
            this.scrollTo -= (int)((float)(n3 / 120 * 28));
        }
    }

    public void click(int n, int n2, int n3) {
        if (n > this.x - 2 && n < this.x + 92 && n2 > this.y - 2 && n2 < this.y + 17) {
            if (n3 == 1) {
                boolean bl = this.extended = !this.extended;
            }
            if (n3 == 0) {
                this.drag = true;
                this.dragX = n - this.x;
                this.dragY = n2 - this.y;
            }
        }
        if (this.extended) {
            this.buttons.stream().filter(button -> button.y < this.y + this.expand).forEach(button -> button.click(n, n2, n3));
        }
    }
}

