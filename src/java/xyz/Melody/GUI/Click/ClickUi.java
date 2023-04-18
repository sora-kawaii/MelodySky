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
            int n = 5;
            for (ModuleType moduleType : ModuleType.values()) {
                windows.add(new Window(moduleType, n, 5));
                n += 105;
            }
        }
    }

    public ClickUi(boolean bl) {
        this.jb = bl;
        this.opacity = new Opacity(10);
        this.animpos = 75.0f;
        if (windows.isEmpty()) {
            int n = 5;
            for (ModuleType moduleType : ModuleType.values()) {
                windows.add(new Window(moduleType, n, 5));
                n += 105;
            }
        }
    }

    public void func_73863_a(int n, int n2, float f) {
        this.animpos = AnimationUtil.moveUD(this.animpos, 1.0f, 0.1f, 0.1f);
        if (this.jb) {
            this.func_146276_q_();
            this.gb.renderBlur(this.opacity.getOpacity());
            this.opacity.interp(140.0f, 5.0f);
        }
        RenderUtil.drawImage(new ResourceLocation("Melody/Melody.png"), this.field_146294_l - 160, this.field_146295_m - 40, 32.0f, 32.0f);
        GL11.glEnable(3042);
        FontLoaders.CNMD34.drawStringWithShadow("MelodySky", this.field_146294_l - 125, this.field_146295_m - 34, -1);
        GL11.glDisable(3042);
        GlStateManager.func_179114_b((float)this.animpos, (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179109_b((float)0.0f, (float)this.animpos, (float)0.0f);
        GlStateManager.func_179094_E();
        windows.forEach(window -> window.render(n, n2));
        GlStateManager.func_179121_F();
        if (Mouse.hasWheel()) {
            int n3 = Mouse.getDWheel();
            this.scrollVelocity = n3 < 0 ? -120 : (n3 > 0 ? 120 : 0);
        }
        windows.forEach(window -> window.mouseScroll(n, n2, this.scrollVelocity));
        super.func_73863_a(n, n2, f);
    }

    protected void func_73864_a(int n, int n2, int n3) throws IOException {
        windows.forEach(window -> window.click(n, n2, n3));
        super.func_73864_a(n, n2, n3);
    }

    protected void func_73869_a(char c, int n) throws IOException {
        if (n == 1 && !binding) {
            this.field_146297_k.func_147108_a(null);
            return;
        }
        windows.forEach(window -> window.key(c, n));
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
        ParticleUtils.drawParticles();
    }

    public void func_146281_b() {
        this.alpha = 0;
        this.timer.reset();
        Client.instance.saveConfig(false);
        Client.instance.saveUISettings(false);
        this.field_146297_k.field_71460_t.func_181022_b();
    }

    public synchronized void sendToFront(Window window) {
        int n = 0;
        for (int i = 0; i < windows.size(); ++i) {
            if (windows.get(i) != window) continue;
            n = i;
            break;
        }
        Window window2 = windows.get(windows.size() - 1);
        windows.set(windows.size() - 1, windows.get(n));
        windows.set(n, window2);
    }
}

