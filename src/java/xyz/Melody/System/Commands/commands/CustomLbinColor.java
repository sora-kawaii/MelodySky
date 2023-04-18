/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.modules.others.FetchLBinData;

public final class CustomLbinColor
extends Command {
    public CustomLbinColor() {
        super("CustomLbinColor", new String[]{"clbc"}, "", "FUCK YOU!");
    }

    @Override
    public String execute(String[] stringArray) {
        if (stringArray.length >= 1) {
            FetchLBinData.colorPrefix = stringArray[0];
            Helper.sendMessage("Set Custom LBin Color to " + FetchLBinData.colorPrefix);
        } else {
            Helper.sendMessageWithoutPrefix("\u00a7bCorrect usage:\u00a77 .clbc [ColorPrefix]");
        }
        return null;
    }
}

