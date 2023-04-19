/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDManager;
import xyz.Melody.GUI.CustomUI.HUDWindow;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.GUI.Particles.ParticleUtils;
import xyz.Melody.System.Managers.Client.FileManager;
import xyz.Melody.Utils.render.RenderUtil;

public final class HUDScreen
extends GuiScreen {
    public static ArrayList<HUDWindow> HUDWindows = Lists.newArrayList();
    public int scrollVelocity;

    public HUDScreen() {
        if (HUDWindows.isEmpty()) {
            for (int n22 = 0; n22 < HUDManager.getApis().size(); ++n22) {
                HUDApi c2 = HUDManager.getApis().get(n22);
                HUDWindows.add(new HUDWindow(c2, c2.x, c2.y));
            }
        }
    }

    @Override
    public void initGui() {
        if (this.mc.theWorld != null) {
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.mc.theWorld == null) {
            RenderUtil.drawImage(new ResourceLocation("Melody/background.png"), 0.0f, 0.0f, this.width, this.height);
            ParticleUtils.drawParticles(mouseX, mouseY);
        }
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            this.scrollVelocity = wheel < 0 ? -120 : (wheel > 0 ? 130 : 0);
        }
        FontLoaders.NMSL20.drawStringWithShadow("RightClick To Toggle.", this.width / 2 - FontLoaders.NMSL20.getStringWidth("RightClick To Toggle.") / 2, this.height - this.height + 9, new Color(255, 20, 0).getRGB());
        HUDWindows.forEach(w2 -> w2.render(mouseX, mouseY));
        HUDManager.getApis().forEach(w2 -> w2.InScreenRender());
        HUDWindows.forEach(arg_0 -> this.lambda$drawScreen$2(mouseX, mouseY, arg_0));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        HUDWindows.forEach(w2 -> w2.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        this.mc.entityRenderer.stopUseShader();
        String x = "";
        for (HUDApi m : HUDManager.getApis()) {
            x = ((StringBuilder)((Object)x)).toString() + String.format("%s:%s:%s:%s%s", m.getName(), m.x, m.y, m.isEnabled(), System.lineSeparator());
        }
        FileManager.save("HUD.txt", x, false);
    }

    private void lambda$drawScreen$2(int mouseX, int mouseY, HUDWindow w2) {
        w2.mouseScroll(mouseX, mouseY, this.scrollVelocity);
    }
}

