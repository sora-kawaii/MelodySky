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
    public String execute(String[] args) {
        if (args.length >= 1) {
            String finalName;
            if (args[0].toLowerCase().equals("reset")) {
                Client.playerName = null;
                Helper.sendMessage("Reset Custom Name to " + Client.playerName + ".");
                Client.instance.saveCustomName();
                return null;
            }
            String name = "";
            for (int i = 0; i < args.length; ++i) {
                name = i == args.length - 1 ? name + args[i] : name + args[i] + " ";
            }
            Client.playerName = finalName = name.replaceAll("&", "\u00a7");
            Helper.sendMessage("Set Custom Name to " + Client.playerName + ".");
            Client.instance.saveCustomName();
            return null;
        }
        Helper.sendMessage("Correct Useage: .name [name] / .name reset");
        return null;
    }
}

