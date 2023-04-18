/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account.gui;

import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Keyboard;
import xyz.Melody.Client;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.System.Melody.Account.altimpl.OfflineAlt;
import xyz.Melody.Utils.shader.GaussianBlur;

public final class AddOfflineGui
extends GuiScreen {
    private GuiScreen previousScreen;
    private String status = "Name: ";
    private GuiTextField nameField;
    private ScaledResolution sr;
    private GaussianBlur gb = new GaussianBlur();

    public AddOfflineGui(GuiScreen guiScreen) {
        this.previousScreen = guiScreen;
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        this.sr = new ScaledResolution(this.field_146297_k);
        this.nameField = new GuiTextField(1, this.field_146297_k.field_71466_p, this.sr.func_78326_a() / 2 - 100, this.sr.func_78328_b() / 2 - 30, 200, 20);
        this.nameField.func_146203_f(Short.MAX_VALUE);
        this.nameField.func_146195_b(true);
        this.field_146292_n.add(new GuiButton(997, this.sr.func_78326_a() / 2 - 100, this.sr.func_78328_b() / 2, 200, 20, "Add"));
        this.field_146292_n.add(new GuiButton(999, this.sr.func_78326_a() / 2 - 100, this.sr.func_78328_b() / 2 + 30, 200, 20, "Back"));
        super.func_73866_w_();
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        super.func_146281_b();
    }

    public void func_73863_a(int n, int n2, float f) {
        this.func_146276_q_();
        this.field_146297_k.field_71466_p.func_78276_b(this.status, this.sr.func_78326_a() / 2 - this.field_146297_k.field_71466_p.func_78256_a(this.status) / 2, this.sr.func_78328_b() / 2 - 60, Color.WHITE.getRGB());
        this.nameField.func_146194_f();
        super.func_73863_a(n, n2, f);
    }

    protected void func_146284_a(GuiButton guiButton) throws IOException {
        if (guiButton.field_146127_k == 997) {
            try {
                String string = this.nameField.func_146179_b();
                Client.instance.getAccountManager().addAlt(new OfflineAlt(string));
                this.field_146297_k.func_147108_a(this.previousScreen);
            }
            catch (Exception exception) {
                this.status = "\u00a7cError: Couldn't set session (check mc logs)";
                exception.printStackTrace();
            }
        }
        if (guiButton.field_146127_k == 999) {
            this.field_146297_k.func_147108_a(this.previousScreen);
        }
        super.func_146284_a(guiButton);
    }

    protected void func_73869_a(char c, int n) throws IOException {
        this.nameField.func_146201_a(c, n);
        if (1 == n) {
            this.field_146297_k.func_147108_a(this.previousScreen);
        } else {
            super.func_73869_a(c, n);
        }
    }

    public void func_146276_q_() {
        BackgroundShader.BACKGROUND_SHADER.startShader();
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(0.0, this.field_146295_m, 0.0).func_181675_d();
        worldRenderer.func_181662_b(this.field_146294_l, this.field_146295_m, 0.0).func_181675_d();
        worldRenderer.func_181662_b(this.field_146294_l, 0.0, 0.0).func_181675_d();
        worldRenderer.func_181662_b(0.0, 0.0, 0.0).func_181675_d();
        tessellator.func_78381_a();
        BackgroundShader.BACKGROUND_SHADER.stopShader();
        this.gb.renderBlur(140.0f);
    }
}

