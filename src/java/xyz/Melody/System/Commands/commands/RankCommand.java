/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;

public final class RankCommand
extends Command {
    public RankCommand() {
        super(".rank", new String[]{"rank"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
        if (args.length >= 1) {
            String finalName;
            if (args[0].toLowerCase().equals("reset")) {
                Client.customRank = "\ufffd\u0379\ufffd\ufffd\ufffd";
                Helper.sendMessage("Reset Custom Rank to " + Client.customRank + ".");
                Client.instance.saveCustomRank();
                return null;
            }
            String name = "";
            for (int i = 0; i < args.length; ++i) {
                name = i == args.length - 1 ? name + args[i] : name + args[i] + " ";
            }
            Client.customRank = finalName = name.replaceAll("&", "\u00a7");
            Helper.sendMessage("Set Custom Rank to " + Client.customRank + ".");
            Client.instance.saveCustomRank();
            return null;
        }
        Helper.sendMessage("Correct Useage: .rank [rank] / .rank reset");
        return null;
    }
}

