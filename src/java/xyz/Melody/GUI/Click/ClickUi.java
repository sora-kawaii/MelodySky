/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.Click;

import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.GUI.Click.Window;
import xyz.Melody.GUI.ClickNew.Opacity;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.GUI.ShaderBG.Shaders.BackgroundShader;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.animate.AnimationUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.Utils.shader.GaussianBlur;
import xyz.Melody.module.ModuleType;

public final class ClickUi
extends GuiScreen {
    private TimerUtil timer = new TimerUtil();
    private GaussianBlur gb = new GaussianBlur();
    public static ArrayList<Window> windows = new ArrayList();
    public int scrollVelocity;
    public static boolean binding = false;
    private int alpha = 0;
    private float animpos;
    private boolean jb;
    private Opacity opacity;

    public ClickUi() {
        this.jb = false;
        this.opacity = new Opacity(10);
        this.animpos = 75.0f;
        if (windows.isEmpty()) {
            int x = 5;
            for (ModuleType c : ModuleType.values()) {
                windows.add(new Window(c, x, 5));
                x += 105;
            }
        }
    }

    public ClickUi(boolean jb) {
        this.jb = jb;
        this.opacity = new Opacity(10);
        this.animpos = 75.0f;
        if (windows.isEmpty()) {
            int x = 5;
            for (ModuleType c : ModuleType.values()) {
                windows.add(new Window(c, x, 5));
                x += 105;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.animpos = AnimationUtil.moveUD(this.animpos, 1.0f, 0.1f, 0.1f);
        if (this.jb) {
            this.drawDefaultBackground();
            this.gb.renderBlur(this.opacity.getOpacity());
            this.opacity.interp(140.0f, 5.0f);
        }
        RenderUtil.drawImage(new ResourceLocation("Melody/Melody.png"), this.width - 160, this.height - 40, 32.0f, 32.0f);
        GL11.glEnable(3042);
        FontLoaders.CNMD34.drawStringWithShadow("MelodySky", this.width - 125, this.height - 34, -1);
        GL11.glDisable(3042);
        GlStateManager.rotate(this.animpos, 0.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, this.animpos, 0.0f);
        GlStateManager.pushMatrix();
        windows.forEach(w -> w.render(mouseX, mouseY));
        GlStateManager.popMatrix();
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            this.scrollVelocity = wheel < 0 ? -120 : (wheel > 0 ? 120 : 0);
        }
        windows.forEach(arg_0 -> this.lambda$drawScreen$1(mouseX, mouseY, arg_0));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        windows.forEach(w -> w.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 && !binding) {
            this.mc.displayGuiScreen(null);
            return;
        }
        windows.forEach(w -> w.key(typedChar, keyCode));
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
        ParticleUtils.drawParticles();
    }

    @Override
    public void onGuiClosed() {
        this.alpha = 0;
        this.timer.reset();
        Client.instance.saveConfig(false);
        Client.instance.saveUISettings(false);
        this.mc.entityRenderer.stopUseShader();
    }

    public synchronized void sendToFront(Window window) {
        int panelIndex = 0;
        for (int i = 0; i < windows.size(); ++i) {
            if (windows.get(i) != window) continue;
            panelIndex = i;
            break;
        }
        Window t = windows.get(windows.size() - 1);
        windows.set(windows.size() - 1, windows.get(panelIndex));
        windows.set(panelIndex, t);
    }

    private void lambda$drawScreen$1(int mouseX, int mouseY, Window w) {
        w.mouseScroll(mouseX, mouseY, this.scrollVelocity);
    }
}

