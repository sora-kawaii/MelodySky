/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;

public final class NameCommand
extends Command {
    public NameCommand() {
        super(".name", new String[]{"name"}, "", "sketit");
    }

    @Override
    public String execute(String[] stringArray) {
        if (stringArray.length >= 1) {
            String string;
            if (stringArray[0].toLowerCase().equals("reset")) {
                Client.playerName = null;
                Helper.sendMessage("Reset Custom Name to " + Client.playerName + ".");
                Client.instance.saveCustomName();
                return null;
            }
            String string2 = "";
            for (int i = 0; i < stringArray.length; ++i) {
                string2 = i == stringArray.length - 1 ? string2 + stringArray[i] : string2 + stringArray[i] + " ";
            }
            Client.playerName = string = string2.replaceAll("&", "\u00a7");
            Helper.sendMessage("Set Custom Name to " + Client.playerName + ".");
            Client.instance.saveCustomName();
            return null;
        }
        Helper.sendMessage("Correct Useage: .name [name] / .name reset");
        return null;
    }
}

