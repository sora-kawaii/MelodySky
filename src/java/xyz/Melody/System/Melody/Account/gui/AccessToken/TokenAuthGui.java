/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account.gui.AccessToken;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Session;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.shader.GaussianBlur;
import xyz.Melody.injection.mixins.client.MCA;

public final class TokenAuthGui
extends GuiScreen {
    private GuiScreen previousScreen;
    private String status = "Session:";
    private GuiTextField sessionField;
    private ScaledResolution sr;
    private GaussianBlur gb = new GaussianBlur();

    public TokenAuthGui(GuiScreen guiScreen) {
        this.previousScreen = guiScreen;
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        this.sr = new ScaledResolution(this.field_146297_k);
        this.sessionField = new GuiTextField(1, this.field_146297_k.field_71466_p, this.sr.func_78326_a() / 2 - 100, this.sr.func_78328_b() / 2 - 30, 200, 20);
        this.sessionField.func_146203_f(Short.MAX_VALUE);
        this.sessionField.func_146195_b(true);
        this.field_146292_n.add(new GuiButton(997, this.sr.func_78326_a() / 2 - 100, this.sr.func_78328_b() / 2, 200, 20, "Login"));
        this.field_146292_n.add(new GuiButton(999, this.sr.func_78326_a() / 2 - 100, this.sr.func_78328_b() / 2 + 30, 200, 20, "Back"));
        super.func_73866_w_();
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        super.func_146281_b();
    }

    public void func_73863_a(int n, int n2, float f) {
        this.func_146276_q_();
        this.field_146297_k.field_71466_p.func_78276_b("Input Format: 1.name:uuid:token 2.token", 20, 13, Color.WHITE.getRGB());
        this.field_146297_k.field_71466_p.func_78276_b("1.name:uuid:token ", 35, 24, Color.WHITE.getRGB());
        this.field_146297_k.field_71466_p.func_78276_b("2.token", 35, 35, Color.WHITE.getRGB());
        this.field_146297_k.field_71466_p.func_78276_b(this.status, this.sr.func_78326_a() / 2 - this.field_146297_k.field_71466_p.func_78256_a(this.status) / 2, this.sr.func_78328_b() / 2 - 60, Color.WHITE.getRGB());
        this.sessionField.func_146194_f();
        super.func_73863_a(n, n2, f);
    }

    protected void func_146284_a(GuiButton guiButton) throws IOException {
        if (guiButton.field_146127_k == 997) {
            try {
                String string;
                String string2;
                String string3;
                String string4 = this.sessionField.func_146179_b();
                if (string4.contains(":")) {
                    string3 = string4.split(":")[0];
                    string2 = string4.split(":")[1];
                    string = string4.split(":")[2];
                } else {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("https://api.minecraftservices.com/minecraft/profile/").openConnection();
                    httpURLConnection.setRequestProperty("Content-type", "application/json");
                    httpURLConnection.setRequestProperty("Authorization", "Bearer " + this.sessionField.func_146179_b());
                    httpURLConnection.setDoOutput(true);
                    JsonObject jsonObject = new JsonParser().parse(IOUtils.toString(httpURLConnection.getInputStream())).getAsJsonObject();
                    string3 = jsonObject.get("name").getAsString();
                    string2 = jsonObject.get("id").getAsString();
                    string = string4;
                    NotificationPublisher.queue("Account Loggedin!", "Logged in as: " + this.field_146297_k.func_110432_I().func_111285_a(), NotificationType.SUCCESS, 3000);
                }
                ((MCA)((Object)this.field_146297_k)).setSession(new Session(string3, string2, string, "mojang"));
                this.field_146297_k.func_147108_a(this.previousScreen);
            }
            catch (Exception exception) {
                this.status = "\u00a7cError: Couldn't set session (check mc logs)";
                exception.printStackTrace();
            }
        }
        if (guiButton.field_146127_k == 999) {
            try {
                this.field_146297_k.func_147108_a(this.previousScreen);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        super.func_146284_a(guiButton);
    }

    protected void func_73869_a(char c, int n) throws IOException {
        this.sessionField.func_146201_a(c, n);
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

