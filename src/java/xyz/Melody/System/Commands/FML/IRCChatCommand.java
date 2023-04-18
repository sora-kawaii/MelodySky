/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.FML;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import xyz.Melody.Client;
import xyz.Melody.Utils.Helper;

public final class IRCChatCommand
extends CommandBase {
    @Override
    public String getCommandName() {
        return "kc";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    public boolean canSenderUseCommand(ICommandSender iCommandSender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) {
        if (stringArray.length == 0) {
            Helper.sendMessage("&cInvalid Syntax. Use &3/kc [message]");
        } else {
            String string = "";
            for (int i = 0; i < stringArray.length; ++i) {
                string = i == stringArray.length - 1 ? string + stringArray[i] : string + stringArray[i] + " ";
            }
            String string2 = string.replaceAll("&", "\u00a7");
            Client.instance.irc.sendPrefixMsg(string2, true);
        }
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "kc";
    }
}

