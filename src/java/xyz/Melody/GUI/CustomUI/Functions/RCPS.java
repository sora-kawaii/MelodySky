/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;

public final class RCPS
extends HUDApi {
    private ArrayList<Long> clicks = new ArrayList();

    public RCPS() {
        super("RCPS", 5, 30);
        this.setEnabled(true);
    }

    @SubscribeEvent
    public void onClick(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState() && this.mc.gameSettings.keyBindUseItem.isKeyDown() && Mouse.getEventButton() == this.mc.gameSettings.keyBindUseItem.getKeyCode() + 100) {
            this.clicks.add(System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onRender(EventRender2D event) {
        if (this.mc.currentScreen instanceof HUDScreen) {
            return;
        }
        this.cpsRender();
    }

    @Override
    public void InScreenRender() {
        this.cpsRender();
    }

    private void cpsRender() {
        int c2 = new Color(30, 30, 30, 100).getRGB();
        CFontRenderer font = FontLoaders.NMSL20;
        RenderUtil.drawFastRoundedRect(this.x, this.y, this.x + font.getStringWidth("RCPS: " + this.getRightCPS()) + 8, this.y + font.getStringHeight("RCPS: " + this.getRightCPS()) + 6, 1.0f, c2);
        FontLoaders.NMSL20.drawString("RCPS: " + this.getRightCPS(), this.x + 4, this.y + 4, -1);
    }

    public int getRightCPS() {
        long curTime = System.currentTimeMillis();
        this.clicks.removeIf(e -> e + 1000L < curTime);
        return this.clicks.size();
    }
}

