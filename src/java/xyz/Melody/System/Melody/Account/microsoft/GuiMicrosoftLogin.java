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
    private MicrosoftLogin microsoftLogin;
    private boolean closed = false;
    private boolean done = false;
    private final GuiScreen parentScreen;
    private String message = "Initializing...";
    private TimerUtil timer = new TimerUtil();
    private GaussianBlur gb = new GaussianBlur();
    private String[] s = new String[]{"Stage 1: ", "Stage 2: ", "Stage 3: ", "Stage 4: ", "Stage 5: "};

    public GuiMicrosoftLogin(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        new Thread("MicrosoftLogin Thread"){

            @Override
            public void run() {
                try {
                    GuiMicrosoftLogin.this.timer.reset();
                    GuiMicrosoftLogin.this.microsoftLogin = new MicrosoftLogin(false);
                    while (GuiMicrosoftLogin.this.mc.currentScreen instanceof GuiMicrosoftLogin) {
                        if (GuiMicrosoftLogin.this.timer.hasReached(10000.0) && !((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.initDone) {
                            GuiMicrosoftLogin.this.message = "Failed: Could not Initialize XBoxLive.";
                            GuiMicrosoftLogin.this.microsoftLogin.close();
                            ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.status = (Object)((Object)EnumChatFormatting.RED) + "Failed: Timed out.";
                            GuiMicrosoftLogin.this.closed = true;
                        } else {
                            if (!((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.logged) continue;
                            GuiMicrosoftLogin.this.microsoftLogin.close();
                            ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.status = (Object)((Object)EnumChatFormatting.GREEN) + "Success! " + ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.userName;
                            ((MCA)((Object)GuiMicrosoftLogin.this.mc)).setSession(new Session(((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.userName, ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.uuid, ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.accessToken, "mojang"));
                            GuiMicrosoftLogin.this.closed = true;
                        }
                        break;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    if (GuiMicrosoftLogin.this.microsoftLogin != null) {
                        GuiMicrosoftLogin.this.message = "Failed: " + e.getClass().getName() + ":" + e.getMessage();
                        ((GuiMicrosoftLogin)GuiMicrosoftLogin.this).microsoftLogin.status = (Object)((Object)EnumChatFormatting.RED) + "Failed " + e.getClass().getName() + ":" + e.getMessage();
                        GuiMicrosoftLogin.this.microsoftLogin.close();
                    }
                    GuiMicrosoftLogin.this.closed = true;
                }
                this.interrupt();
            }
        }.start();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) {
            if (this.microsoftLogin != null) {
                this.microsoftLogin.bruhSir.close();
            }
            if (this.microsoftLogin != null && !this.closed) {
                this.microsoftLogin.close();
                this.closed = true;
            }
            if (this.closed) {
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.done) {
            this.buttonList.add(new ClientButton(0, this.width / 2 - 75, this.height / 2 + 20, 150, 20, "Back", new Color(0, 0, 0, 110)));
        } else {
            this.buttonList.add(new ClientButton(0, this.width / 2 - 75, this.height / 2 + 20, 150, 20, "Cancle", new Color(0, 0, 0, 110)));
        }
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
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
                this.mc.fontRendererObj.drawStringWithShadow(this.microsoftLogin.status, (float)this.width / 2.0f - (float)(this.mc.fontRendererObj.getStringWidth(this.microsoftLogin.status) / 2), (float)this.height / 2.0f - 5.0f, -1);
            } else {
                for (int i = this.s.length - 1; i >= 0; --i) {
                    String str = i == this.microsoftLogin.stage - 1 ? (Object)((Object)EnumChatFormatting.GREEN) + this.s[i] : this.s[i];
                    this.mc.fontRendererObj.drawStringWithShadow(str, (float)this.width / 2.0f - (float)(this.mc.fontRendererObj.getStringWidth(str) / 2), (float)this.height / 2.0f - 5.0f - (float)(i * 12), -1);
                }
                if (this.microsoftLogin.stage == 5 && !this.done) {
                    this.done = true;
                    this.initGui();
                }
            }
        } else {
            this.mc.fontRendererObj.drawStringWithShadow(this.message, (float)this.width / 2.0f - (float)(this.mc.fontRendererObj.getStringWidth(this.message) / 2), (float)this.height / 2.0f - 5.0f, -1);
        }
    }

    @Override
    public void drawDefaultBackground() {
        BackgroundShader.BACKGROUND_SHADER.startShader();
        Tessellator instance = Tessellator.getInstance();
        WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(0.0, this.height, 0.0).endVertex();
        worldRenderer.pos(this.width, this.height, 0.0).endVertex();
        worldRenderer.pos(this.width, 0.0, 0.0).endVertex();
        worldRenderer.pos(0.0, 0.0, 0.0).endVertex();
        instance.draw();
        BackgroundShader.BACKGROUND_SHADER.stopShader();
        this.gb.renderBlur(140.0f);
    }
}

