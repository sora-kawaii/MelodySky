/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Mouse;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;

public final class KeyStrokes
extends HUDApi {
    int lastA = 0;
    int lastW = 0;
    int lastS = 0;
    int lastD = 0;
    double lastX = 0.0;
    double lastZ = 0.0;
    CFontRenderer ufr = FontLoaders.NMSL19;

    public KeyStrokes() {
        super("KeyStrokes", 66, 247);
        this.setEnabled(true);
    }

    @EventHandler
    public void onRender(EventRender2D event) {
        if (this.mc.currentScreen instanceof HUDScreen) {
            return;
        }
        this.keyRender();
    }

    @Override
    public void InScreenRender() {
        this.keyRender();
    }

    private void keyRender() {
        GameSettings set = this.mc.gameSettings;
        int press = new Color(230, 230, 230, 120).getRGB();
        int unPress = new Color(30, 30, 30, 120).getRGB();
        int colorW = set.keyBindForward.isKeyDown() ? press : unPress;
        int colorS = set.keyBindBack.isKeyDown() ? press : unPress;
        int colorA = set.keyBindLeft.isKeyDown() ? press : unPress;
        int colorD = set.keyBindRight.isKeyDown() ? press : unPress;
        int colorSpace = set.keyBindJump.isKeyDown() ? press : unPress;
        int colorL = Mouse.isButtonDown(0) ? press : unPress;
        int colorR = Mouse.isButtonDown(1) ? press : unPress;
        int colorW1 = set.keyBindForward.isKeyDown() ? unPress : press;
        int colorS1 = set.keyBindBack.isKeyDown() ? unPress : press;
        int colorA1 = set.keyBindLeft.isKeyDown() ? unPress : press;
        int colorD1 = set.keyBindRight.isKeyDown() ? unPress : press;
        int colorSpace1 = set.keyBindJump.isKeyDown() ? unPress : press;
        int colorLMB = Mouse.isButtonDown(0) ? unPress : press;
        int colorRMB = Mouse.isButtonDown(1) ? unPress : press;
        RenderUtil.drawFastRoundedRect(this.x, this.y, this.x + 25, this.y + 25, 2.0f, colorW);
        FontLoaders.NMSL20.drawString("W", this.x + 8, this.y + 9, colorW1);
        RenderUtil.drawFastRoundedRect(this.x, this.y + 29, this.x + 25, this.y + 54, 2.0f, colorS);
        FontLoaders.NMSL20.drawString("S", this.x + 10, this.y + 32 + 6, colorS1);
        RenderUtil.drawFastRoundedRect(this.x - 29, this.y + 29, this.x - 4, this.y + 54, 2.0f, colorA);
        FontLoaders.NMSL20.drawString("A", (float)(this.x - 32) + 12.5f, this.y + 32 + 6, colorA1);
        RenderUtil.drawFastRoundedRect(this.x + 29, this.y + 29, this.x + 54, this.y + 54, 2.0f, colorD);
        FontLoaders.NMSL20.drawString("D", this.x + 32 + 6, this.y + 32 + 6, colorD1);
        RenderUtil.drawFastRoundedRect(this.x - 29, this.y + 58, this.x + 11, this.y + 79, 2.0f, colorL);
        FontLoaders.NMSL20.drawString("LMB", this.x - 32 + 13, this.y + 64 + 1, colorLMB);
        RenderUtil.drawFastRoundedRect(this.x + 15, this.y + 58, this.x + 54, this.y + 79, 2.0f, colorR);
        FontLoaders.NMSL20.drawString("RMB", this.x + 18 + 6, this.y + 64 + 1, colorRMB);
        RenderUtil.drawFastRoundedRect(this.x - 29, this.y + 81, this.x + 54, this.y + 94, 2.0f, colorSpace);
        FontLoaders.NMSL20.drawString("-----", this.x - 29 + 31, this.y + 83, colorSpace1);
    }

    public int flop(int a, int b) {
        return b - a;
    }
}

