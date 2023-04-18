/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.FMLModules;

import java.util.Random;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Utils.Music;
import xyz.Melody.module.modules.QOL.Dungeons.LeverAura;
import xyz.Melody.module.modules.QOL.GhostBlock;
import xyz.Melody.module.modules.macros.PowderChestMacro;

public final class ChatMonitor {
    public static boolean shouldShow = false;

    @SubscribeEvent(receiveCanceled=true)
    public void onNecron(ClientChatReceivedEvent clientChatReceivedEvent) {
        String string = StringUtils.stripControlCodes(clientChatReceivedEvent.message.getUnformattedText());
        if (string.equals("[BOSS] Necron: Goodbye.")) {
            clientChatReceivedEvent.message = new ChatComponentText(clientChatReceivedEvent.message.getFormattedText().replaceAll("Goodbye.", "Goldor, Fuck You!"));
        }
        if (string.equals("[BOSS] Necron: ARGH!")) {
            clientChatReceivedEvent.message = new ChatComponentText(clientChatReceivedEvent.message.getFormattedText().replaceAll("ARGH!", "NMSL!"));
        }
        if (string.equals("[BOSS] Necron: All this, for nothing...")) {
            clientChatReceivedEvent.message = new ChatComponentText(clientChatReceivedEvent.message.getFormattedText().replaceAll("All this, for nothing...", "No handle for you..."));
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onChat(ClientChatReceivedEvent clientChatReceivedEvent) {
        String string = StringUtils.stripControlCodes(clientChatReceivedEvent.message.getUnformattedText());
        if (string.contains("Mining Speed Boost is now available!")) {
            Client.pickaxeAbilityReady = true;
        }
        if (string.contains("You used your Mining Speed Boost Pickaxe Ability!")) {
            Client.pickaxeAbilityReady = false;
        }
        if (string.contains("You have successfully picked the lock on this chest!")) {
            PowderChestMacro.nextRotation = null;
            PowderChestMacro.done.add(PowderChestMacro.chestPos);
            PowderChestMacro.chest = null;
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onOMG(ClientChatReceivedEvent clientChatReceivedEvent) {
        String string = StringUtils.stripControlCodes(clientChatReceivedEvent.message.getUnformattedText());
        String string2 = clientChatReceivedEvent.message.getFormattedText();
        if (!string.contains("XJC") && !string.contains("Guild >") && (string.startsWith("PUZZLE FAIL") || string.contains("You were killed by") || string.contains("You were crushed") || string.contains("You fell into the void") || string.contains("You suffocated") || string.contains("You burnt to death") || string.contains("You died"))) {
            Music.playSound(this.getClass(), "kill_it.WAV");
            Client.instance.irc.sendPrefixMsg(string2, true);
        }
        if (!string.contains("XJC") && !string.contains("Guild >") && (string.startsWith("RARE REWARD") || string.startsWith("PET DROP"))) {
            boolean bl = new Random().nextBoolean();
            if (bl) {
                Music.playSound(this.getClass(), "gg.WAV");
            } else {
                Music.playSound(this.getClass(), "iron_punch.WAV");
            }
            Client.instance.irc.sendPrefixMsg(string2, true);
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onBossSay(ClientChatReceivedEvent clientChatReceivedEvent) {
        String string = StringUtils.stripControlCodes(clientChatReceivedEvent.message.getUnformattedText());
        if (string.contains("[BOSS]") && string.contains(":") && !string.contains("The Watcher")) {
            Client.instance.dungeonUtils.inBoss = true;
        }
        if (string.contains("[BOSS]") && string.contains(":") && string.contains("The Watcher")) {
            Client.instance.dungeonUtils.inBoss = false;
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        if (((Boolean)PowderChestMacro.autoClear.getValue()).booleanValue()) {
            PowderChestMacro.done.clear();
        }
        LeverAura.allLevers.clear();
        LeverAura.clicked.clear();
        LeverAura.blockPos = null;
        GhostBlock.blockposs.clear();
        Client.pickaxeAbilityReady = false;
    }
}

