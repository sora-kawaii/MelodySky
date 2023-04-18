/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventChat;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AntiLobbyCommand
extends Module {
    private Option<Boolean> od = new Option<Boolean>("DungeonOnly", true);

    public AntiLobbyCommand() {
        super("AntiLobbyCommands", new String[]{"alc", "asc", "lobby"}, ModuleType.Others);
        this.addValues(this.od);
        this.setModInfo("Prevents You using /l or /spawn.");
    }

    @EventHandler
    private void onChat(EventChat eventChat) {
        if (!Client.inDungeons && ((Boolean)this.od.getValue()).booleanValue()) {
            return;
        }
        String string = eventChat.getMessage();
        if (string.toLowerCase().contains("/lobby")) {
            Helper.sendMessage((Object)((Object)EnumChatFormatting.GREEN) + "[AntiLobbyCommand] " + (Object)((Object)EnumChatFormatting.DARK_GREEN) + "Prevented you from using " + (Object)((Object)EnumChatFormatting.RED) + "/lobby " + (Object)((Object)EnumChatFormatting.DARK_GREEN) + "Command.");
            eventChat.setCancelled(true);
            return;
        }
        if (string.toLowerCase().equals("/l")) {
            Helper.sendMessage((Object)((Object)EnumChatFormatting.GREEN) + "[AntiLobbyCommand] " + (Object)((Object)EnumChatFormatting.DARK_GREEN) + "Prevented you from using " + (Object)((Object)EnumChatFormatting.RED) + "/l " + (Object)((Object)EnumChatFormatting.DARK_GREEN) + "Command.");
            eventChat.setCancelled(true);
            return;
        }
        if (string.toLowerCase().contains("/spawn")) {
            Helper.sendMessage((Object)((Object)EnumChatFormatting.GREEN) + "[AntiLobbyCommand] " + (Object)((Object)EnumChatFormatting.DARK_GREEN) + "Prevented you from using " + (Object)((Object)EnumChatFormatting.RED) + "/spawn " + (Object)((Object)EnumChatFormatting.DARK_GREEN) + "Command.");
            eventChat.setCancelled(true);
            return;
        }
    }
}

