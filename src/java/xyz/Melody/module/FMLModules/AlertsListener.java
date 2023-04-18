/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.FMLModules;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Utils.Music;
import xyz.Melody.module.modules.QOL.Dungeons.Alerts;

public final class AlertsListener {
    public static boolean shouldShowWatcherReady = false;
    public static boolean shouldShowSpiritMaskPoped = false;
    public static boolean shouldShowBonzoMaskPoped = false;
    public static boolean shouldShowBonzoMask2Poped = false;

    @SubscribeEvent(receiveCanceled=true)
    public void onChat(ClientChatReceivedEvent clientChatReceivedEvent) {
        String string = StringUtils.func_76338_a((String)clientChatReceivedEvent.message.func_150260_c());
        if (!Client.inDungeons) {
            return;
        }
        Alerts alerts = (Alerts)Client.instance.getModuleManager().getModuleByClass(Alerts.class);
        if (string.equals("[BOSS] The Watcher: That will be enough for now.") && alerts.isEnabled() && ((Boolean)alerts.watcher.getValue()).booleanValue()) {
            shouldShowWatcherReady = true;
            if (((Boolean)alerts.rases.getValue()).booleanValue()) {
                Music.playSound(this.getClass(), "wc.WAV");
            } else {
                Minecraft.func_71410_x().field_71439_g.func_85030_a("random.orb", 1.0f, 0.5f);
            }
        }
        if (string.equals("Second Wind Activated! Your Spirit Mask saved your life!") && alerts.isEnabled() && ((Boolean)alerts.spirit.getValue()).booleanValue()) {
            shouldShowSpiritMaskPoped = true;
            if (((Boolean)alerts.rases.getValue()).booleanValue()) {
                Music.playSound(this.getClass(), "wc.WAV");
            } else {
                Minecraft.func_71410_x().field_71439_g.func_85030_a("random.orb", 1.0f, 0.5f);
            }
        }
        if (string.equals("Your Bonzo's Mask saved your life!") && alerts.isEnabled() && ((Boolean)alerts.bonzo.getValue()).booleanValue()) {
            shouldShowBonzoMaskPoped = true;
            if (((Boolean)alerts.rases.getValue()).booleanValue()) {
                Music.playSound(this.getClass(), "wc.WAV");
            } else {
                Minecraft.func_71410_x().field_71439_g.func_85030_a("random.orb", 1.0f, 0.5f);
            }
        }
        if ((string.equals("Your \u269a Bonzo's Mask saved your life!") || string.equals("Your \u269a Bonzo's Mask saved your life!")) && alerts.isEnabled() && ((Boolean)alerts.bonzo.getValue()).booleanValue()) {
            shouldShowBonzoMask2Poped = true;
            if (((Boolean)alerts.rases.getValue()).booleanValue()) {
                Music.playSound(this.getClass(), "wc.WAV");
            } else {
                Minecraft.func_71410_x().field_71439_g.func_85030_a("random.orb", 1.0f, 0.5f);
            }
        }
    }
}

