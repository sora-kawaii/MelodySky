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
        float f = 3.0f;
        float f2 = 3.0f;
        RenderUtil.drawFastRoundedRect(this.x, this.y, this.x + 200, this.y + this.oof * 13 + 18, 2.0f, new Color(10, 10, 10, 50).getRGB());
        FontLoaders.CNMD24.drawString("Players Within " + this.range + " Blocks", (float)this.x + f, (float)this.y + f2, Colors.GRAY.c);
        int n = 0;
        for (String string : this.playerList.keySet()) {
            float f3 = this.playerList.get(string).floatValue();
            EnumChatFormatting enumChatFormatting = f3 <= 15.0f ? EnumChatFormatting.RED : EnumChatFormatting.AQUA;
            FontLoaders.CNMD20.drawString(" -" + string + " " + (Object)((Object)enumChatFormatting) + f3 + "m", (float)this.x + f + 10.0f, (float)(this.y + n * 13 + 16) + f2, -1);
            this.oof = ++n;
        }
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        PlayerList playerList = (PlayerList)Client.instance.getModuleManager().getModuleByClass(PlayerList.class);
        if (!playerList.isEnabled()) {
            this.playerList.clear();
            return;
        }
        if (this.timer.hasReached((Double)playerList.scanDelay.getValue())) {
            this.range = ((Double)playerList.range.getValue()).floatValue();
            this.playerList = this.getPlayersIn(this.range);
            this.timer.reset();
        }
    }

    @EventHandler
    private void onR2D(EventRender2D eventRender2D) {
        if (this.playerList.isEmpty()) {
            return;
        }
        if (this.mc.field_71462_r instanceof HUDScreen) {
            return;
        }
        float f = 5.0f;
        float f2 = 4.0f;
        RenderUtil.drawFastRoundedRect(this.x, this.y, this.x + 200, this.y + this.oof * 13 + 20, 2.0f, new Color(10, 10, 10, 50).getRGB());
        FontLoaders.CNMD24.drawString("Players Within " + this.range + " Blocks", (float)this.x + f, (float)this.y + f2, Colors.ORANGE.c);
        int n = 0;
        for (String string : this.playerList.keySet()) {
            float f3 = this.playerList.get(string).floatValue();
            EnumChatFormatting enumChatFormatting = f3 <= 15.0f ? EnumChatFormatting.RED : EnumChatFormatting.AQUA;
            FontLoaders.CNMD20.drawString(" -" + string + " " + (Object)((Object)enumChatFormatting) + f3 + "m", (float)this.x + f + 6.0f, (float)(this.y + n * 13 + 16) + f2, -1);
            this.oof = ++n;
        }
    }

    private Map<String, Float> getPlayersIn(float f) {
        HashMap<String, Float> hashMap = new HashMap<String, Float>();
        AntiBot antiBot = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        for (EntityPlayer entityPlayer : this.mc.field_71441_e.field_73010_i) {
            float f2 = this.mc.field_71439_g.func_70032_d(entityPlayer);
            if (!(f2 <= f) || entityPlayer == this.mc.field_71439_g || entityPlayer.func_70005_c_() == null || !antiBot.isInTablist(entityPlayer)) continue;
            hashMap.put(entityPlayer.func_70005_c_(), Float.valueOf(f2));
        }
        return hashMap;
    }
}

