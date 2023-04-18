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

    public void func_73866_w_() {
        if (this.field_146297_k.field_71441_e != null) {
            this.field_146297_k.field_71460_t.func_175069_a(new ResourceLocation("shaders/post/blur.json"));
        }
        super.func_73866_w_();
    }

    public void func_73863_a(int n, int n2, float f) {
        if (this.field_146297_k.field_71441_e == null) {
            RenderUtil.drawImage(new ResourceLocation("Melody/background.png"), 0.0f, 0.0f, this.field_146294_l, this.field_146295_m);
            ParticleUtils.drawParticles(n, n2);
        }
        if (Mouse.hasWheel()) {
            int n3 = Mouse.getDWheel();
            this.scrollVelocity = n3 < 0 ? -120 : (n3 > 0 ? 130 : 0);
        }
        FontLoaders.NMSL20.drawStringWithShadow("RightClick To Toggle.", this.field_146294_l / 2 - FontLoaders.NMSL20.getStringWidth("RightClick To Toggle.") / 2, this.field_146295_m - this.field_146295_m + 9, new Color(255, 20, 0).getRGB());
        HUDWindows.forEach(hUDWindow -> hUDWindow.render(n, n2));
        HUDManager.getApis().forEach(hUDApi -> hUDApi.InScreenRender());
        HUDWindows.forEach(hUDWindow -> hUDWindow.mouseScroll(n, n2, this.scrollVelocity));
        super.func_73863_a(n, n2, f);
    }

    protected void func_73864_a(int n, int n2, int n3) throws IOException {
        HUDWindows.forEach(hUDWindow -> hUDWindow.click(n, n2, n3));
        super.func_73864_a(n, n2, n3);
    }

    public void func_146281_b() {
        this.field_146297_k.field_71460_t.func_181022_b();
        String string = "";
        for (HUDApi hUDApi : HUDManager.getApis()) {
            string = ((StringBuilder)((Object)string)).toString() + String.format("%s:%s:%s:%s%s", hUDApi.getName(), hUDApi.x, hUDApi.y, hUDApi.isEnabled(), System.lineSeparator());
        }
        FileManager.save("HUD.txt", string, false);
    }
}

