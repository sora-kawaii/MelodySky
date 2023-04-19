/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.modules.macros.CustomNuker;

public final class CustomNukerCMD
extends Command {
    public CustomNukerCMD() {
        super("customnuker", new String[]{"cusnuker", "cusn"}, "", "FUCK YOU!");
    }

    @Override
    public String execute(String[] args) {
        if (args.length >= 1) {
            CustomNuker cusn = (CustomNuker)Client.instance.getModuleManager().getModuleByClass(CustomNuker.class);
            if (args[0].toLowerCase().contains("set")) {
                if (cusn != null) {
                    cusn.blockName = args[1].toLowerCase();
                    Helper.sendMessage("Set CustomNuker Block to " + args[1].toLowerCase());
                    cusn.saveCustomNuker();
                } else {
                    Helper.sendMessage("Unexpected Error.");
                }
            } else if (args[0].toLowerCase().contains("help")) {
                Helper.sendMessageWithoutPrefix("====================== CustomNuker ======================");
                Helper.sendMessageWithoutPrefix("CustomNuker:> .cusn set <Block Name> - Start Mining.");
                Helper.sendMessageWithoutPrefix("CustomNuker:> .cusn help - Stop Mining.");
            } else {
                Helper.sendMessage("useage: .customnuker help");
            }
        } else {
            Helper.sendMessage("useage: .customnuker help");
        }
        return null;
    }
}

