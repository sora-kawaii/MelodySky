/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;

public final class Toggle
extends Command {
    public Toggle() {
        super("t", new String[]{"toggle", "togl", "turnon", "enable"}, "", "Toggles a specified Module");
    }

    @Override
    public String execute(String[] args) {
        if (args.length <= 1 && args.length < 1) {
            Helper.sendMessageWithoutPrefix("\u00a7bCorrect usage:\u00a77 .t <module>");
        }
        boolean found = false;
        Module m = Client.instance.getModuleManager().getAlias(args[0]);
        if (m != null) {
            if (!m.isEnabled()) {
                m.setEnabled(true);
            } else {
                m.setEnabled(false);
            }
            found = true;
            if (m.isEnabled()) {
                Helper.sendMessage("> " + m.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " was" + (Object)((Object)EnumChatFormatting.GREEN) + " enabled");
            } else {
                Helper.sendMessage("> " + m.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " was" + (Object)((Object)EnumChatFormatting.RED) + " disabled");
            }
        }
        if (!found) {
            Helper.sendMessage("> Module name " + (Object)((Object)EnumChatFormatting.RED) + args[0] + (Object)((Object)EnumChatFormatting.GRAY) + " is invalid");
        }
        return null;
    }
}

