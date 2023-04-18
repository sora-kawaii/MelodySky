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
            for (int i = 0; i < HUDManager.getApis().size(); ++i) {
                HUDApi hUDApi = HUDManager.getApis().get(i);
                HUDWindows.add(new HUDWindow(hUDApi, hUDApi.x, hUDApi.y));
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
    public void drawScreen(int n, int n2, float f) {
        if (this.mc.theWorld == null) {
            RenderUtil.drawImage(new ResourceLocation("Melody/background.png"), 0.0f, 0.0f, this.width, this.height);
            ParticleUtils.drawParticles(n, n2);
        }
        if (Mouse.hasWheel()) {
            int n3 = Mouse.getDWheel();
            this.scrollVelocity = n3 < 0 ? -120 : (n3 > 0 ? 130 : 0);
        }
        FontLoaders.NMSL20.drawStringWithShadow("RightClick To Toggle.", this.width / 2 - FontLoaders.NMSL20.getStringWidth("RightClick To Toggle.") / 2, this.height - this.height + 9, new Color(255, 20, 0).getRGB());
        HUDWindows.forEach(hUDWindow -> hUDWindow.render(n, n2));
        HUDManager.getApis().forEach(hUDApi -> hUDApi.InScreenRender());
        HUDWindows.forEach(hUDWindow -> hUDWindow.mouseScroll(n, n2, this.scrollVelocity));
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        HUDWindows.forEach(hUDWindow -> hUDWindow.click(n, n2, n3));
        super.mouseClicked(n, n2, n3);
    }

    @Override
    public void onGuiClosed() {
        this.mc.entityRenderer.stopUseShader();
        String string = "";
        for (HUDApi hUDApi : HUDManager.getApis()) {
            string = ((StringBuilder)((Object)string)).toString() + String.format("%s:%s:%s:%s%s", hUDApi.getName(), hUDApi.x, hUDApi.y, hUDApi.isEnabled(), System.lineSeparator());
        }
        FileManager.save("HUD.txt", string, false);
    }
}

