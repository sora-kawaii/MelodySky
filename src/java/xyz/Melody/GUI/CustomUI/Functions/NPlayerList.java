/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI.Functions;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.GUI.CustomUI.HUDScreen;
import xyz.Melody.GUI.Font.FontLoaders;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.balance.AntiBot;
import xyz.Melody.module.modules.others.PlayerList;

public final class NPlayerList
extends HUDApi {
    private TimerUtil timer = new TimerUtil();
    private Map<String, Float> playerList = new HashMap<String, Float>();
    private float range = 0.0f;
    private int oof = 0;

    public NPlayerList() {
        super("PlayerList", 300, 300);
        this.setEnabled(true);
    }

    @Override
    public void InScreenRender() {
        float ax = 3.0f;
        float ay = 3.0f;
        RenderUtil.drawFastRoundedRect(this.x, this.y, this.x + 200, this.y + this.oof * 13 + 18, 2.0f, new Color(10, 10, 10, 50).getRGB());
        FontLoaders.CNMD24.drawString("Players Within " + this.range + " Blocks", (float)this.x + ax, (float)this.y + ay, Colors.GRAY.c);
        int row = 0;
        for (String name : this.playerList.keySet()) {
            float distance = this.playerList.get(name).floatValue();
            EnumChatFormatting format = distance <= 15.0f ? EnumChatFormatting.RED : EnumChatFormatting.AQUA;
            FontLoaders.CNMD20.drawString(" -" + name + " " + (Object)((Object)format) + distance + "m", (float)this.x + ax + 10.0f, (float)(this.y + row * 13 + 16) + ay, -1);
            this.oof = ++row;
        }
    }

    @EventHandler
    private void onTick(EventTick event) {
        PlayerList mod = (PlayerList)Client.instance.getModuleManager().getModuleByClass(PlayerList.class);
        if (!mod.isEnabled()) {
            this.playerList.clear();
            return;
        }
        if (this.timer.hasReached((Double)mod.scanDelay.getValue())) {
            this.range = ((Double)mod.range.getValue()).floatValue();
            this.playerList = this.getPlayersIn(this.range);
            this.timer.reset();
        }
    }

    @EventHandler
    private void onR2D(EventRender2D event) {
        if (this.playerList.isEmpty()) {
            return;
        }
        if (this.mc.currentScreen instanceof HUDScreen) {
            return;
        }
        float ax = 5.0f;
        float ay = 4.0f;
        RenderUtil.drawFastRoundedRect(this.x, this.y, this.x + 200, this.y + this.oof * 13 + 20, 2.0f, new Color(10, 10, 10, 50).getRGB());
        FontLoaders.CNMD24.drawString("Players Within " + this.range + " Blocks", (float)this.x + ax, (float)this.y + ay, Colors.ORANGE.c);
        int row = 0;
        for (String name : this.playerList.keySet()) {
            float distance = this.playerList.get(name).floatValue();
            EnumChatFormatting format = distance <= 15.0f ? EnumChatFormatting.RED : EnumChatFormatting.AQUA;
            FontLoaders.CNMD20.drawString(" -" + name + " " + (Object)((Object)format) + distance + "m", (float)this.x + ax + 6.0f, (float)(this.y + row * 13 + 16) + ay, -1);
            this.oof = ++row;
        }
    }

    private Map<String, Float> getPlayersIn(float range) {
        HashMap<String, Float> playerMap = new HashMap<String, Float>();
        AntiBot ab = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        for (EntityPlayer player : this.mc.theWorld.playerEntities) {
            float distance = this.mc.thePlayer.getDistanceToEntity(player);
            if (!(distance <= range) || player == this.mc.thePlayer || player.getName() == null || !ab.isInTablist(player)) continue;
            playerMap.put(player.getName(), Float.valueOf(distance));
        }
        return playerMap;
    }
}

