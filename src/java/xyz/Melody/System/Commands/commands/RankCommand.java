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
    public String execute(String[] stringArray) {
        if (stringArray.length >= 1) {
            String string;
            if (stringArray[0].toLowerCase().equals("reset")) {
                Client.customRank = "\ufffd\u0379\ufffd\ufffd\ufffd";
                Helper.sendMessage("Reset Custom Rank to " + Client.customRank + ".");
                Client.instance.saveCustomRank();
                return null;
            }
            String string2 = "";
            for (int i = 0; i < stringArray.length; ++i) {
                string2 = i == stringArray.length - 1 ? string2 + stringArray[i] : string2 + stringArray[i] + " ";
            }
            Client.customRank = string = string2.replaceAll("&", "\u00a7");
            Helper.sendMessage("Set Custom Rank to " + Client.customRank + ".");
            Client.instance.saveCustomRank();
            return null;
        }
        Helper.sendMessage("Correct Useage: .rank [rank] / .rank reset");
        return null;
    }
}

