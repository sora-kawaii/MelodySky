/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Melody.Account.microsoft;

import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import xyz.Melody.GUI.ClientButton;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.System.Melody.Account.microsoft.MicrosoftLogin;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.shader.GaussianBlur;
import xyz.Melody.injection.mixins.client.MCA;

public final class GuiMicrosoftLogin
extends GuiScreen {
    private volatile MicrosoftLogin microsoftLogin;
    private volatile boolean closed = false;
    private volatile boolean done = false;
    private final GuiScreen parentScreen;
    private String message = "Initializing...";
    private TimerUtil timer = new TimerUtil();
    private GaussianBlur gb = new GaussianBlur();
    private String[] s = new String[]{"Stage 1: ", "Stage 2: ", "Stage 3: ", "Stage 4: ", "Stage 5: "};

    public GuiMicrosoftLogin(GuiScreen guiScreen) {
        this.parentScreen = guiScreen;
        new Thread("MicrosoftLogin Thread"){

            @Override
            public void run() {
                try {
                    GuiMicrosoftLogin.this.timer.reset();
                    GuiMicrosoftLogin.this.microsoftLogin = new MicrosoftLogin(false);
                    while (GuiMicrosoftLogin.this.field_146297_k.field_71462_r instanceof GuiMicrosoftLogin) {
                        if (GuiMicrosoftLogin.this.timer.hasReached(10000.0) && !((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.initDone) {
                            GuiMicrosoftLogin.this.message = "Failed: Could not Initialize XBoxLive.";
                            GuiMicrosoftLogin.this.microsoftLogin.close();
                            ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.status = (Object)((Object)EnumChatFormatting.RED) + "Failed: Timed out.";
                            GuiMicrosoftLogin.this.closed = true;
                        } else {
                            if (!((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.logged) continue;
                            GuiMicrosoftLogin.this.microsoftLogin.close();
                            ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.userName;
                            ((MCA)((Object)GuiMicrosoftLogin.this.field_146297_k)).setSession(new Session(((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.userName, ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.uuid, ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.accessToken, "mojang"));
                            GuiMicrosoftLogin.this.closed = true;
                        }
                        break;
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    if (GuiMicrosoftLogin.this.microsoftLogin != null) {
                        GuiMicrosoftLogin.this.message = "Failed: " + exception.getClass().getName() + ":" + exception.getMessage();
                        ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.status = (Object)((Object)EnumChatFormatting.RED) + "Failed " + exception.getClass().getName() + ":" + exception.getMessage();
                        GuiMicrosoftLogin.this.microsoftLogin.close();
                    }
                    GuiMicrosoftLogin.this.closed = true;
                }
                this.interrupt();
            }
        }.start();
    }

    protected void func_146284_a(GuiButton guiButton) throws IOException {
        super.func_146284_a(guiButton);
        if (guiButton.field_146127_k == 0) {
            if (this.microsoftLogin != null) {
                this.microsoftLogin.bruhSir.close();
            }
            if (this.microsoftLogin != null && !this.closed) {
                this.microsoftLogin.close();
                this.closed = true;
            }
            if (this.closed) {
                this.field_146297_k.func_147108_a(this.parentScreen);
            }
        }
    }

    public void func_73866_w_() {
        this.field_146292_n.clear();
        if (this.done) {
            this.field_146292_n.add(new ClientButton(0, this.field_146294_l / 2 - 75, this.field_146295_m / 2 + 20, 150, 20, "Back", new Color(0, 0, 0, 110)));
        } else {
            this.field_146292_n.add(new ClientButton(0, this.field_146294_l / 2 - 75, this.field_146295_m / 2 + 20, 150, 20, "Cancle", new Color(0, 0, 0, 110)));
        }
        super.func_73866_w_();
    }

    public void func_73863_a(int n, int n2, float f) {
        this.func_146276_q_();
        super.func_73863_a(n, n2, f);
        if (this.microsoftLogin != null) {
            if (this.microsoftLogin.stage == 1) {
                this.s[0] = "Stage 1: " + this.microsoftLogin.status;
            }
            if (this.microsoftLogin.stage == 2) {
                this.s[1] = "Stage 2: " + this.microsoftLogin.status;
            }
            if (this.microsoftLogin.stage == 3) {
                this.s[2] = "Stage 3: " + this.microsoftLogin.status;
            }
            if (this.microsoftLogin.stage == 4) {
                this.s[3] = "Stage 4: " + this.microsoftLogin.status;
            }
            if (this.microsoftLogin.stage == 5) {
                this.s[4] = "Stage 5: " + this.microsoftLogin.status;
            }
            if (this.microsoftLogin.stage == 0) {
                this.field_146297_k.field_71466_p.func_175063_a(this.microsoftLogin.status, (float)this.field_146294_l / 2.0f - (float)(this.field_146297_k.field_71466_p.func_78256_a(this.microsoftLogin.status) / 2), (float)this.field_146295_m / 2.0f - 5.0f, -1);
            } else {
                for (int i = this.s.length - 1; i >= 0; --i) {
                    String string = i == this.microsoftLogin.stage - 1 ? (Object)((Object)EnumChatFormatting.GREEN) + this.s[i] : this.s[i];
                    this.field_146297_k.field_71466_p.func_175063_a(string, (float)this.field_146294_l / 2.0f - (float)(this.field_146297_k.field_71466_p.func_78256_a(string) / 2), (float)this.field_146295_m / 2.0f - 5.0f - (float)(i * 12), -1);
                }
                if (this.microsoftLogin.stage == 5 && !this.done) {
                    this.done = true;
                    this.func_73866_w_();
                }
            }
        } else {
            this.field_146297_k.field_71466_p.func_175063_a(this.message, (float)this.field_146294_l / 2.0f - (float)(this.field_146297_k.field_71466_p.func_78256_a(this.message) / 2), (float)this.field_146295_m / 2.0f - 5.0f, -1);
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

