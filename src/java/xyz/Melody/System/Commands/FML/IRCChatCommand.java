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
    public String func_71517_b() {
        return "kc";
    }

    public int func_82362_a() {
        return -1;
    }

    public boolean canSenderUseCommand(ICommandSender iCommandSender) {
        return true;
    }

    public void func_71515_b(ICommandSender iCommandSender, String[] stringArray) {
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

    public String func_71518_a(ICommandSender iCommandSender) {
        return "kc";
    }
}

