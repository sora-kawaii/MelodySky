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
    public void onChat(ClientChatReceivedEvent event) {
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
        if (!Client.inDungeons) {
            return;
        }
        Alerts omg = (Alerts)Client.instance.getModuleManager().getModuleByClass(Alerts.class);
        if (message.equals("[BOSS] The Watcher: That will be enough for now.") && omg.isEnabled() && ((Boolean)omg.watcher.getValue()).booleanValue()) {
            shouldShowWatcherReady = true;
            if (((Boolean)omg.rases.getValue()).booleanValue()) {
                Music.playSound(this.getClass(), "wc.WAV");
            } else {
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0f, 0.5f);
            }
        }
        if (message.equals("Second Wind Activated! Your Spirit Mask saved your life!") && omg.isEnabled() && ((Boolean)omg.spirit.getValue()).booleanValue()) {
            shouldShowSpiritMaskPoped = true;
            if (((Boolean)omg.rases.getValue()).booleanValue()) {
                Music.playSound(this.getClass(), "wc.WAV");
            } else {
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0f, 0.5f);
            }
        }
        if (message.equals("Your Bonzo's Mask saved your life!") && omg.isEnabled() && ((Boolean)omg.bonzo.getValue()).booleanValue()) {
            shouldShowBonzoMaskPoped = true;
            if (((Boolean)omg.rases.getValue()).booleanValue()) {
                Music.playSound(this.getClass(), "wc.WAV");
            } else {
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0f, 0.5f);
            }
        }
        if ((message.equals("Your \u269a Bonzo's Mask saved your life!") || message.equals("Your \u269a Bonzo's Mask saved your life!")) && omg.isEnabled() && ((Boolean)omg.bonzo.getValue()).booleanValue()) {
            shouldShowBonzoMask2Poped = true;
            if (((Boolean)omg.rases.getValue()).booleanValue()) {
                Music.playSound(this.getClass(), "wc.WAV");
            } else {
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0f, 0.5f);
            }
        }
    }
}

