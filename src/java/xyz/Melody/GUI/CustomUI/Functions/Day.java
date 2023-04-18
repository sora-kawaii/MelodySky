/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;

public final class Day
extends HUDApi {
    private float xxx = 0.0f;

    public Day() {
        super("DayCounter", 300, 200);
        this.setEnabled(true);
    }

    @EventHandler
    public void onRender(EventRender2D eventRender2D) {
        if (this.mc.currentScreen instanceof HUDScreen) {
            return;
        }
        this.DAY();
    }

    @Override
    public void InScreenRender() {
        this.DAY();
    }

    private void DAY() {
        int n = new Color(30, 30, 30, 100).getRGB();
        CFontRenderer cFontRenderer = FontLoaders.NMSL20;
        RenderUtil.drawFastRoundedRect(this.x, this.y, this.x + cFontRenderer.getStringWidth("Day " + Long.valueOf(this.mc.theWorld.getWorldTime() / 24000L)) + 8, this.y + cFontRenderer.getStringHeight("Day " + Long.valueOf(this.mc.theWorld.getWorldTime() / 24000L)) + 6, 1.0f, n);
        FontLoaders.NMSL20.drawString("Day " + Long.valueOf(this.mc.theWorld.getWorldTime() / 24000L), this.x + 4, this.y + 4, -1);
    }
}

