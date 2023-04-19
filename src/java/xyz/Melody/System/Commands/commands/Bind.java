/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import org.lwjgl.input.Keyboard;
import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;

public final class Bind
extends Command {
    public Bind() {
        super("Bind", new String[]{"b"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
        if (args.length >= 2) {
            Module m = Client.instance.getModuleManager().getAlias(args[0]);
            if (m != null) {
                int k = Keyboard.getKeyIndex(args[1].toUpperCase());
                m.setKey(k);
                Object[] arrobject = new Object[]{m.getName(), k == 0 ? "none" : args[1].toUpperCase()};
                Helper.sendMessage(String.format("> Bound %s to %s", arrobject));
            } else {
                Helper.sendMessage("> Invalid module name, double check spelling.");
            }
        } else {
            Helper.sendMessageWithoutPrefix("\u00a7bCorrect usage:\u00a77 .bind <module> <key>");
        }
        return null;
    }
}

