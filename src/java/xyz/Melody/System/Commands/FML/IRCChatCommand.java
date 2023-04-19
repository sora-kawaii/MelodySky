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

    public boolean canSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            Helper.sendMessage("&cInvalid Syntax. Use &3/kc [message]");
        } else {
            String msg = "";
            for (int i = 0; i < args.length; ++i) {
                msg = i == args.length - 1 ? msg + args[i] : msg + args[i] + " ";
            }
            String finalMsg = msg.replaceAll("&", "\u00a7");
            Client.instance.irc.sendPrefixMsg(finalMsg, true);
        }
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "kc";
    }
}

