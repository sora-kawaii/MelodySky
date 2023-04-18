/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import net.minecraft.entity.player.EntityPlayer;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.CFontRenderer;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.render.RenderUtil;

public final class CurrentServerInfo
extends HUDApi {
    public CurrentServerInfo() {
        super("Area", 5, 50);
        this.setEnabled(true);
    }

    @EventHandler
    public void onRender(EventRender2D eventRender2D) {
        if (this.mc.currentScreen instanceof HUDScreen) {
            return;
        }
        this.render();
    }

    @Override
    public void InScreenRender() {
        this.render();
    }

    private void render() {
        int n = new Color(30, 30, 30, 100).getRGB();
        if (!Client.inSkyblock) {
            return;
        }
        CFontRenderer cFontRenderer = FontLoaders.NMSL20;
        if (!Client.inDungeons) {
            RenderUtil.drawFastRoundedRect(this.x, this.y, this.x + cFontRenderer.getStringWidth("Area: " + (Object)((Object)Client.instance.sbArea.getCurrentArea())) + 8, this.y + 10 + 6, 1.0f, n);
            FontLoaders.NMSL20.drawString("Area: " + (Object)((Object)Client.instance.sbArea.getCurrentArea()), this.x + 4, this.y + 4, -1);
        } else if (Client.inDungeons) {
            if (Client.inDungeons) {
                int n2 = 0;
                FontLoaders.NMSL18.drawString("Score: " + Client.instance.dungeonUtils.score, this.x + 3, this.y + 3, -1);
                FontLoaders.NMSL18.drawString("Mimic: " + Client.instance.dungeonUtils.foundMimic, this.x + 4, this.y + 13, -1);
                FontLoaders.NMSL18.drawString("Secrets Found:" + Client.instance.dungeonUtils.secretsFound, this.x + 3, this.y + 23, -1);
                FontLoaders.NMSL18.drawString("Crypts:" + Client.instance.dungeonUtils.cryptsFound, this.x + 3, this.y + 33, -1);
                FontLoaders.NMSL18.drawString("Deaths:" + Client.instance.dungeonUtils.deaths, this.x + 3, this.y + 43, -1);
                FontLoaders.NMSL18.drawString("Teams:", this.x + 3, this.y + 53, -1);
                for (EntityPlayer entityPlayer : Client.instance.dungeonUtils.teammates) {
                    FontLoaders.NMSL18.drawString(" - " + entityPlayer.getName(), this.x + 3, this.y + 63 + n2 * 10, -1);
                    ++n2;
                }
                FontLoaders.NMSL18.drawString("Floor: " + Client.instance.dungeonUtils.floor.name(), this.x + 3, this.y + 63 + n2 * 10, -1);
                FontLoaders.NMSL18.drawString("Master: " + Client.isMMD, this.x + 3, this.y + 73 + n2 * 10, -1);
                FontLoaders.NMSL18.drawString("In Boss: " + Client.instance.dungeonUtils.inBoss, this.x + 3, this.y + 83 + n2 * 10, -1);
            } else {
                FontLoaders.NMSL20.drawString("Unexpected Error.", this.x + 4, this.y + 4, -1);
            }
        }
    }
}

