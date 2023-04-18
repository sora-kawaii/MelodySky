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
    public String execute(String[] stringArray) {
        if (stringArray.length >= 2) {
            Module module = Client.instance.getModuleManager().getAlias(stringArray[0]);
            if (module != null) {
                int n = Keyboard.getKeyIndex(stringArray[1].toUpperCase());
                module.setKey(n);
                Object[] objectArray = new Object[]{module.getName(), n == 0 ? "none" : stringArray[1].toUpperCase()};
                Helper.sendMessage(String.format("> Bound %s to %s", objectArray));
            } else {
                Helper.sendMessage("> Invalid module name, double check spelling.");
            }
        } else {
            Helper.sendMessageWithoutPrefix("\u00a7bCorrect usage:\u00a77 .bind <module> <key>");
        }
        return null;
    }
}

